package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.markers.Marker.Update;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RequestMapping("/films")
@RestController
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @PostMapping
    public Film create(@Validated @RequestBody Film film) {
        log.info("'{}' фильм добавлен в библиотеку с id '{}'", film.getName(), film.getId());
        return filmService.create(film);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Long id) {
        log.info("Поступил запрос на получение фильма по id '{}'", filmService.getFilmById(id));
        return filmService.getFilmById(id);
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("Количество фильмов '{}'", filmService.getFilms().size());
        return filmService.getFilms();
    }

    @PutMapping
    public Film update(@Validated(Update.class) @RequestBody Film film) {
        log.info("'{}' фильм обновлен в библиотеке с id '{}'", film.getName(), film.getId());
        return filmService.updateFilm(film);
    }

    @Validated
    @GetMapping("/popular")
    public List<Film> getBestFilms(@RequestParam(value = "count", defaultValue = "10") @Positive Long count) {
        log.info("Поступил запрос на получение списка популярных фильмов.");
        return filmService.getTopFilms(count);
    }

    @PutMapping("/{id}/like/{filmId}")
    public void like(@PathVariable(value = "id") Long id, @PathVariable Long filmId) {
        log.info("Поступил запрос на присвоение лайка '{}' фильму с filmId '{}'", id, filmId);
        filmService.addLikeFilm(id, filmId);
    }

    @DeleteMapping("/{id}/like/{filmId}")
    public void deleteLike(@PathVariable(value = "id") Long id, @PathVariable Long filmId) {
        log.info("Поступил запрос на удаление лайка '{}' у фильма с filmId '{}'", id, filmId);
        filmService.deleteLikeFilm(filmId, id);
    }
}