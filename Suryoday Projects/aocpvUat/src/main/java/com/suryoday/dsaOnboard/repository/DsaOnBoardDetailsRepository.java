package com.suryoday.dsaOnboard.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.suryoday.dsaOnboard.pojo.DsaOnboardDetails;
import com.suryoday.dsaOnboard.pojo.DsaOnboardResponse;

@Repository
public interface DsaOnBoardDetailsRepository extends JpaRepository<DsaOnboardDetails, Long> {

	Optional<DsaOnboardDetails> getByApplicationNo(long applicationno);

	@Query(value = "SELECT top 1 a.[application_no] from [dsa_onboard_details] a order by a.[application_no] desc", nativeQuery = true)
	Optional<String> fetchLastApplicationNo();

	@Query(value = "SELECT a from DsaOnboardDetails a where a.emailId =:emailId and a.password =:encryptPassword")
	Optional<DsaOnboardDetails> getByMobAndPass(String emailId, String encryptPassword);

	@Query(value = "SELECT new com.suryoday.dsaOnboard.pojo.DsaOnboardResponse(l.applicationNo, l.entity, l.typeOfRelationship, l.productType, l.noOfPartner, l.constitutionType, l.status, l.flowStatus, l.branchId, l.mobileNo, l.companyName, l.dsaCode) from DsaOnboardDetails l where l.status =:Status and l.branchId =:BranchId and l.updatedDate between :s and :e order by l.updatedDate desc")
	List<DsaOnboardResponse> fetchByDate(@Param("s") LocalDateTime startdate, @Param("e") LocalDateTime enddate,
			String Status, String BranchId);

	@Query(value = "SELECT new com.suryoday.dsaOnboard.pojo.DsaOnboardResponse(l.applicationNo, l.entity, l.typeOfRelationship, l.productType, l.noOfPartner, l.constitutionType, l.status, l.flowStatus, l.branchId, l.mobileNo, l.companyName, l.dsaCode) from DsaOnboardDetails l where l.updatedDate between :s and :e order by l.updatedDate desc")
	List<DsaOnboardResponse> fetchByDate(@Param("s") LocalDateTime startdate, @Param("e") LocalDateTime enddate);

	@Query(value = "SELECT new com.suryoday.dsaOnboard.pojo.DsaOnboardResponse(l.applicationNo, l.entity, l.typeOfRelationship, l.productType, l.noOfPartner, l.constitutionType, l.status, l.flowStatus, l.branchId, l.mobileNo, l.companyName, l.dsaCode) from DsaOnboardDetails l where l.status =:Status and l.updatedDate between :s and :e order by l.updatedDate desc")
	List<DsaOnboardResponse> fetchByDateAndStatus(@Param("s") LocalDateTime startdate,
			@Param("e") LocalDateTime enddate, String Status);

	@Query(value = "SELECT new com.suryoday.dsaOnboard.pojo.DsaOnboardResponse(l.applicationNo, l.entity, l.typeOfRelationship, l.productType, l.noOfPartner, l.constitutionType, l.status, l.flowStatus, l.branchId, l.mobileNo, l.companyName, l.dsaCode) from DsaOnboardDetails l where l.branchId =:BranchId and l.updatedDate between :s and :e order by l.updatedDate desc")
	List<DsaOnboardResponse> fetchByDateAndBranch(@Param("s") LocalDateTime startdate,
			@Param("e") LocalDateTime enddate, String BranchId);

	@Query(value = "SELECT new com.suryoday.dsaOnboard.pojo.DsaOnboardResponse(l.applicationNo, l.entity, l.typeOfRelationship, l.productType, l.noOfPartner, l.constitutionType, l.status, l.flowStatus, l.branchId, l.mobileNo, l.companyName, l.dsaCode) from DsaOnboardDetails l where l.applicationNo =:applicationNo")
	List<DsaOnboardResponse> getByApplication(long applicationNo);

	@Query(value = "SELECT a from DsaOnboardDetails a where a.mobileNo =:mobile")
	List<DsaOnboardDetails> validateMobileNo(String mobile);

	Optional<DsaOnboardDetails> getByRegiCode(String regicode);

	@Query(value = "SELECT new com.suryoday.dsaOnboard.pojo.DsaOnboardResponse(l.applicationNo, l.entity, l.typeOfRelationship, l.productType, l.noOfPartner, l.constitutionType, l.status, l.flowStatus, l.branchId, l.mobileNo, l.companyName, l.dsaCode) from DsaOnboardDetails l where l.emailId =:emailId")
	List<DsaOnboardResponse> getByEmailId(String emailId);

	@Query(value = "SELECT new com.suryoday.dsaOnboard.pojo.DsaOnboardResponse(l.applicationNo, l.entity, l.typeOfRelationship, l.productType, l.noOfPartner, l.constitutionType, l.status, l.flowStatus, l.branchId, l.mobileNo, l.companyName, l.dsaCode) from DsaOnboardDetails l where l.mobileNo =:mobileNo")
	List<DsaOnboardResponse> getMobileNo(String mobileNo);

	@Query(value = "SELECT a.dsaCode from DsaOnboardDetails a where a.dsaCode like :code+'%' order by a.dsaCode desc")
	List<String> getlastDsaCode(String code);

	@Query(value = "SELECT new com.suryoday.dsaOnboard.pojo.DsaOnboardResponse(l.applicationNo, l.entity, l.typeOfRelationship, l.productType, l.noOfPartner, l.constitutionType, l.status, l.flowStatus, l.branchId, l.mobileNo, l.companyName, l.dsaCode) from DsaOnboardDetails l where l.branchId =:branchId and l.applicationNo like :customSearch+'%'")
	List<DsaOnboardResponse> getByAppID(String customSearch, String branchId);

	@Query(value = "SELECT new com.suryoday.dsaOnboard.pojo.DsaOnboardResponse(l.applicationNo, l.entity, l.typeOfRelationship, l.productType, l.noOfPartner, l.constitutionType, l.status, l.flowStatus, l.branchId, l.mobileNo, l.companyName, l.dsaCode) from DsaOnboardDetails l where l.branchId =:branchId and l.mobileNo like :customSearch+'%'")
	List<DsaOnboardResponse> getByMob(String customSearch, String branchId);

}
