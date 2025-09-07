package com.codework.demohotel.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JwtResponse {
	private Long id;
	private String email;
	private String token;
	private String type = "Bearer";
	private List<String> roles;
	
	public JwtResponse(Long id, String email, String jwt, List<String> roles) {
		this.id = id;
		this.email = email;
		this.token = jwt;
		this.roles = roles;
	}
}
