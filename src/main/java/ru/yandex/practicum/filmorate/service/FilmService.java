package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private static final LocalDate date = LocalDate.of(1895, 12, 28);
    private static final int characterCount = 200;
    private static long id = 1;

    //    @Autowired
    private final FilmStorage filmStorage;
    //    @Autowired
    private final UserStorage userStorage;

    public Film create(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film getFilmById(Long id) {
        return filmStorage.getFilmById(id);
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public List<Film> getTopFilms(Long count) {
        return filmStorage.getFilms()
                .stream()
                .sorted((film1, film2) -> film2.getLikesIds().size() - film1.getLikesIds().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    public void addLikeFilm(Long filmId, Long userId) {
        filmStorage.getFilmById(filmId).addLike(userId);
        userStorage.getUserById(userId).addLikedFilm(filmId);
        filmStorage.getFilmById(filmId);
    }

    public void deleteLikeFilm(Long filmId, Long userId) {
        filmStorage.getFilmById(filmId).deleteLike(userId);
        userStorage.getUserById(userId).deleteLikedFilm(filmId);
        filmStorage.getFilmById(filmId);
    }

    public static void filmValidation(Film film) {
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
