package com.suryoday.dsaOnboard.service;

import java.util.List;

import com.suryoday.dsaOnboard.pojo.DsaUserLog;

public interface DsaUserLogService {

	void saveUserLog(long applicationNo, String stage, String x_User_ID, String remarks);

	List<DsaUserLog> getByApplicationNo(long applicationno);
}
