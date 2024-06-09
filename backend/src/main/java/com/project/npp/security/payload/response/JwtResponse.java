package com.project.npp.security.payload.response;

import com.project.npp.entities.Operator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class JwtResponse {
	
	private String accessToken;
	private String tokenType = "Bearer";
	private Integer id;
	private String username;
	private Operator operator;
	@Exclude
	private String role;
	public JwtResponse(String accessToken, Integer id, String username, String role,Operator operator) {
		this.accessToken = accessToken;
		this.id = id;
		this.username = username;
		this.role = role;
		this.operator=operator;
	}
}
