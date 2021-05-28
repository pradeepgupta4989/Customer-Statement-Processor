package com.bank.statement.controller;

import com.bank.statement.constants.StatementsProcessorConstants;
import com.bank.statement.model.FailedTransaction;
import com.bank.statement.model.StatementProcessorResponse;
import com.bank.statement.model.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestUtil {

    public static StatementProcessorResponse getStatementProcessorResponseFailedWithDuplicateRference(){
        List<FailedTransaction> failedTransactionList = new ArrayList<>();
        failedTransactionList.add(FailedTransaction.builder().reference(new Long(12345)).accountNumber("782736482").build());
        failedTransactionList.add(FailedTransaction.builder().reference(new Long(12345)).accountNumber("782736482").build());
        return StatementProcessorResponse.builder()
                .result(StatementsProcessorConstants.DUPLICATE_REFERENCE)
                .failedTransactionList(failedTransactionList)
                .build();
    }

    public static StatementProcessorResponse getStatementProcessorResponseSuccess(){
        return StatementProcessorResponse.builder()
                .result(StatementsProcessorConstants.SUCCESSFUL)
                .build();
    }

    public static StatementProcessorResponse getStatementProcessorResponseFailedWithEndBalance(){
        List<FailedTransaction> failedTransactionList = new ArrayList<>();
        failedTransactionList.add(FailedTransaction.builder().reference(new Long(123456)).accountNumber("782723482").build());
        failedTransactionList.add(FailedTransaction.builder().reference(new Long(123458)).accountNumber("782736482").build());
        return StatementProcessorResponse.builder()
                .result(StatementsProcessorConstants.INCORRECT_END_BALANCE)
                .failedTransactionList(failedTransactionList)
                .build();
    }
    public static StatementProcessorResponse getStatementProcessorResponseFailedWithBoth(){
        List<FailedTransaction> failedTransactionList = new ArrayList<>();
        failedTransactionList.add(FailedTransaction.builder().reference(new Long(123456)).accountNumber("782723482").build());
        failedTransactionList.add(FailedTransaction.builder().reference(new Long(123458)).accountNumber("782736482").build());
        return StatementProcessorResponse.builder()
                .result(StatementsProcessorConstants.DUPLICATE_REFERENCE+"_"+StatementsProcessorConstants.INCORRECT_END_BALANCE)
                .failedTransactionList(failedTransactionList)
                .build();
    }

    public static StatementProcessorResponse getStatementProcessorResponseFailedWithBadRequest(){
            return StatementProcessorResponse.builder()
                .result(StatementsProcessorConstants.BAD_REQUEST)
                .build();
    }
}
