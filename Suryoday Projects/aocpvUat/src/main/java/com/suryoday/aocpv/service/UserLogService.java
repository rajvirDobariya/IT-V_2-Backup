package com.suryoday.aocpv.service;

import java.util.List;

import com.suryoday.aocpv.pojo.UserLog;

public interface UserLogService {

	void save(String x_User_ID, String applicationNo, String status, String pendingWith, int versionCode);

	List<UserLog> getByApplicationNo(long applicationNoInLong);

	List<UserLog> getByApplicationNoAndStatus(String status, long applicationNo);

}
