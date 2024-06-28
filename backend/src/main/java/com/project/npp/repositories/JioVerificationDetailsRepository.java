package com.project.npp.repositories;
 
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
 
import com.project.npp.entities.JioVerificationDetails;
 
@Repository
public interface JioVerificationDetailsRepository extends CrudRepository<JioVerificationDetails, Long> {
 
}