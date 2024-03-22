package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.markers.Marker.Update;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@Slf4j
@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public User addUser(@Validated @RequestBody @NotNull User user) {
        log.info("Пользователь '{}' сохранен с id '{}'", user.getEmail(), user.getId());
        return userService.addUser(user);
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("Количество пользователей '{}'", userService.getUsers().size());
        return userService.getUsers();
    }

    @PutMapping
    public User update(@Validated(Update.class) @RequestBody @NotNull User user) {
        log.info("'{}' информация пользователя с id '{}' обновлена", user.getLogin(), user.getId());
        return userService.updateUser(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        log.info("Поступил запрос на получение пользователя по id '{}'", userService.getUserById(id));
        return userService.getUserById(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Long id) {
        log.info("Поступил запрос на получение списка друзей пользователя с id '{}'", id);
        return userService.getFriendsIds(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Пользователю с id '{}' поступил запрос на добавление в друзья от пользователя с id '{}'",
                id, friendId);
        userService.addFriend(id, friendId);
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

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) {
        log.info("Поступил запрос на удаление  пользователя с id '{}'", id);
        userService.deleteUserById(id);
    }
}