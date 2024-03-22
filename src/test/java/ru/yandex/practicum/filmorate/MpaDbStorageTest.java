package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaDbStorageTest {
    private final MpaDbStorage mpaDbStorage;
    Mpa mpa = new Mpa(4, "R");

    @Test
    public void getMpaTest() {
        Mpa mpaTest = mpaDbStorage.getMpaById(4);
        Assertions.assertEquals(4, mpaTest.getId(), "id не совпадает");
        Assertions.assertEquals(mpa, mpaTest, "mpa не совпадают");
    }

    @Test
    public void getAllMpaTest() {
        List<Mpa> rates = mpaDbStorage.getAllMpa();
        Assertions.assertEquals(5, rates.size(), "Кол-во больше 6");
        Assertions.assertEquals(mpa, rates.get(3), "mpa не совпадают");
    }
}