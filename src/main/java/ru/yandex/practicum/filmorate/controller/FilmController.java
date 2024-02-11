package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
public class FilmController {
    private final HashMap<Integer, Film> filmsHashMap = new HashMap<>();
    private int id = 0;

    @ResponseBody
    @PostMapping(value = "/filmHashMap")
    public Film create(@Valid @RequestBody Film film) {
        filmValidation(film);
        filmsHashMap.put(film.getId(), film);
        log.info("'{}' фильм добавлен в библиотеку с id '{}'", film.getName(), film.getId());
        return film;
    }

    @ResponseBody
    @GetMapping("/films")
    public List<Film> getFilms() {
        log.info("Количество фильмов в билиотеке '{}'", filmsHashMap.size());
        return new ArrayList<>(filmsHashMap.values());
    }

    @ResponseBody
    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film film) {
        filmValidation(film);
        if (filmsHashMap.containsKey(film.getId())) {
            filmsHashMap.put(film.getId(), film);
            log.info("'{}' фильм обновлен в библиотеке с id '{}'", film.getName(), film.getId());
        } else {
            throw new ValidationException("Попытка обновить несуществующий в библиотеке фильм");
        }
        return film;
    }

    private void filmValidation(Film film) {
        if (film.getReleaseDate() == null ||
                film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Неверная дата");
        }
        if (film.getName().isEmpty() || film.getName().isBlank()) {
            throw new ValidationException("Попытка задать пустое имя фильму");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Попытка установить продолжительность меньше нуля");
        }
        if (film.getDescription().length() > 200 || film.getDescription().isEmpty()) {
            throw new ValidationException("Описание бьльше 200 символов или пустое");
        }
        if (film.getId() <= 0) {
            film.setId(++id);
            log.info("У фильма установлен неверный id '{}", film.getId());
        }
    }
}
