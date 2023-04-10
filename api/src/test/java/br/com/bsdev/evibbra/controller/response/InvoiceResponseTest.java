package br.com.bsdev.evibbra.controller.response;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import br.com.bsdev.evibbra.controllers.response.CompanyResponse;
import br.com.bsdev.evibbra.controllers.response.InvoiceResponse;

class InvoiceResponseTest {

    @Test
    void testInvoiceResponseGetters() {
        // Arrange
        Long expectedId = 1L;
        Integer expectedNumber = 1001;
        Double expectedAmount = 250.0;
        String expectedServiceDescription = "Consulting Services";
        Integer expectedCompetenceMonth = 10;
        Integer expectedCompetenceYear = 2024;
        LocalDate expectedPaymentDate = LocalDate.of(2024, 10, 10);

        // Creating a dummy company response
        CompanyResponse expectedCompany = new CompanyResponse(1L, "Company Name", "12.345.678/0001-90", "Legal Company Name", null);

        // Act
        InvoiceResponse invoiceResponse = new InvoiceResponse(expectedId, expectedNumber, expectedAmount,
                expectedServiceDescription, expectedCompetenceMonth, expectedCompetenceYear, 
                expectedPaymentDate, expectedCompany);

        // Assert
        assertEquals(expectedId, invoiceResponse.getId());
        assertEquals(expectedNumber, invoiceResponse.getNumber());
        assertEquals(expectedAmount, invoiceResponse.getAmount());
        assertEquals(expectedServiceDescription, invoiceResponse.getServiceDescription());
        assertEquals(expectedCompetenceMonth, invoiceResponse.getCompetenceMonth());
        assertEquals(expectedCompetenceYear, invoiceResponse.getCompetenceYear());
        assertEquals(expectedPaymentDate, invoiceResponse.getPaymentDate());
        assertEquals(expectedCompany, invoiceResponse.getCompany());
    }
}