package com.suryoday.uam.serviceImp;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.uam.pojo.AocpvLastLogin;
import com.suryoday.uam.repository.AocpvLastLoginRepository;
import com.suryoday.uam.service.AocpvLastLoginService;

@Service
public class AocpvLastLoginServiceImpl implements AocpvLastLoginService{

	
	@Autowired
	AocpvLastLoginRepository aocpvLastLoginRepository;
	
	
	@Override
	public void save(AocpvLastLogin jm) {
		
		aocpvLastLoginRepository.save(jm);
	}

}
