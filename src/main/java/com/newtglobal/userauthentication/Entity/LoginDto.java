package com.newtglobal.userauthentication.Entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class LoginDto {
	private String userSucc= "User Logged in successfully!!!";
	private String usernameOrEmail;
    private String password;
    
    
    public String getUserSucc() {
		return userSucc;
	}
    
	public String getUsernameOrEmail() {
		return usernameOrEmail;
	}
	public void setUsernameOrEmail(String usernameOrEmail) {
		this.usernameOrEmail = usernameOrEmail;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
    
}
