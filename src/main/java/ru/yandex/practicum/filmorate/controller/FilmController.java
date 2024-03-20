package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.markers.Marker.Update;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RequestMapping("/films")
@RestController
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;
    private final FilmStorage filmStorage;

    @PostMapping
    public Film addFilm(@Validated @RequestBody @NotNull Film film) {
        log.info("'{}' фильм добавлен в библиотеку с id '{}'", film.getName(), film.getId());
        return filmStorage.addFilm(film);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Long id) {
        log.info("Поступил запрос на получение фильма по id '{}'", filmStorage.getFilmById(id));
        return filmStorage.getFilmById(id);
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("Количество фильмов '{}'", filmStorage.getFilms().size());
        return filmStorage.getFilms();
    }

    @PutMapping
    public Film updateFilm(@Validated(Update.class) @RequestBody @NotNull Film film) {
        log.info("'{}' фильм обновлен в библиотеке с id '{}'", film.getName(), film.getId());
        return filmStorage.updateFilm(film);
    }

    @Validated
    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam(value = "count", defaultValue = "10") @Positive int count) {
        log.info("Поступил запрос на получение списка популярных фильмов.");
        return filmStorage.getTopFilms(count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLikeFilm(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Поступил запрос на присвоение лайка '{}' фильму с filmId '{}'", userId, id);
        filmService.addLikeFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable(value = "id") Long id, @PathVariable Long userId) {
        log.info("Поступил запрос на удаление лайка '{}' у фильма с filmId '{}'", userId, id);
        filmService.removeFilmLike(id, userId);
    }
}