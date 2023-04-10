package br.com.bsdev.evibbra.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PreferenceRegisterDto {
	
	private Double annualInvoiceLimit;
	private Boolean emailNotificationsEnabled;
	private Boolean smsNotificationsEnabled;

}
