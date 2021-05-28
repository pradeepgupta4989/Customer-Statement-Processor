package com.bank.statement.service.impl;

import com.bank.statement.constants.StatementsProcessorConstants;
import com.bank.statement.model.StatementProcessorResponse;
import com.bank.statement.model.Transaction;
import com.bank.statement.service.Impl.StatementProcessServiceImpl;
import com.bank.statement.service.StatementProcessorService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
public class StatementProcessorServiceImplTest {

    private StatementProcessorService statementProcessService;


    @Before
    public void setUp(){
         statementProcessService = new StatementProcessServiceImpl();
    }
    @Test
    public void testProcessStatementWithDuplicateRecords(){
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(Transaction.builder().reference(new Long(12345)).startBalance(new BigDecimal(100))
                .mutation(new BigDecimal(20)).endBalance(new BigDecimal(120)).accountNumber("1283712").build());
        transactionList.add(Transaction.builder().reference(new Long(12345)).startBalance(new BigDecimal(100))
                .mutation(new BigDecimal(20)).endBalance(new BigDecimal(120)).accountNumber("1283712").build());

        StatementProcessorResponse statementProcessorResponse = statementProcessService.processStatement(transactionList);
        Assert.assertEquals(StatementsProcessorConstants.DUPLICATE_REFERENCE, statementProcessorResponse.getResult());
    }

    @Test
    public void testProcessStatementWithIncorrectEndBalance(){
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(Transaction.builder().reference(new Long(12345)).startBalance(new BigDecimal(100))
                .mutation(new BigDecimal(20)).endBalance(new BigDecimal(130)).accountNumber("1283712").build());

        StatementProcessorResponse statementProcessorResponse = statementProcessService.processStatement(transactionList);
        Assert.assertEquals(StatementsProcessorConstants.INCORRECT_END_BALANCE, statementProcessorResponse.getResult());
    }

    @Test
    public void testProcessStatementWithDuplicateReferenceAndIncorrectEndBalance(){
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(Transaction.builder().reference(new Long(12345)).startBalance(new BigDecimal(100))
                .mutation(new BigDecimal(20)).endBalance(new BigDecimal(130)).accountNumber("1283712").build());
        transactionList.add(Transaction.builder().reference(new Long(12345)).startBalance(new BigDecimal(100))
                .mutation(new BigDecimal(20)).endBalance(new BigDecimal(130)).accountNumber("1283712").build());
        transactionList.add(Transaction.builder().reference(new Long(1234567)).startBalance(new BigDecimal(120))
                .mutation(new BigDecimal(20)).endBalance(new BigDecimal(150)).accountNumber("9234929").build());

        StatementProcessorResponse statementProcessorResponse = statementProcessService.processStatement(transactionList);
        Assert.assertEquals(StatementsProcessorConstants.DUPLICATE_REFERENCE+"_"+StatementsProcessorConstants.INCORRECT_END_BALANCE, statementProcessorResponse.getResult());
    }
}
