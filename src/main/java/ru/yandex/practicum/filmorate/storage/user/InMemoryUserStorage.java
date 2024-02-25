package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long id = 1;

    public long getId() {
        return id++;
    }

    @Override
    public User addUser(User user) {
        UserService.userValidation(user);
        user.setFriendsIds(new HashSet<>());
        user.setId(getId());
        users.put(user.getId(), user);
        log.info("Пользователь '{}' сохранен с id '{}'", user.getEmail(), user.getId());
        return user;
    }

    @Override
    public User getUserById(Long id) {
        if (users.containsKey(id)) {
            log.info("Поступил запрос на получение пользователя по id '{}'", id);
            return users.get(id);
        } else throw new NotFoundException("Пользователь не найден по id");
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User updateUser(User user) {
        UserService.userValidation(user);
        user.setFriendsIds(new HashSet<>());
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("'{}' информация пользователя с id '{}' была обновлена", user.getLogin(), user.getId());
        } else {
            throw new NotFoundException("Попытка обновления несуществующего пользователя");
        }
        return user;
    }

    @Override
    public User addFriend(Long userId, Long friendId) {
        getUserById(userId).getFriendsIds().add(friendId);
        getUserById(friendId).getFriendsIds().add(userId);
        return getUserById(userId);
    }

    @Override
    public List<User> getFriendsByUserId(Long id) {
        return getUsers().stream()
                .filter(user -> user.getFriendsIds().contains(id))
                .collect(Collectors.toList());
    }

    @Override
    public User deleteFriend(Long userId, Long friendId) {
        getUserById(userId).getFriendsIds().remove(friendId);
        getUserById(friendId).getFriendsIds().remove(userId);
        return getUserById(userId);
    }

    @Override
    public List<User> getMutualFriends(Long userId, Long friendId) {
        List<User> mutualFriends1 = getFriendsByUserId(userId);
        List<User> mutualFriends2 = getFriendsByUserId(friendId);
        List<User> mutualFriendsId = mutualFriends1
                .stream()
                .filter(mutualFriends2::contains)
                .collect(Collectors.toList());
        return getUsers()
                .stream()
                .filter(mutualFriendsId::contains)
                .collect(Collectors.toList());
    }
}