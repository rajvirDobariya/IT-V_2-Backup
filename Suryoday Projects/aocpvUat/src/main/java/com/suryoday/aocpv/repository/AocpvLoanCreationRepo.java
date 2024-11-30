package com.suryoday.aocpv.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.suryoday.aocpv.pojo.AocpvLoanCreation;

@Repository
public interface AocpvLoanCreationRepo extends JpaRepository<AocpvLoanCreation, Long> {

	@Query(value = "SELECT a from AocpvLoanCreation a where a.applicationNo =:applicationNo")
	Optional<AocpvLoanCreation> getByApplicationNo(long applicationNo);

	@Query(value = "SELECT a from AocpvLoanCreation a where a.branchId =:branchId and a.status =:status")
	List<AocpvLoanCreation> fetchByBranchId(String branchId, String status);

	@Query(value = "SELECT a from AocpvLoanCreation a where a.applicationNo =:applicationNoInLong and a.status =:status")
	Optional<AocpvLoanCreation> getByApplicationNoAndStatus(long applicationNoInLong, String status);

	@Query(value = "SELECT a from AocpvLoanCreation a where a.customerId =:customer")
	List<AocpvLoanCreation> fetchByCustomer(long customer);

	@Modifying
	@Transactional
	@Query(value = "UPDATE AocpvLoanCreation set IsDmsUpload =:IsDmsUpload where applicationNo =:applicationNo")
	void updateStatusbyapplicaton(long applicationNo, String IsDmsUpload);

	@Query(value = "SELECT a from AocpvLoanCreation a where a.IsDmsUpload =:IsDmsUpload")
	List<AocpvLoanCreation> fetchByIsdms(String IsDmsUpload);

	@Query(value = "SELECT a from AocpvLoanCreation a where a.IsBrNetImageUpload =:IsBrNetImageUpload")
	List<AocpvLoanCreation> fetchByIsBrImage(String IsBrNetImageUpload);
}
