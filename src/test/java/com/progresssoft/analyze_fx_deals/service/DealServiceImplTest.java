package com.progresssoft.analyze_fx_deals.service;

import com.progresssoft.analyze_fx_deals.dto.RequestDTO;
import com.progresssoft.analyze_fx_deals.mapper.DealMapper;
import com.progresssoft.analyze_fx_deals.model.Deal;
import com.progresssoft.analyze_fx_deals.repository.DealRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DealServiceImplTest {

    @Mock
    private DealRepository dealRepository;

    @Mock
    private DealMapper mapper;

    @Mock
    private Validator validator;

    @InjectMocks
    private DealServiceImpl dealService;

    private Deal testDeal;
    private RequestDTO testDTO;

    @BeforeEach
    void setUp() {
        testDeal = new Deal();
        testDeal.setDealUniqueId("DEAL001");
        testDeal.setFromCurrencyIsoCode("USD");
        testDeal.setToCurrencyIsoCode("EUR");
        testDeal.setDealTimestamp(LocalDateTime.of(2024, 1, 15, 10, 30));
        testDeal.setDealAmount(new BigDecimal("1000.00"));

        testDTO = new RequestDTO();
        testDTO.setDealUniqueId("DEAL001");
        testDTO.setFromCurrencyIsoCode("USD");
        testDTO.setToCurrencyIsoCode("EUR");
        testDTO.setDealTimestamp(LocalDateTime.of(2024, 1, 15, 10, 30));
        testDTO.setDealAmount(new BigDecimal("1000.00"));
    }

    @Test
    void importDeals_WithValidFile_ShouldImportDeals() throws IOException {
        // Given
        String csvContent = "dealUniqueId,fromCurrencyIsoCode,toCurrencyIsoCode,dealTimestamp,dealAmount\n" +
                "DEAL001,USD,EUR,2024-01-15T10:30:00,1000.00\n" +
                "DEAL002,GBP,JPY,2024-01-15T11:00:00,2000.00";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "deals.csv",
                "text/csv",
                csvContent.getBytes()
        );

        when(mapper.toEntity(any(RequestDTO.class))).thenReturn(testDeal);
        when(validator.validate(any(RequestDTO.class))).thenReturn(Collections.emptySet());
        when(dealRepository.existsByDealUniqueId(anyString())).thenReturn(false);
        when(dealRepository.save(any(Deal.class))).thenReturn(testDeal);

        // When
        List<Deal> result = dealService.importDeals(file);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(dealRepository, times(2)).save(any(Deal.class));
        verify(validator, times(2)).validate(any(RequestDTO.class));
    }

    @Test
    void importDeals_WithInvalidRowFormat_ShouldThrowException() {
        // Given
        String csvContent = "dealUniqueId,fromCurrencyIsoCode,toCurrencyIsoCode,dealTimestamp,dealAmount\n" +
                "DEAL001,USD,EUR";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "deals.csv",
                "text/csv",
                csvContent.getBytes()
        );

        // When/Then
        assertThrows(IllegalArgumentException.class, () -> dealService.importDeals(file));
        verify(dealRepository, never()).save(any(Deal.class));
    }

    @Test
    void importDeals_WithValidationErrors_ShouldSkipInvalidDeals() throws IOException {
        // Given
        String csvContent = "dealUniqueId,fromCurrencyIsoCode,toCurrencyIsoCode,dealTimestamp,dealAmount\n" +
                "DEAL001,USD,EUR,2024-01-15T10:30:00,1000.00\n" +
                "DEAL002,GBP,JPY,2024-01-15T11:00:00,2000.00";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "deals.csv",
                "text/csv",
                csvContent.getBytes()
        );

        ConstraintViolation<RequestDTO> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("Invalid currency code");
        Set<ConstraintViolation<RequestDTO>> violations = new HashSet<>();
        violations.add(violation);

        when(mapper.toEntity(any(RequestDTO.class))).thenReturn(testDeal);
        when(validator.validate(any(RequestDTO.class)))
                .thenReturn(violations) 
                .thenReturn(Collections.emptySet());  
        when(dealRepository.existsByDealUniqueId(anyString())).thenReturn(false);
        when(dealRepository.save(any(Deal.class))).thenReturn(testDeal);

        // When
        List<Deal> result = dealService.importDeals(file);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size()); 
        verify(dealRepository, times(1)).save(any(Deal.class));
    }

    @Test
    void importDeals_WithDuplicateId_ShouldSkipDuplicates() throws IOException {
        // Given
        String csvContent = "dealUniqueId,fromCurrencyIsoCode,toCurrencyIsoCode,dealTimestamp,dealAmount\n" +
                "DEAL001,USD,EUR,2024-01-15T10:30:00,1000.00\n" +
                "DEAL002,GBP,JPY,2024-01-15T11:00:00,2000.00";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "deals.csv",
                "text/csv",
                csvContent.getBytes()
        );

        when(mapper.toEntity(any(RequestDTO.class))).thenReturn(testDeal);
        when(validator.validate(any(RequestDTO.class))).thenReturn(Collections.emptySet());
        when(dealRepository.existsByDealUniqueId("DEAL001")).thenReturn(true);  
        when(dealRepository.existsByDealUniqueId("DEAL002")).thenReturn(false); 
        when(dealRepository.save(any(Deal.class))).thenReturn(testDeal);

        // When
        List<Deal> result = dealService.importDeals(file);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size()); 
        verify(dealRepository, times(1)).save(any(Deal.class));
    }

    @Test
    void importDeals_WithEmptyFile_ShouldReturnEmptyList() throws IOException {
        // Given
        String csvContent = "dealUniqueId,fromCurrencyIsoCode,toCurrencyIsoCode,dealTimestamp,dealAmount\n";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "empty.csv",
                "text/csv",
                csvContent.getBytes()
        );

        // When
        List<Deal> result = dealService.importDeals(file);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(dealRepository, never()).save(any(Deal.class));
    }

    @Test
    void importDeals_WithOnlyHeaders_ShouldReturnEmptyList() throws IOException {
        // Given
        String csvContent = "dealUniqueId,fromCurrencyIsoCode,toCurrencyIsoCode,dealTimestamp,dealAmount";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "headers.csv",
                "text/csv",
                csvContent.getBytes()
        );

        // When
        List<Deal> result = dealService.importDeals(file);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(dealRepository, never()).save(any(Deal.class));
    }

    @Test
    void saveDeal_ShouldSaveDeal() {
        // Given
        when(dealRepository.save(any(Deal.class))).thenReturn(testDeal);

        // When
        dealService.saveDeal(testDeal);

        // Then
        verify(dealRepository, times(1)).save(testDeal);
    }

    @Test
    void getAllDeals_ShouldReturnAllDeals() {
        // Given
        List<Deal> deals = Arrays.asList(testDeal);
        when(dealRepository.findAll()).thenReturn(deals);

        // When
        List<Deal> result = dealService.getAllDeals();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testDeal, result.get(0));
        verify(dealRepository, times(1)).findAll();
    }

    @Test
    void getAllDeals_WhenEmpty_ShouldReturnEmptyList() {
        // Given
        when(dealRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<Deal> result = dealService.getAllDeals();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(dealRepository, times(1)).findAll();
    }

    @Test
    void getDealById_WithValidId_ShouldReturnDeal() {
        // Given
        when(dealRepository.getDealByDealUniqueId("DEAL001")).thenReturn(testDeal);

        // When
        Deal result = dealService.getDealById("DEAL001");

        // Then
        assertNotNull(result);
        assertEquals(testDeal, result);
        assertEquals("DEAL001", result.getDealUniqueId());
        verify(dealRepository, times(1)).getDealByDealUniqueId("DEAL001");
    }

    @Test
    void getDealById_WithNonExistingId_ShouldReturnNull() {
        // Given
        when(dealRepository.getDealByDealUniqueId("NONEXISTENT")).thenReturn(null);

        // When
        Deal result = dealService.getDealById("NONEXISTENT");

        // Then
        assertNull(result);
        verify(dealRepository, times(1)).getDealByDealUniqueId("NONEXISTENT");
    }

    @Test
    void importDeals_WithInvalidDateFormat_ShouldThrowException() {
        // Given
        String csvContent = "dealUniqueId,fromCurrencyIsoCode,toCurrencyIsoCode,dealTimestamp,dealAmount\n" +
                "DEAL001,USD,EUR,INVALID_DATE,1000.00";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "deals.csv",
                "text/csv",
                csvContent.getBytes()
        );

        // When/Then
        assertThrows(Exception.class, () -> dealService.importDeals(file));
        verify(dealRepository, never()).save(any(Deal.class));
    }

    @Test
    void importDeals_WithInvalidAmount_ShouldThrowException() {
        // Given
        String csvContent = "dealUniqueId,fromCurrencyIsoCode,toCurrencyIsoCode,dealTimestamp,dealAmount\n" +
                "DEAL001,USD,EUR,2024-01-15T10:30:00,INVALID_AMOUNT";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "deals.csv",
                "text/csv",
                csvContent.getBytes()
        );

        // When/Then
        assertThrows(Exception.class, () -> dealService.importDeals(file));
        verify(dealRepository, never()).save(any(Deal.class));
    }
}
