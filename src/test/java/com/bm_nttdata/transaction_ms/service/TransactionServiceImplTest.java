package com.bm_nttdata.transaction_ms.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.bm_nttdata.transaction_ms.client.AccountClient;
import com.bm_nttdata.transaction_ms.client.CreditClient;
import com.bm_nttdata.transaction_ms.dto.ApiResponseDto;
import com.bm_nttdata.transaction_ms.entity.Transaction;
import com.bm_nttdata.transaction_ms.enums.ProductTypeEnum;
import com.bm_nttdata.transaction_ms.enums.TransactionStatusEnum;
import com.bm_nttdata.transaction_ms.enums.TransactionTypeEnum;
import com.bm_nttdata.transaction_ms.exception.BusinessRuleException;
import com.bm_nttdata.transaction_ms.exception.ServiceException;
import com.bm_nttdata.transaction_ms.mapper.TransactionMapper;
import com.bm_nttdata.transaction_ms.model.CreditChargeRequestDto;
import com.bm_nttdata.transaction_ms.model.DepositRequestDto;
import com.bm_nttdata.transaction_ms.model.PaymentRequestDto;
import com.bm_nttdata.transaction_ms.model.ProductMovementsResponseDto;
import com.bm_nttdata.transaction_ms.model.TransferRequestDto;
import com.bm_nttdata.transaction_ms.model.WithdrawalRequestDto;
import com.bm_nttdata.transaction_ms.repository.TransactionRepository;
import com.bm_nttdata.transaction_ms.service.impl.TransactionServiceImpl;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Clase de pruebas unitarias para el servicio de transacciones.
 */
@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

    @Mock
    private AccountClient accountClient;
    @Mock
    private CreditClient creditClient;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionServiceImpl transactionService;


    @Test
    @DisplayName("Depósito exitoso a cuenta debería completar la transacción")
    void testProcessDeposit_Success() {

        DepositRequestDto depositRequest = new DepositRequestDto();
        depositRequest.setTargetAccountId("674beae71a57944b1b191867");
        depositRequest.setAmount(BigDecimal.valueOf(100.0));

        Transaction mockTransaction = new Transaction();
        ApiResponseDto successResponse = new ApiResponseDto();
        successResponse.setStatus("SUCCESS");

        when(transactionMapper.depositRequestToTransaction(any()))
                .thenReturn(mockTransaction);
        when(accountClient.depositToAccount(any()))
                .thenReturn(successResponse);
        when(transactionRepository.save(any()))
                .thenReturn(mockTransaction);


        Transaction result = transactionService.processDeposit(depositRequest);


        Assertions.assertNotNull(result);
        Assertions.assertEquals(TransactionStatusEnum.COMPLETED, result.getStatus());
        Mockito.verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Retiro exitoso de cuenta debería completar la transacción")
    void testProcessWithdrawal_Success() {

        WithdrawalRequestDto withdrawalRequest = new WithdrawalRequestDto();
        withdrawalRequest.setSourceAccountId("674beae71a57944b1b191867");
        withdrawalRequest.setAmount(BigDecimal.valueOf(50.0));

        Transaction mockTransaction = new Transaction();
        ApiResponseDto successResponse = new ApiResponseDto();
        successResponse.setStatus("SUCCESS");

        when(transactionMapper.withdrawalRequestToTransaction(any())).thenReturn(mockTransaction);
        when(accountClient.withdrawalToAccount(any())).thenReturn(successResponse);
        when(transactionRepository.save(any())).thenReturn(mockTransaction);

        Transaction result = transactionService.processWithdrawal(withdrawalRequest);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(TransactionStatusEnum.COMPLETED, result.getStatus());
        Mockito.verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Pago exitoso de crédito debería completar la transacción")
    void testProcessPayment_CreditSuccess() {

        PaymentRequestDto paymentRequest = new PaymentRequestDto();
        paymentRequest.setCreditId("674beae71a57944b1b191867");
        paymentRequest.setCreditType(PaymentRequestDto.CreditTypeEnum.CREDIT);
        paymentRequest.setAmount(BigDecimal.valueOf(200.0));

        Transaction mockTransaction = new Transaction();
        ApiResponseDto successResponse = new ApiResponseDto();
        successResponse.setStatus("SUCCESS");

        when(transactionMapper.paymentRequestToTransaction(any())).thenReturn(mockTransaction);
        when(creditClient.paymentCredit(any())).thenReturn(successResponse);
        when(transactionRepository.save(any())).thenReturn(mockTransaction);

        Transaction result = transactionService.processPayment(paymentRequest);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(TransactionStatusEnum.COMPLETED, result.getStatus());
        Assertions.assertEquals(ProductTypeEnum.CREDIT, result.getProductType());
        Mockito.verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Pago exitoso de tarjeta de crédito debería completar la transacción")
    void testProcessPayment_CreditCardSuccess() {

        PaymentRequestDto paymentRequest = new PaymentRequestDto();
        paymentRequest.setCreditId("674beae71a57944b1b191867");
        paymentRequest.setCreditType(PaymentRequestDto.CreditTypeEnum.CREDIT_CARD);
        paymentRequest.setAmount(BigDecimal.valueOf(200.0));

        Transaction mockTransaction = new Transaction();
        ApiResponseDto successResponse = new ApiResponseDto();
        successResponse.setStatus("SUCCESS");

        when(transactionMapper.paymentRequestToTransaction(any())).thenReturn(mockTransaction);
        when(creditClient.paymentCreditCard(any())).thenReturn(successResponse);
        when(transactionRepository.save(any())).thenReturn(mockTransaction);

        Transaction result = transactionService.processPayment(paymentRequest);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(TransactionStatusEnum.COMPLETED, result.getStatus());
        Assertions.assertEquals(ProductTypeEnum.CREDIT_CARD, result.getProductType());
        Mockito.verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Cargo exitoso a tarjeta de crédito debería completar la transacción")
    void testProcessCreditCharge_Success() {

        CreditChargeRequestDto chargeRequest = new CreditChargeRequestDto();
        chargeRequest.setCreditId("674beae71a57944b1b191867");
        chargeRequest.setAmount(BigDecimal.valueOf(150.0));

        Transaction mockTransaction = new Transaction();
        ApiResponseDto successResponse = new ApiResponseDto();
        successResponse.setStatus("SUCCESS");

        when(transactionMapper.chargeRequestToTransaction(any())).thenReturn(mockTransaction);
        when(creditClient.chargeCreditCard(any())).thenReturn(successResponse);
        when(transactionRepository.save(any())).thenReturn(mockTransaction);

        Transaction result = transactionService.processCreditCharge(chargeRequest);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(TransactionStatusEnum.COMPLETED, result.getStatus());
        Assertions.assertEquals(ProductTypeEnum.CREDIT_CARD, result.getProductType());
        Mockito.verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    @DisplayName(
            "Consulta de movimientos de cuenta bancaria debería retornar transacciones del periodo")
    void testGetAccountProductMovements_Success() {

        String productId = "674beae71a57944b1b191867";
        String productType = "BANK_ACCOUNT";
        LocalDate startDate = LocalDate.now().minusDays(30);
        LocalDate endDate = LocalDate.now();

        Transaction mockTransaction = new Transaction();
        mockTransaction.setTransactionDate(LocalDateTime.now());
        mockTransaction.setStatus(TransactionStatusEnum.COMPLETED);

        List<Transaction> mockTransactions = Arrays.asList(mockTransaction);

        when(transactionRepository.findByProductTypeAndSourceAccountId(any(), any()))
                .thenReturn(mockTransactions);
        when(transactionRepository.findByProductTypeAndTargetAccountId(any(), any()))
                .thenReturn(Arrays.asList());

        ProductMovementsResponseDto result = transactionService.getProductMovements(
                productId, productType, startDate, endDate);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(productId, result.getProductId());
        Assertions.assertEquals(ProductMovementsResponseDto.ProductTypeEnum.BANK_ACCOUNT,
                result.getProductType());
    }

    @Test
    @DisplayName(
            "Consulta de movimientos de producto de crédito "
                   + "debería retornar transacciones del periodo")
    void testGetCreditProductMovements_Success() {

        String productId = "674beae71a57944b1b191867";
        String productType = "CREDIT";
        LocalDate startDate = LocalDate.now().minusDays(30);
        LocalDate endDate = LocalDate.now();

        Transaction mockTransaction = new Transaction();
        mockTransaction.setTransactionDate(LocalDateTime.now());
        mockTransaction.setStatus(TransactionStatusEnum.COMPLETED);

        List<Transaction> mockTransactions = Arrays.asList(mockTransaction);

        when(transactionRepository.findByProductTypeAndCreditId(any(), any()))
                .thenReturn(mockTransactions);

        ProductMovementsResponseDto result = transactionService.getProductMovements(
                productId, productType, startDate, endDate);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(productId, result.getProductId());
        Assertions.assertEquals(ProductMovementsResponseDto.ProductTypeEnum.CREDIT,
                result.getProductType());
    }

    @Test
    @DisplayName("Consulta con rango de fechas inválido debería lanzar excepción")
    void testGetProductMovements_InvalidDateRange() {

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().minusDays(1);

        assertThrows(BusinessRuleException.class, () ->
                transactionService.getProductMovements(
                        "674beae71a57944b1b191867", "BANK_ACCOUNT", startDate, endDate));
    }

    @Test
    @DisplayName("Transferencia exitosa entre cuentas debería completar la transacción")
    void testProcessTransfer_Success() {

        TransferRequestDto transferRequest = new TransferRequestDto();
        transferRequest.setSourceAccountId("674beae71a57944b1b191123");
        transferRequest.setTargetAccountId("674beae71a57944b1b191456");
        transferRequest.setAmount(BigDecimal.valueOf(300.0));

        Transaction mockTransaction = new Transaction();
        ApiResponseDto successResponse = new ApiResponseDto();
        successResponse.setStatus("SUCCESS");

        when(transactionMapper.transferRequestToTransaction(any())).thenReturn(mockTransaction);
        when(accountClient.transferToAccount(any())).thenReturn(successResponse);
        when(transactionRepository.save(any())).thenReturn(mockTransaction);

        Transaction result = transactionService.processTransfer(transferRequest);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(TransactionStatusEnum.COMPLETED, result.getStatus());
        Assertions.assertEquals(TransactionTypeEnum.TRANSFER, result.getTransactionType());
        Mockito.verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Depósito fallido debería marcar transacción como fallida con mensaje de error")
    void testProcessDeposit_Failed() {

        DepositRequestDto depositRequest = new DepositRequestDto();
        Transaction mockTransaction = new Transaction();
        ApiResponseDto failedResponse = new ApiResponseDto();
        failedResponse.setStatus("FAILED");
        failedResponse.setError("Insufficient funds");

        when(transactionMapper.depositRequestToTransaction(any())).thenReturn(mockTransaction);
        when(accountClient.depositToAccount(any())).thenReturn(failedResponse);
        when(transactionRepository.save(any())).thenReturn(mockTransaction);

        Transaction result = transactionService.processDeposit(depositRequest);

        Assertions.assertEquals(TransactionStatusEnum.FAILED, result.getStatus());
        Assertions.assertEquals("Insufficient funds", result.getErrorMessage());
    }

    @Test
    @DisplayName("Retiro fallido debería marcar transacción como fallida con mensaje de error")
    void testProcessWithdrawal_Failed() {

        WithdrawalRequestDto withdrawalRequest = new WithdrawalRequestDto();
        Transaction mockTransaction = new Transaction();
        ApiResponseDto failedResponse = new ApiResponseDto();
        failedResponse.setStatus("FAILED");
        failedResponse.setError("Insufficient funds");

        when(transactionMapper.withdrawalRequestToTransaction(any())).thenReturn(mockTransaction);
        when(accountClient.withdrawalToAccount(any())).thenReturn(failedResponse);
        when(transactionRepository.save(any())).thenReturn(mockTransaction);

        Transaction result = transactionService.processWithdrawal(withdrawalRequest);

        Assertions.assertEquals(TransactionStatusEnum.FAILED, result.getStatus());
        Assertions.assertEquals("Insufficient funds", result.getErrorMessage());
    }

    @Test
    @DisplayName("Cargo fallido debería marcar transacción como fallida con mensaje de error")
    void testProcessCreditCharge_Failed() {

        CreditChargeRequestDto chargeRequest = new CreditChargeRequestDto();
        Transaction mockTransaction = new Transaction();
        ApiResponseDto failedResponse = new ApiResponseDto();
        failedResponse.setStatus("FAILED");
        failedResponse.setError("Insufficient funds");

        when(transactionMapper.chargeRequestToTransaction(any())).thenReturn(mockTransaction);
        when(creditClient.chargeCreditCard(any())).thenReturn(failedResponse);
        when(transactionRepository.save(any())).thenReturn(mockTransaction);

        Transaction result = transactionService.processCreditCharge(chargeRequest);

        Assertions.assertEquals(TransactionStatusEnum.FAILED, result.getStatus());
        Assertions.assertEquals("Insufficient funds", result.getErrorMessage());
    }

    @Test
    @DisplayName("Transferencia fallida debería marcar transacción "
            + "como fallida con mensaje de error")
    void testProcessTransfer_Failed() {

        TransferRequestDto transferRequest = new TransferRequestDto();
        Transaction mockTransaction = new Transaction();
        ApiResponseDto failedResponse = new ApiResponseDto();
        failedResponse.setStatus("FAILED");
        failedResponse.setError("Insufficient funds");

        when(transactionMapper.transferRequestToTransaction(any())).thenReturn(mockTransaction);
        when(accountClient.transferToAccount(any())).thenReturn(failedResponse);
        when(transactionRepository.save(any())).thenReturn(mockTransaction);

        Transaction result = transactionService.processTransfer(transferRequest);

        Assertions.assertEquals(TransactionStatusEnum.FAILED, result.getStatus());
        Assertions.assertEquals("Insufficient funds", result.getErrorMessage());
    }

    @Test
    void testProcessPayment_ThrowsServiceException_WhenMapperFails() {

        PaymentRequestDto paymentRequest = new PaymentRequestDto();
        when(transactionMapper.paymentRequestToTransaction(any()))
                .thenThrow(new RuntimeException("Mapping error"));

        ServiceException exception = assertThrows(ServiceException.class,
                () -> transactionService.processPayment(paymentRequest));
        Assertions.assertEquals(
                "Unexpected error while saving transaction", exception.getMessage());
    }

    @Test
    void testProcessPayment_ThrowsServiceException_WhenClientFails() {

        PaymentRequestDto paymentRequest = new PaymentRequestDto();
        paymentRequest.setCreditType(PaymentRequestDto.CreditTypeEnum.CREDIT);

        Transaction mockTransaction = new Transaction();
        when(transactionMapper.paymentRequestToTransaction(any())).thenReturn(mockTransaction);
        when(creditClient.paymentCredit(any())).thenThrow(new RuntimeException("Client error"));

        ServiceException exception = assertThrows(ServiceException.class,
                () -> transactionService.processPayment(paymentRequest));
        Assertions.assertEquals(
                "Unexpected error while saving transaction", exception.getMessage());
    }

    @Test
    void testProcessPayment_ThrowsServiceException_WhenRepositorySaveFails() {

        PaymentRequestDto paymentRequest = new PaymentRequestDto();
        paymentRequest.setCreditType(PaymentRequestDto.CreditTypeEnum.CREDIT);

        Transaction mockTransaction = new Transaction();
        ApiResponseDto successResponse = new ApiResponseDto();
        successResponse.setStatus("SUCCESS");

        when(transactionMapper.paymentRequestToTransaction(any())).thenReturn(mockTransaction);
        when(creditClient.paymentCredit(any())).thenReturn(successResponse);
        when(transactionRepository.save(any())).thenThrow(new RuntimeException("Save error"));

        ServiceException exception = assertThrows(ServiceException.class,
                () -> transactionService.processPayment(paymentRequest));
        Assertions.assertEquals(
                "Unexpected error while saving transaction", exception.getMessage());
    }
}
