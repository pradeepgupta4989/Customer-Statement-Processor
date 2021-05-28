package com.bank.statement.controller;

import com.bank.statement.StatementProcessorApplication;
import com.bank.statement.constants.StatementsProcessorConstants;
import com.bank.statement.model.StatementProcessorResponse;
import com.bank.statement.service.StatementProcessorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.WebApplicationContext;

import java.nio.file.Files;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.MOCK, classes={ StatementProcessorApplication.class })
public class StatementProcessorControllerTest {
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockBean
    private StatementProcessorService statementProcessorService;

    @Before
    public void setUp() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testStatementProcessorSuccessWithNoFailedRecords() throws Exception{
        when(statementProcessorService.processStatement(any(List.class))).thenReturn(TestUtil.getStatementProcessorResponseSuccess());

        MvcResult result = this.mockMvc.perform(
                post("/statement/v1/process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Files.readAllBytes(ResourceUtils.getFile("classpath:transaction-records-success.json").toPath()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        StatementProcessorResponse statementProcessorResponseActual = objectMapper.readValue(result.getResponse().getContentAsString(),StatementProcessorResponse.class);
        Assert.assertEquals(StatementsProcessorConstants.SUCCESSFUL,statementProcessorResponseActual.getResult());
    }

    @Test
    public void testStatementProcessorSuccessWithDuplicateReference() throws Exception{

       when(statementProcessorService.processStatement(any(List.class))).thenReturn(TestUtil.getStatementProcessorResponseFailedWithDuplicateRference());

        MvcResult result = this.mockMvc.perform(
                post("/statement/v1/process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Files.readAllBytes(ResourceUtils.getFile("classpath:transaction-records-success.json").toPath()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        StatementProcessorResponse statementProcessorResponseActual = objectMapper.readValue(result.getResponse().getContentAsString(),StatementProcessorResponse.class);
        Assert.assertEquals(StatementsProcessorConstants.DUPLICATE_REFERENCE,statementProcessorResponseActual.getResult());
    }
    @Test
    public void testStatementProcessorSuccessWithIncorrectEndBalance() throws Exception{

        when(statementProcessorService.processStatement(any(List.class))).thenReturn(TestUtil.getStatementProcessorResponseFailedWithEndBalance());

        MvcResult result = this.mockMvc.perform(
                post("/statement/v1/process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Files.readAllBytes(ResourceUtils.getFile("classpath:transaction-records-success.json").toPath()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        StatementProcessorResponse statementProcessorResponseActual = objectMapper.readValue(result.getResponse().getContentAsString(),StatementProcessorResponse.class);
        Assert.assertEquals(StatementsProcessorConstants.INCORRECT_END_BALANCE,statementProcessorResponseActual.getResult());
    }
    @Test
    public void testStatementProcessorSuccessWithDuplicateAndIncorrectEndBalance() throws Exception{

        when(statementProcessorService.processStatement(any(List.class))).thenReturn(TestUtil.getStatementProcessorResponseFailedWithBoth());

        MvcResult result = this.mockMvc.perform(
                post("/statement/v1/process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Files.readAllBytes(ResourceUtils.getFile("classpath:transaction-records-success.json").toPath()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        StatementProcessorResponse statementProcessorResponseActual = objectMapper.readValue(result.getResponse().getContentAsString(),StatementProcessorResponse.class);
        Assert.assertEquals(StatementsProcessorConstants.DUPLICATE_REFERENCE+"_"+StatementsProcessorConstants.INCORRECT_END_BALANCE,statementProcessorResponseActual.getResult());
    }
    @Test
    public void testStatementProcessorSuccessWithBadRequest() throws Exception{
        when(statementProcessorService.processStatement(any(List.class))).thenReturn(TestUtil.getStatementProcessorResponseFailedWithBadRequest());

        MvcResult result = this.mockMvc.perform(
                post("/statement/v1/process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Files.readAllBytes(ResourceUtils.getFile("classpath:transaction-records-bad.json").toPath()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        StatementProcessorResponse statementProcessorResponseActual = objectMapper.readValue(result.getResponse().getContentAsString(),StatementProcessorResponse.class);
        Assert.assertEquals(StatementsProcessorConstants.BAD_REQUEST,statementProcessorResponseActual.getResult());
    }
}
