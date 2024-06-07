package com.project.npp.service;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.npp.entities.ERole;
import com.project.npp.entities.Role;
import com.project.npp.entities.UserEntity;
import com.project.npp.exceptionmessages.QueryMapper;
import com.project.npp.repositories.UserRepository;

@Service
public class UserEntityServiceImpl implements UserEntityService {

	private static Logger loggers = LogManager.getLogger(UserEntityServiceImpl.class);

	@Autowired
	private UserRepository repo;

	// Method to update the role of a user
	@Override
	public String updateRole(Integer userId, Role role) {
		Optional<UserEntity> user = repo.findById(userId);
		if (user.isPresent()) {
			user.get().setRole(role);
			repo.save(user.get());
			loggers.info(QueryMapper.ROLE_UPDATE_SUCCESSFULL);
			return "Role Updated Successfully!!!";
		}
		loggers.info(QueryMapper.ROLE_UPDATE_UNSUCCESSFULL);
		return "Cannot Update Role !!!";
	}

	// Method to add a user entity
	@Override
	public UserEntity addUserEntity(UserEntity user) {
		UserEntity userEntity = repo.save(user);
		loggers.info(QueryMapper.ADD_USER);
		return userEntity;
	}

	// Method to find a user by username
	@Override
	public Optional<UserEntity> findByUsername(String username) {
		Optional<UserEntity> user = repo.findByUsername(username);
		loggers.info(QueryMapper.FIND_BY_USERNAME);
		return user;
	}

	// Method to check if a user exists by username
	@Override
	public Boolean existsByUsername(String username) {
		Boolean b = repo.existsByUsername(username);
		loggers.info(QueryMapper.EXIST_BY_USERNAME);
		return b;
	}

	// Method to find a user by role
	@Override
	public Optional<UserEntity> findByRole(ERole role) {
		Optional<UserEntity> user = repo.findByRole(role);
		loggers.info(QueryMapper.ROLE_FOUND);
		return user;
	}

	// Method to get the list of users
	@Override
	public List<UserEntity> getAllUserEntities()  {
		List<UserEntity> allUsers= (List<UserEntity>) repo.findAll();
		loggers.info(QueryMapper.ROLE_FOUND);
		return allUsers;
	}

}
