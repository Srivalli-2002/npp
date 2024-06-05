package com.project.npp.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Table(name="port_request")
public class PortRequest {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer requestId;
	
	@ManyToOne
	private Customer customer;
	
	private LocalDate requestDate;
	
	@Enumerated(EnumType.STRING)
	private Status approvalStatus;
	
	private Boolean complianceChecked;
	
	private LocalDate completionDate;
}
