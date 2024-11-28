package com.bm_nttdata.transaction_ms.service;

import com.bm_nttdata.transaction_ms.entity.Transaction;
import com.bm_nttdata.transaction_ms.enums.ProductTypeEnum;

import java.time.LocalDateTime;

public interface ITransactionService {

    Transaction processDeposit(DepositRequestDTO request);

    Transaction processWithdrawal(WithdrawalRequest request);

    Transaction processTransfer(TransferRequest request);

    Transaction processPayment(PaymentRequest request);

    Transaction processCreditCharge(CreditChargeRequest request);

    ProductMovementsResponse getProductMovements(String productId, ProductTypeEnum productType,
                                                 LocalDateTime startDate, LocalDateTime endDate);

}
