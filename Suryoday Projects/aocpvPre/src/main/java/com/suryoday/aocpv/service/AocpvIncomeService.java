package com.suryoday.aocpv.service;

import java.util.List;

import org.json.JSONObject;

import com.suryoday.aocpv.pojo.AocpvIncomeDetails;
import com.suryoday.aocpv.pojo.IncomeDetailWeb;

public interface AocpvIncomeService {

	String saveAll(List<AocpvIncomeDetails> aocpvIncomeDetails);

	List<AocpvIncomeDetails> getByApplicationNo(long applicationNo);

	String getByApplicationNoAndMember(long applicationNo);

	String documentVerify(long applicationNoInLong, String document, String documentType, String documentVerify);

	List<IncomeDetailWeb> fetchByMember(long applicationNoInLong);
	
	String mobileVerify(long mobileNoInLong);

	String saveIncome(AocpvIncomeDetails aocpvIncomeDetails);

	void fetchByPancardNo(String panCardNo);

	void fetchByAadharCard(String aadhar);
	
	void fetchByVoterId(String voterId);
	
	String deleteMember(long applicationNoInLong, String member);

	int getByApplicationNoAndmember(long applicationNoInLong);

	void updateversionCode(long applicationNoInLong, int versioncode);

	List<AocpvIncomeDetails> getByApplicationAndVersion(long applicationNo, int versioncode);
	
	String updateMobileVerify(long applicationNoInLong, String member);

	AocpvIncomeDetails getByApplicationNoAndmember(long applicationNoInLong, String string);

	void savePanResponse(String panResponse, String applicationNo, String member, String isVerify, JSONObject jsonRequest);

	void savevoterIdResponse(String voterResponse, String applicationNo, String member);

	void savePassport(String passportResponse, String applicationNo, String member);

	void saveDrivingLicense(String drivingLicenseReponse, String applicationNo, String member);

	void save(AocpvIncomeDetails incomeDetails);

	AocpvIncomeDetails getByAppNoAndmember(long parseLong, String member);
}
