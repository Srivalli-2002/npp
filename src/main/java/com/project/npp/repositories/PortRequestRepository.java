package com.project.npp.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.project.npp.entities.PortRequest;

@Repository
public interface PortRequestRepository extends CrudRepository<PortRequest, Integer> {

}
