package com.suryoday.twowheeler.service;

import com.suryoday.twowheeler.pojo.TwowheelerLoanCreation;

public interface TwowheelerLoanCreationService {

	TwowheelerLoanCreation getbyApplicationNo(String applicationNo);

	TwowheelerLoanCreation fetchByapplicationNo(String applicationNo);

	void save(TwowheelerLoanCreation twowheelerLoanCreation);

}
