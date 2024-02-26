package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.markers.Marker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class Film {
    @NotNull(groups = {Marker.Update.class})
    private long id;

    @NotBlank
    private String name;

    @Size(min = 1, max = 200)
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate releaseDate;

    @PositiveOrZero
    private int duration;

    @JsonIgnoreProperties({"likesIds"})
    private Set<Long> likesIds;

    public void addLike(Long id) {
        if (likesIds == null) {
            likesIds = new HashSet<>();
        }
        likesIds.add(id);
    }

    public void deleteLike(Long id) {
        if (likesIds == null) {
            likesIds = new HashSet<>();
        }
        likesIds.remove(id);
    }
}