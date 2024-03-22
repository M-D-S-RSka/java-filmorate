package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

import static ru.yandex.practicum.filmorate.exceptions.Validation.validationFilm;

@SpringBootTest
public class FilmValidationTest {
    private Film film;
    Mpa mpa = new Mpa(1, "G");
    LinkedHashSet<Genre> genre = new LinkedHashSet<Genre>(1);

    @BeforeEach
    public void createTestFilm() {
        film = new Film(2, "name", "description", LocalDate.of(1998, 7, 3),
                85, Set.of(1L), mpa, genre);
    }

    @Test
    public void createFilmEmptyNameTest() {
        film.setName("");
        final RuntimeException exception = Assertions.assertThrows(RuntimeException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                validationFilm(film);
            }
        });
        Assertions.assertEquals("В названии фильма ничего не указано",
                exception.getMessage(), "сообщение об исключении не совпало");
    }

    @Test
    public void createFilmBeforeDateTest() {
        film.setReleaseDate(LocalDate.of(1777, 3, 7));
        final RuntimeException exception = Assertions.assertThrows(RuntimeException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                validationFilm(film);
            }
        });
        Assertions.assertEquals("Дата выхода фильма не может быть раньше 28.12.1895г",
                exception.getMessage(), "сообщение об исключении не совпало");
    }

    @Test
    public void createFilmOver200SimbolsTest() {
        film.setDescription("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaa");
        final RuntimeException exception = Assertions.assertThrows(RuntimeException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                validationFilm(film);
            }
        });
        Assertions.assertEquals("В описании фильма больше 200 символов",
                exception.getMessage(), "сообщение об исключении не совпало");
    }

    @Test
    public void createFilmNegativeDurationTest() {
        film.setDuration(-5);
        final RuntimeException exception = Assertions.assertThrows(RuntimeException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                validationFilm(film);
            }
        });
        Assertions.assertEquals("Продолжительность фильма не может быть отрицательной",
                exception.getMessage(), "сообщение об исключении не совпало");
    }
}
