package br.com.bsdev.evibbra.controllers.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BalanceResponse {

    private Integer competenceYear;      // The year of competence
    private Integer competenceMonth;     // The month of competence
    private Double totalAmountExpense;   // Total amount of expenses for that month
    private Double totalAmountInvoice;   // Total amount of invoices for that month
}