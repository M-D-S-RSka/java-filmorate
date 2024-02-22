package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    @Autowired
    FilmStorage filmStorage;
    @Autowired
    UserStorage userStorage;

    public List<Film> getTopFilms(Long count) {
        return filmStorage.getFilms().stream().sorted((film1, film2) ->
                        film2.getLikesIds().size() - film1.getLikesIds().size())
                .limit(count).collect(Collectors.toList());
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
}
