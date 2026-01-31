package ru.practicum.shareit.user;

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
class UserDtoJsonTest {

    @Autowired
    private ObjectMapper objectMapper;

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldFailInvalidEmail() throws Exception {
        String json = "{\"name\":\"test\",\"email\":\"invalid\"}";

        UserDto dto = objectMapper.readValue(json, UserDto.class);

        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailBlankEmail() throws Exception {
        String json = "{\"name\":\"test\",\"email\":\" \"}";

        UserDto dto = objectMapper.readValue(json, UserDto.class);

        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailBlankName() throws Exception {
        String json = "{\"name\":\"\",\"email\":\"test@mail.ru\"}";

        UserDto dto = objectMapper.readValue(json, UserDto.class);

        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldPassValidDto() throws Exception {
        String json = "{\"name\":\"test\",\"email\":\"test@mail.ru\"}";

        UserDto dto = objectMapper.readValue(json, UserDto.class);

        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }
}