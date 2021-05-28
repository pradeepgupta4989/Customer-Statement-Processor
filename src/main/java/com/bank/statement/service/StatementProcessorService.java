package com.bank.statement.service;

import com.bank.statement.model.StatementProcessorResponse;
import com.bank.statement.model.Transaction;

import java.util.List;

public interface StatementProcessorService {
	StatementProcessorResponse processStatement(List<Transaction> transactionList);
}
