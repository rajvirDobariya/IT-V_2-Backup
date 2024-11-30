package com.suryoday.aocpv.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.aocpv.pojo.AocpvLastLogin;
import com.suryoday.aocpv.repository.AocpvLastLoginRepository;
import com.suryoday.aocpv.service.AocpvLastLoginService;

@Service
public class AocpvLastLoginServiceImpl implements AocpvLastLoginService{

	
	@Autowired
	AocpvLastLoginRepository aocpvLastLoginRepository;
	
	
	@Override
	public void save(AocpvLastLogin jm) {
		
		aocpvLastLoginRepository.save(jm);
	}

}
