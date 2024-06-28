package com.project.npp.repositories;
 
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
 
import com.project.npp.entities.AirtelVerificationDetails;
 
@Repository
public interface AirtelVerificationDetailsRepository extends CrudRepository<AirtelVerificationDetails, Long> {
 
}