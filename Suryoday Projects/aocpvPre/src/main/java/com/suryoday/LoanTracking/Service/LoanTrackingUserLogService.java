package com.suryoday.LoanTracking.Service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.suryoday.LoanTracking.Pojo.LoanTrackingUserLog;

@Component
public interface LoanTrackingUserLogService {

	void save(LoanTrackingUserLog userlog);

	List<LoanTrackingUserLog> fetchByApplicationNo(String applicationId);

	List<LoanTrackingUserLog> fetchByDiscrepancy(long parseLong, String role1, String role2);

	String saveuserlog(LoanTrackingUserLog userlog);

	LoanTrackingUserLog getByApplication(long applicationNo, String fromStatus, String toStatus);

}
