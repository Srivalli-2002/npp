package com.project.npp.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.project.npp.entities.ComplianceLogs;
import com.project.npp.entities.Customer;

@Repository
public interface ComplianceLogsRepository extends CrudRepository<ComplianceLogs, Integer> {

	public Optional<ComplianceLogs> findByCustomer(Customer customer);
}
