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
        log.info("Пользователь userId = {} добавил в друзья friendId = {}", userId, friendId);
        userStorage.getUserById(userId);
    }

    public List<User> getFriendsIds(Long userId) {
        log.info("Список друзей пользователя userId {}", userId);
        return friendshipStorage.getFriendsIds(userId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        friendshipStorage.deleteFriend(userId, friendId);
        log.info("Пользователи userId {} и friendId {} больше не друзья", userId, friendId);
        userStorage.getUserById(userId);
    }

    public List<User> getMutualFriends(Long userId, Long othersId) {
        log.info("Список общих друзей пользователей userId {} и othersId {}", userId, othersId);
        return friendshipStorage.getFriendship(userId, othersId);
    }
}