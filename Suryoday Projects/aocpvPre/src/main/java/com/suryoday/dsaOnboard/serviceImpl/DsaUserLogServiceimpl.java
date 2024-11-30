package com.suryoday.dsaOnboard.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.dsaOnboard.pojo.DsaUserLog;
import com.suryoday.dsaOnboard.repository.DsaUserLogRepository;
import com.suryoday.dsaOnboard.service.DsaUserLogService;

@Service
public class DsaUserLogServiceimpl implements DsaUserLogService{

	@Autowired
	DsaUserLogRepository dsaUserLogRepository;
	
	@Override
	public void saveUserLog(long applicationNo, String stage, String x_User_ID,String remarks) {
		DsaUserLog dsaUserLog=new DsaUserLog(applicationNo, stage, x_User_ID, LocalDateTime.now());
		if(remarks != null) {
			dsaUserLog.setRemarks(remarks);
		}
		dsaUserLogRepository.save(dsaUserLog);
	}

	@Override
	public List<DsaUserLog> getByApplicationNo(long applicationno) {
		List<DsaUserLog> list=dsaUserLogRepository.getByApplicationNo(applicationno);
		return list;
	}

}
