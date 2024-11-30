package com.suryoday.connector.service;

import java.util.List;

import com.suryoday.connector.pojo.BranchListResponse;
import com.suryoday.connector.pojo.BranchMaster;

public interface BranchMappingService {

	List<BranchMaster> fetchTopTenBranches();

	String addBranches(BranchMaster branchMaster);

	List<BranchListResponse> fetchAllBranches();

}
