package br.com.bsdev.evibbra.controllers.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter // Lombok annotation to generate getters for all fields
@AllArgsConstructor // Lombok annotation to generate a constructor with all fields
@NoArgsConstructor
public class InvoiceResponse {

	private Long id; // Invoice ID
	private Integer number; // Invoice number
	private Double amount; // Invoice amount
	private String serviceDescription; // Description of the service provided
	private Integer competenceMonth; // Competence month (the month the service refers to)
	private Integer competenceYear; // Competence year (the year the service refers to)
	private LocalDate paymentDate; // Payment date, if paid
	private CompanyResponse company; // ID of the associated company

}