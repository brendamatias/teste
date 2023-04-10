package br.com.bsdev.evibbra.dtos;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseRegisterDto {

	private String expenseName; // Name of the expense
	private Double amount; // Amount of the expense
	private LocalDate paymentDate; // Payment date of the expense
	private Integer competenceMonth; // Competence month
	private Integer competenceYear; // Competence year
	private Long categoryId; // ID of the associated category
	private Long companyId; // (Optional) ID of the associated company, if applicable
}