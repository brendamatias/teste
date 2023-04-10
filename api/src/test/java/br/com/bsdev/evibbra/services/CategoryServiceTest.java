package br.com.bsdev.evibbra.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.bsdev.evibbra.controllers.response.CategoryResponse;
import br.com.bsdev.evibbra.dtos.CategoryRegisterDto;
import br.com.bsdev.evibbra.entities.Category;
import br.com.bsdev.evibbra.exceptions.InvalidStatusException;
import br.com.bsdev.evibbra.exceptions.ResourceNotFoundException;
import br.com.bsdev.evibbra.repositories.CategoryRepository;

import java.util.List;
import java.util.Optional;

class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        category = new Category("Test Category", "Test Description", "active");
        category.setId(1L);
    }

    @Test
    void testAllCategories() {
        when(categoryRepository.findAll()).thenReturn(List.of(category));
        
        List<CategoryResponse> categories = categoryService.all();

        assertEquals(1, categories.size());
        assertEquals("Test Category", categories.get(0).getName());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testSaveCategory() {
        CategoryRegisterDto dto = new CategoryRegisterDto("New Category", "New Description");
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryResponse savedCategory = categoryService.save(dto);

        assertEquals("Test Category", savedCategory.getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testUpdateCategory_Success() {
        CategoryRegisterDto dto = new CategoryRegisterDto("Updated Category", "Updated Description");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryResponse updatedCategory = categoryService.update(1L, dto);

        assertEquals("Updated Category", updatedCategory.getName());
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testUpdateCategory_NotFound() {
        CategoryRegisterDto dto = new CategoryRegisterDto("Updated Category", "Updated Description");
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.update(1L, dto);
        });

        assertEquals("Category not found with ID: 1", exception.getMessage());
    }

    @Test
    void testUpdateStatus_InvalidStatus() {
        InvalidStatusException exception = assertThrows(InvalidStatusException.class, () -> {
            categoryService.updateStatusArchivedOrActive(1L, "invalid");
        });

        assertEquals("Status must be either 'active' or 'archived'.", exception.getMessage());
    }

    @Test
    void testUpdateStatus_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.updateStatusArchivedOrActive(1L, "active");
        });

        assertEquals("Category not found with ID: 1", exception.getMessage());
    }
}