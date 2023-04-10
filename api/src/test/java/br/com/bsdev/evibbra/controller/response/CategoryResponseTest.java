package br.com.bsdev.evibbra.controller.response;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import br.com.bsdev.evibbra.controllers.response.CategoryResponse;

class CategoryResponseTest {

    @Test
    void testCategoryResponseGetters() {
        // Arrange
        Long expectedId = 1L;
        String expectedName = "Category Name";
        String expectedDescription = "Category Description";
        String expectedStatus = "active";

        // Act
        CategoryResponse categoryResponse = new CategoryResponse(expectedId, expectedName, expectedDescription, expectedStatus);

        // Assert
        assertEquals(expectedId, categoryResponse.getId());
        assertEquals(expectedName, categoryResponse.getName());
        assertEquals(expectedDescription, categoryResponse.getDescription());
        assertEquals(expectedStatus, categoryResponse.getStatus());
    }
}