package com.suryoday.LoanTracking.ServiceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suryoday.LoanTracking.Pojo.LoanTrackingUserLog;
import com.suryoday.LoanTracking.Repository.LoanTrackingUserLogRepo;
import com.suryoday.LoanTracking.Service.LoanTrackingUserLogService;
import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;

@Component
public class LoanTrackingUserLogServImpl implements LoanTrackingUserLogService {
	@Autowired
	LoanTrackingUserLogRepo userlogrepo;

	@Override
	public void save(LoanTrackingUserLog userlog) {
		userlogrepo.save(userlog);
	}

	@Override
	public List<LoanTrackingUserLog> fetchByApplicationNo(String applicationId) {
		List<LoanTrackingUserLog> list = userlogrepo.fetchByApplication(Long.parseLong(applicationId));
		if (list.isEmpty()) {
			throw new NoSuchElementException("No record found");
		}
		return list;
	}

	@Override
	public List<LoanTrackingUserLog> fetchByDiscrepancy(long appNo, String role1, String role2) {
		List<LoanTrackingUserLog> list = userlogrepo.fetchByDiscrepancy(appNo, role1, role2);
		if (list.isEmpty()) {
			throw new NoSuchElementException("No record found");
		}
		return list;
	}

	@Override
	public String saveuserlog(LoanTrackingUserLog userlog) {
		userlogrepo.save(userlog);
		return "Success";
	}

	@Override
	public LoanTrackingUserLog getByApplication(long applicationNo, String fromStatus, String toStatus) {
		Optional<LoanTrackingUserLog> optional = userlogrepo.getByApplication(applicationNo, fromStatus, toStatus);
		if (optional.isPresent()) {
			return optional.get();
		}
		throw new NoSuchElementException("No record found");
	}

}
