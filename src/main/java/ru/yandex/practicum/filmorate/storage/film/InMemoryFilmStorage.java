package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long id = 1;

    public long getId() {
        return id++;
    }

    @Override
    public Film addFilm(Film film) {
        FilmService.filmValidation(film);
        film.setLikesIds(new HashSet<>());
        film.setId(getId());
        films.put(film.getId(), film);
        log.info("'{}' фильм добавлен в библиотеку с id '{}'", film.getName(), film.getId());
        return film;
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film updateFilm(Film film) {
        FilmService.filmValidation(film);
        film.setLikesIds(new HashSet<>());
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("'{}' фильм обновлен в библиотеке с id '{}'", film.getName(), film.getId());
        } else throw new NotFoundException("Попытка обновить несуществующий в библиотеке фильм");
        return film;
    }

    @Override
    public Film getFilmById(Long id) {
        if (films.containsKey(id)) {
            log.info("Поступил запрос на получение фильма по id '{}'", id);
            return films.get(id);
        } else throw new NotFoundException("Фильм не найден по id");
    }
}