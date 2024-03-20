package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mapper.Mapper;

import java.util.List;

@Primary
@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User addUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("Users")
                .usingGeneratedKeyColumns("user_id");
        Long id = (long) simpleJdbcInsert.executeAndReturnKey(user.toMap()).intValue();
        user.setId(id);
        return user;
    }

    @Override
    public User getUserById(Long id) {
        String sqlQuery = "SELECT * FROM Users WHERE user_id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, Mapper::userMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
    }

    @Override
    public List<User> getUsers() {
        String sqlQuery = "SELECT * FROM Users";
        return jdbcTemplate.query(sqlQuery, Mapper::userMapper);
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "UPDATE Users SET email = ?, login = ?, name = ?, birthday = ? " +
                "WHERE user_id = ?";
        int updateResult = jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        if (updateResult == 0) {
            throw new NotFoundException("Пользователь " + user + " не найден");
        }
        return user;
    }

    @Override
    public void deleteUserById(Long id) {
        String sqlQuery = "DELETE FROM Users WHERE user_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }
}