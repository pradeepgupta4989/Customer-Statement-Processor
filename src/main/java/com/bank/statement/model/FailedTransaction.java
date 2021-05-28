package com.bank.statement.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public class FailedTransaction {
    @JsonProperty(value = "reference")
    private Long reference;
    @JsonProperty(value = "accountNumber")
    private String accountNumber;
}
