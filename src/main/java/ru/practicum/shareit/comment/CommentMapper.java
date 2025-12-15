package ru.practicum.shareit.comment;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CreateCommentDto;

@Component
public class CommentMapper {
    public CommentDto toDto(Comment comment) {
        String authorName = comment.getCreator() != null ? comment.getCreator().getName() : null;
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                authorName,
                comment.getCreated()
        );
    }

    public Comment toEntity(CreateCommentDto dto) {
        return Comment.builder()
                .text(dto.text())
                .build();
    }
}
