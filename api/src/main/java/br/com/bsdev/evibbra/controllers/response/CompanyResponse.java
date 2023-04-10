package br.com.bsdev.evibbra.controllers.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter // Lombok annotation to generate getters for all fields
@AllArgsConstructor
@NoArgsConstructor
public class CompanyResponse {

	private Long id;
	private String name;
	private String cnpj;
	private String legalName;
	private LocalDateTime inactivatedAt;

}