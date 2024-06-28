package com.project.npp.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.project.npp.entities.ComplianceLogs;
import com.project.npp.entities.Customer;

@Repository
public interface ComplianceLogsRepository extends CrudRepository<ComplianceLogs, Integer> {

	public List<ComplianceLogs> findByCustomer(Customer customer);
}
