package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.markers.Marker.Update;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RequestMapping("/users")
@RestController
public class UserController {
    private final Map<Long, User> users = new HashMap<>();
    private long id = 1;

    @PostMapping
    public User create(@Validated @RequestBody User user) {
        userValidation(user);
        user.setId(getId());
        users.put(user.getId(), user);
        log.info("Пользователь '{}' сохранен с id '{}'", user.getEmail(), user.getId());
        return user;
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("Количество пользователей '{}'", users.size());
        return new ArrayList<>(users.values());
    }

    @PutMapping
    public User update(@Validated(Update.class) @RequestBody User user) {
        userValidation(user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("'{}' информация с id '{}' была обновлена", user.getLogin(), user.getId());
        } else {
            throw new ValidationException("Попытка обновления несуществующего пользователя");
        }
        return user;
    }

    private void userValidation(User user) {
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

    public long getId() {
        return id++;
    }
}