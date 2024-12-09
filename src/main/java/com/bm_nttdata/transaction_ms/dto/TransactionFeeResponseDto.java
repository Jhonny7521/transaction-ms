package com.bm_nttdata.transaction_ms.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa la respuesta del cálculo de comisión para una transacción.
 * Contiene la información sobre si aplica una comisión y el monto de la misma.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionFeeResponseDto {

    private Boolean subjectToFee;
    private BigDecimal feeAmount;
}
