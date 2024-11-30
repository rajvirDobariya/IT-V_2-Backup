package com.suryoday.travelvisit.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.suryoday.travelvisit.dto.BranchDTO;
import com.suryoday.travelvisit.model.BranchHygiene;
import com.suryoday.travelvisit.model.VisitTo;

public interface BranchHygieneRepository extends JpaRepository<BranchHygiene, Long> {

	// FOR RO
	public List<BranchHygiene> findByCreatedDateBetween(Date startDate, Date endDate);

	// FOR CREDIT_OPS
	public List<BranchHygiene> findByCreatedDateBetweenAndCreatedBy(Date startDate, Date endDate, Long userId);

	@Query(value = "SELECT branch_id, branch_name FROM branch_master", nativeQuery = true)
	public List<Object[]> getAllBranches();

}
