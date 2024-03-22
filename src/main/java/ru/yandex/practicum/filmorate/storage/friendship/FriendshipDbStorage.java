package ru.yandex.practicum.filmorate.storage.friendship;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mapper.Mapper;

import java.util.List;

@Primary
@Repository
@RequiredArgsConstructor
public class FriendshipDbStorage implements FriendshipStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(Long userId, Long friendId) {
        String sqlQuery = "INSERT INTO Friendship (user_id, friend_id) " +
                "VALUES (?, ?)";
        try {
            jdbcTemplate.update(sqlQuery, userId, friendId);
        } catch (DataAccessException e) {
            throw new NotFoundException("Пользователь " + userId + " не найден");
        }
    }

    @Override
    public List<User> getFriendsIds(Long userId) {
        String sqlQuery = "SELECT Users.user_id, email, login, name, birthday " +
                "FROM Users " +
                "LEFT JOIN Friendship f on Users.user_id = f.friend_id " +
                "where f.user_id = ?";
        return jdbcTemplate.query(sqlQuery, Mapper::userMapper, userId);
    }

    @Override
    public List<User> getFriendship(Long userId, Long friendId) {
        String sqlQuery = "SELECT u.user_id, email, login, name, birthday " +
                "FROM Friendship AS f " +
                "LEFT JOIN Users u ON u.user_id = f.friend_id " +
                "WHERE f.user_id = ? AND f.friend_id IN ( " +
                "SELECT friend_id " +
                "FROM Friendship AS f " +
                "LEFT JOIN Users AS u ON u.user_id = f.friend_id " +
                "WHERE f.user_id = ? )";

        return jdbcTemplate.query(sqlQuery, Mapper::userMapper, userId, friendId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        String sqlQuery = "DELETE FROM Friendship WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }
}