package com.suryoday.connector.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.suryoday.connector.pojo.BranchListResponse;
import com.suryoday.connector.pojo.BranchMaster;

@Repository
public interface BranchInfoRepository extends JpaRepository<BranchMaster, Integer> {

	@Query(value = "SELECT a from BranchMaster a where a.branchId =:branchId")
	Optional<BranchMaster> fetchByBranchId(String branchId);

	@Query(value = "SELECT TOP 10 * from branch_master u order by u.created_time desc", nativeQuery = true)
	List<BranchMaster> fetchTopTenBranches();

	@Query(value = "SELECT top 1 a.sr_no from branch_master a order by a.sr_no desc", nativeQuery = true)
	int fetchlastSrno();

	@Query(value = "SELECT new com.suryoday.connector.pojo.BranchListResponse(l.branchId, l.branchName) from BranchMaster l")
	List<BranchListResponse> fetchAllBranches();

}
