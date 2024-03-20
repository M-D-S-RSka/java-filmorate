package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface GenreStorage {

    Genre getGenreById(int id);

    List<Genre> getAllGenre();

    void addFilmGenre(Long filmId, Set<Genre> genres);

    List<Genre> getFilmGenre(Long filmId);

    void deleteFilmGenre(Long filmId);

    Map<Long, LinkedHashSet<Genre>> getAllFilmsGenres();
}