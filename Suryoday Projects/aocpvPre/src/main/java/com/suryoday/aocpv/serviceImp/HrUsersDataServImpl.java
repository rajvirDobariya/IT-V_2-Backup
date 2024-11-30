package com.suryoday.aocpv.serviceImp;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.aocpv.pojo.HRUsersData;
import com.suryoday.aocpv.repository.HRUserDataRepo;
import com.suryoday.aocpv.service.HrUSersDataService;
@Service
public class HrUsersDataServImpl implements HrUSersDataService{

	@Autowired
	HRUserDataRepo repo;
	
	@Override
	public Optional<HRUsersData> fetchUserFromHr(String userId) {
		Optional<HRUsersData> fetchUserFromHr = repo.fetchUserFromHr(userId);
		return fetchUserFromHr;
	}

	@Override
	public void save(HRUsersData hrUsersData) {
//		System.out.println(hrUsersData);
		repo.save(hrUsersData);
		
	}

}
