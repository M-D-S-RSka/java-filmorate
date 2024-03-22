package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;
    private final FriendshipStorage friendshipStorage;

    public void addFriend(Long userId, Long friendId) {
        friendshipStorage.addFriend(userId, friendId);
        userStorage.getUserById(userId);
    }

    public List<User> getFriendsIds(Long userId) {
        return friendshipStorage.getFriendsIds(userId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        friendshipStorage.deleteFriend(userId, friendId);
        userStorage.getUserById(userId);
    }

    public List<User> getMutualFriends(Long userId, Long othersId) {
        return friendshipStorage.getFriendship(userId, othersId);
    }

    public User addUser(User user) {
        userStorage.addUser(user);
        return user;
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User updateUser(User user) {
        userStorage.updateUser(user);
        return user;
    }

    public User getUserById(Long id) {
        return userStorage.getUserById(id);
    }

    public void deleteUserById(Long id) {
        userStorage.deleteUserById(id);
    }
}