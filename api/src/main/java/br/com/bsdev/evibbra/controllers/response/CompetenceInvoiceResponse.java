package br.com.bsdev.evibbra.controllers.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CompetenceInvoiceResponse {

	private Integer competenceYear; // The year of competence
	private Integer competenceMonth; // The month of competence
	private Double totalAmount; // The total amount of invoices for that month

	public CompetenceInvoiceResponse(Integer competenceYear, Integer competenceMonth, Double totalAmount) {
		this.competenceYear = competenceYear;
		this.competenceMonth = competenceMonth;
		this.totalAmount = totalAmount;
	}
}