package br.com.bsdev.evibbra.controllers.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter // Lombok annotation to generate getters for all fields
@AllArgsConstructor // Lombok annotation to generate a constructor with all fields as parameters
public class CategoryResponse {
	
	private Long id;
    private String name;
    private String description;
    private String status;

}