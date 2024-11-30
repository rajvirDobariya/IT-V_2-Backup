package com.suryoday.roaocpv.serviceImpl;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.aocpv.pojo.AocpvLoanCreation;
import com.suryoday.aocpv.repository.AocpvLoanCreationRepo;
import com.suryoday.roaocpv.service.LoanAccountCreationService;

@Service
public class LoanAccountCreationServiceImpl implements LoanAccountCreationService {

	@Autowired
	AocpvLoanCreationRepo aocpvLoanCreationRepo;

	@Override
	public JSONObject cifCreation() {
		int max = 899999;
		int min = 800000;
		int random_int = (int) Math.floor(Math.random() * (max - min + 1) + min);
		String stan = Integer.toString(random_int);
		AocpvLoanCreation aocpvLoanCreation = new AocpvLoanCreation();
		aocpvLoanCreation.setCifNumber(stan);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("Message", "SuccessFully CIFNumber Generated");
		aocpvLoanCreation.setCifMessage(jsonObject.toString());
		aocpvLoanCreation.setDisbursementAmount(stan);
		aocpvLoanCreation.setExistingLoanBalance(stan);
		aocpvLoanCreation.setCreditAmount(stan);
		aocpvLoanCreation.setRateOfInterest("15");
		aocpvLoanCreation.setSanctionedLoanAmount(stan);
		aocpvLoanCreation.setTenure("10");
		aocpvLoanCreation.setCustomerId(random_int);
		aocpvLoanCreation.setApplicationNo(random_int);
		aocpvLoanCreationRepo.save(aocpvLoanCreation);
		jsonObject.put("CIFNumber", stan);
		return jsonObject;
	}

	@Override
	public JSONObject accountCreation() {
		int max = 899999;
		int min = 800000;
		int random_int = (int) Math.floor(Math.random() * (max - min + 1) + min);

		AocpvLoanCreation aocpvLoanCreation = new AocpvLoanCreation();

		JSONObject jsonObject = new JSONObject();
		String stan = Integer.toString(random_int);
		if (random_int % 2 == 0) {

			aocpvLoanCreation.setAccountNumber(stan);
			jsonObject.put("Message", "SuccessFully AccountNumber Generated");
			aocpvLoanCreation.setDisbursementAmount(stan);
			aocpvLoanCreation.setExistingLoanBalance(stan);
			aocpvLoanCreation.setCreditAmount(stan);
			aocpvLoanCreation.setRateOfInterest("15");
			aocpvLoanCreation.setSanctionedLoanAmount(stan);
			aocpvLoanCreation.setTenure("10");
			aocpvLoanCreation.setCustomerId(random_int);
			aocpvLoanCreation.setAccountMessage(jsonObject.toString());
			aocpvLoanCreation.setApplicationNo(random_int);
			aocpvLoanCreationRepo.save(aocpvLoanCreation);
			jsonObject.put("AccountNumber", stan);
			return jsonObject;
		} else {
			
			jsonObject.put("Message", "Falied To Generated Account Number");
			return jsonObject;
		}

	}

	@Override
	public JSONObject loanCreation() {
		int max = 899999;
		int min = 800000;
		int random_int = (int) Math.floor(Math.random() * (max - min + 1) + min);
		String stan = Integer.toString(random_int);
		AocpvLoanCreation aocpvLoanCreation = new AocpvLoanCreation();

		JSONObject jsonObject = new JSONObject();

		aocpvLoanCreation.setLoanAccoutNumber(stan);
		jsonObject.put("Message", "SuccessFully LoanAccountNumber Generated");
		aocpvLoanCreation.setDisbursementAmount(stan);
		aocpvLoanCreation.setExistingLoanBalance(stan);
		aocpvLoanCreation.setCreditAmount(stan);
		aocpvLoanCreation.setRateOfInterest("15");
		aocpvLoanCreation.setSanctionedLoanAmount(stan);
		aocpvLoanCreation.setTenure("10");
		aocpvLoanCreation.setCustomerId(random_int);
		aocpvLoanCreation.setLoanAccountMessage(jsonObject.toString());
		aocpvLoanCreation.setApplicationNo(random_int);
		aocpvLoanCreationRepo.save(aocpvLoanCreation);
		jsonObject.put("LoanAccountNumber", stan);
		return jsonObject;
	}

	@Override
	public JSONObject disbursement() {
		int max = 899999;
		int min = 800000;
		int random_int = (int) Math.floor(Math.random() * (max - min + 1) + min);
		String stan = Integer.toString(random_int);
		AocpvLoanCreation aocpvLoanCreation = new AocpvLoanCreation();

		JSONObject jsonObject = new JSONObject();

		aocpvLoanCreation.setDisbursementAmount(stan);
		aocpvLoanCreation.setExistingLoanBalance(stan);
		aocpvLoanCreation.setCreditAmount(stan);
		aocpvLoanCreation.setRateOfInterest("15");
		aocpvLoanCreation.setSanctionedLoanAmount(stan);
		aocpvLoanCreation.setTenure("10");
		aocpvLoanCreation.setCustomerId(random_int);
		jsonObject.put("Message", "Disbursement Amount SuccessFully");
		aocpvLoanCreation.setDisbursementMessage(jsonObject.toString());
		aocpvLoanCreation.setApplicationNo(random_int);
		aocpvLoanCreationRepo.save(aocpvLoanCreation);
		jsonObject.put("DisbursementAmount", stan);
		return jsonObject;

	}

}
