package ru.practicum.shareit.user.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class User {

    private Long id;

    private String name;

    @Email(message = "User's email has wrong format")
    @NotBlank(message = "User's email missing")
    private String email;

    public User() {

    }
}