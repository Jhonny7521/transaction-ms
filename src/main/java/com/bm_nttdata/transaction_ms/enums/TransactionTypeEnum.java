package com.bm_nttdata.transaction_ms.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TransactionTypeEnum {
    DEPOSIT("DEPOSIT"),
    WITHDRAWAL("WITHDRAWAL"),
    PAYMENT("PAYMENT"),
    CREDIT_CHARGE("CREDIT_CHARGE"),
    TRANSFER("TRANSFER");

    private final String value;

    TransactionTypeEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString(){ return String.valueOf(value); }

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
