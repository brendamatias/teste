package br.com.bsdev.evibbra.controller.response;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import br.com.bsdev.evibbra.controllers.response.CompanyResponse;

class CompanyResponseTest {

    @Test
    void testCompanyResponseGetters() {
        // Arrange
        Long expectedId = 1L;
        String expectedName = "Company Name";
        String expectedCnpj = "12.345.678/0001-90";
        String expectedLegalName = "Legal Company Name";

        // Act
        CompanyResponse companyResponse = new CompanyResponse(expectedId, expectedName, expectedCnpj, expectedLegalName, null);

        // Assert
        assertEquals(expectedId, companyResponse.getId());
        assertEquals(expectedName, companyResponse.getName());
        assertEquals(expectedCnpj, companyResponse.getCnpj());
        assertEquals(expectedLegalName, companyResponse.getLegalName());
    }
}