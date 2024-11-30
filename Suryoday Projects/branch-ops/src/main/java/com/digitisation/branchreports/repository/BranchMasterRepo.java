package com.digitisation.branchreports.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.digitisation.branchreports.model.BranchMaster;

@Repository

public interface BranchMasterRepo extends JpaRepository<BranchMaster, Integer> {

	@Query("select p from BranchMaster p where p.applicablefor=:applicablefor")
	public List<BranchMaster> getbranches(String applicablefor);

	@Query("select p from BranchMaster p")
	public List<BranchMaster> findAll();

	@Query("select p from BranchMaster p WHERE branchid IN :branchIds")
	public List<BranchMaster> findByBranches(List<Long> branchIds);

	@Query("select p.branchname from BranchMaster p where p.branchcode=:branchcode")
	public String getbranchname(String branchcode);

	@Query("SELECT e FROM BranchMaster e WHERE e.applicablefor IN (:applicablefor)")
	List<BranchMaster> getonlydigibranches(@Param("applicablefor") List<String> applicablefor);

	public List<BranchMaster> findByBranchid(Long branchCode);

}
