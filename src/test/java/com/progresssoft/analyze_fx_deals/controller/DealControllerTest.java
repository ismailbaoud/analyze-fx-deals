package com.progresssoft.analyze_fx_deals.controller;

import com.progresssoft.analyze_fx_deals.model.Deal;
import com.progresssoft.analyze_fx_deals.service.DealService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DealControllerTest {

    @Mock
    private DealService dealService;

    @InjectMocks
    private DealController dealController;

    private Deal testDeal;

    @BeforeEach
    void setUp() {
        testDeal = new Deal();
        testDeal.setDealUniqueId("DEAL001");
        testDeal.setFromCurrencyIsoCode("USD");
        testDeal.setToCurrencyIsoCode("EUR");
        testDeal.setDealTimestamp(LocalDateTime.of(2024, 1, 15, 10, 30));
        testDeal.setDealAmount(new BigDecimal("1000.00"));
    }

    @Test
    void importDeals_WithValidFile_ShouldReturnSuccess() throws Exception {
        String csvContent = "dealUniqueId,fromCurrencyIsoCode,toCurrencyIsoCode,dealTimestamp,dealAmount\n" +
                "DEAL001,USD,EUR,2024-01-15T10:30:00,1000.00";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "deals.csv",
                "text/csv",
                csvContent.getBytes()
        );
        
        List<Deal> deals = Arrays.asList(testDeal);
        when(dealService.importDeals(any())).thenReturn(deals);

        ResponseEntity<List<Deal>> response = dealController.importDealsFile(file);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(dealService, times(1)).importDeals(any());
    }

    @Test
    void importDeals_WithEmptyFile_ShouldReturnBadRequest() throws Exception {
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file",
                "empty.csv",
                "text/csv",
                new byte[0]
        );

        when(dealService.importDeals(any())).thenReturn(Collections.emptyList());

        ResponseEntity<List<Deal>> response = dealController.importDealsFile(emptyFile);

        assertNotNull(response);
        verify(dealService, times(1)).importDeals(any());
    }

    @Test
    void getAllDeals_ShouldReturnListOfDeals() {
        List<Deal> deals = Arrays.asList(testDeal);
        when(dealService.getAllDeals()).thenReturn(deals);

        ResponseEntity<List<Deal>> response = dealController.getAllDeals();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(dealService, times(1)).getAllDeals();
    }

    @Test
    void getAllDeals_WhenEmpty_ShouldReturnEmptyList() {
        when(dealService.getAllDeals()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Deal>> response = dealController.getAllDeals();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void getDealById_WithValidId_ShouldReturnDeal() {
        when(dealService.getDealById("DEAL001")).thenReturn(testDeal);

        ResponseEntity<Deal> response = dealController.getDealById("DEAL001");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testDeal, response.getBody());
        verify(dealService, times(1)).getDealById("DEAL001");
    }

    @Test
    void getDealById_WithNonExistingId_ShouldReturnNotFound() {
        when(dealService.getDealById("NONEXISTENT")).thenReturn(null);

        ResponseEntity<Deal> response = dealController.getDealById("NONEXISTENT");

        assertNotNull(response);
        verify(dealService, times(1)).getDealById("NONEXISTENT");
    }
}
