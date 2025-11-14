package com.progresssoft.analyze_fx_deals.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleIllegalArgument_ShouldReturnBadRequest() {
        // Given
        IllegalArgumentException exception = new IllegalArgumentException("Invalid currency code");

        // When
        ResponseEntity<ProblemDetail> response = exceptionHandler.handleIllegalArgument(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid Request Data", response.getBody().getTitle());
        assertEquals("Invalid currency code", response.getBody().getDetail());
        assertTrue(response.getBody().getProperties().containsKey("timestamp"));
    }

    @Test
    void handleIllegalArgument_WithNullMessage_ShouldHandleGracefully() {
        // Given
        IllegalArgumentException exception = new IllegalArgumentException();

        // When
        ResponseEntity<ProblemDetail> response = exceptionHandler.handleIllegalArgument(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void handleGeneralException_ShouldReturnInternalServerError() {
        // Given
        Exception exception = new Exception("Unexpected error");

        // When
        ResponseEntity<ProblemDetail> response = exceptionHandler.handleGeneralException(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Internal Server Error", response.getBody().getTitle());
        assertEquals("Something went wrong. Please try again later.", response.getBody().getDetail());
        assertTrue(response.getBody().getProperties().containsKey("timestamp"));
    }

    @Test
    void handleGeneralException_WithRuntimeException_ShouldHandleCorrectly() {
        // Given
        RuntimeException exception = new RuntimeException("Runtime error");

        // When
        ResponseEntity<ProblemDetail> response = exceptionHandler.handleGeneralException(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Internal Server Error", response.getBody().getTitle());
    }

    @Test
    void handleGeneralException_WithNullPointerException_ShouldHandleCorrectly() {
        // Given
        NullPointerException exception = new NullPointerException("Null value encountered");

        // When
        ResponseEntity<ProblemDetail> response = exceptionHandler.handleGeneralException(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void problemDetail_ShouldHaveCorrectStatusCode() {
        // Given
        IllegalArgumentException exception = new IllegalArgumentException("Test");

        // When
        ResponseEntity<ProblemDetail> response = exceptionHandler.handleIllegalArgument(exception);

        // Then
        assertEquals(400, response.getBody().getStatus());
    }
}
