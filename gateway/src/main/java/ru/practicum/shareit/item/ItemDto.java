package ru.practicum.shareit.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ItemDto(
        Long id,
        @NotBlank(groups = Create.class)
        String name,
        @NotBlank(groups = Create.class)
        String description,
        @NotNull(groups = Create.class)
        Boolean available,
        Long requestId
) {
    public interface Create {
    }

    public interface Update {
    }
}
