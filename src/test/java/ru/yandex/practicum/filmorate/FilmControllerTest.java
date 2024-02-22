package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.Set;

@SpringBootTest
public class FilmControllerTest {

    FilmController controller;
    FilmStorage filmStorage;
    FilmService filmService;
    Film film = Film.builder()
            .id(1)
            .name("Movie")
            .description("Самый потрясающий фильм, который я когда-либо видел")
            .releaseDate(LocalDate.of(2020, 2, 2))
            .duration(120)
            .likesIds(Set.of(1L))
            .build();

    @BeforeEach
    protected void start() {
        filmStorage = new InMemoryFilmStorage();
        filmService = new FilmService();
        controller = new FilmController(filmStorage, filmService);
    }

    @Test
    void create_shouldAddAMovie() {
        controller.create(film);

        Assertions.assertEquals(film, filmStorage.getFilmById(1L));
        Assertions.assertEquals(1, controller.getFilms().size());
    }

    @Test
    void update_shouldUpdateMovieData() {
        Film thisFilm = new Film(1, "Movie", "I cried at the end, it was very thoughtful",
                LocalDate.of(2020, 2, 2), 120, Set.of(1L));
        controller.create(film);
        controller.update(thisFilm);

        Assertions.assertEquals("I cried at the end, it was very thoughtful", thisFilm.getDescription());
        Assertions.assertEquals(1, controller.getFilms().size());
    }

    @Test
    void create_shouldNotAddAMovieWithAnEmptyName() {
        film.setName("");

        Assertions.assertThrows(ValidationException.class, () -> controller.create(film));
        Assertions.assertEquals(0, controller.getFilms().size());
    }

    @Test
    void create_shouldNotAddAMovieWithDescriptionMoreThan200() {
        film.setDescription("This is the most amazing and terrifying movie in my life. I love scary movies," +
                "but I've never seen such precise details of serial killers doing thier job." +
                "You should deffinately see this one. Actually, this movie was based on a true story. Creepy...");

        Assertions.assertThrows(ValidationException.class, () -> controller.create(film));
        Assertions.assertEquals(0, controller.getFilms().size());
    }

    @Test
    void create_shouldNotAddAMovieWithDateReleaseMoreThan1895() {
        film.setReleaseDate(LocalDate.of(1891, 2, 2));

        Assertions.assertThrows(ValidationException.class, () -> controller.create(film));
        Assertions.assertEquals(0, controller.getFilms().size());
    }

    @Test
    void create_shouldNotAddAMovieIfDurationIsMoreThan0() {
        film.setDuration(-15);

        Assertions.assertThrows(ValidationException.class, () -> controller.create(film));
        Assertions.assertEquals(0, controller.getFilms().size());
    }

}