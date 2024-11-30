package com.suryoday.mhl.service;

import java.time.LocalDateTime;
import java.util.List;

import com.suryoday.mhl.pojo.CreateLead;

public interface CreateLeadService {

			String save(CreateLead createLead);

			List<CreateLead> findByBranchIdAndStatus(String branchId, String status);

			List<CreateLead> findByDate(LocalDateTime startdate, LocalDateTime enddate);

			void updateStatus(String applicationNo, String status);	
}
