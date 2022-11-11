package com.newtglobal.userauthentication.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OtpValidator {
	public String otp;
	
	public String password;
	
	public String email;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getOtp() {
		// TODO Auto-generated method stub
		return otp;
	}
}
