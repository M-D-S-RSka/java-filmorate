package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreStorage genreStorage;

    @GetMapping
    public List<Genre> getAllGenre() {
        log.info("Получен запрос на получение списка всех жанров");
        return genreStorage.getAllGenre();
    }

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable("id") int genreId) {
        log.info("Получен запрос на получение жанра по id {}", genreId);
        return genreStorage.getGenreById(genreId);
    }
}
