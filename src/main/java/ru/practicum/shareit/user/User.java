package ru.practicum.shareit.user;

import lombok.Data;

@Data
public class User {
    private long id;
    private String name;
    private String email;
}