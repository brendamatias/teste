package br.com.bsdev.evibbra.controllers.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryExpenseResponse {
	
	private String category;
    private Double totalAmount;
    
    public CategoryExpenseResponse(String category, Double totalAmount) {
		this.category = category;
		this.totalAmount = totalAmount;
	}
    
}