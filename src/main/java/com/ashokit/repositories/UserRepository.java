package com.ashokit.repositories;

import com.ashokit.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface UserRepository extends JpaRepository<UserEntity, Serializable> {
	
	public UserEntity findByUserEmail(String userEmail);

}
