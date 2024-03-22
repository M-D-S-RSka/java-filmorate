package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static ru.yandex.practicum.filmorate.exceptions.Validation.validationUser;

public class UserValidationTest {
    User user;

    @BeforeEach
    public void createTestUser() {
        user = User.builder()
                .login("login")
                .name("name")
                .email("email@email.email")
                .birthday(LocalDate.of(1998, 3, 7))
                .id(1L)
                .build();
    }

    @Test
    public void validateEmptyEmailUser() {
        user.setEmail("");
        final RuntimeException exception = Assertions.assertThrows(RuntimeException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                validationUser(user);
            }
        });
        Assertions.assertEquals("email не может быть пустым и должен содержать символ @",
                exception.getMessage());
    }

    @Test
    public void validateNonSymbolEmailUser() {
        user.setEmail("emailemail.email");
        final RuntimeException exception = Assertions.assertThrows(RuntimeException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                validationUser(user);
            }
        });
        Assertions.assertEquals("email не может быть пустым и должен содержать символ @",
                exception.getMessage());
    }

    @Test
    public void validateEmptyLoginUser() {
        user.setLogin("");
        final RuntimeException exception = Assertions.assertThrows(RuntimeException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                validationUser(user);
            }
        });
        Assertions.assertEquals("Login не может быть пустым или содержать пробелы",
                exception.getMessage());
    }

    @Test
    public void validateLoginWithBlankUser() {
        user.setLogin("Log in");
        final RuntimeException exception = Assertions.assertThrows(RuntimeException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                validationUser(user);
            }
        });
        Assertions.assertEquals("Login не может быть пустым или содержать пробелы",
                exception.getMessage());
    }

    @Test
    public void validateBirthdayInTheFutureUser() {
        user.setBirthday(LocalDate.of(2035, 3, 7));
        final RuntimeException exception = Assertions.assertThrows(RuntimeException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                validationUser(user);
            }
        });
        Assertions.assertEquals("Birthday не может быть в будущем",
                exception.getMessage(), "сообщение об исключении не совпало");
    }
}