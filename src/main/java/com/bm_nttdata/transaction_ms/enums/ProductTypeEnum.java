package com.bm_nttdata.transaction_ms.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ProductTypeEnum {

    BANK_ACCOUNT("BANK_ACCOUNT"),
    CREDIT("CREDIT"),
    CREDIT_CARD("CREDIT_CARD");

    private final String value;

    ProductTypeEnum(String value){ this.value = value; }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString(){ return String.valueOf(value); }

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
