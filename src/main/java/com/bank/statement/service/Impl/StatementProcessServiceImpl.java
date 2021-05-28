package com.bank.statement.service.Impl;

import com.bank.statement.constants.StatementsProcessorConstants;
import com.bank.statement.model.FailedTransaction;
import com.bank.statement.model.StatementProcessorResponse;
import com.bank.statement.model.Transaction;
import com.bank.statement.service.StatementProcessorService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatementProcessServiceImpl implements StatementProcessorService{
	private Set<String> errorList;

	public StatementProcessorResponse processStatement(List<Transaction> inputTransactionList) {
		errorList = new TreeSet<>();
		return StatementProcessorResponse.builder()
				.failedTransactionList(inputTransactionList
						.parallelStream()
						.filter(record -> isReferenceNotUnique(inputTransactionList, record)  || isEndBalanceNotCorrect(record))
						.map(new StatementProcessServiceImpl()::createFailedTransactionRecord)
						.collect(Collectors.toList()))
				.result(errorList.stream().map(errors -> new String().concat(errors)).collect(Collectors.joining("_")))
				.build();
	}

	private boolean isReferenceNotUnique(List<Transaction> inputTransactionList, Transaction transaction) {
		boolean isReferenceNotUnique = Collections.frequency(inputTransactionList, transaction) > 1;
		if(isReferenceNotUnique)
			errorList.add(StatementsProcessorConstants.DUPLICATE_REFERENCE);
		return isReferenceNotUnique;
	}

	private boolean isEndBalanceNotCorrect(Transaction record) {
		boolean isEndBalanceNotCorrect = !record.getStartBalance().add(record.getMutation()).equals(record.getEndBalance());
		if(isEndBalanceNotCorrect)
			errorList.add(StatementsProcessorConstants.INCORRECT_END_BALANCE);
		return isEndBalanceNotCorrect;
	}

	private FailedTransaction createFailedTransactionRecord(Transaction record) {
		return FailedTransaction.builder().reference(record.getReference()).accountNumber(record.getAccountNumber()).build();
	}
}
