package com.bm_nttdata.transaction_ms.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TransactionStatusEnum {

    COMPLETED("COMPLETED"),
    FAILED("FAILED");

    private final String value;

    TransactionStatusEnum(String value){ this.value = value; }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString(){ return String.valueOf(value); }

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
