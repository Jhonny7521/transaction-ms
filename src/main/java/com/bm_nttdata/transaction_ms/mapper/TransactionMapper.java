package com.bm_nttdata.transaction_ms.mapper;

import com.bm_nttdata.transaction_ms.entity.Transaction;
import com.bm_nttdata.transaction_ms.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    Transaction depositRequestToTransaction(DepositRequestDTO depositRequest);

    Transaction withdrawalRequestToTransaction(WithdrawalRequestDTO withdrawalRequest);

    Transaction paymentRequestToTransaction(PaymentRequestDTO paymentRequest);

    Transaction chargeRequestToTransaction(CreditChargeRequestDTO chargeRequest);

    TransactionResponseDTO transactionToResponseDTO(Transaction transaction);


    default OffsetDateTime map(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.atOffset(ZoneOffset.UTC);
    }

    default LocalDateTime map(OffsetDateTime offsetDateTime) {
        if (offsetDateTime == null) {
            return null;
        }
        return offsetDateTime.toLocalDateTime();
    }

}
