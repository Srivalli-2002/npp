package com.project.npp.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.project.npp.entities.Operator;

@Repository
public interface OperatorRepository  extends CrudRepository<Operator, Integer>{

}
