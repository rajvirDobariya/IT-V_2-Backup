package com.digitisation.branchreports.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.digitisation.branchreports.model.NewBranchMaster;


public interface NewBranchMasterRepo extends JpaRepository<NewBranchMaster, String> {

	public NewBranchMaster findByBranchCode(String branchCode);
}
