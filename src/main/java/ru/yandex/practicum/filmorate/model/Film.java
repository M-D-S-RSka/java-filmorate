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

    @Min(1)
    @Max(200)
    private String description;

    @Positive
    private LocalDate releaseDate;

    @PositiveOrZero
    private int duration;
}