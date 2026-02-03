package ru.practicum.shareit.request.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ItemRequestDto(
        Long id,
        String description,
        Long requestorId,
        LocalDateTime created
) {
}