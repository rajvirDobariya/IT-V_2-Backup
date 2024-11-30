package com.suryoday.collections.serviceImpl;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.collections.pojo.UserlogUpdate;
import com.suryoday.collections.repository.UserLogUpdateRepository;
import com.suryoday.collections.service.UserLogUpadteService;

@Service
public class UserLogUpadteServiceImpl implements UserLogUpadteService{

	@Autowired
	UserLogUpdateRepository userLogUpdateRepository;
	
	@Override
	public String userLogUpdatedSave(UserlogUpdate userLogUpadte) {
			
		LocalDate now = LocalDate.now();
		userLogUpadte.setCreatedDate(now);		
		userLogUpdateRepository.save(userLogUpadte);
		
		return "UserLogUpdate Table";
	}

}
