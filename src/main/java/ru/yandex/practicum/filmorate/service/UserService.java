package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private static long id = 1;

    private final UserStorage userStorage;

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUserById(Long id) {
        return userStorage.getUserById(id);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public void addFriend(Long userId, Long friendId) {
        userStorage.getUserById(userId).addFriends(friendId);
        userStorage.getUserById(friendId).addFriends(userId);
    }

    public List<User> getFriendsIds(Long userId) {
        return userStorage.getFriendsByUserId(userId);
    }


    public void deleteFriend(Long userId, Long friendId) {
        userStorage.deleteFriend(userId, friendId);
        userStorage.getUserById(userId);
    }

    public List<User> getMutualFriends(Long id, Long othersId) {
        return userStorage.getMutualFriends(id, othersId);
    }

    public static void userValidation(User user) {
        if (user.getBirthday().isAfter(LocalDate.now()) || user.getBirthday() == null) {
            throw new ValidationException("Неправильная дата рождения пользователя с id '" + user.getId() + "'");
        }
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Неверный адрес электронной почты пользователя с id '" + user.getId() + "'");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Имя пользователя с id '{}' было установлено как '{}'", user.getId(), user.getName());
        }
        if (user.getId() == 0 || user.getId() < 0) {
            user.setId(id);
            log.info("Неверный id пользователя был задан как '{}'", user.getId());
        }
        if (user.getLogin().isBlank() || user.getLogin().isEmpty()) {
            throw new ValidationException("Неверный логин пользователя с id '" + user.getId() + "'");
        }
    }
}