package com.suryoday.mhl.service;

import com.suryoday.mhl.pojo.CreateLead;

public interface MhlUserLogService {

	void save(CreateLead createLead, String role, String applicationNo, String status);

}
