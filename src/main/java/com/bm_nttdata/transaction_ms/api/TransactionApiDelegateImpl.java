package com.bm_nttdata.transaction_ms.api;

import com.bm_nttdata.transaction_ms.entity.Transaction;
import com.bm_nttdata.transaction_ms.mapper.TransactionMapper;
import com.bm_nttdata.transaction_ms.model.*;
import com.bm_nttdata.transaction_ms.service.ITransactionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class TransactionApiDelegateImpl implements TransactionApiDelegate{

    private final ITransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @Override
    public ResponseEntity<TransactionResponseDTO> createDeposit(DepositRequestDTO depositRequestDTO) {
        log.info("Processing deposit request: {}", depositRequestDTO);
        Transaction transaction = transactionService.processDeposit(depositRequestDTO);
        return ResponseEntity.ok(transactionMapper.transactionToResponseDTO(transaction));
    }

    @Override
    public ResponseEntity<TransactionResponseDTO> createWithdrawal(WithdrawalRequestDTO withdrawalRequestDTO) {
        log.info("Processing withdrawal request: {}", withdrawalRequestDTO);
        Transaction transaction = transactionService.processWithdrawal(withdrawalRequestDTO);
        return ResponseEntity.ok(transactionMapper.transactionToResponseDTO(transaction));
    }

    @Override
    public ResponseEntity<TransactionResponseDTO> createPayment(PaymentRequestDTO paymentRequestDTO) {
        log.info("Processing payment request: {}", paymentRequestDTO);
        Transaction transaction = transactionService.processPayment(paymentRequestDTO);
        return ResponseEntity.ok(transactionMapper.transactionToResponseDTO(transaction));
    }

    @Override
    public ResponseEntity<TransactionResponseDTO> createCreditCharge(CreditChargeRequestDTO creditChargeRequestDTO) {
        log.info("Processing charge request: {}", creditChargeRequestDTO);
        Transaction transaction = transactionService.processCreditCharge(creditChargeRequestDTO);
        return ResponseEntity.ok(transactionMapper.transactionToResponseDTO(transaction));
    }

    @Override
    public ResponseEntity<TransactionResponseDTO> createTransfer(TransferRequestDTO transferRequestDTO) {
        return TransactionApiDelegate.super.createTransfer(transferRequestDTO);
    }

    @Override
    public ResponseEntity<ProductMovementsResponseDTO> getProductMovements(String productId, String productType, LocalDate startDate, LocalDate endDate) {
        log.info("Obtain product movements: {}", productId);
        ProductMovementsResponseDTO productMovements = transactionService.getProductMovements(productId, productType, startDate, endDate);
        return ResponseEntity.ok(productMovements);
    }

}
