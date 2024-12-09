package com.bm_nttdata.transaction_ms.client;

import com.bm_nttdata.transaction_ms.dto.ApiResponseDto;
import com.bm_nttdata.transaction_ms.dto.TransactionFeeRequestDto;
import com.bm_nttdata.transaction_ms.dto.TransactionFeeResponseDto;
import com.bm_nttdata.transaction_ms.model.DepositRequestDto;
import com.bm_nttdata.transaction_ms.model.TransferRequestDto;
import com.bm_nttdata.transaction_ms.model.WithdrawalRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Cliente Feign para interactuar con el microservicio de cuentas bancarias.
 * Proporciona operaciones para verificar comisiones de transacciones y realizar
 * operaciones de depósito, retiro y transferencias entre cuentas bancarias.
 */
@FeignClient(name = "account-ms", url = "${account-service.url}")
public interface AccountClient {

    /**
     * Verifica la comisión aplicable para una transacción específica.
     *
     * @param transactionFeeRequest DTO que contiene los detalles de la transacción
     *                             para calcular la comisión correspondiente
     * @return TransactionFeeResponseDto objeto que contiene la información de la
     *         comisión calculada para la transacción
     */
    @PostMapping("/accounts/fee-check")
    TransactionFeeResponseDto checkTransactionFee(TransactionFeeRequestDto transactionFeeRequest);

    /**
     * Realiza un depósito en una cuenta bancaria.
     *
     * @param depositRequest DTO que contiene la información necesaria para el depósito,
     *                      incluyendo el identificador de la cuenta y el monto a depositar
     * @return ApiResponseDto objeto de respuesta que indica el resultado de la
     *         operación de depósito
     */
    @PostMapping("/accounts/deposit")
    ApiResponseDto depositToAccount(DepositRequestDto depositRequest);

    /**
     * Realiza un retiro de una cuenta bancaria.
     *
     * @param withdrawalRequest DTO que contiene la información necesaria para el retiro,
     *                         incluyendo el identificador de la cuenta y el monto a retirar
     * @return ApiResponseDto objeto de respuesta que indica el resultado de la
     *         operación de retiro
     */
    @PostMapping("/accounts/withdraw")
    ApiResponseDto withdrawalToAccount(WithdrawalRequestDto withdrawalRequest);

    /**
     * Realiza una transferencia bancaria entre dos cuentas del mismo banco.
     *
     * @param transferRequest DTO que contiene la información necesaria para la
     *                        transferencia bancaria
     * @return ApiResponseDto objeto de respuesta que indica el resultado de la
     *         operación de transferencia
     */
    @PostMapping("/accounts/transfer")
    ApiResponseDto transferToAccount(TransferRequestDto transferRequest);
}
