package br.com.bsdev.evibbra.services;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.bsdev.evibbra.controllers.response.CategoryResponse;
import br.com.bsdev.evibbra.dtos.CategoryRegisterDto;
import br.com.bsdev.evibbra.entities.Category;
import br.com.bsdev.evibbra.exceptions.InvalidStatusException;
import br.com.bsdev.evibbra.exceptions.ResourceNotFoundException;
import br.com.bsdev.evibbra.repositories.CategoryRepository;

@Service // Annotation indicating that this class is a service in the Spring context
public class CategoryService {

	private final CategoryRepository categoryRepository; // Repository to handle Category entity

	// Constructor-based dependency injection for CategoryRepository
	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	// Retrieve all active categories and map them to response DTOs
	public List<CategoryResponse> all() {
		return categoryRepository.findAll().stream().map(this::mapping) // Use method reference for clarity
				.sorted((o1, o2) -> o1.getName().compareTo(o2.getName())).toList();
	}

	// Retrieve one category and map them to CategoryResponse DTOs
	public CategoryResponse getCategory(Long id) {
		return mapping(findCategoryById(id));
	}

	// Save a new category and return the saved category as a response DTO
	public CategoryResponse save(CategoryRegisterDto dto) {
		var category = new Category(dto.getName(), dto.getDescription(), "active");
		var categorySave = categoryRepository.save(category);
		return mapping(categorySave);
	}

	// Update an existing category by ID
	public CategoryResponse update(Long id, CategoryRegisterDto dto) {
		var category = findCategoryById(id);
		category.setName(dto.getName());
		category.setDescription(dto.getDescription());

		return mapping(categoryRepository.save(category));
	}

	// Update the status of a category to either 'archived' or 'active'
	public CategoryResponse updateStatusArchivedOrActive(Long id, String status) {
		if (!status.equalsIgnoreCase("active") && !status.equalsIgnoreCase("archived")) {
			// Throwing custom exception for invalid status
			throw new InvalidStatusException("Status must be either 'active' or 'archived'.");
		}

		var category = findCategoryById(id);
		category.setStatus(status);
		return mapping(categoryRepository.save(category));
	}

	// Helper method to retrieve category by ID
	private Category findCategoryById(Long id) {
		return categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));
	}

	// Map Category entity to CategoryResponse DTO
	private CategoryResponse mapping(Category category) {
		return new CategoryResponse(category.getId(), category.getName(), category.getDescription(),
				category.getStatus());
	}
}