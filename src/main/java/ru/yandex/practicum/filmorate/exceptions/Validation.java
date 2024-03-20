package ru.yandex.practicum.filmorate.exceptions;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
public class Validation {
    public void validationUser(User user) throws ValidationException {
        if(user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ValidationException("email не может быть пустым и должен содержать символ @");
        } else if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("Login не может быть пустым или содержать пробелы");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Birthday не может быть в будущем");
        }
    }
    public void validationFilm(Film film) throws ValidationException {
        if (film.getName().isEmpty()) {
            throw new ValidationException("В названии фильма ничего не указано");
        } else if (film.getDescription().length() > 200) {
            throw new ValidationException("В описании фильма больше 200 символов");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата выхода фильма не может быть раньше 28.12.1895г");
        } else if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма не может быть отрицательной");
        } else if (film.getMpa() == null) {
            throw new ValidationException("В фильме не указан рейтинг");
        }
    }
}