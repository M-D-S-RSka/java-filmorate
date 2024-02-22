package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long id = 1;
    private static final LocalDate date = LocalDate.of(1895, 12, 28);
    private static final int characterCount = 200;

    public long getId() {
        return id++;
    }

    @Override
    public Film addFilm(Film film) {
        filmValidation(film);
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
        filmValidation(film);
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
}