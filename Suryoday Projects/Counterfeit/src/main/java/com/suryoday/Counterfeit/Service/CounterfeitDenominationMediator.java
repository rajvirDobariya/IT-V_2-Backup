package com.suryoday.Counterfeit.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.Counterfeit.Exception.CounterfeitException;
import com.suryoday.Counterfeit.Pojo.Counterfeit;
import com.suryoday.Counterfeit.Pojo.Denomination;
import com.suryoday.Counterfeit.Repository.CounterfeitRepository;
import com.suryoday.Counterfeit.Repository.DenominationRepository;
import com.suryoday.Counterfeit.Utils.DateUtils;
import com.suryoday.Counterfeit.enums.UserRole;

@Service
public class CounterfeitDenominationMediator {

	Logger LOG = LoggerFactory.getLogger(CounterfeitDenominationMediator.class);


	@Autowired
	private DenominationRepository denominationRepository;
	
	@Autowired
	private CounterfeitRepository counterfeitRepository;
	
	public void validateDenominations(JSONArray denominationsJsonArray) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		for (int i = 0; i < denominationsJsonArray.length(); i++) {
			JSONObject denominationJson = (JSONObject) denominationsJsonArray.get(i);

			// Get Fields
			String detectDateString = denominationJson.optString("detectDate");
			String securityFeatureBreached = denominationJson.optString("securityFeatureBreached");
			String serialNumber = denominationJson.optString("serialNumber");
			String tendererAccountNumber = denominationJson.optString("tendererAccountNumber");
			String tendererCustomerName = denominationJson.optString("tendererCustomerName");
			Integer denominationNote = denominationJson.optInt("denominationNote");

			// Validate detectDateString: Not null or empty, and in valid yyyy-MM-dd format
			if (detectDateString == null || detectDateString.isEmpty()) {
				String errorMessage = String.format("detectDate is null or empty in Denomination at index %d!", i);
				LOG.debug("{}", errorMessage);
				throw new CounterfeitException(errorMessage);
			}

			LocalDate detectDate;
			try {
				detectDate = LocalDate.parse(detectDateString, formatter);
			} catch (DateTimeParseException e) {
				String errorMessage = String
						.format("detectDate is not in valid yyyy-MM-dd format in Denomination at index %d!", i);
				LOG.debug("{}", errorMessage);
				throw new CounterfeitException(errorMessage);
			}

			// Validate tendererAccountNumber: Alphanumeric only
			if (!tendererAccountNumber.matches("[a-zA-Z0-9]+")) {
				String errorMessage = String.format(
						"tendererAccountNumber contains non-alphanumeric characters in Denomination at index %d!", i);
				LOG.debug("{}", errorMessage);
				throw new CounterfeitException(errorMessage);
			}

			// Validate tendererCustomerName: Only characters
			if (!tendererCustomerName.matches("[a-zA-Z ]+")) {
				String errorMessage = String.format(
						"tendererCustomerName contains non-alphabetic characters in Denomination at index %d!", i);
				LOG.debug("{}", errorMessage);
				throw new CounterfeitException(errorMessage);
			}

			// Validate denominationNote: Must be one of the specified values
			List<Integer> validDenominations = Arrays.asList(500, 200, 100, 50, 20, 10);
			if (!validDenominations.contains(denominationNote)) {
				String errorMessage = String.format(
						"denominationNote is invalid in Denomination at index %d! Allowed values: 500, 200, 100, 50, 20, 10.",
						i);
				LOG.debug("{}", errorMessage);
				throw new CounterfeitException(errorMessage);
			}

			// Validate serialNumber: Alphanumeric only, no special characters
			if (!serialNumber.matches("[a-zA-Z0-9]+")) {
				String errorMessage = String
						.format("serialNumber contains special characters in Denomination at index %d!", i);
				LOG.debug("{}", errorMessage);
				throw new CounterfeitException(errorMessage);
			}

			// Validate securityFeatureBreached: Only characters including dot and comma
			if (!securityFeatureBreached.matches("[a-zA-Z., ]+")) {
				String errorMessage = String
						.format("securityFeatureBreached contains invalid characters in Denomination at index %d!", i);
				LOG.debug("{}", errorMessage);
				throw new CounterfeitException(errorMessage);
			}
		}
	}

	public int createDenomination(JSONArray denominationsJsonArray, Long roId, Long counterfeitId) {
		List<Denomination> denominations = new ArrayList<>();
		for (int i = 0; i < denominationsJsonArray.length(); i++) {
			JSONObject denominationJson = (JSONObject) denominationsJsonArray.get(i);
			if (denominationJson.has("dailyMonthly") && denominationJson.optString("dailyMonthly").equals("Daily")) {
				continue;
			}
			LocalDate detectDate = DateUtils.convertDateStringIntoLocalDate(denominationJson.optString("detectDate"));

			// 4 CREATE DENOMINATION
			Long id = denominationJson.optLong("id");
			String securityFeatureBreached = denominationJson.optString("securityFeatureBreached");
			String serialNumber = denominationJson.optString("serialNumber");
			String tendererAccountNumber = denominationJson.optString("tendererAccountNumber");
			String tendererCustomerName = denominationJson.optString("tendererCustomerName");
			Integer denominationNote = denominationJson.optInt("denominationNote");

			// Create Denomination
			Denomination denomination = new Denomination();
			if (id == 0) {
				denomination.setCreatedBy(roId);
				denomination.setCreatedDate(new Date());
				denomination.setCounterfeitId(counterfeitId);
			} else {
				denomination = denominationRepository.findById(id).get();
				denomination.setUpdateBy(roId);
				denomination.setUpdatedDate(new Date());
			}
			denomination.setDenominationNote(denominationNote);
			denomination.setDetectDate(detectDate);
			denomination.setSecurityFeatureBreached(securityFeatureBreached);
			denomination.setSerialNumber(serialNumber);
			denomination.setTendererAccountNumber(tendererAccountNumber);
			denomination.setTendererCustomerName(tendererCustomerName);
			denominations.add(denomination);
		}
		// Denominations Save in DB
		return denominationRepository.saveAll(denominations).size();
//		return denominationsJsonArray.length();
	}
	
	public void deleteDenominationsByCounterfeitId(Long counterfeitId) {
		denominationRepository.deleteAllByCounterfeitId(counterfeitId);
	}
	
	public UserRole validateUserRole(String userRole) {
		if (userRole == null) {
			throw new CounterfeitException("User Role null!");
		}

		if (userRole == null || userRole.isEmpty()) {
			LOG.debug("User role is null or empty :: {}", userRole);
			throw new CounterfeitException("User role is null or empty : " + userRole);
		}

		UserRole userRoleEnum = null;
		for (UserRole role : UserRole.values()) {
			if (role.getRole().equals(userRole)) {
				userRoleEnum = role;
				break;
			}
		}
		if (userRoleEnum == null) {
			throw new CounterfeitException("Wrong User role!");
		}
		return userRoleEnum;

	}
	
	public Counterfeit getCounterfeitById(long id) {
		Counterfeit counterfeit = counterfeitRepository.findById(id);
		if (counterfeit == null) {
			throw new CounterfeitException("Activity " + id + " not found!");
		}
		return counterfeit;
	}
	
	public Counterfeit saveCounterfeit(Counterfeit counterfeit) {
		return counterfeitRepository.save(counterfeit);
	}

	public String fetchSessionId(String sessionId) {
		return counterfeitRepository.getAllSessionIds(sessionId);
	}
	
	public void denominationdeleteAll() {
		denominationRepository.deleteAll();
	}

}
