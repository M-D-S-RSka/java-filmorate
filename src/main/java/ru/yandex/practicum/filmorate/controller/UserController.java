package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.markers.Marker.Update;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;

    @PostMapping
    public User create(@Validated @RequestBody User user) {
        log.info("Пользователь '{}' сохранен с id '{}'", user.getEmail(), user.getId());
        return userStorage.addUser(user);
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("Количество пользователей '{}'", userStorage.getUsers().size());
        return userStorage.getUsers();
    }

    @PutMapping
    public User update(@Validated(Update.class) @RequestBody User user) {
        log.info("'{}' информация пользователя с id '{}' обновлена", user.getLogin(), user.getId());
        return userStorage.updateUser(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        log.info("Поступил запрос на получение пользователя по id '{}'", userStorage.getUserById(id));
        return userStorage.getUserById(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Long id) {
        log.info("Поступил запрос на получение списка друзей пользователя с id '{}'", id);
        return userService.getFriendsIds(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public Map<Long, Set<Long>> addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Пользователю с id '{}' поступил запрос на добавление в друзья от пользователя с id '{}'",
                id, friendId);
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Пользователю с id '{}' поступил запрос на удаление из друзей с id '{}'", id, friendId);
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("Поступил запрос на получение списка общих друзей пользователя с id '{}' с пользователем с id '{}'",
                id, otherId);
        return userService.getMutualFriends(id, otherId);
    }
}