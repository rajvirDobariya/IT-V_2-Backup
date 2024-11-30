package com.suryoday.mhl.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.mhl.pojo.MHLIncomeDetails;
import com.suryoday.mhl.repository.MHLIncomeDetailsRepository;
import com.suryoday.mhl.service.MHLIncomeDetailsService;

@Service
public class MHLIncomeDetailsServiceImpl implements MHLIncomeDetailsService{

	@Autowired
	MHLIncomeDetailsRepository incomeDetailsRepository;
	
	@Override
	public void saveIncomeDetails(MHLIncomeDetails income) {
		
		Optional<MHLIncomeDetails> optional = incomeDetailsRepository.getByAadharNo(income.getAadharCard());
		if(optional.isPresent()) {
			MHLIncomeDetails incomeDetails = optional.get();
			income.setId(incomeDetails.getId());
		}
		incomeDetailsRepository.save(income);
	}

}
