package com.progresssoft.analyze_fx_deals.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RequestDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testGettersAndSetters() {
        RequestDTO dto = new RequestDTO();

        dto.setDealUniqueId("DEAL001");
        dto.setFromCurrencyIsoCode("USD");
        dto.setToCurrencyIsoCode("EUR");
        dto.setDealTimestamp(LocalDateTime.of(2024, 1, 15, 10, 30));
        dto.setDealAmount(new BigDecimal("1000.00"));

        assertEquals("DEAL001", dto.getDealUniqueId());
        assertEquals("USD", dto.getFromCurrencyIsoCode());
        assertEquals("EUR", dto.getToCurrencyIsoCode());
        assertEquals(LocalDateTime.of(2024, 1, 15, 10, 30), dto.getDealTimestamp());
        assertEquals(new BigDecimal("1000.00"), dto.getDealAmount());
    }

    @Test
    void testValidDTO() {
        RequestDTO dto = new RequestDTO();
        dto.setDealUniqueId("DEAL001");
        dto.setFromCurrencyIsoCode("USD");
        dto.setToCurrencyIsoCode("EUR");
        dto.setDealTimestamp(LocalDateTime.of(2024, 1, 15, 10, 30));
        dto.setDealAmount(new BigDecimal("1000.00"));

        Set<ConstraintViolation<RequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testToString() {
        RequestDTO dto = new RequestDTO();
        dto.setDealUniqueId("DEAL001");
        dto.setFromCurrencyIsoCode("USD");

        String result = dto.toString();

        assertNotNull(result);
    }

    @Test
    void testEqualsAndHashCode() {
        RequestDTO dto1 = new RequestDTO();
        dto1.setDealUniqueId("DEAL001");
        dto1.setFromCurrencyIsoCode("USD");

        RequestDTO dto2 = new RequestDTO();
        dto2.setDealUniqueId("DEAL001");
        dto2.setFromCurrencyIsoCode("USD");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testNotEquals() {
        RequestDTO dto1 = new RequestDTO();
        dto1.setDealUniqueId("DEAL001");

        RequestDTO dto2 = new RequestDTO();
        dto2.setDealUniqueId("DEAL002");

        assertNotEquals(dto1, dto2);
    }
}
