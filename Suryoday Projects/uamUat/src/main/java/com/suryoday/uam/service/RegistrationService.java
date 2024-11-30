package com.suryoday.uam.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.suryoday.uam.pojo.RegistrationPage;

@Service
public interface RegistrationService {

	void saveAllDetails(RegistrationPage registrationPage);

	List<RegistrationPage> getReveiewDetails(Long appln, String imageFlow);

}
