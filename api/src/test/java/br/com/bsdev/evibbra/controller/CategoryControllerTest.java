package br.com.bsdev.evibbra.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import br.com.bsdev.evibbra.controllers.CategoryController;
import br.com.bsdev.evibbra.controllers.response.CategoryResponse;
import br.com.bsdev.evibbra.dtos.CategoryRegisterDto;
import br.com.bsdev.evibbra.services.CategoryService;

class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    @Test
    void testGetAllCategories() throws Exception {
        // Given a list of CategoryResponse
        List<CategoryResponse> categories = new ArrayList<>();
        categories.add(new CategoryResponse(1L, "Food", "Expense for food", "active"));
        categories.add(new CategoryResponse(2L, "Transport", "Expense for transport", "active"));

        // Mock the behavior of categoryService
        when(categoryService.all()).thenReturn(categories);

        // Perform the GET request and verify the response
        mockMvc.perform(get("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testCreateCategory() throws Exception {
        CategoryRegisterDto newCategoryDto = new CategoryRegisterDto();
        newCategoryDto.setName("New Category Name");
        newCategoryDto.setDescription("New Category Description");

        CategoryResponse expectedResponse = new CategoryResponse(1L, newCategoryDto.getName(), newCategoryDto.getDescription(), "active");

        // Mock the behavior of categoryService
        when(categoryService.save(any(CategoryRegisterDto.class))).thenReturn(expectedResponse);

        // Perform the create request and verify the response
        mockMvc.perform(post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"New Category Name\", \"description\":\"New Category Description\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("New Category Name"))
                .andExpect(jsonPath("$.description").value("New Category Description"));
    }

    @Test
    void testUpdateCategory() throws Exception {
        Long categoryId = 1L;
        CategoryRegisterDto updateDto = new CategoryRegisterDto();
        updateDto.setName("Updated Category Name");
        updateDto.setDescription("Updated Category Description");

        CategoryResponse expectedResponse = new CategoryResponse(categoryId, updateDto.getName(), updateDto.getDescription(), "active");

        when(categoryService.update(eq(categoryId), any(CategoryRegisterDto.class))).thenReturn(expectedResponse);

        mockMvc.perform(put("/api/v1/categories/" + categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Category Name\", \"description\":\"Updated Category Description\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(categoryId))
                .andExpect(jsonPath("$.name").value("Updated Category Name"))
                .andExpect(jsonPath("$.description").value("Updated Category Description"));
    }

    @Test
    void testUpdateCategoryStatus() throws Exception {
        // Given a valid category ID and a valid status
        Long categoryId = 1L;
        String status = "active";
        CategoryResponse categoryResponse = new CategoryResponse(categoryId, "Food", "Expense for food", "active");

        // Mock the behavior of categoryService
        when(categoryService.updateStatusArchivedOrActive(categoryId, status)).thenReturn(categoryResponse);

        // Perform the PATCH request and verify the response
        mockMvc.perform(patch("/api/v1/categories/{id}/status", categoryId)
                .param("status", status))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Food"));
    }

    @Test
    void testUpdateCategoryStatusInvalid() throws Exception {
        // Given a valid category ID and an invalid status
        Long categoryId = 1L;
        String status = "invalidStatus";

        // Perform the PATCH request and verify the response
        mockMvc.perform(patch("/api/v1/categories/{id}/status", categoryId)
                .param("status", status))
                .andExpect(status().isBadRequest());
    }
}