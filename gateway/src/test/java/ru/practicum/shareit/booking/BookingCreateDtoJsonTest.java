package ru.practicum.shareit.booking;

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
class BookingCreateDtoJsonTest {

    @Autowired
    private ObjectMapper objectMapper;

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldFailPastStart() throws Exception {
        String json = "{\"start\":\"2020-01-01T10:00:00\",\"end\":\"2026-02-01T10:00:00\",\"itemId\":1}";

        BookingCreateDto dto = objectMapper.readValue(json, BookingCreateDto.class);

        Set<ConstraintViolation<BookingCreateDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailNotFutureEnd() throws Exception {
        String json = "{\"start\":\"2026-01-01T10:00:00\",\"end\":\"2020-01-01T10:00:00\",\"itemId\":1}";

        BookingCreateDto dto = objectMapper.readValue(json, BookingCreateDto.class);

        Set<ConstraintViolation<BookingCreateDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailNullItemId() throws Exception {
        String json = "{\"start\":\"2026-01-01T10:00:00\",\"end\":\"2026-02-01T10:00:00\",\"itemId\":null}";

        BookingCreateDto dto = objectMapper.readValue(json, BookingCreateDto.class);

        Set<ConstraintViolation<BookingCreateDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldPassValidDto() throws Exception {
        String json = "{\"start\":\"2026-02-01T10:00:00\",\"end\":\"2026-02-02T10:00:00\",\"itemId\":1}";

        BookingCreateDto dto = objectMapper.readValue(json, BookingCreateDto.class);

        Set<ConstraintViolation<BookingCreateDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }
}