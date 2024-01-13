package com.ashokit.rest;

import com.ashokit.bindings.User;
import com.ashokit.constants.AppConstants;
import com.ashokit.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class RegistrationRestController {
	
	@Autowired
	private RegistrationService regService;
	
	@GetMapping("/email{Email}")
	public String CheckEmail(@PathVariable String email) {
		boolean uniqueEmail = regService.UniqueEmail(email);  
		if (uniqueEmail) {
			return AppConstants.UNIQUE;
		}else {
			return AppConstants.DULPICATE;
		}
		
	}
	 @GetMapping("/countries")
	    public Map<Integer , String> getCountries(){
	    	 Map<Integer, String> countries = regService.getCountries();
	    	 return countries;
	    }
	 @GetMapping("/states/{countryId}")
	 public Map<Integer, String> getStates(@PathVariable Integer countryId){
		 return regService.getStates(countryId);
	 }
	 
	 @GetMapping("/cities/{stateId}")
	 public Map<Integer,String> getCity(@PathVariable Integer stateId){
		 Map<Integer, String> cities = regService.getCities(stateId);
		 return cities;
	 }
	 
	 @PostMapping("/saveuser")
	 public String saveuser(@RequestBody  User user) {
		 boolean registerUser = regService.registerUser(user);
		 if (registerUser) {
			return AppConstants.SUCCESS;
		}else {
			return AppConstants.FAIL;
		}
		 
	 }
    

}
