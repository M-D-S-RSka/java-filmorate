package ru.yandex.practicum.filmorate.storage.friendship;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendshipStorage {
    void addFriend(Long userId, Long friendId);

    List<User> getFriendsIds(Long userId);

    List<User> getFriendship(Long userId, Long friendId);

    void deleteFriend(Long userId, Long friendId);
}