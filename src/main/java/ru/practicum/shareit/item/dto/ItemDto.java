package ru.practicum.shareit.item.dto;

import jdk.jfr.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class ItemDto {

    private Long id;

    @NotBlank(message = "Item's name  is missing")
    private String name;

    @NotEmpty(message = "Item's description can't be empty")
    private String description;

    @BooleanFlag
    @NotNull(message = "Item's available status can't be null")
    private Boolean available;

    private User owner;

    private Long requestId;

    public ItemDto() {

    }

    public ItemDto(String name, String description, Boolean available, User owner, long request) {
    }
}