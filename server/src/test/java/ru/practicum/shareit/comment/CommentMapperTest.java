package ru.practicum.shareit.comment;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CreateCommentDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CommentMapperTest {

    private final CommentMapper mapper = new CommentMapper();

    @Test
    void toDto_whenCreatorExists_shouldReturnDtoWithAuthorName() {
        User author = User.builder().name("test").build();
        Comment comment = Comment.builder()
                .id(1L)
                .text("text")
                .creator(author)
                .created(LocalDateTime.now())
                .build();

        CommentDto dto = mapper.toDto(comment);

        assertEquals("test", dto.authorName());
        assertEquals("text", dto.text());
    }

    @Test
    void toDto_whenCreatorIsNull_shouldReturnDtoWithNullAuthor() {
        Comment comment = Comment.builder()
                .id(1L)
                .text("a")
                .creator(null)
                .build();

        CommentDto dto = mapper.toDto(comment);

        assertNull(dto.authorName());
    }

    @Test
    void toEntity_shouldMapTextCorrectly() {
        CreateCommentDto dto = new CreateCommentDto("comment");

        Comment entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals("comment", entity.getText());
    }
}