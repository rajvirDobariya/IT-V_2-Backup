package com.suryoday.aocpv.serviceImp;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.aocpv.pojo.AppVersion;
import com.suryoday.aocpv.repository.AppVersionRepository;
import com.suryoday.aocpv.service.AppVersionService;

@Service
public class AppVersionServiceImpl implements AppVersionService{

	@Autowired
	AppVersionRepository appVersionRepository;
	
	@Override
	public AppVersion findVersion() {

		Optional<AppVersion> optional=appVersionRepository.findLatestVersion();
		if(optional.isPresent()) {
			
			AppVersion appVersion = optional.get();
			return optional.get();
		}
		throw new NoSuchElementException("data is not present");
	}

}
