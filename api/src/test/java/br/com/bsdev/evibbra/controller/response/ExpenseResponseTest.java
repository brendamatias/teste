package br.com.bsdev.evibbra.controller.response;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import br.com.bsdev.evibbra.controllers.response.CategoryResponse;
import br.com.bsdev.evibbra.controllers.response.CompanyResponse;
import br.com.bsdev.evibbra.controllers.response.ExpenseResponse;

class ExpenseResponseTest {

    @Test
    void testExpenseResponseGetters() {
        // Arrange
        Long expectedId = 1L;
        String expectedExpenseName = "Office Supplies";
        Double expectedAmount = 150.0;
        LocalDate expectedPaymentDate = LocalDate.of(2024, 10, 6);
        Integer expectedCompetenceMonth = 10;
        Integer expectedCompetenceYear = 2024;

        // Creating dummy category and company responses
        CategoryResponse expectedCategory = new CategoryResponse(1L, "Supplies", "Office supplies category", "active");
        CompanyResponse expectedCompany = new CompanyResponse(1L, "Company Name", "12.345.678/0001-90", "Legal Company Name", null);

        // Act
        ExpenseResponse expenseResponse = new ExpenseResponse(expectedId, expectedExpenseName, expectedAmount,
                expectedPaymentDate, expectedCompetenceMonth, expectedCompetenceYear, expectedCategory, expectedCompany);

        // Assert
        assertEquals(expectedId, expenseResponse.getId());
        assertEquals(expectedExpenseName, expenseResponse.getExpenseName());
        assertEquals(expectedAmount, expenseResponse.getAmount());
        assertEquals(expectedPaymentDate, expenseResponse.getPaymentDate());
        assertEquals(expectedCompetenceMonth, expenseResponse.getCompetenceMonth());
        assertEquals(expectedCompetenceYear, expenseResponse.getCompetenceYear());
        assertEquals(expectedCategory, expenseResponse.getCategory());
        assertEquals(expectedCompany, expenseResponse.getCompany());
    }
}