package com.project.npp.entities.request;

import com.project.npp.entities.ERole;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRoleRequest {
	
	private String username;
	
	private ERole role;
}