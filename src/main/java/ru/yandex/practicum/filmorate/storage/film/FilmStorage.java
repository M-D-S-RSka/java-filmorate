package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film addFilm(Film film);

    Film getFilmById(Long id);

    List<Film> getFilms();

    Film updateFilm(Film film);

    List<Film> getTopFilms(int count);
}