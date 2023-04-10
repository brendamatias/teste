package br.com.bsdev.evibbra.controllers.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter // Lombok annotation to generate getters for all fields
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseResponse {

	private Long id; // ID of the expense
	private String expenseName; // Name of the expense
	private Double amount; // Amount of the expense
	private LocalDate paymentDate; // Payment date
	private Integer competenceMonth; // Competence month
	private Integer competenceYear; // Competence year
	private CategoryResponse category; // ID of the associated category
	private CompanyResponse company; // (Optional) ID of the associated company
}