package br.com.bsdev.evibbra.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.bsdev.evibbra.controllers.response.CategoryResponse;
import br.com.bsdev.evibbra.dtos.CategoryRegisterDto;
import br.com.bsdev.evibbra.services.CategoryService;

import java.util.List;

// Extends BaseController to inherit the /api/v1 path
@RestController
public class CategoryController implements BaseController {

	public static final String PATH = "categories"; // The path for this controller

    private final CategoryService categoryService;

    // Constructor-based dependency injection for CategoryService
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Endpoint to retrieve all categories
    @GetMapping(PATH)
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        var categories = categoryService.all();
        return ResponseEntity.ok(categories);
    }

    // Endpoint to retrieve one category
    @GetMapping(PATH + "/{id}")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable(name = "id") Long id) {
        var category = categoryService.getCategory(id);
        return ResponseEntity.ok(category);
    }

    // Endpoint to save a new category
    @PostMapping(PATH)
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRegisterDto dto) {
        CategoryResponse categoryResponse = categoryService.save(dto);
        return ResponseEntity.status(201).body(categoryResponse); // Return 201 Created status
    }

    // Endpoint to update an existing category by ID
    @PutMapping(PATH + "/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable(name = "id") Long id, @RequestBody CategoryRegisterDto dto) {
        CategoryResponse categoryResponse = categoryService.update(id, dto);
        return ResponseEntity.ok(categoryResponse);
    }

    // Endpoint to update the status of a category
    @PatchMapping(PATH + "/{id}/status")
    public ResponseEntity<CategoryResponse> updateCategoryStatus(@PathVariable(name = "id") Long id, @RequestParam(name = "status") String status) {
        if (!status.equalsIgnoreCase("active") && !status.equalsIgnoreCase("archived")) {
            // Return 400 Bad Request if the status is not valid
            return ResponseEntity.badRequest().body(null);
        }

        CategoryResponse categoryResponse = categoryService.updateStatusArchivedOrActive(id, status);
        return ResponseEntity.ok(categoryResponse);
    }
}
