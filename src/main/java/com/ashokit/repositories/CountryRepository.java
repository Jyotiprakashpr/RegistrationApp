package com.ashokit.repositories;

import com.ashokit.entities.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface CountryRepository extends JpaRepository<CountryEntity, Serializable> {
	


}
