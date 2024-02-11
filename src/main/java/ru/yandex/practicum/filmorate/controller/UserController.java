package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;


import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@RequestMapping("/users")
@RestController
public class UserController {
    private final HashMap<Integer, User> userHashMap = new HashMap<>();
    private int id = 0;

    @ResponseBody
    @PostMapping
    public User create(@Valid @RequestBody User user) {
        userValidation(user);
        userHashMap.put(user.getId(), user);
        log.info("Пользователь '{}' сохранен с id '{}'", user.getEmail(), user.getId());
        return user;
    }

    @ResponseBody
    @GetMapping
    public List<User> getUserHashMap() {
        log.info("Количество пользователей '{}'", userHashMap.size());
        return new ArrayList<>(userHashMap.values());
    }

    @ResponseBody
    @PutMapping
    public User update(@Valid @RequestBody User user) {
        userValidation(user);
        if (userHashMap.containsKey(user.getId())) {
            userHashMap.put(user.getId(), user);
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
            user.setId(++id);
            log.info("Неверный id пользователя был задан как '{}'", user.getId());
        }
        if (user.getLogin().isBlank() || user.getLogin().isEmpty()) {
            throw new ValidationException("Неверный логин пользователя с id '" + user.getId() + "'");
        }
    }
}