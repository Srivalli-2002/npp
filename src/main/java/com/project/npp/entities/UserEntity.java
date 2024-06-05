package com.project.npp.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "user_accounts", uniqueConstraints = { @UniqueConstraint(columnNames = "username") })
public class UserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;

	private String username;

	private String passwordHash;

	@ManyToOne
	private Role role;

	@ManyToOne
	private Operator operator;

	public UserEntity(String username, String password,Operator operator) {
		this.username = username;
		this.passwordHash = password;
		this.operator=operator;
	}
}															
