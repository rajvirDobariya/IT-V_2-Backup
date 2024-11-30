package com.suryoday.connector.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.connector.pojo.RegistrationPage;
import com.suryoday.connector.repository.RegistrationReository;
import com.suryoday.connector.service.RegistrationService;

@Service
public class RegistrationServiceImpl implements RegistrationService {

	RegistrationReository registrationReository;

	@Override
	public void saveAllDetails(RegistrationPage registrationPage) {

		Optional<RegistrationPage> optional = registrationReository.fetchBymobileNumber(registrationPage.getMobile());
		if (optional.isPresent()) {
			RegistrationPage registrationPage2 = optional.get();
			registrationPage.setId(registrationPage2.getId());
			registrationReository.save(registrationPage);
		} else {
			registrationReository.save(registrationPage);
		}
	}

	@Override
	public List<RegistrationPage> getReveiewDetails(Long appln, String imageFlow) {
		List<RegistrationPage> list = registrationReository.findByApplicationNoReview(appln, imageFlow);
		if (list.isEmpty()) {
			throw new NoSuchElementException("list is empty");
		}
		return list;
	}

}
