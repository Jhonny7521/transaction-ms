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
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    @CircuitBreaker(name = "createDeposit", fallbackMethod = "createDepositFallback")
    public ResponseEntity<TransactionResponseDto> createDeposit(DepositRequestDto depositRequest) {
        log.info("Processing deposit request: {}", depositRequest);
        Transaction transaction = transactionService.processDeposit(depositRequest);
        return ResponseEntity.ok(transactionMapper.transactionToResponseDto(transaction));
    }

    @Override
    @CircuitBreaker(name = "createWithdrawal", fallbackMethod = "createWithdrawalFallback")
    public ResponseEntity<TransactionResponseDto> createWithdrawal(
            WithdrawalRequestDto withdrawalRequest) {
        log.info("Processing withdrawal request: {}", withdrawalRequest);
        Transaction transaction = transactionService.processWithdrawal(withdrawalRequest);
        return ResponseEntity.ok(transactionMapper.transactionToResponseDto(transaction));
    }

    @Override
    @CircuitBreaker(name = "createPayment", fallbackMethod = "createPaymentFallback")
    public ResponseEntity<TransactionResponseDto> createPayment(PaymentRequestDto paymentRequest) {
        log.info("Processing payment request: {}", paymentRequest);
        Transaction transaction = transactionService.processPayment(paymentRequest);
        return ResponseEntity.ok(transactionMapper.transactionToResponseDto(transaction));
    }

    @Override
    @CircuitBreaker(name = "createCreditCharge", fallbackMethod = "createCreditChargeFallback")
    public ResponseEntity<TransactionResponseDto> createCreditCharge(
            CreditChargeRequestDto creditChargeRequest) {
        log.info("Processing charge request: {}", creditChargeRequest);
        Transaction transaction = transactionService.processCreditCharge(creditChargeRequest);
        return ResponseEntity.ok(transactionMapper.transactionToResponseDto(transaction));
    }

    @Override
    @CircuitBreaker(name = "createTransfer", fallbackMethod = "createTransferFallback")
    public ResponseEntity<TransactionResponseDto> createTransfer(
            TransferRequestDto transferRequest) {
        log.info("Processing transfer request: {}", transferRequest);
        Transaction transaction = transactionService.processTransfer(transferRequest);
        return ResponseEntity.ok(transactionMapper.transactionToResponseDto(transaction));
    }

    @Override
    public ResponseEntity<ProductMovementsResponseDto> getProductMovements(
            String productId, String productType, LocalDate startDate, LocalDate endDate) {
        log.info("Obtain product movements: {}", productId);
        ProductMovementsResponseDto productMovements =
                transactionService.getProductMovements(productId, productType, startDate, endDate);
        return ResponseEntity.ok(productMovements);
    }

    private ResponseEntity<TransactionResponseDto> createDepositFallback(
            DepositRequestDto depositRequest, Exception e) {
        log.error("Fallback to process deposit request: {}", depositRequest);
        log.error("Error: {} ", e.getMessage());
        return new ResponseEntity(
                "We are experiencing some errors. Please try again later", HttpStatus.OK);
    }

    private ResponseEntity<TransactionResponseDto> createWithdrawalFallback(
            WithdrawalRequestDto withdrawalRequest, Exception e) {
        log.error("Fallback to process withdrawal request: {}", withdrawalRequest);
        log.error("Error: {}", e.getMessage());
        return new ResponseEntity(
                "We are experiencing some errors. Please try again later", HttpStatus.OK);
    }

    private ResponseEntity<TransactionResponseDto> createPaymentFallback(
            PaymentRequestDto paymentRequest, Exception e) {
        log.error("Fallback to process payment request: {}", paymentRequest);
        log.error("Error: {}", e.getMessage());
        return new ResponseEntity(
                "We are experiencing some errors. Please try again later", HttpStatus.OK);
    }

    private ResponseEntity<TransactionResponseDto> createCreditChargeFallback(
            CreditChargeRequestDto creditChargeRequest, Exception e) {
        log.error("Fallback to process payment request: {}", creditChargeRequest);
        log.error("Error: {}", e.getMessage());
        return new ResponseEntity(
                "We are experiencing some errors. Please try again later", HttpStatus.OK);
    }

    private ResponseEntity<TransactionResponseDto> createTransferFallback(
            TransferRequestDto transferRequest, Exception e) {
        log.error("Fallback to process transfer request: {}", transferRequest);
        log.error("Error: {}", e.getMessage());
        return new ResponseEntity(
                "We are experiencing some errors. Please try again later", HttpStatus.OK);
    }
}
