package com.bm_nttdata.transaction_ms.DTO;

import com.bm_nttdata.transaction_ms.enums.TransactionTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionFeeRequestDTO {

    private TransactionTypeEnum transactionType;
    private Integer transactionMade;
    private BigDecimal transactionAmount;

}
