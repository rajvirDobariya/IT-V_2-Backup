package com.suryoday.aocpv.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.suryoday.aocpv.pojo.AocpvIncomeDetails;

@Repository
public interface AocpvIncomeDetailsRepository extends JpaRepository<AocpvIncomeDetails, Integer> {

	@Query(value = "SELECT a from AocpvIncomeDetails a where a.applicationNo =:applicationNo and a.member =:member")
	Optional<AocpvIncomeDetails> find(long applicationNo, String member);

//	@Modifying
//	@Transactional
//	@Query(value = "UPDATE AocpvIncomeDetails set earning =:a.earning ,occupation =:a.occupation where applicationNo =:a.applicationNo and member =:a.member")
//	void updateIncomeDetail(AocpvIncomeDetails a);

	List<AocpvIncomeDetails> findByApplicationNo(long applicationNo);

	List<AocpvIncomeDetails> getByMobile(long mobile);

	@Modifying
	@Transactional
	@Query(value = "UPDATE AocpvIncomeDetails set aadharNoVerify =:documentVerify where isActive ='YES' and applicationNo =:applicationNoInLong and aadharCard =:document")
	void aadharCardVerify(long applicationNoInLong, String document, String documentVerify);

	@Modifying
	@Transactional
	@Query(value = "UPDATE AocpvIncomeDetails set pancardNoVerify =:documentVerify where isActive ='YES' and applicationNo =:applicationNoInLong and panCard =:document")
	void panCardVerify(long applicationNoInLong, String document, String documentVerify);

	@Modifying
	@Transactional
	@Query(value = "UPDATE AocpvIncomeDetails set voterIdVerify =:documentVerify where isActive ='YES' and applicationNo =:applicationNoInLong and voterId =:document")
	void voterIdVerify(long applicationNoInLong, String document, String documentVerify);

	@Query(value = "SELECT a.applicationNo from AocpvIncomeDetails a where a.isActive ='YES' and a.mobile =:mobileNoInLong and a.member =:member")
	Optional<Long> mobileVerify(long mobileNoInLong, String member);

	@Query(value = "SELECT top 1 a.id from aocpv_income_details a order by a.id desc", nativeQuery = true)
	Optional<Integer> fetchLastId();

	@Query(value = "SELECT a from AocpvIncomeDetails a where a.isActive ='YES' and a.applicationNo =:applicationNoInLong order by a.id asc")
	List<AocpvIncomeDetails> getByApp(long applicationNoInLong);

	@Query(value = "SELECT a from AocpvIncomeDetails a where a.panCard =:panCardNo and a.isActive ='YES'")
	Optional<AocpvIncomeDetails> getByPanCard(String panCardNo);

	@Query(value = "SELECT a from AocpvIncomeDetails a where a.voterId =:voterId and a.isActive ='YES'")
	Optional<AocpvIncomeDetails> getByVoterId(String voterId);

	@Query(value = "SELECT a from AocpvIncomeDetails a where a.aadharCard =:aadhar and a.isActive ='YES'")
	Optional<AocpvIncomeDetails> getByAadharCard(String aadhar);

	@Modifying
	@Transactional
	@Query(value = "delete [aocpv_income_details] where application_no =:applicationNoInLong and member =:member", nativeQuery = true)
	void deleteByApp(long applicationNoInLong, String member);

	@Modifying
	@Transactional
	@Query(value = "UPDATE AocpvIncomeDetails set versionCode =:versioncode where applicationNo =:applicationNoInLong")
	void updateVersionCode(long applicationNoInLong, int versioncode);

	@Query(value = "select * from  [aocpv_income_details_data_history] where application_no =:applicationNo and [version_code] =:versioncode", nativeQuery = true)
	List<AocpvIncomeDetails> getByAppAndVersionCode(long applicationNo, int versioncode);

}
