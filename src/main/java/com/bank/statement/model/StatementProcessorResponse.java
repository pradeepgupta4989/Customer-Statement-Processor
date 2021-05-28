package com.bank.statement.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Builder
@Data
public class StatementProcessorResponse {
	@JsonProperty(value = "result")
	private String result;
	@JsonProperty(value = "errorRecords")
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<FailedTransaction> failedTransactionList;
}
