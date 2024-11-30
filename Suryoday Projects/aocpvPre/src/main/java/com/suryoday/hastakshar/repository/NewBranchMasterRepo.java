package com.suryoday.hastakshar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.suryoday.hastakshar.pojo.NewBranchMaster;

public interface NewBranchMasterRepo extends JpaRepository<NewBranchMaster, String> {

	public NewBranchMaster findByBranchCode(String branchCode);
}
