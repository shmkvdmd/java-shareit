package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ItemRequestDto(
        Long id,
        @NotBlank String description,
        Long requestorId,
        LocalDateTime created
) {
}