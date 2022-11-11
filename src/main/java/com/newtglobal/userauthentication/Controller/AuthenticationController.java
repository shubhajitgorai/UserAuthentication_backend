package com.newtglobal.userauthentication.Controller;

import com.newtglobal.userauthentication.Entity.ForgotPasswordDto;
import com.newtglobal.userauthentication.Entity.LoginDto;
import com.newtglobal.userauthentication.Entity.OtpMailSender;
import com.newtglobal.userauthentication.Entity.OtpValidator;
import com.newtglobal.userauthentication.Entity.OtpGenerator;
import com.newtglobal.userauthentication.Entity.Role;
import com.newtglobal.userauthentication.Entity.SignUpDto;
import com.newtglobal.userauthentication.Entity.User;
import com.newtglobal.userauthentication.Repository.RoleRepository;
import com.newtglobal.userauthentication.Repository.UserRepository;
import com.newtglobal.userauthentication.Service.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.saml2.Saml2RelyingPartyProperties.AssertingParty.Verification;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

	User user = new User();

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private OtpMailSender mailSender;
	
	private String OTP; 
	@CrossOrigin(origins = "*")
	@PostMapping("/signin")
	public ResponseEntity<String> authenticateUser(@RequestBody LoginDto loginDto) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		return new ResponseEntity<String>("User logged in successfully", HttpStatus.OK);
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto) {

		// add check for username exists in a DB
		if (userRepository.existsByUsername(signUpDto.getUsername())) {
			return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
		}

		// add check for email exists in DB
		if (userRepository.existsByEmail(signUpDto.getEmail())) {
			return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
		}

		// create user object
		User user = new User();
		user.setName(signUpDto.getName());
		user.setUsername(signUpDto.getUsername());
		user.setEmail(signUpDto.getEmail());
		user.setDob(signUpDto.getDob());
		user.setPhonenumber(signUpDto.getPhonenumber());
		user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

		Role roles = roleRepository.findByName("ROLE_ADMIN").get();
		user.setRoles(Collections.singleton(roles));

		userRepository.save(user);

		return new ResponseEntity<>("User registered successfully", HttpStatus.OK);

	}

	@PostMapping("/forgotPassword")
	public boolean forgotPass(@RequestBody ForgotPasswordDto forgotPasswordDto) {
		User dbEmail = userRepository.findByEmail(forgotPasswordDto.getEmail());
		System.out.println("this is user email" + forgotPasswordDto.getEmail());
		System.out.println("this is user dbEmail" + dbEmail);
		OtpGenerator otpGenerator = new OtpGenerator();
		OTP = otpGenerator.getAlphaNumericString();
		if (dbEmail.getEmail().equals(forgotPasswordDto.getEmail())) {
			mailSender.sendSimpleEmail(forgotPasswordDto.getEmail(), "Hi!!! This mail is for generating OTP for user authentication", "Generated OTP :"+ OTP);
			System.out.println("this otp : "+OTP);
			return true;
		}
		return false;
	}
	
	@PostMapping("/otpValidation")
	public String otpValidator(@RequestBody OtpValidator otpValidate) {
		System.out.println("saved otp ;"+ OTP);
		System.out.println("entered otp :"+otpValidate.getOtp());
		if(otpValidate.getOtp().equals(OTP)) {
			BCryptPasswordEncoder passEncrypt = new BCryptPasswordEncoder();
			String encrpytPass = passEncrypt.encode(otpValidate.getPassword()); 
			userRepository.updateUserPass(encrpytPass, otpValidate.getEmail());
			return "password has been changed";
		}
		return "Incorrect OTP!! Please provide correct OTP";
	}
	
}
