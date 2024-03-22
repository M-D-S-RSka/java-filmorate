package ru.yandex.practicum.filmorate.storage.like;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;

@Primary
@Repository
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFilmLike(Long filmId, Long userId) {
        String sqlQuery = "INSERT INTO Likes (film_id, user_id) VALUES (?, ?)";
        try {
            jdbcTemplate.update(sqlQuery, filmId, userId);
        } catch (DataAccessException e) {
            throw new NotFoundException("Фильм с filmId " + filmId + " не найден");
        }
    }

    @Override
    public Long getFilmLikeCount(Long filmId) {
        String sqlQuery = "SELECT COUNT(*) FROM Likes WHERE film_id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, Long.class, filmId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Фильм с filmId" + filmId + " не найден");
        }
    }

    @Override
    public void removeFilmLike(Long filmId, Long userId) {
        String sqlQuery = "DELETE FROM Likes WHERE film_id = ? AND user_id = ?";
        int resultUpdate = jdbcTemplate.update(sqlQuery, filmId, userId);
        if (resultUpdate == 0) {
            throw new NotFoundException("Фильм с filmId" + filmId + " не найден");
        }
    }
}