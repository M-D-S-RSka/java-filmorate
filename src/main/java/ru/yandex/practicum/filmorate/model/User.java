package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class User {
    private int id;

    @Email
    private String email;

    @NotBlank
    private String login;
    private String name;

    @PastOrPresent
    private LocalDate birthday;
}