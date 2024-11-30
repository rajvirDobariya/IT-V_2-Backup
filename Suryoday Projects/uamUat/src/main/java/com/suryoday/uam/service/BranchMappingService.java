package com.suryoday.uam.service;

import java.util.List;

import com.suryoday.uam.pojo.BranchListResponse;
import com.suryoday.uam.pojo.BranchMaster;

public interface BranchMappingService {

	List<BranchMaster> fetchTopTenBranches();

	String addBranches(BranchMaster branchMaster);

	List<BranchListResponse> fetchAllBranches();

}
