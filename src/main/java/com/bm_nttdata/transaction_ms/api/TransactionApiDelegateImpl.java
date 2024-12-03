package com.bm_nttdata.transaction_ms.api;

import com.bm_nttdata.transaction_ms.entity.Transaction;
import com.bm_nttdata.transaction_ms.mapper.TransactionMapper;
import com.bm_nttdata.transaction_ms.model.CreditChargeRequestDto;
import com.bm_nttdata.transaction_ms.model.DepositRequestDto;
import com.bm_nttdata.transaction_ms.model.PaymentRequestDto;
import com.bm_nttdata.transaction_ms.model.ProductMovementsResponseDto;
import com.bm_nttdata.transaction_ms.model.TransactionResponseDto;
import com.bm_nttdata.transaction_ms.model.TransferRequestDto;
import com.bm_nttdata.transaction_ms.model.WithdrawalRequestDto;
import com.bm_nttdata.transaction_ms.service.TransactionService;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Implementación del delegado de la API de transacciones.
 * Maneja las peticiones HTTP recibidas por los endpoints de la API,
 * delegando la lógica de negocio al servicio correspondiente y
 * transformando las respuestas al formato requerido por la API.
 */
@Slf4j
@Component
@AllArgsConstructor
public class TransactionApiDelegateImpl implements TransactionApiDelegate {

    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @Override
    public ResponseEntity<TransactionResponseDto> createDeposit(DepositRequestDto depositRequest) {
        log.info("Processing deposit request: {}", depositRequest);
        Transaction transaction = transactionService.processDeposit(depositRequest);
        return ResponseEntity.ok(transactionMapper.transactionToResponseDto(transaction));
    }

    @Override
    public ResponseEntity<TransactionResponseDto> createWithdrawal(
            WithdrawalRequestDto withdrawalRequest) {
        log.info("Processing withdrawal request: {}", withdrawalRequest);
        Transaction transaction = transactionService.processWithdrawal(withdrawalRequest);
        return ResponseEntity.ok(transactionMapper.transactionToResponseDto(transaction));
    }

    @Override
    public ResponseEntity<TransactionResponseDto> createPayment(PaymentRequestDto paymentRequest) {
        log.info("Processing payment request: {}", paymentRequest);
        Transaction transaction = transactionService.processPayment(paymentRequest);
        return ResponseEntity.ok(transactionMapper.transactionToResponseDto(transaction));
    }

    @Override
    public ResponseEntity<TransactionResponseDto> createCreditCharge(
            CreditChargeRequestDto creditChargeRequest) {
        log.info("Processing charge request: {}", creditChargeRequest);
        Transaction transaction = transactionService.processCreditCharge(creditChargeRequest);
        return ResponseEntity.ok(transactionMapper.transactionToResponseDto(transaction));
    }

    @Override
    public ResponseEntity<TransactionResponseDto> createTransfer(
            TransferRequestDto transferRequest) {
        return TransactionApiDelegate.super.createTransfer(transferRequest);
    }

    @Override
    public ResponseEntity<ProductMovementsResponseDto> getProductMovements(
            String productId, String productType, LocalDate startDate, LocalDate endDate) {
        log.info("Obtain product movements: {}", productId);
        ProductMovementsResponseDto productMovements =
                transactionService.getProductMovements(productId, productType, startDate, endDate);
        return ResponseEntity.ok(productMovements);
    }

}
