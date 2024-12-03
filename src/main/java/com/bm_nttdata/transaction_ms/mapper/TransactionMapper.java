package com.bm_nttdata.transaction_ms.mapper;

import com.bm_nttdata.transaction_ms.entity.Transaction;
import com.bm_nttdata.transaction_ms.model.CreditChargeRequestDto;
import com.bm_nttdata.transaction_ms.model.DepositRequestDto;
import com.bm_nttdata.transaction_ms.model.PaymentRequestDto;
import com.bm_nttdata.transaction_ms.model.TransactionResponseDto;
import com.bm_nttdata.transaction_ms.model.WithdrawalRequestDto;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Interfaz de mapeo para la conversión entre entidades y DTOs relacionados con transacciones.
 * Utiliza MapStruct para la implementación automática de las conversiones.
 */
@Mapper(componentModel = "spring")
public interface TransactionMapper {

    /**
     * Convierte un objeto DTO de solicitud de depósito a una entidad de transacción.
     *
     * @param depositRequest DTO con la información de solicitud de depósito
     * @return Entidad Transacción con los datos mapeados
     */
    Transaction depositRequestToTransaction(DepositRequestDto depositRequest);

    /**
     * Convierte un objeto DTO de solicitud de retiro a una entidad de transacción.
     *
     * @param withdrawalRequest DTO con la información de solicitud de retiro
     * @return Entidad Transacción con los datos mapeados
     */
    Transaction withdrawalRequestToTransaction(WithdrawalRequestDto withdrawalRequest);

    /**
     * Convierte un objeto DTO de solicitud de pago a una entidad de transacción.
     *
     * @param paymentRequest DTO con la información de solicitud de pago
     * @return Entidad Transacción con los datos mapeados
     */
    Transaction paymentRequestToTransaction(PaymentRequestDto paymentRequest);

    /**
     * Convierte un objeto DTO de solicitud de cargo a una entidad de transacción.
     *
     * @param chargeRequest DTO con la información de solicitud de cargo
     * @return Entidad Transacción con los datos mapeados
     */
    Transaction chargeRequestToTransaction(CreditChargeRequestDto chargeRequest);

    /**
     * Convierte una entidad de transacción a un DTO de respuesta de transacción.
     *
     * @param transaction DTO con la información de solicitud de depósito
     * @return Entidad Transacción con los datos mapeados
     */
    TransactionResponseDto transactionToResponseDto(Transaction transaction);


    /**
     * Convierte un LocalDateTime a OffsetDateTime en UTC.
     *
     * @param localDateTime Fecha y hora local
     * @return Fecha y hora con zona horaria UTC
     */
    default OffsetDateTime map(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.atOffset(ZoneOffset.UTC);
    }

    /**
     * Convierte un OffsetDateTime a LocalDateTime.
     *
     * @param offsetDateTime Fecha y hora con zona horaria
     * @return Fecha y hora local
     */
    default LocalDateTime map(OffsetDateTime offsetDateTime) {
        if (offsetDateTime == null) {
            return null;
        }
        return offsetDateTime.toLocalDateTime();
    }

}
