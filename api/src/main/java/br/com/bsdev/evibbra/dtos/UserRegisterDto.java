package br.com.bsdev.evibbra.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDto {
	
	private String email;
	private String password;
	private String name;
	private String phoneNumber;

}
