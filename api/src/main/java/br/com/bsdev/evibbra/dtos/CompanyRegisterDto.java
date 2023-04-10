package br.com.bsdev.evibbra.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyRegisterDto {

	private String name;
	private String cnpj;
	private String legalName;
}
