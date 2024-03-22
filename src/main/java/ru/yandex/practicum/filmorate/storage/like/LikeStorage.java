package ru.yandex.practicum.filmorate.storage.like;

public interface LikeStorage {
    void addFilmLike(Long filmId, Long userId);

    void removeFilmLike(Long filmId, Long userId);

    Long getFilmLikeCount(Long filmId);
}
