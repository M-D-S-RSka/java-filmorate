package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mapper.Mapper;

import java.util.List;

@Primary
@Repository
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    public Mpa getMpaById(int mpaId) {
        String sqlQuery = "SELECT * FROM Mpa WHERE mpa_id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, Mapper::mpaMapper, mpaId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Mpa с mpaId = " + mpaId + " не найден");
        }
    }

    @Override
    public List<Mpa> getAllMpa() {
        String sqlQuery = "SELECT * FROM Mpa";
        return jdbcTemplate.query(sqlQuery, Mapper::mpaMapper);
    }
}