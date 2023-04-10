package br.com.bsdev.evibbra.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequestDto {

	private String toName;
	private String toEmail;
	private String subject;
	private String template;
	private String remainderAmount;
	private String totalAmount;
	private String limitMei;
}
