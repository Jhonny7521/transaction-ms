package com.bm_nttdata.transaction_ms.service;

import com.bm_nttdata.transaction_ms.entity.Transaction;
import com.bm_nttdata.transaction_ms.model.CreditChargeRequestDto;
import com.bm_nttdata.transaction_ms.model.DepositRequestDto;
import com.bm_nttdata.transaction_ms.model.PaymentRequestDto;
import com.bm_nttdata.transaction_ms.model.ProductMovementsResponseDto;
import com.bm_nttdata.transaction_ms.model.TransferRequestDto;
import com.bm_nttdata.transaction_ms.model.WithdrawalRequestDto;
import java.time.LocalDate;

/**
 * Servicio que gestiona las operaciones de transacciones financieras.
 * Proporciona métodos para procesar diferentes tipos de transacciones como depósitos,
 * retiros, pagos y cargos a créditos, así como consultas de movimientos.
 */
public interface TransactionService {

    /**
     * Procesa una operación de depósito en una cuenta.
     * Valida la solicitud, realiza el depósito y registra la transacción correspondiente.
     *
     * @param request DTO que contiene la información necesaria para realizar el depósito,
     *               incluyendo cuenta destino y monto
     * @return Transaction objeto que representa la transacción realizada
     */
    Transaction processDeposit(DepositRequestDto request);

    /**
     * Procesa una operación de retiro de una cuenta.
     * Valida la solicitud, verifica fondos disponibles y registra la transacción.
     *
     * @param request DTO que contiene la información necesaria para realizar el retiro,
     *               incluyendo cuenta origen y monto
     * @return Transaction objeto que representa la transacción realizada
     */
    Transaction processWithdrawal(WithdrawalRequestDto request);

    /**
     * Procesa un pago hacia un crédito o tarjeta de crédito.
     * Valida la solicitud, procesa el pago y registra la transacción.
     *
     * @param request DTO que contiene la información necesaria para realizar el pago,
     *               incluyendo identificador del crédito y monto
     * @return Transaction objeto que representa la transacción realizada
     */
    Transaction processPayment(PaymentRequestDto request);

    /**
     * Procesa un cargo a una tarjeta de crédito.
     * Valida la solicitud, verifica el límite disponible y registra la transacción.
     *
     * @param request DTO que contiene la información necesaria para realizar el cargo,
     *               incluyendo identificador de la tarjeta y monto
     * @return Transaction objeto que representa la transacción completada
     */
    Transaction processCreditCharge(CreditChargeRequestDto request);

    /**
     * Obtiene los movimientos de un producto financiero en un período específico.
     *
     * @param productId identificador único del producto bancario
     * @param productType tipo de producto (BANK_ACCOUNT, CREDIT, CREDIT_CARD)
     * @param startDate fecha inicial del período de consulta
     * @param endDate fecha final del período de consulta
     * @return ProductMovementsResponseDto objeto que contiene la lista de movimientos
     *         y el resumen de transacciones del período
     */
    ProductMovementsResponseDto getProductMovements(String productId, String productType,
                                                    LocalDate startDate, LocalDate endDate);

    /**
     * Procesa una transferencia bancaria.
     * Valida la solicitud, verifica si la cuenta origen contiene saldo
     * disponible y registra la transacción.
     *
     * @param transferRequest DTO que contiene la información necesaria para realizar
     *               la transferencia, incluyendo cuenta destino y monto
     * @return Transaction objeto que representa la transacción completada
     */
    Transaction processTransfer(TransferRequestDto transferRequest);
}
