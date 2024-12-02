package com.bm_nttdata.transaction_ms.client;

import com.bm_nttdata.transaction_ms.DTO.ApiResponseDTO;
import com.bm_nttdata.transaction_ms.model.CreditChargeRequestDTO;
import com.bm_nttdata.transaction_ms.model.PaymentRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "credit-ms", url = "${credit-service.url}")
public interface CreditClient {

    @PostMapping("/credits/payment")
    ApiResponseDTO paymentCredit(PaymentRequestDTO paymentRequest);

    @PostMapping("/credit-cards/payment")
    ApiResponseDTO paymentCreditCard(PaymentRequestDTO paymentRequest);

    @PostMapping("/credit-cards/charge")
    ApiResponseDTO chargeCreditCard(CreditChargeRequestDTO chargeRequest);

}
