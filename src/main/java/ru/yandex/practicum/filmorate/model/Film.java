package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.annootation.ValidReleaseDate;
import ru.yandex.practicum.filmorate.markers.Marker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
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
    @ValidReleaseDate
    private LocalDate releaseDate;

    @PositiveOrZero
    private int duration;

    @JsonIgnoreProperties({"likesIds"})
    private Set<Long> likesIds;

    private Mpa mpa;

    private LinkedHashSet<Genre> genres;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        values.put("description", description);
        values.put("release_date", releaseDate);
        values.put("duration", duration);
        values.put("mpa_id", mpa.getId());
        return values;
    }
}