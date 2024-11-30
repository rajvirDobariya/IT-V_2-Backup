package com.suryoday.twowheeler.service;

import java.util.List;

import com.suryoday.twowheeler.pojo.TwowheelerUserLog;

public interface TwowheelerUserLogService {

	void saveUserLog(String applicationNo, String stage, String x_User_ID, String userRole);

	List<TwowheelerUserLog> getByApplicationNo(String applicationNo);

}
