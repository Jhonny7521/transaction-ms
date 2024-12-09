package com.bm_nttdata.transaction_ms.service.impl;

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
import com.bm_nttdata.transaction_ms.service.TransactionService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del servicio de transacciones.
 * Proporciona métodos para procesar diferentes tipos de transacciones como depósitos,
 * retiros, pagos y cargos a créditos, así como consultas de movimientos.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final AccountClient accountClient;
    private final CreditClient creditClient;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    /**
     * Procesa una operación de depósito en una cuenta.
     * Valida la solicitud, realiza el depósito y registra la transacción correspondiente.
     *
     * @param depositRequest DTO que contiene la información necesaria para realizar el depósito,
     *               incluyendo cuenta destino y monto
     * @return Transaction objeto que representa la transacción realizada
     * @throws ServiceException si ocurre un error durante el proceso de depósito
     */
    @Override
    public Transaction processDeposit(DepositRequestDto depositRequest) {

        log.info("Starting deposit process to bank account: {}",
                depositRequest.getTargetAccountId());
        ApiResponseDto response = null;

        try {
            Transaction transaction =
                    transactionMapper.depositRequestToTransaction(depositRequest);
            response = accountClient.depositToAccount(depositRequest);
            log.info("Results of deposit: " + response);

            transaction.setTransactionType(TransactionTypeEnum.DEPOSIT);
            transaction.setProductType(ProductTypeEnum.BANK_ACCOUNT);
            transaction.setStatus(TransactionStatusEnum.COMPLETED);
            transaction.setTransactionDate(LocalDateTime.now());
            transaction.setCreatedAt(LocalDateTime.now());
            transaction.setUpdatedAt(LocalDateTime.now());

            if (!response.getStatus().equals("SUCCESS")) {
                transaction.setStatus(TransactionStatusEnum.FAILED);
                transaction.setErrorMessage(response.getError());
            }

            transaction = transactionRepository.save(transaction);
            log.info(" *** End of deposit process *** ");

            return transaction;

        } catch (Exception e) {
            String depositStatus = (response != null) ? response.getStatus() : "UNKNOWN";
            log.info("Deposit status: {}.", depositStatus);
            log.error("Unexpected error while saving transaction: ",  e.getMessage());
            throw new ServiceException("Unexpected error while saving transaction");
        }
    }

    /**
     * Procesa una operación de retiro de una cuenta.
     * Valida la solicitud, verifica fondos disponibles y registra la transacción.
     *
     * @param withdrawalRequest DTO que contiene la información necesaria para realizar el retiro,
     *               incluyendo cuenta origen y monto
     * @return Transaction objeto que representa la transacción realizada
     * @throws ServiceException si ocurre un error durante el proceso de retiro
     */
    @Override
    public Transaction processWithdrawal(WithdrawalRequestDto withdrawalRequest) {

        log.info("Starting withdrawal process to bank account: {}",
                withdrawalRequest.getSourceAccountId());
        ApiResponseDto response = null;

        try {
            Transaction transaction =
                    transactionMapper.withdrawalRequestToTransaction(withdrawalRequest);
            response = accountClient.withdrawalToAccount(withdrawalRequest);
            log.info("Results of withdrawal: " + response);

            transaction.setTransactionType(TransactionTypeEnum.WITHDRAWAL);
            transaction.setProductType(ProductTypeEnum.BANK_ACCOUNT);
            transaction.setStatus(TransactionStatusEnum.COMPLETED);
            transaction.setTransactionDate(LocalDateTime.now());
            transaction.setCreatedAt(LocalDateTime.now());
            transaction.setUpdatedAt(LocalDateTime.now());

            if (!response.getStatus().equals("SUCCESS")) {
                transaction.setStatus(TransactionStatusEnum.FAILED);
                transaction.setErrorMessage(response.getError());
            }

            transaction = transactionRepository.save(transaction);
            log.info(" *** End of withdrawal process *** ");

            return transaction;

        } catch (Exception e) {
            String depositStatus = (response != null) ? response.getStatus() : "UNKNOWN";
            log.info("Withdrawal status: {}.", depositStatus);
            log.error("Unexpected error while saving transaction: ",  e.getMessage());
            throw new ServiceException("Unexpected error while saving transaction");
        }
    }

    /**
     * Procesa un pago hacia un crédito o tarjeta de crédito.
     * Valida la solicitud, procesa el pago y registra la transacción.
     *
     * @param paymentRequest DTO que contiene la información necesaria para realizar el pago,
     *               incluyendo identificador del crédito y monto
     * @return Transaction objeto que representa la transacción realizada
     * @throws BusinessRuleException si el tipo de producto es desconocido
     * @throws ServiceException si ocurre un error durante el proceso de pago
     */
    @Override
    public Transaction processPayment(PaymentRequestDto paymentRequest) {

        log.info("Starting credit product payment process: {}", paymentRequest.getCreditId());
        ApiResponseDto response = null;

        try {
            Transaction transaction =
                    transactionMapper.paymentRequestToTransaction(paymentRequest);
            String creditType = paymentRequest.getCreditType().getValue();

            switch (creditType) {
                case "CREDIT" -> {
                    response = creditClient.paymentCredit(paymentRequest);
                    transaction.setProductType(ProductTypeEnum.CREDIT);
                }
                case "CREDIT-CARD" -> {
                    response = creditClient.paymentCreditCard(paymentRequest);
                    transaction.setProductType(ProductTypeEnum.CREDIT_CARD);
                }
                default ->
                        throw new BusinessRuleException("Unknown credit type");
            }

            log.info("Payment results: " + response);

            transaction.setTransactionType(TransactionTypeEnum.PAYMENT);
            transaction.setStatus(TransactionStatusEnum.COMPLETED);
            transaction.setTransactionDate(LocalDateTime.now());
            transaction.setCreatedAt(LocalDateTime.now());
            transaction.setUpdatedAt(LocalDateTime.now());

            if (!response.getStatus().equals("SUCCESS")) {
                transaction.setStatus(TransactionStatusEnum.FAILED);
                transaction.setErrorMessage(response.getError());
            }

            transaction = transactionRepository.save(transaction);
            log.info(" *** End of payment process *** ");

            return transaction;

        } catch (Exception e) {
            String paymentStatus = (response != null) ? response.getStatus() : "UNKNOWN";
            log.info("Payment status: {}.", paymentStatus);
            log.error("Unexpected error while saving transaction: ",  e.getMessage());
            throw new ServiceException("Unexpected error while saving transaction");
        }
    }

    /**
     * Procesa un cargo a una tarjeta de crédito.
     * Valida la solicitud, verifica el límite disponible y registra la transacción.
     *
     * @param chargeRequest DTO que contiene la información necesaria para realizar el cargo,
     *               incluyendo identificador de la tarjeta y monto
     * @return Transaction objeto que representa la transacción realizada
     * @throws ServiceException si ocurre un error durante el proceso de cargo
     */
    @Override
    public Transaction processCreditCharge(CreditChargeRequestDto chargeRequest) {

        log.info("Starting credit card charge process : {}", chargeRequest.getCreditId());
        ApiResponseDto response = null;

        try {
            Transaction transaction =
                    transactionMapper.chargeRequestToTransaction(chargeRequest);

            response = creditClient.chargeCreditCard(chargeRequest);
            log.info("Charge results: " + response);

            transaction.setTransactionType(TransactionTypeEnum.CREDIT_CHARGE);
            transaction.setProductType(ProductTypeEnum.CREDIT_CARD);
            transaction.setStatus(TransactionStatusEnum.COMPLETED);
            transaction.setTransactionDate(LocalDateTime.now());
            transaction.setCreatedAt(LocalDateTime.now());
            transaction.setUpdatedAt(LocalDateTime.now());

            if (!response.getStatus().equals("SUCCESS")) {
                transaction.setStatus(TransactionStatusEnum.FAILED);
                transaction.setErrorMessage(response.getError());
            }

            transaction = transactionRepository.save(transaction);
            log.info(" *** End of charge process *** ");

            return transaction;

        } catch (Exception e) {
            String paymentStatus = (response != null) ? response.getStatus() : "UNKNOWN";
            log.info("Charge status: {}.", paymentStatus);
            log.error("Unexpected error while saving transaction: ",  e.getMessage());
            throw new ServiceException("Unexpected error while saving transaction");
        }
    }

    /**
     * Obtiene los movimientos de un producto financiero en un período específico.
     *
     * @param productId identificador único del producto financiero
     * @param productType tipo de producto (ACCOUNT, CREDIT, CREDIT_CARD)
     * @param startDate fecha inicial del período de consulta
     * @param endDate fecha final del período de consulta
     * @return ProductMovementsResponseDto objeto que contiene la lista de movimientos
     *         y el resumen de transacciones del período
     * @throws BusinessRuleException si el periodo de fechas es incorrecto o tipo de
     *                              producto es desconocido
     * @throws ServiceException si ocurre un error durante la búsqueda
     */
    @Override
    public ProductMovementsResponseDto getProductMovements(
            String productId, String productType, LocalDate startDate, LocalDate endDate) {

        log.info("Getting movements. Product: {}, ProductType: {}, StartDate: {}, EndDate: {}",
                productId, productType, startDate, endDate);

        if (startDate.isAfter(endDate)) {
            throw new BusinessRuleException("StartDate can not be greater than EndDate");
        }

        try {
            List<Transaction> transactionList;

            switch (productType) {
                case "BANK_ACCOUNT" -> {
                    // Deposits
                    transactionList = transactionRepository.findByProductTypeAndSourceAccountId(
                            ProductTypeEnum.valueOf(productType), productId);
                    // Withdrawals
                    transactionList.addAll(
                            transactionRepository.findByProductTypeAndTargetAccountId(
                                    ProductTypeEnum.valueOf(productType), productId));
                }
                case "CREDIT", "CREDIT-CARD" -> {
                    transactionList =
                            transactionRepository.findByProductTypeAndCreditId(
                                    ProductTypeEnum.valueOf(productType), productId);
                }
                default ->
                    throw new BusinessRuleException("Unknown product type");
            }

            transactionList = transactionList.stream()
                    .filter(transaction ->
                            transaction.getTransactionDate().toLocalDate().isAfter(startDate))
                    .filter(transaction ->
                            transaction.getTransactionDate().toLocalDate().isBefore(endDate))
                    .filter(transaction ->
                            transaction.getStatus().equals(TransactionStatusEnum.COMPLETED))
                    .collect(Collectors.toList());
            log.info("Movements: " + transactionList);

            return buildProductMovementsResponse(
                    transactionList, productId, productType, startDate, endDate);

        } catch (Exception e) {
            log.error("Unexpected error when getting movements: ",  e.getMessage());
            throw new ServiceException("Unexpected error when getting movements");
        }
    }

    /**
     * Procesa una transferencia bancaria.
     * Valida la solicitud, verifica si la cuenta origen contiene saldo
     * disponible y registra la transacción.
     *
     * @param transferRequest DTO que contiene la información necesaria para realizar
     *               la transferencia, incluyendo cuenta destino y monto
     * @return Transaction objeto que representa la transacción completada
     */
    @Override
    public Transaction processTransfer(TransferRequestDto transferRequest) {
        log.info("Starting transfer from account {} to account {}",
                transferRequest.getSourceAccountId(),
                transferRequest.getTargetAccountId());
        ApiResponseDto response = null;

        try {
            Transaction transaction =
                    transactionMapper.transferRequestToTransaction(transferRequest);
            response = accountClient.transferToAccount(transferRequest);
            log.info("Results of transfer: " + response);

            setTransferResults(
                    response, transaction,
                    TransactionTypeEnum.TRANSFER,
                    ProductTypeEnum.BANK_ACCOUNT);

            transaction = transactionRepository.save(transaction);
            log.info(" *** End of transfer process *** ");

            return transaction;

        } catch (Exception e) {
            String depositStatus = (response != null) ? response.getStatus() : "UNKNOWN";
            log.info("Transfer status: {}.", depositStatus);
            log.error("Unexpected error while saving transaction: ",  e.getMessage());
            throw new ServiceException("Unexpected error while saving transaction");
        }

    }

    private void setTransferResults(
            ApiResponseDto response, Transaction transaction,
            TransactionTypeEnum transactionType, ProductTypeEnum productType) {
        transaction.setTransactionType(transactionType);
        transaction.setProductType(productType);
        transaction.setStatus(TransactionStatusEnum.COMPLETED);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());

        if (!response.getStatus().equals("SUCCESS")) {
            transaction.setStatus(TransactionStatusEnum.FAILED);
            transaction.setErrorMessage(response.getError());
        }
    }

    private ProductMovementsResponseDto buildProductMovementsResponse(
            List<Transaction> transactionList, String productId,
            String productType, LocalDate startDate, LocalDate endDate) {

        ProductMovementsResponseDto movementsResponse = new ProductMovementsResponseDto();
        movementsResponse.setProductId(productId);
        movementsResponse.setProductType(
                ProductMovementsResponseDto.ProductTypeEnum.valueOf(productType));
        movementsResponse.setStartDate(startDate);
        movementsResponse.setEndDate(endDate);
        movementsResponse.setMovements(transactionList.stream()
                        .map(transaction -> transactionMapper.transactionToResponseDto(transaction))
                .collect(Collectors.toList()));

        return movementsResponse;
    }
}
