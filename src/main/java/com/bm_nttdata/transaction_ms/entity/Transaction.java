package com.bm_nttdata.transaction_ms.entity;

import com.bm_nttdata.transaction_ms.enums.ProductTypeEnum;
import com.bm_nttdata.transaction_ms.enums.TransactionStatusEnum;
import com.bm_nttdata.transaction_ms.enums.TransactionTypeEnum;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Representa una entidad de transacciones en el sistema bancario.
 * Esta clase maneja el almacenamiento y gestión de la información de transacciones.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "transactions")
public class Transaction {

    @Id
    private String id;
    private TransactionTypeEnum transactionType;
    private ProductTypeEnum productType;
    private BigDecimal amount;
    private BigDecimal commission;
    private String sourceAccountId;
    private String targetAccountId;
    private String creditId;
    private String description;
    private TransactionStatusEnum status;
    private LocalDateTime transactionDate;
    private String errorMessage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
