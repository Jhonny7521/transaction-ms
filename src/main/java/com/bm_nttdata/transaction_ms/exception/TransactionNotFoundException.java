package com.bm_nttdata.transaction_ms.exception;

/**
 * Excepción que se lanza cuando no se encuentra una transacción en específico.
 * Esta excepción es utilizada para manejar casos donde se intenta acceder a una
 * transacción que no existe.
 */
public class TransactionNotFoundException extends RuntimeException {

    /**
     * Construye una nueva excepción de transacción no encontrada con el mensaje especificado.
     *
     * @param message Mensaje con la razón por la que no se encontró la transacción
     */
    public TransactionNotFoundException(String message) {
        super(message);
    }
}
