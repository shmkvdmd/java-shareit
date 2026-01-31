package ru.practicum.shareit.comment.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentDto(
        Long id,
        String text,
        String authorName,
        LocalDateTime created
) {
}
