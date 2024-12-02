package com.bm_nttdata.transaction_ms.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionFeeResponseDTO {

    private Boolean subjectToFee;
    private BigDecimal feeAmount;
}
