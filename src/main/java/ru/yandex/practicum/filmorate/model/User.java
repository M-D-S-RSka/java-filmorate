package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.markers.Marker;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class User {

    @NotNull(groups = {Marker.Update.class})
    private long id;

    @Email
    private String email;

    @NotBlank
    private String login;
    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @PastOrPresent
    private LocalDate birthday;

    private Set<Long> friendsIds;
    private Set<Long> likedFilms;


    public void addLikedFilm(Long filmId) {
        if (likedFilms == null) {
            likedFilms = new HashSet<>();
        }
        likedFilms.add(filmId);
    }

    public void deleteLikedFilm(Long filmId) {
        if (likedFilms == null) {
            likedFilms = new HashSet<>();
        }
        likedFilms.remove(filmId);
    }

    public void addFriends(Long friendId) {
        if (friendsIds == null) {
            friendsIds = new HashSet<>();
        }
        friendsIds.add(friendId);
    }

    public void deleteFriends(Long strangerId) {
        if (friendsIds == null) {
            friendsIds = new HashSet<>();
        }
        friendsIds.remove(strangerId);
    }
}