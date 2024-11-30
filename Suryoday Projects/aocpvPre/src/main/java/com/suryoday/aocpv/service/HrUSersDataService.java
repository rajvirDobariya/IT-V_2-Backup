package com.suryoday.aocpv.service;

import java.util.Optional;

import com.suryoday.aocpv.pojo.HRUsersData;

public interface HrUSersDataService {

	Optional<HRUsersData> fetchUserFromHr(String string);

	void save(HRUsersData hrUsersData);

}
