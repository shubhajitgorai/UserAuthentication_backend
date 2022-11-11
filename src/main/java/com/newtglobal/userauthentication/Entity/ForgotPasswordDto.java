package com.newtglobal.userauthentication.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ForgotPasswordDto {
	private String email;


	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		// TODO Auto-generated method stub
		return email;
	}
}
