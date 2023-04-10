package br.com.bsdev.evibbra.dtos;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CategoryRegisterDtoTest {

    // Test the constructor and getters of CategoryRegisterDto
    @Test
    void testCategoryRegisterDtoConstructorAndGetters() {
        // Create an instance of the DTO using the constructor
        String name = "Test Category";
        String description = "This is a test description";
        CategoryRegisterDto dto = new CategoryRegisterDto(name, description);

        // Check if the values are assigned correctly
        assertEquals(name, dto.getName());
        assertEquals(description, dto.getDescription());
    }

    // Test the setters of CategoryRegisterDto
    @Test
    void testCategoryRegisterDtoSetters() {
        // Create an instance of the DTO using the no-args constructor
        CategoryRegisterDto dto = new CategoryRegisterDto();

        // Use setters to assign values
        String name = "Another Category";
        String description = "This is another test description";
        dto.setName(name);
        dto.setDescription(description);

        // Verify if the values are assigned correctly
        assertEquals(name, dto.getName());
        assertEquals(description, dto.getDescription());
    }

    // Test the no-args constructor of CategoryRegisterDto
    @Test
    void testCategoryRegisterDtoNoArgsConstructor() {
        // Create an instance of the DTO using the no-args constructor
        CategoryRegisterDto dto = new CategoryRegisterDto();

        // Check if the default values are null
        assertEquals(null, dto.getName());
        assertEquals(null, dto.getDescription());
    }
}