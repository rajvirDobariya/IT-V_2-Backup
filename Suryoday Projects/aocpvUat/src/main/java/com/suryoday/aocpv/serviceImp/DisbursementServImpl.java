package com.suryoday.aocpv.serviceImp;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suryoday.aocpv.pojo.AocpvLoanCreation;
import com.suryoday.aocpv.repository.AocpvLoanCreationRepo;
import com.suryoday.aocpv.service.DisbursementService;

@Component
public class DisbursementServImpl implements DisbursementService {
	private static Logger logger = LoggerFactory.getLogger(DisbursementServImpl.class);
	@Autowired
	AocpvLoanCreationRepo loancreationrepo;

	@Override
	public AocpvLoanCreation fetchDisbursementStatus(long applicationNo) {

		Optional<AocpvLoanCreation> optional = loancreationrepo.getByApplicationNo(applicationNo);
		if (optional.isPresent()) {
			return optional.get();
		}
		throw new NoSuchElementException("No record found");
	}

}
