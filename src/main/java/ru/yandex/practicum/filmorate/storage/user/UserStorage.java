package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User addUser(User user);

    User getUserById(Long id);

    List<User> getUsers();

    User updateUser(User user);

    User addFriend(Long userId, Long friendId);

    List<User> getFriendsByUserId(Long id);

    User deleteFriend(Long userId, Long friendId);

    List<User> getMutualFriends(Long id, Long othersId);
}