package com.bm_nttdata.transaction_ms.client;

import com.bm_nttdata.transaction_ms.dto.ApiResponseDto;
import com.bm_nttdata.transaction_ms.model.CreditChargeRequestDto;
import com.bm_nttdata.transaction_ms.model.PaymentRequestDto;
import feign.FeignException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Cliente Feign para la comunicación con el microservicio de créditos.
 * Proporciona métodos para realizar operaciones relacionadas con creditos
 * y trajetas de créditos a través de llamadas HTTP REST.
 */
@FeignClient(name = "credit-ms", url = "${credit-service.url}")
public interface CreditClient {

    /**
     * Realiza el pago de la cuota mensual de un credito.
     *
     * @param paymentRequest  DTO que contiene la información necesaria para procesar el pago,
     *                     incluyendo el identificador del crédito y el monto a pagar
     * @return DTO de respuesta que indica el resultado de la operación de pago
     * @throws FeignException cuando ocurre un error en la comunicación con el servicio
     */
    @PostMapping("/credits/payment")
    ApiResponseDto paymentCredit(PaymentRequestDto paymentRequest);

    /**
     * Realiza el pago de la cuota mesual de una tarjeta de credito.
     *
     * @param paymentRequest DTO que contiene la información necesaria para procesar el pago,
     *                     incluyendo el identificador de la tarjeta y el monto a pagar
     * @return DTO de respuesta que indica el resultado de la operación de pago
     * @throws FeignException cuando ocurre un error en la comunicación con el servicio
     */
    @PostMapping("/credit-cards/payment")
    ApiResponseDto paymentCreditCard(PaymentRequestDto paymentRequest);

    /**
     * Realiza un cargo a una tarjeta de credito en base al numero de cuotas.
     *
     * @param chargeRequest DTO que contiene la información necesaria para procesar el cargo,
     *                     incluyendo el identificador de la tarjeta y el monto a cargar
     * @return DTO de respuesta que indica el resultado de la operación de pago
     * @throws FeignException cuando ocurre un error en la comunicación con el servicio
     */
    @PostMapping("/credit-cards/charge")
    ApiResponseDto chargeCreditCard(CreditChargeRequestDto chargeRequest);

}
