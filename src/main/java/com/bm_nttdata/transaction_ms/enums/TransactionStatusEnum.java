package com.bm_nttdata.transaction_ms.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enumeración que representa los tipos de estatus de una transacción.
 * Define si la transaccion fue completada o fallida.
 */
public enum TransactionStatusEnum {

    COMPLETED("COMPLETED"),
    FAILED("FAILED");

    private final String value;

    /**
     * Constructor del enum TransactionStatusEnum.
     *
     * @param value Valor string que representa el tipo de estatus
     */
    TransactionStatusEnum(String value) {
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
     * Retorna la representación en string del tipo de estatus de transacción.
     *
     * @return String que representa el tipo de estatus de transacción
     */
    @Override
    public String toString() {
        return String.valueOf(value);
    }

    /**
     * Convierte un valor string a su correspondiente enum TransactionStatusEnum.
     *
     * @param value Valor string a convertir
     * @return El enum TransactionStatusEnum correspondiente al valor
     * @throws IllegalArgumentException si el valor no corresponde a ningún tipo de estatus de
     *                                  transacción válido
     */
    @JsonCreator
    public static TransactionStatusEnum fromValue(String value) {
        for (TransactionStatusEnum type : TransactionStatusEnum.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unexpected value: " + value);
    }
}
