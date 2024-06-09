package com.project.npp.repositories;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.project.npp.entities.ERole;
import com.project.npp.entities.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer> {
	
	public Optional<UserEntity> findByUsername(String username);

	public Boolean existsByUsername(String username);
	
	public Optional<UserEntity> findByRole(ERole role);
}
