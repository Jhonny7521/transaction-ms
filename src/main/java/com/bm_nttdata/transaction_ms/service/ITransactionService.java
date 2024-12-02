package com.bm_nttdata.transaction_ms.service;

import com.bm_nttdata.transaction_ms.entity.Transaction;
import com.bm_nttdata.transaction_ms.enums.ProductTypeEnum;
import com.bm_nttdata.transaction_ms.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ITransactionService {

    Transaction processDeposit(DepositRequestDTO request);

    Transaction processWithdrawal(WithdrawalRequestDTO request);

    Transaction processPayment(PaymentRequestDTO request);

    Transaction processCreditCharge(CreditChargeRequestDTO request);

    ProductMovementsResponseDTO getProductMovements(String productId, String productType,
                                                    LocalDate startDate, LocalDate endDate);

}
