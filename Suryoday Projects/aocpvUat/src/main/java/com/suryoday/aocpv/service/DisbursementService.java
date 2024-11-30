package com.suryoday.aocpv.service;

import org.springframework.stereotype.Component;

import com.suryoday.aocpv.pojo.AocpvLoanCreation;

@Component
public interface DisbursementService {

	AocpvLoanCreation fetchDisbursementStatus(long applicationNo);

}
