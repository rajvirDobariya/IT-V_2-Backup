package com.suryoday.twowheeler.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.twowheeler.pojo.TwowheelerLoanCreation;
import com.suryoday.twowheeler.repository.TwowheelerLoanCreationRepository;
import com.suryoday.twowheeler.service.TwowheelerLoanCreationService;

@Service
public class TwowheelerLoanCreationServiceImpl implements TwowheelerLoanCreationService {

	@Autowired
	TwowheelerLoanCreationRepository loanCreationRepository;

	@Override
	public TwowheelerLoanCreation getbyApplicationNo(String applicationNo) {
		Optional<TwowheelerLoanCreation> optional = loanCreationRepository.getByApplicationNo(applicationNo);
		if (optional.isPresent()) {
			return optional.get();
		}
		throw new NoSuchElementException("No record Found");
	}

	@Override
	public TwowheelerLoanCreation fetchByapplicationNo(String applicationNo) {
		Optional<TwowheelerLoanCreation> optional = loanCreationRepository.getByApplicationNo(applicationNo);
		if (optional.isPresent()) {
			return optional.get();
		}
		return new TwowheelerLoanCreation();
	}

	@Override
	public void save(TwowheelerLoanCreation twowheelerLoanCreation) {
		loanCreationRepository.save(twowheelerLoanCreation);
	}

}
