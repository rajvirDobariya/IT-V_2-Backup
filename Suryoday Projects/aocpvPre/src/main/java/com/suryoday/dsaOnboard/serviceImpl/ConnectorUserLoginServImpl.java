package com.suryoday.dsaOnboard.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suryoday.aocpv.exceptionhandling.DuplicateEntryException;
import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.dsaOnboard.pojo.ConnectorUserLogin;
import com.suryoday.dsaOnboard.repository.ConnectorUserLoginRepo;
import com.suryoday.dsaOnboard.service.ConnectorUserLoginService;

@Component
public class ConnectorUserLoginServImpl implements ConnectorUserLoginService{
	@Autowired
	ConnectorUserLoginRepo userLoginRepository;
	@Override
	public String checkEmailId(String emailId) {
		Optional<String> optional=userLoginRepository.checkEmailId(emailId);
		if(optional.isPresent()) {
			 throw new DuplicateEntryException("EmailId Already present");
		}
		else {
			return "OK";
		}
	}

	@Override
	public String checkMobileNo(String mobileNo) {
		Optional<String> optional=userLoginRepository.checkMobileNo(mobileNo);
		if(optional.isPresent()) {
			 throw new DuplicateEntryException("MobileNo Already present");
		}
		else {
			return "OK";
		}
	}

	@Override
	public void save(ConnectorUserLogin user) {
		userLoginRepository.save(user);
		
	}

	@Override
	public ConnectorUserLogin validateUser(String emailId, String encryptPassword) {
		Optional<ConnectorUserLogin> optional=userLoginRepository.getByMobAndPass(emailId,encryptPassword);
		if(optional.isPresent()) {
			return optional.get();
		}
		throw new NoSuchElementException("No Record Found");
	}

}
