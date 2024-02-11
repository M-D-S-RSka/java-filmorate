package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class Film {
    private int id;

    @NotBlank
    private String name;

    @Size(min = 1, max = 200)
    private String description;

    private LocalDate releaseDate;

    @PositiveOrZero
    private int duration;
}