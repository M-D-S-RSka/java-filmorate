package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final GenreStorage genreStorage;
    private final LikeStorage likeStorage;

    public Film addFilm(Film film) {
        filmStorage.addFilm(film);
        return film;
    }

    public Film getFilmById(Long id) {
        Film film = filmStorage.getFilmById(id);
        film.setGenres(new LinkedHashSet<Genre>(genreStorage.getFilmGenre(film.getId())));
        return film;
    }

    public List<Film> getFilms() {
        List<Film> films = filmStorage.getFilms();
        Map<Long, LinkedHashSet<Genre>> allFilmsGenres = genreStorage.getAllFilmsGenres();
        System.out.println(allFilmsGenres);
        for (Film film : films) {
            if (allFilmsGenres.containsKey(film.getId())) {
                film.setGenres(allFilmsGenres.get(film.getId()));
            } else {
                film.setGenres(new LinkedHashSet<Genre>());
            }
        }
        return films;
    }

    public Film updateFilm(@NotNull Film film) {
        genreStorage.deleteFilmGenre(film.getId());
        genreStorage.addFilmGenre(film.getId(), film.getGenres());
        filmStorage.updateFilm(film);
        return film;
    }

    public List<Film> getTopFilms(int count) {
        List<Film> films = filmStorage.getTopFilms(count);
        for (Film film : films) {
            film.setGenres(new LinkedHashSet<Genre>(genreStorage.getFilmGenre(film.getId())));
        }
        return films;
    }

    public void addLikeFilm(Long filmId, Long userId) {
        likeStorage.addFilmLike(filmId, userId);
        filmStorage.getFilmById(filmId);
    }

    public void removeFilmLike(Long filmId, Long userId) {
        likeStorage.removeFilmLike(filmId, userId);
        filmStorage.getFilmById(filmId);
    }
}