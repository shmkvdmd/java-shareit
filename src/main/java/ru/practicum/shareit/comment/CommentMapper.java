package ru.practicum.shareit.comment;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

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

    public Comment toEntity(CommentDto dto, Item item, User creator) {
        return Comment.builder()
                .id(dto.id())
                .text(dto.text())
                .item(item)
                .creator(creator)
                .created(dto.created() != null ? dto.created() : LocalDateTime.now())
                .build();
    }
}
