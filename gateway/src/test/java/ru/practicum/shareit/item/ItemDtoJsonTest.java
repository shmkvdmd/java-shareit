package ru.practicum.shareit.item;

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
class ItemDtoJsonTest {

    @Autowired
    private ObjectMapper objectMapper;

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldFailBlankName() throws Exception {
        String json = "{\"name\":\"\",\"description\":\"desc\",\"available\":true}";

        ItemDto dto = objectMapper.readValue(json, ItemDto.class);

        Set<ConstraintViolation<ItemDto>> violations = validator.validate(dto, ItemDto.Create.class);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailBlankDescription() throws Exception {
        String json = "{\"name\":\"name\",\"description\":\"\",\"available\":true}";

        ItemDto dto = objectMapper.readValue(json, ItemDto.class);

        Set<ConstraintViolation<ItemDto>> violations = validator.validate(dto, ItemDto.Create.class);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailNullAvailable() throws Exception {
        String json = "{\"name\":\"name\",\"description\":\"desc\",\"available\":null}";

        ItemDto dto = objectMapper.readValue(json, ItemDto.class);

        Set<ConstraintViolation<ItemDto>> violations = validator.validate(dto, ItemDto.Create.class);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldPassValidDtoForCreate() throws Exception {
        String json = "{\"name\":\"item\",\"description\":\"desc\",\"available\":true}";

        ItemDto dto = objectMapper.readValue(json, ItemDto.class);

        Set<ConstraintViolation<ItemDto>> violations = validator.validate(dto, ItemDto.Create.class);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldPassPartialUpdate() throws Exception {
        String json = "{\"name\":\"\",\"description\":null,\"available\":null}";

        ItemDto dto = objectMapper.readValue(json, ItemDto.class);

        Set<ConstraintViolation<ItemDto>> violations = validator.validate(dto, ItemDto.Update.class);

        assertTrue(violations.isEmpty());
    }
}