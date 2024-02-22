package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Set;

@SpringBootTest
public class UserControllerTest {

    UserController controller;
    UserStorage userStorage;
    UserService userService;
    User user = User.builder()
            .id(1)
            .email("yandex@yandex.ru")
            .login("user")
            .name("User")
            .birthday(LocalDate.of(1990, 1, 1))
            .friendsIds(Set.of(2L))
            .build();

    @BeforeEach
    protected void start() {
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);
        controller = new UserController(userStorage, userService);
    }

    @Test
    void create_shouldCreateAUser() {
        controller.create(user);

        Assertions.assertEquals(user, userStorage.getUserById(1L));
        Assertions.assertEquals(1, controller.getUsers().size());
    }

    @Test
    void update_shouldUpdateUserData() {
        User thisUser = new User(1, "mail@yandex.ru", "user", "User",
                LocalDate.of(1976, 9, 20), Set.of(2L), Set.of(1L));
        controller.create(user);
        controller.update(thisUser);

        Assertions.assertEquals("mail@yandex.ru", thisUser.getEmail());
        Assertions.assertEquals(user.getId(), thisUser.getId());
        Assertions.assertEquals(1, controller.getUsers().size());
    }

    @Test
    void create_shouldCreateAUserIfNameIsEmpty() {
        User thisUser = new User(1, "mail@yandex.ru", "user", null,
                LocalDate.of(1990, 1, 1), Set.of(2L), Set.of(1L));
        controller.create(thisUser);

        Assertions.assertEquals(1, thisUser.getId());
        Assertions.assertEquals("user", thisUser.getName());
    }

    @Test
    void create_shouldThrowExceptionIfEmailIncorrect() {
        user.setEmail("yandex.mail.ru");

        Assertions.assertThrows(ValidationException.class, () -> controller.create(user));
        Assertions.assertEquals(0, controller.getUsers().size());
    }

    @Test
    void create_shouldThrowExceptionIfEmailIsEmpty() {
        user.setEmail("");

        Assertions.assertThrows(ValidationException.class, () -> controller.create(user));
        Assertions.assertEquals(0, controller.getUsers().size());
    }

    @Test
    void create_shouldNotAddUserIfLoginIsEmpty() {
        user.setLogin("");

        Assertions.assertThrows(ValidationException.class, () -> controller.create(user));
        Assertions.assertEquals(0, controller.getUsers().size());
    }

    @Test
    void create_shouldNotAddUserIfBirthdayIsInTheFuture() {
        user.setBirthday(LocalDate.of(2024, 6, 28));

        Assertions.assertThrows(ValidationException.class, () -> controller.create(user));
        Assertions.assertEquals(0, controller.getUsers().size());
    }
}