package ru.practicum.shareit.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ItemDto(
        Long id,

        @NotBlank(message = "Name must not be empty")
        String name,

        @NotBlank(message = "Description must not be empty")
        String description,

        @NotNull(message = "Available must not be null")
        Boolean available,
        Long ownerId,
        Long requestId) {
}
