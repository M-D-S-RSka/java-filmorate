package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreStorageTest {
    private final GenreDbStorage genreDbStorage;
    Genre genre = Genre.builder()
            .id(4)
            .name("Триллер")
            .build();

    @Test
    public void getGenreFromIdTest() {
        Genre genreTest = genreDbStorage.getGenreById(4);
        Assertions.assertEquals(4, genreTest.getId(), "id не совпадает");
        Assertions.assertEquals(genre, genreTest, "genre не совпадают");
    }

    @Test
    public void getAllGenreTest() {
        List<Genre> genres = genreDbStorage.getAllGenre();
        Assertions.assertEquals(6, genres.size(), "Размер списка не равен 6");
        Assertions.assertEquals(genre, genres.get(3), "Объекты не совпадают");
    }
}