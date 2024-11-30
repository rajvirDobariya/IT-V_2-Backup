package com.suryoday.loantrackingphase2.service;

import java.util.List;

import org.json.JSONObject;

import com.suryoday.loantrackingphase2.model.LoanProduct;

public interface LoanPrductService {

	public JSONObject getAll(String channel, String X_Session_ID, String X_User_ID,
			Boolean isEncy);

	 public List<LoanProduct> addLoanProducts() ;

}
