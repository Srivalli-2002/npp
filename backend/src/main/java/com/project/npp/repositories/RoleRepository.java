package com.project.npp.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.project.npp.entities.ERole;
import com.project.npp.entities.Role;

@Repository
public interface RoleRepository  extends CrudRepository<Role, Integer>{
	
	Optional<Role> findByName(ERole name);
}
