package com.suryoday.twowheeler.serviceImpl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.twowheeler.pojo.TwowheelerUserLog;
import com.suryoday.twowheeler.repository.TwowheelerUserLogRepository;
import com.suryoday.twowheeler.service.TwowheelerUserLogService;

@Service
public class TwowheelerUserLogServiceImpl implements TwowheelerUserLogService{

	@Autowired
	TwowheelerUserLogRepository userLogRepository;
	
	@Override
	public void saveUserLog(String applicationNo, String stage, String x_User_ID) {
		
		TwowheelerUserLog twowheelerUserLog=new TwowheelerUserLog(applicationNo, stage, x_User_ID, LocalDateTime.now());
		userLogRepository.save(twowheelerUserLog);
	}

}
