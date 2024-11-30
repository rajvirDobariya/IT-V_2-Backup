package com.suryoday.dsaOnboard.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.dsaOnboard.pojo.DsaOnboardPayoutDetails;
import com.suryoday.dsaOnboard.repository.DsaPayoutDetailsRepository;
import com.suryoday.dsaOnboard.service.DsaPayoutDetailsService;
@Service
public class DsaPayoutDetailsServiceImpl implements DsaPayoutDetailsService{

	@Autowired
	DsaPayoutDetailsRepository dsaPayoutDetailsRepository;
	
	@Override
	public DsaOnboardPayoutDetails getByApplicationNo(String applicationNo) {
		Optional<DsaOnboardPayoutDetails> optional=dsaPayoutDetailsRepository.getByApplicationNo(Long.parseLong(applicationNo));
		if(optional.isPresent()) {
			return optional.get();
		}
		return new DsaOnboardPayoutDetails();
	}

	@Override
	public String saveData(DsaOnboardPayoutDetails dsaOnboardPayoutDetails) {
		dsaPayoutDetailsRepository.save(dsaOnboardPayoutDetails);
		return "update successfully";
	}

}
