package com.ashokit.service;

import com.ashokit.bindings.User;
import com.ashokit.constants.AppConstants;
import com.ashokit.entities.CityEntity;
import com.ashokit.entities.CountryEntity;
import com.ashokit.entities.StateEntity;
import com.ashokit.entities.UserEntity;
import com.ashokit.exception.RegAppException;
import com.ashokit.props.AppProperties;
import com.ashokit.repositories.CityRepository;
import com.ashokit.repositories.CountryRepository;
import com.ashokit.repositories.StateRepository;
import com.ashokit.repositories.UserRepository;
import com.ashokit.util.EmailUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

@Service
public class RegistrationServiceImpl implements RegistrationService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private CountryRepository countryRepo;

	@Autowired
	private StateRepository stateRepo;

	@Autowired
	private CityRepository cityRepo;
	
	@Autowired
	private EmailUtils emailUtils;
	
	@Autowired
	private AppProperties appProp;

	@Override
	public boolean UniqueEmail(String email) {
		UserEntity userEntity = userRepo.findByUserEmail(email);
		if (userEntity != null) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public Map<Integer, String> getCountries() {
		List<CountryEntity> findAll = countryRepo.findAll();
		Map<Integer, String> countryMap = new HashMap<>();
		for (CountryEntity entity : findAll) {
			countryMap.put(entity.getCountryId(), entity.getCountryName());
		}
		return countryMap;
	}

	@Override
	public Map<Integer, String> getStates(Integer countryId) {
		List<StateEntity> statesList = stateRepo.findByCountryId(countryId);
		Map<Integer, String> stateMap = new HashMap<>();
		for (StateEntity state : statesList) {
			stateMap.put(state.getCountryId(), state.getStateName());
		}

		return stateMap;
	}

	@Override
	public Map<Integer, String> getCities(Integer StateId) {
		List<CityEntity> citiesList = cityRepo.findByStateId(StateId);
		Map<Integer, String> cityMap = new HashMap<>();
		for (CityEntity city : citiesList) {
			cityMap.put(city.getCityId(), city.getCityName());
		}
		return cityMap;
	}

	@Override
	public boolean registerUser(User user) {
		user.setUserPwd(generateTempPwd());
		user.setUserAccStatus(AppConstants.LOCKED);
		UserEntity entity = new UserEntity();

		BeanUtils.copyProperties(user, entity);
		UserEntity save = userRepo.save(entity);
		if (null != save.getUserId()) {
			return sendRegEmail(user);
		}
		return false;
	}

	private String generateTempPwd() {
		String tempPwd = null;
		// TODO: Logic to generate temp pwd
		int leftLimit = 48; // numeral '0'
	    int rightLimit = 122; // letter 'z'
	    int targetStringLength = 6;
	    Random random = new Random();

	      tempPwd = random.ints(leftLimit, rightLimit + 1)
	      .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
	      .limit(targetStringLength)
	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
	      .toString();
		return tempPwd;

	}

	private boolean sendRegEmail(User user) {
		boolean emailsent = false;
		// TODO: Logic to send Email
		  // String subject = "User Registration Success";
		try {
			Map<String, String> messages = appProp.getMessages();
			   String subject = messages.get(AppConstants.REG_MAIL_SUBJECT);
			   String bodyFileName = messages.get(AppConstants.REG_MAIL_BODY_TEMPLATE_FILE);
			   String body = readMailBody(bodyFileName, user );
			   emailsent = emailUtils.sendEmail(subject, body,user.getUserEmail());
			   emailsent = true;
		} catch (Exception e) {
			// TODO: handle exception
			throw new RegAppException(e.getMessage());
		}
		   
		return emailsent;
	}
	
	public String readMailBody (String FileName , User user) {
		String mailBody = null;
		StringBuffer buffer = new StringBuffer();
		Path path = Paths.get(FileName);
		try (Stream<String> stream = Files.lines(path)) {
			stream.forEach(line -> {
				buffer.append(line);
			});
			mailBody = buffer.toString();
			mailBody = mailBody.replace(AppConstants.FNAME, user.getUserFname());
			mailBody = mailBody.replace(AppConstants.EMAIL, user.getUserEmail());
			mailBody = mailBody.replace(AppConstants.TEMP_PWD, user.getUserPwd());
		}catch (Exception e) {
			e.printStackTrace();
		}
		return mailBody;
	}
}
