package com.bm_nttdata.transaction_ms.service.impl;

import com.bm_nttdata.transaction_ms.DTO.ApiResponseDTO;
import com.bm_nttdata.transaction_ms.client.AccountClient;
import com.bm_nttdata.transaction_ms.client.CreditClient;
import com.bm_nttdata.transaction_ms.entity.Transaction;
import com.bm_nttdata.transaction_ms.enums.ProductTypeEnum;
import com.bm_nttdata.transaction_ms.enums.TransactionStatusEnum;
import com.bm_nttdata.transaction_ms.enums.TransactionTypeEnum;
import com.bm_nttdata.transaction_ms.exception.BusinessRuleException;
import com.bm_nttdata.transaction_ms.exception.ServiceException;
import com.bm_nttdata.transaction_ms.mapper.TransactionMapper;
import com.bm_nttdata.transaction_ms.model.*;
import com.bm_nttdata.transaction_ms.repository.TransactionRepository;
import com.bm_nttdata.transaction_ms.service.ITransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TransactionServiceImpl implements ITransactionService {

    private final AccountClient accountClient;
    private final CreditClient creditClient;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public Transaction processDeposit(DepositRequestDTO depositRequest) {

        log.info("Starting deposit process to bank account: {}", depositRequest.getTargetAccountId());
        ApiResponseDTO response = null;

        try {
            Transaction transaction = transactionMapper.depositRequestToTransaction(depositRequest);
            response = accountClient.depositToAccount(depositRequest);
            log.info("Results of deposit: " + response);

            transaction.setTransactionType(TransactionTypeEnum.DEPOSIT);
            transaction.setProductType(ProductTypeEnum.BANK_ACCOUNT);
            transaction.setStatus(TransactionStatusEnum.COMPLETED);
            transaction.setTransactionDate(LocalDateTime.now());
            transaction.setCreatedAt(LocalDateTime.now());
            transaction.setUpdatedAt(LocalDateTime.now());

            if (!response.getStatus().equals("SUCCESS")){
                transaction.setStatus(TransactionStatusEnum.FAILED);
                transaction.setErrorMessage(response.getError());
            }

            transaction = transactionRepository.save(transaction);
            log.info(" *** End of deposit process *** ");

            return transaction;

        } catch (Exception e){
            String depositStatus = (response != null) ? response.getStatus() : "UNKNOWN";
            log.info("Deposit status: {}.", depositStatus );
            log.error("Unexpected error while saving transaction: ",  e.getMessage());
            throw new ServiceException("Unexpected error while saving transaction");
        }
    }

    @Override
    public Transaction processWithdrawal(WithdrawalRequestDTO withdrawalRequest) {

        log.info("Starting withdrawal process to bank account: {}", withdrawalRequest.getSourceAccountId());
        ApiResponseDTO response = null;

        try {
            Transaction transaction = transactionMapper.withdrawalRequestToTransaction(withdrawalRequest);
            response = accountClient.withdrawalToAccount(withdrawalRequest);
            log.info("Results of withdrawal: " + response);

            transaction.setTransactionType(TransactionTypeEnum.WITHDRAWAL);
            transaction.setProductType(ProductTypeEnum.BANK_ACCOUNT);
            transaction.setStatus(TransactionStatusEnum.COMPLETED);
            transaction.setTransactionDate(LocalDateTime.now());
            transaction.setCreatedAt(LocalDateTime.now());
            transaction.setUpdatedAt(LocalDateTime.now());

            if (!response.getStatus().equals("SUCCESS")){
                transaction.setStatus(TransactionStatusEnum.FAILED);
                transaction.setErrorMessage(response.getError());
            }

            transaction = transactionRepository.save(transaction);
            log.info(" *** End of withdrawal process *** ");

            return transaction;

        } catch (Exception e){
            String depositStatus = (response != null) ? response.getStatus() : "UNKNOWN";
            log.info("Withdrawal status: {}.", depositStatus );
            log.error("Unexpected error while saving transaction: ",  e.getMessage());
            throw new ServiceException("Unexpected error while saving transaction");
        }
    }

    @Override
    public Transaction processPayment(PaymentRequestDTO paymentRequest) {

        log.info("Starting credit product payment process: {}", paymentRequest.getCreditId());
        ApiResponseDTO response = null;

        try {
            Transaction transaction = transactionMapper.paymentRequestToTransaction(paymentRequest);

            if (paymentRequest.getCreditType().getValue().equals("CREDIT")){
                response = creditClient.paymentCredit(paymentRequest);
                transaction.setProductType(ProductTypeEnum.CREDIT);

            } else if (paymentRequest.getCreditType().getValue().equals("CREDIT-CARD")){
                response = creditClient.paymentCreditCard(paymentRequest);
                transaction.setProductType(ProductTypeEnum.CREDIT_CARD);
            }

            log.info("Payment results: " + response);

            transaction.setTransactionType(TransactionTypeEnum.PAYMENT);
            transaction.setStatus(TransactionStatusEnum.COMPLETED);
            transaction.setTransactionDate(LocalDateTime.now());
            transaction.setCreatedAt(LocalDateTime.now());
            transaction.setUpdatedAt(LocalDateTime.now());

            if (!response.getStatus().equals("SUCCESS")){
                transaction.setStatus(TransactionStatusEnum.FAILED);
                transaction.setErrorMessage(response.getError());
            }

            transaction = transactionRepository.save(transaction);
            log.info(" *** End of payment process *** ");

            return transaction;

        } catch (Exception e){
            String paymentStatus = (response != null) ? response.getStatus() : "UNKNOWN";
            log.info("Payment status: {}.", paymentStatus );
            log.error("Unexpected error while saving transaction: ",  e.getMessage());
            throw new ServiceException("Unexpected error while saving transaction");
        }
    }

    @Override
    public Transaction processCreditCharge(CreditChargeRequestDTO chargeRequest) {

        log.info("Starting credit card charge process : {}", chargeRequest.getCreditId());
        ApiResponseDTO response = null;

        try {
            Transaction transaction = transactionMapper.chargeRequestToTransaction(chargeRequest);

            response = creditClient.chargeCreditCard(chargeRequest);
            log.info("Charge results: " + response);

            transaction.setTransactionType(TransactionTypeEnum.CREDIT_CHARGE);
            transaction.setProductType(ProductTypeEnum.CREDIT_CARD);
            transaction.setStatus(TransactionStatusEnum.COMPLETED);
            transaction.setTransactionDate(LocalDateTime.now());
            transaction.setCreatedAt(LocalDateTime.now());
            transaction.setUpdatedAt(LocalDateTime.now());

            if (!response.getStatus().equals("SUCCESS")){
                transaction.setStatus(TransactionStatusEnum.FAILED);
                transaction.setErrorMessage(response.getError());
            }

            transaction = transactionRepository.save(transaction);
            log.info(" *** End of charge process *** ");

            return transaction;

        } catch (Exception e){
            String paymentStatus = (response != null) ? response.getStatus() : "UNKNOWN";
            log.info("Charge status: {}.", paymentStatus );
            log.error("Unexpected error while saving transaction: ",  e.getMessage());
            throw new ServiceException("Unexpected error while saving transaction");
        }
    }

    @Override
    public ProductMovementsResponseDTO getProductMovements(String productId, String productType, LocalDate startDate, LocalDate endDate) {

        log.info("Getting movements. Product: {}, ProductType: {}, StartDate: {}, EndDate: {}", productId, productType, startDate, endDate);

        if (startDate.isAfter(endDate)) throw new BusinessRuleException("StartDate can not be greater than EndDate");

        try {
            List<Transaction> transactionList;

            switch (productType){
                case "BANK_ACCOUNT":
                    // Deposits
                    transactionList = transactionRepository.findByProductTypeAndSourceAccountId(ProductTypeEnum.valueOf(productType), productId);
                    // Withdrawals
                    transactionList.addAll(transactionRepository.findByProductTypeAndTargetAccountId(ProductTypeEnum.valueOf(productType), productId));
                    break;
                case "CREDIT":
                    transactionList = transactionRepository.findByProductTypeAndCreditId(ProductTypeEnum.valueOf(productType), productId);
                    break;
                case "CREDIT-CARD":
                    transactionList = transactionRepository.findByProductTypeAndCreditId(ProductTypeEnum.valueOf(productType), productId);
                    break;
                default:
                    throw new BusinessRuleException("Unknown product type");
            }

            transactionList = transactionList.stream()
                    .filter(transaction -> transaction.getTransactionDate().toLocalDate().isAfter(startDate))
                    .filter(transaction -> transaction.getTransactionDate().toLocalDate().isBefore(endDate))
                    .filter(transaction -> transaction.getStatus().equals(TransactionStatusEnum.COMPLETED))
                    .collect(Collectors.toList());
            log.info("Movements: " + transactionList);

            return buildProductMovementsResponse(transactionList, productId, productType, startDate, endDate);

        } catch (Exception e){
            log.error("Unexpected error when getting movements: ",  e.getMessage());
            throw new ServiceException("Unexpected error when getting movements");
        }
    }

    private ProductMovementsResponseDTO buildProductMovementsResponse(List<Transaction> transactionList, String productId, String productType, LocalDate startDate, LocalDate endDate) {

        ProductMovementsResponseDTO movementsResponse = new ProductMovementsResponseDTO();
        movementsResponse.setProductId(productId);
        movementsResponse.setProductType(ProductMovementsResponseDTO.ProductTypeEnum.valueOf(productType));
        movementsResponse.setStartDate(startDate);
        movementsResponse.setEndDate(endDate);
        movementsResponse.setMovements(transactionList.stream()
                        .map(transaction -> transactionMapper.transactionToResponseDTO(transaction))
                .collect(Collectors.toList()));

        return movementsResponse;
    }
}
