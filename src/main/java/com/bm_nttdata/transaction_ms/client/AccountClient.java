package com.bm_nttdata.transaction_ms.client;

import com.bm_nttdata.transaction_ms.DTO.ApiResponseDTO;
import com.bm_nttdata.transaction_ms.DTO.TransactionFeeRequestDTO;
import com.bm_nttdata.transaction_ms.DTO.TransactionFeeResponseDTO;
import com.bm_nttdata.transaction_ms.model.DepositRequestDTO;
import com.bm_nttdata.transaction_ms.model.WithdrawalRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "account-ms", url = "${account-service.url}")
public interface AccountClient {

    @PostMapping("/accounts/fee-check")
    TransactionFeeResponseDTO checkTransactionFee(TransactionFeeRequestDTO transactionFeeRequestDTO);

    @PostMapping("/accounts/deposit")
    ApiResponseDTO depositToAccount(DepositRequestDTO depositRequest);

    @PostMapping("/accounts/withdraw")
    ApiResponseDTO withdrawalToAccount(WithdrawalRequestDTO withdrawalRequest);

}
