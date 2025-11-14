package com.progresssoft.analyze_fx_deals.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDTO {
    @NotBlank(message = "Deal ID cannot be blank")
    @Size(min = 1 , max=50 , message = "Deal ID must be between 1 and 50 characters")
    private String dealUniqueId;

    @NotBlank(message = "From currency cannot be blank")
    @Size(min = 3 ,max = 3 ,message = "Currency code must be exactly 3 letters")
    private String fromCurrencyIsoCode;

    @NotBlank(message = "To currency cannot be blank")
    @Size(min = 3 ,max = 3 ,message = "Currency code must be exactly 3 letters")
    private String toCurrencyIsoCode;

    @NotNull(message = "Deal timestamp cannot be null")
    private LocalDateTime dealTimestamp;

    @NotNull(message = "Deal amount cannot be null")
    @DecimalMin(value = "0.01", message = "Deal amount must be greater than 0")
    private BigDecimal dealAmount;
}
