package ru.practicum.shareit.comment.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@JsonTest
class CreateCommentDtoJsonTest {

    @Autowired
    private ObjectMapper objectMapper;

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldFailBlankText() throws Exception {
        String json = "{\"text\":\"\"}";

        CreateCommentDto dto = objectMapper.readValue(json, CreateCommentDto.class);

        Set<ConstraintViolation<CreateCommentDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailNullText() throws Exception {
        String json = "{\"text\":null}";

        CreateCommentDto dto = objectMapper.readValue(json, CreateCommentDto.class);

        Set<ConstraintViolation<CreateCommentDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldPassValidDto() throws Exception {
        String json = "{\"text\":\"test\"}";

        CreateCommentDto dto = objectMapper.readValue(json, CreateCommentDto.class);

        Set<ConstraintViolation<CreateCommentDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }
}