package com.project.npp.repositories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.project.npp.entities.VerificationDetails;

@Repository
public interface VerificationDetailsRepository extends CrudRepository<VerificationDetails, Long> {

}
