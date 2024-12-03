package com.bm_nttdata.transaction_ms.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enumeración que representa los tipos de transacciones.
 * Define si la transaccion a realizar es un Deposito, Retiro, Pago,
 * Cargo o Transferencia.
 */
public enum TransactionTypeEnum {
    DEPOSIT("DEPOSIT"),
    WITHDRAWAL("WITHDRAWAL"),
    PAYMENT("PAYMENT"),
    CREDIT_CHARGE("CREDIT_CHARGE"),
    TRANSFER("TRANSFER");

    private final String value;

    /**
     * Constructor del enum TransactionTypeEnum.
     *
     * @param value Valor string que representa el tipo de estatus
     */
    TransactionTypeEnum(String value) {
        this.value = value;
    }

    /**
     * Obtiene el valor string del tipo de estatus.
     *
     * @return El valor string asociado al tipo de estatus
     */
    @JsonValue
    public String getValue() {
        return value;
    }

    /**
     * Retorna la representación en string del tipo de transacción.
     *
     * @return String que representa el tipo de transacción
     */
    @Override
    public String toString() {
        return String.valueOf(value);
    }

    /**
     * Convierte un valor string a su correspondiente enum TransactionTypeEnum.
     *
     * @param value Valor string a convertir
     * @return El enum TransactionTypeEnum correspondiente al valor
     * @throws IllegalArgumentException si el valor no corresponde a ningún tipo de
     *                                  transacción válido
     */
    @JsonCreator
    public static TransactionTypeEnum fromValue(String value) {
        for (TransactionTypeEnum type : TransactionTypeEnum.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid TransactionType: " + value);
    }
}
