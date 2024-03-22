package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mapper.Mapper;

import java.sql.ResultSet;
import java.util.*;

@Primary
@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre getGenreById(int id) {
        String sqlQuery = "SELECT * FROM Genre WHERE genre_id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, Mapper::genreMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Жанр с id = " + id + " не найден");
        }
    }

    @Override
    public List<Genre> getAllGenre() {
        String sqlQuery = "SELECT * FROM Genre";
        return jdbcTemplate.query(sqlQuery, Mapper::genreMapper);
    }

    @Override
    public List<Genre> getFilmGenre(Long filmId) {
        String sqlQuery = "SELECT * FROM Genre " +
                "WHERE genre_id IN (SELECT genre_id FROM Film_genre WHERE film_id = ?)";
        return jdbcTemplate.query(sqlQuery, Mapper::genreMapper, filmId);
    }

    @Override
    public void deleteFilmGenre(Long filmId) {
        String sqlQuery = "DELETE FROM Film_genre WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    @Override
    public void addFilmGenre(Long filmId, Set<Genre> genres) {
        if (genres != null && !genres.isEmpty()) {
            String sqlQuery = "MERGE INTO Film_genre (film_id, genre_id) VALUES (?, ?)";
            jdbcTemplate.batchUpdate(sqlQuery, genres, genres.size(),
                    (ps, genre) -> {
                        ps.setLong(1, filmId);
                        ps.setInt(2, genre.getId());
                    });
        }
    }

    @Override
    public Map<Long, LinkedHashSet<Genre>> getAllFilmsGenres() {
        String sqlQuery = "SELECT f.film_id, g.genre_id, g.genre_name " +
                "FROM Film_genre f " +
                "LEFT JOIN Genre g ON f.genre_id = g.genre_id " +
                "ORDER BY f.film_id";
        Map<Long, LinkedHashSet<Genre>> results = new HashMap<>();
        jdbcTemplate.query(sqlQuery, (ResultSet rs) -> {
            if (!results.containsKey(rs.getLong("film_id"))) {
                results.put(rs.getLong("film_id"), new LinkedHashSet<Genre>());
            }
            results.get(rs.getLong("film_id")).add(Genre.builder()
                    .id(rs.getInt("genre_id"))
                    .name(rs.getString("genre_name"))
                    .build());
        });
        return results;
    }
}
