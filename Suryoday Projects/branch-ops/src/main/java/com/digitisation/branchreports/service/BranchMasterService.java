package com.digitisation.branchreports.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.digitisation.branchreports.model.BranchMaster;

@Service
public interface BranchMasterService {

	public List<BranchMaster> getAll();

	public List<BranchMaster> getBranchById(String requestString);

	// Testing
	public List<Long> getTestingBranches();

	public String updateTestingBranches(String requestString);

	public void addRemainingBranches();

	public String getTestingBranchesCount();

}
