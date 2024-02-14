package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.markers.Marker.Update;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();
    private static final LocalDate date = LocalDate.of(1895, 12, 28);
    private static final int characterCount = 200;
    private long id = 1;

    @PostMapping(value = "/films")
    public Film create(@Validated @RequestBody Film film) {
        filmValidation(film);
        film.setId(getId());
        films.put(film.getId(), film);
        log.info("'{}' фильм добавлен в библиотеку с id '{}'", film.getName(), film.getId());
        return film;
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        log.info("Количество фильмов в билиотеке '{}'", films.size());
        return new ArrayList<>(films.values());
    }

    @PutMapping("/films")
    public Film update(@Validated(Update.class) @RequestBody Film film) {
        filmValidation(film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("'{}' фильм обновлен в библиотеке с id '{}'", film.getName(), film.getId());
        } else {
            throw new ValidationException("Попытка обновить несуществующий в библиотеке фильм");
        }
        return film;
    }

    private void filmValidation(Film film) {
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(date)) {
            throw new ValidationException("Неверная дата");
        }
        if (film.getName().isEmpty() || film.getName().isBlank()) {
            throw new ValidationException("Попытка задать пустое имя фильму");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Попытка установить продолжительность меньше нуля");
        }
        if (film.getDescription().length() > characterCount || film.getDescription().isEmpty()) {
            throw new ValidationException("Описание бьльше 200 символов или пустое");
        }
        if (film.getId() <= 0) {
            film.setId(id);
            log.info("У фильма установлен неверный id '{}", film.getId());
        }
    }

    public long getId() {
        return id++;
    }
}