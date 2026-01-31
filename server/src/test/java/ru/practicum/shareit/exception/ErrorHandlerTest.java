package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorHandlerTest {

    private final ErrorHandler handler = new ErrorHandler();

    @Test
    void handleNotFound_shouldReturn404() {
        NotFoundException ex = new NotFoundException("Not found");
        ErrorResponse response = handler.handleNotFound(ex);
        assertEquals("Not found", response.error());
    }

    @Test
    void handleEmailExists_shouldReturn409() {
        EmailAlreadyExistsException ex = new EmailAlreadyExistsException("Conflict");
        ErrorResponse response = handler.handleEmailExists(ex);
        assertEquals("Conflict", response.error());
    }

    @Test
    void handleBadRequest_shouldReturn400() {
        IllegalArgumentException ex = new IllegalArgumentException("Bad request");
        ErrorResponse response = handler.handleBadRequest(ex);
        assertEquals("Bad request", response.error());
    }

    @Test
    void handleNotImplemented_shouldReturn501() {
        NotImplementedException ex = new NotImplementedException("Unknown state");
        ErrorResponse response = handler.handleNotImplemented(ex);
        assertEquals("Unknown state", response.error());
    }
}