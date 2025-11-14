package com.progresssoft.analyze_fx_deals.mapper;

import com.progresssoft.analyze_fx_deals.dto.RequestDTO;
import com.progresssoft.analyze_fx_deals.model.Deal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DealMapperTest {

    private DealMapper dealMapper;

    @BeforeEach
    void setUp() {
        dealMapper = Mappers.getMapper(DealMapper.class);
    }

    @Test
    void toDto_WithValidDeal_ShouldReturnDTO() {
        // Given
        Deal deal = new Deal();
        deal.setDealUniqueId("DEAL001");
        deal.setFromCurrencyIsoCode("USD");
        deal.setToCurrencyIsoCode("EUR");
        deal.setDealTimestamp(LocalDateTime.of(2024, 1, 15, 10, 30));
        deal.setDealAmount(new BigDecimal("1000.00"));

        // When
        RequestDTO dto = dealMapper.toDto(deal);

        // Then
        assertNotNull(dto);
    }

    @Test
    void toDto_WithNullDeal_ShouldReturnNull() {
        // When
        RequestDTO dto = dealMapper.toDto(null);

        // Then
        assertNull(dto);
    }

    @Test
    void toEntity_WithValidDTO_ShouldReturnEntity() {
        // Given
        RequestDTO dto = new RequestDTO();
        dto.setDealUniqueId("DEAL001");
        dto.setFromCurrencyIsoCode("USD");
        dto.setToCurrencyIsoCode("EUR");
        dto.setDealTimestamp(LocalDateTime.of(2024, 1, 15, 10, 30));
        dto.setDealAmount(new BigDecimal("1000.00"));

        // When
        Deal deal = dealMapper.toEntity(dto);

        // Then
        assertNotNull(deal);
    }

    @Test
    void toEntity_WithNullDTO_ShouldReturnNull() {
        // When
        Deal deal = dealMapper.toEntity(null);

        // Then
        assertNull(deal);
    }

    @Test
    void toDto_WithMinimalData_ShouldNotThrowException() {
        // Given
        Deal deal = new Deal();
        deal.setDealUniqueId("DEAL002");

        // When
        RequestDTO dto = dealMapper.toDto(deal);

        // Then
        assertNotNull(dto);
    }

    @Test
    void toEntity_WithMinimalData_ShouldNotThrowException() {
        // Given
        RequestDTO dto = new RequestDTO();
        dto.setDealUniqueId("DEAL002");

        // When
        Deal deal = dealMapper.toEntity(dto);

        // Then
        assertNotNull(deal);
    }

    @Test
    void toDto_WithNullFields_ShouldHandleGracefully() {
        // Given
        Deal deal = new Deal();

        // When
        RequestDTO dto = dealMapper.toDto(deal);

        // Then
        assertNotNull(dto);
    }

    @Test
    void toEntity_WithNullFields_ShouldHandleGracefully() {
        // Given
        RequestDTO dto = new RequestDTO();

        // When
        Deal deal = dealMapper.toEntity(dto);

        // Then
        assertNotNull(deal);
    }
}
