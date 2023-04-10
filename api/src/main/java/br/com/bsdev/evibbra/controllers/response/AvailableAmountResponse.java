package br.com.bsdev.evibbra.controllers.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter // Lombok annotation to generate getters for all fields
@AllArgsConstructor // Lombok annotation to generate a constructor with all fields as parameters
public class AvailableAmountResponse {
    
    // The total amount of invoices issued
    Double totalInvoiceAmount;

    // The annual invoice limit for the company
    Double annualInvoiceLimit;
}