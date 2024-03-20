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
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private final UserDbStorage userDbStorage;
    private final UserService userService;
    protected User user;

    @BeforeEach
    void initDbUser() {
        if (jdbcTemplate.queryForObject("SELECT COUNT(user_id) FROM Users", Integer.class) == 0) {
            String sqlQuery = "INSERT INTO users (email, login, name, birthday)" + "VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(sqlQuery, "user@yandex", "user.Login", "userName",
                    LocalDate.now());
        }
        user = new User(1L, "email@email.ru", "login2", "name2",
                LocalDate.of(1985, 3, 15), Set.of(1L));
    }

    @Test
    public void createUserTest() {
        userDbStorage.addUser(user);
        User user1 = userDbStorage.getUserById(2L);
        Assertions.assertEquals(2, user1.getId(), "Id не совпадает");
        Assertions.assertEquals("email@email.ru", user1.getEmail(), "email не совпадает");
        Assertions.assertEquals("login2", user1.getLogin(), "login не совпадает");
        Assertions.assertEquals("name2", user1.getName(), "Name не совпадает");
    }

    @Test
    public void updateUserTest() {
        user.setName("nameTest");
        userDbStorage.updateUser(user);
        User user1 = userDbStorage.getUserById(1L);
        Assertions.assertEquals(1, user1.getId(), "Id не совпадает");
        Assertions.assertEquals("email@email.ru", user1.getEmail(), "email не совпадает");
        Assertions.assertEquals("login2", user1.getLogin(), "login не совпадает");
        Assertions.assertEquals("nameTest", user1.getName(), "Name не совпадает");
    }

    @Test
    public void getUserFromIdTest() {
        User user = userDbStorage.getUserById(1L);
        Assertions.assertEquals(1, user.getId(), "Id не совпадает");

    }

    @Test
    public void getUsersTest() {
        List<User> userList = userDbStorage.getUsers();
        Assertions.assertEquals(2, userList.size(), "Длина списка не совпадает");
    }

    @Test
    public void addFriendTest() {
        String sqlQuery = "INSERT INTO users (email, login, name, birthday)" + "VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(), user.getName(),
                LocalDate.now());
        userService.addFriend(1L, 2L);
        SqlRowSet friendRows = jdbcTemplate.queryForRowSet("select * from Friendship where user_id = ? ", 1);
        if (friendRows.next()) {
            Assertions.assertEquals(2, friendRows.getLong(2), "Id друга не совпадает");
            Assertions.assertTrue(friendRows.getBoolean(1));
        }
    }

    @Test
    public void getSetListFriendsTest() {
        List<User> listFriends = userService.getFriendsIds(1L);
        Assertions.assertFalse(listFriends.isEmpty(), "Список пустой");
        Assertions.assertEquals(1, listFriends.size(), "Длина списка друзей не совпадает");
        Assertions.assertFalse(listFriends.contains(user), "Список не содержит id = 2");
    }

    @Test
    public void deleteFriendTest() {
        userService.deleteFriend(1L, 2L);
        String sqlQuery = "SELECT COUNT(user_id) FROM Friendship";
        Assertions.assertEquals(0, jdbcTemplate.queryForObject(sqlQuery, Integer.class),
                "Список друзей имеет записи");
    }
}