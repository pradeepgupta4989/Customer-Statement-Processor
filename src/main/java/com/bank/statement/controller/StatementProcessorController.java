package com.bank.statement.controller;

import com.bank.statement.constants.StatementsProcessorConstants;
import com.bank.statement.model.StatementProcessorResponse;
import com.bank.statement.model.Transaction;
import com.bank.statement.service.StatementProcessorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/statement/v1/")
public class StatementProcessorController {

	StatementProcessorService statementProcessorService;
	@Autowired
	public StatementProcessorController(StatementProcessorService statementProcessorService){
		this.statementProcessorService = statementProcessorService;
	}

	@PostMapping(value = "process", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<StatementProcessorResponse> process(@RequestBody List<Transaction> transactionList) {

		StatementProcessorResponse response;
		try {
			response = statementProcessorService.processStatement(transactionList);
			if(null != response && CollectionUtils.isEmpty(response.getFailedTransactionList()))
				response.setResult(StatementsProcessorConstants.SUCCESSFUL);
			log.info("Statement Processor Service response : "  + response);
		} catch (Exception exception) {
			log.error(StatementsProcessorConstants.INTERNAL_SERVER_ERROR, exception);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(StatementProcessorResponse.builder()
							.result(StatementsProcessorConstants.INTERNAL_SERVER_ERROR)
							.failedTransactionList(new ArrayList<>())
							.build());
		}
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
