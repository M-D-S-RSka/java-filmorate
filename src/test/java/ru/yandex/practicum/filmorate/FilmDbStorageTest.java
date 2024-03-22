package ru.yandex.practicum.filmorate;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {

    private final JdbcTemplate jdbcTemplate;
    private final FilmDbStorage filmDbStorage;
    private final FilmService filmService;
    protected Film film;

    @BeforeEach
    void initDbFilm() {
        if (jdbcTemplate.queryForObject("SELECT COUNT(film_id) FROM films", Integer.class) == 0) {
            String sqlQuery = "INSERT INTO films (name, description, release_date, duration, mpa_id) VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(sqlQuery, "film1", "film1description", LocalDate.now(), 100, 3);
            String sqlQuery1 = "INSERT INTO users (email, login, name, birthday)" + "VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(sqlQuery1, "user@yandex", "user.Login", "userName",
                    LocalDate.now());
        }
        Mpa mpa = new Mpa(1, "G");
        LinkedHashSet<Genre> genre = new LinkedHashSet<Genre>(1);
        film = new Film(2, "film2", "description", LocalDate.now(), 1, Set.of(1L), mpa,
                genre);
    }

    @Test
    public void addFilmTest() {
        filmDbStorage.addFilm(film);
        Film film1 = filmDbStorage.getFilmById(2L);
        Assertions.assertEquals(2, film1.getId(), "Идентификатор не совпадает");
        Assertions.assertEquals("film2", film1.getName(), "Название фильма не совпадает");
        Assertions.assertEquals(1, film1.getDuration(), "Продолжительность фильма не совпадает");
    }

    @Test
    public void updateFilmTest() {
        film.setDuration(2);
        filmDbStorage.updateFilm(film);
        Film film1 = filmDbStorage.getFilmById(2L);
        Assertions.assertEquals(2, film1.getId(), "Идентификатор не совпадает");
        Assertions.assertEquals("film2", film1.getName(), "Название фильма не совпадает");
        Assertions.assertEquals(2, film1.getDuration(), "Продолжительность фильма не совпадает");
    }

    @Test
    public void getFilmsTest() {
        List<Film> filmList = filmDbStorage.getFilms();
        Assertions.assertEquals(2, filmList.size(), "Длина списка не совпадает");
    }

    @Test
    public void getFilmFromIdTest() {
        Film film2 = filmDbStorage.getFilmById(1L);
        Assertions.assertEquals(1, film2.getId(), "id фильма не совпадает с ожидаемым");
        Assertions.assertEquals("film1", film2.getName(), "Название фильма не совпадает");
        Assertions.assertEquals(100, film2.getDuration(), "Продолжительность фильма не совпадает");
    }

    @Test
    public void putLikeToFilmTest() {
        filmService.addLikeFilm(1L, 1L);
        SqlRowSet likesRows = jdbcTemplate.queryForRowSet("select * from likes where user_id = ? ", 1);
        if (likesRows.next()) {
            Assertions.assertEquals(1, likesRows.getLong(1), "id фильма не совпадает");
            Assertions.assertEquals(1, likesRows.getLong(2), "id пользователя не совпадает");
        }
    }
}
