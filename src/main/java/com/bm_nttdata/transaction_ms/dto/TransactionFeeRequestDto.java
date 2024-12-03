package com.bm_nttdata.transaction_ms.dto;

import com.bm_nttdata.transaction_ms.enums.TransactionTypeEnum;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa la solicitud para calcular la comisión de una transacción.
 * Esta clase contiene toda la información necesaria para determinar si una transacción
 * está sujeta a comisiones y calcular el monto correspondiente.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionFeeRequestDto {

    private TransactionTypeEnum transactionType;
    private Integer transactionMade;
    private BigDecimal transactionAmount;

}
