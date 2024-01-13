package com.ashokit.service;

import com.ashokit.bindings.User;

import java.util.Map;

public interface RegistrationService {

	public boolean UniqueEmail(String email);

	public Map<Integer, String> getCountries();     

	public Map<Integer, String> getStates(Integer countryId);

	public Map<Integer, String> getCities(Integer stateId);

	public boolean registerUser(User user);

}
