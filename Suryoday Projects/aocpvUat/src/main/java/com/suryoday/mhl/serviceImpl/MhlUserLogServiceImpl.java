package com.suryoday.mhl.serviceImpl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.mhl.pojo.CreateLead;
import com.suryoday.mhl.pojo.MhlUserLog;
import com.suryoday.mhl.repository.MhlUserLogRepository;
import com.suryoday.mhl.service.MhlUserLogService;

@Service
public class MhlUserLogServiceImpl implements MhlUserLogService {

	@Autowired
	MhlUserLogRepository mhlUserLogRepository;

	@Override
	public void save(CreateLead createLead, String role, String applicationNo, String status) {
		MhlUserLog userlog = new MhlUserLog();
		userlog.setApplicationNo(applicationNo);
		userlog.setCreatedBy(createLead.getCreatedBy());
		LocalDateTime now = LocalDateTime.now();
		userlog.setCreatedDate(now);
		userlog.setUpdatedDate(now);
		userlog.setRole(role);
		userlog.setRemark("QDE COMPLETED");
		userlog.setStatus(status);
		userlog.setBranchId(createLead.getBranchId());
		userlog.setBranchName(createLead.getBranchName());

		Optional<MhlUserLog> optional = mhlUserLogRepository.fetchByApplicationNoAndstatus(applicationNo, status);
		if (optional.isPresent()) {
			MhlUserLog mhlUserLog = optional.get();
			userlog.setUserId(mhlUserLog.getUserId());
			mhlUserLogRepository.save(userlog);
		} else {
			mhlUserLogRepository.save(userlog);
		}
	}

}
