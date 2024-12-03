package com.bm_nttdata.transaction_ms.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enumeración que representa los tipos de producto bancario.
 * Define si el producto es Cuenta Bancaria, Crédito o Tarjeta de crédito.
 */
public enum ProductTypeEnum {

    BANK_ACCOUNT("BANK_ACCOUNT"),
    CREDIT("CREDIT"),
    CREDIT_CARD("CREDIT_CARD");

    private final String value;

    /**
     * Constructor del enum ProductTypeEnum.
     *
     * @param value Valor string que representa el tipo de estatus
     */
    ProductTypeEnum(String value) {
        this.value = value;
    }

    /**
     * Obtiene el valor string del tipo de producto.
     *
     * @return El valor string asociado al tipo de producto
     */
    @JsonValue
    public String getValue() {
        return value;
    }

    /**
     * Retorna la representación en string del tipo de producto.
     *
     * @return String que representa el tipo de producto
     */
    @Override
    public String toString() {
        return String.valueOf(value);
    }

    /**
     * Convierte un valor string a su correspondiente enum ProductTypeEnum.
     *
     * @param value Valor string a convertir
     * @return El enum ProductTypeEnum correspondiente al valor
     * @throws IllegalArgumentException si el valor no corresponde a ningún tipo de producto válido
     */
    @JsonCreator
    public static ProductTypeEnum fromValue(String value) {
        for (ProductTypeEnum type : ProductTypeEnum.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unexpected value: " + value);
    }
}
