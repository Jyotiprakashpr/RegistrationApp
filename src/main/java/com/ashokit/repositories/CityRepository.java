package com.ashokit.repositories;

import com.ashokit.entities.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;

public interface CityRepository extends JpaRepository<CityEntity, Serializable>{
	
	public List<CityEntity> findByStateId(Integer stateId);

}
