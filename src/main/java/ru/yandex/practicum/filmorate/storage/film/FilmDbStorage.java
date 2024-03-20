package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.mapper.Mapper;

import java.util.List;

@Slf4j
@Primary
@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film addFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("Films")
                .usingGeneratedKeyColumns("film_id");
        film.setId(simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue());
        log.info("'{}' фильм добавлен в библиотеку с id '{}'", film.getName(), film.getId());
        return film;
    }

    @Override
    public Film getFilmById(Long id) {
        String sqlQuery = "SELECT Films.*, M.* " +
                "FROM Films " +
                "JOIN Mpa M ON M.mpa_id = Films.mpa_id " +
                "WHERE Films.film_id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, Mapper::filmMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Фильм с id=" + id + " не найден");
        }
    }

    @Override
    public List<Film> getFilms() {
        String sqlQuery = "SELECT Films.*, m.* " +
                "FROM Films " +
                "JOIN Mpa m ON m.mpa_id = Films.mpa_id";
        return jdbcTemplate.query(sqlQuery, Mapper::filmMapper);
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "UPDATE Films SET name = ?, description = ?, release_date = ?, " +
                "duration = ?, mpa_id = ?" +
                "WHERE film_id = ?";
        int resultUpdate = jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        if (resultUpdate == 0) {
            throw new NotFoundException("Фильм " + film + " не найден");
        }
        return film;
    }

    @Override
    public List<Film> getTopFilms(int count) {
        String sqlQuery = "SELECT Films.*, m.* " +
                "FROM Films " +
                "LEFT JOIN Likes fl ON films.film_id = fl.film_id " +
                "LEFT JOIN Mpa m on m.mpa_id = films.mpa_id " +
                "GROUP BY films.film_id, fl.film_id IN ( " +
                "SELECT film_id " +
                "FROM Likes " +
                ") " +
                "ORDER BY COUNT(fl.film_id) DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sqlQuery, Mapper::filmMapper, count);
    }
}