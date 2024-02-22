package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public Map<Long, Set<Long>> addFriend(Long userId, Long friendId) {
        Map<Long, Set<Long>> friendList = new HashMap<>();
        userStorage.getUserById(userId).addFriends(friendId);
        userStorage.getUserById(friendId).addFriends(userId);
        friendList.put(userId, userStorage.getUserById(userId).getFriendsIds());
        friendList.put(friendId, userStorage.getUserById(friendId).getFriendsIds());
        return friendList;
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
}
