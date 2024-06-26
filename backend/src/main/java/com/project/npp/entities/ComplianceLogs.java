package com.project.npp.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
@Table(name="compliance_logs")
public class ComplianceLogs {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer logId;
	
	@OneToOne
	private PortRequest portRequest;
	
	private boolean checkPassed;
	
	private String notes;
	
	private LocalDate checkDate;
	
	@ManyToOne
	private Customer customer;
}
