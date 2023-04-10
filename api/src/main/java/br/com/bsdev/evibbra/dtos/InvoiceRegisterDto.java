package br.com.bsdev.evibbra.dtos;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceRegisterDto {

	private Integer number; // Invoice number
	private Double amount; // Invoice amount
	private String serviceDescription; // Description of the service provided
	private Long companyId; // ID of the associated company
	private Integer competenceMonth; // Competence month (the month the service refers to)
	private Integer competenceYear; // Competence year (the year the service refers to)
	private LocalDate paymentDate; // Payment date, if paid
}