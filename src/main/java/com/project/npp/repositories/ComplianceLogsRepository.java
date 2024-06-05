package com.project.npp.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.project.npp.entities.ComplianceLogs;

@Repository
public interface ComplianceLogsRepository extends CrudRepository<ComplianceLogs, Integer> {

}
