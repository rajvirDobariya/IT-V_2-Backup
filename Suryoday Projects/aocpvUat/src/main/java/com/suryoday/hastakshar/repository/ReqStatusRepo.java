package com.suryoday.hastakshar.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.suryoday.hastakshar.pojo.HastReqStatus;

@Repository
public interface ReqStatusRepo extends JpaRepository<HastReqStatus, String> {

	@Query(value = "SELECT a FROM HastReqStatus a WHERE a.applictioNO=:AppNo")
	List<HastReqStatus> checkApplictionNo(String AppNo);

	@Query(value = "SELECT a FROM HastReqStatus a WHERE a.accountNo=:accountNo")
	List<HastReqStatus> fetchReconList(String accountNo);

	@Query(value = "SELECT TOP(10) * FROM hastakshar_req_status a WHERE a.request_by=:empId order by a.create_date desc;", nativeQuery = true)
	List<HastReqStatus> fetchReqList(String empId);

	@Query(value = "SELECT TOP(100) * FROM hastakshar_req_status a order by a.create_date desc;", nativeQuery = true)
	List<HastReqStatus> fetchOps();

	@Query(value = "SELECT TOP(10) * FROM hastakshar_req_status a WHERE a.approver1=:empId OR a.approver2=:empId OR a.approver3=:empId OR a.approver4=:empId OR a.approver5=:empId order by a.create_date desc;", nativeQuery = true)
	List<HastReqStatus> fetchAprList(String empId);

	/*
	 * @Query(value =
	 * "SELECT a FROM HastReqStatus a WHERE a.policyNo=:policyNo AND a.requestBy=:empId"
	 * ) List<HastReqStatus> fetchBySearchReq(String policyNo, String empId);
	 */

	@Query(value = "SELECT a FROM HastReqStatus a WHERE a.applictioNO=:applicationNo AND a.requestBy=:empId")
	List<HastReqStatus> fetchBySearchReq(String applicationNo, String empId);

	@Query(value = "SELECT a FROM HastReqStatus a WHERE a.applictioNO=:applicationNo")
	List<HastReqStatus> fetchByApplicationNo(String applicationNo);

	@Query(value = "SELECT a FROM HastReqStatus a WHERE a.nature=:nature AND a.requestBy=:requestBy")
	List<HastReqStatus> fetchByNatureAndRequestBy(String nature, String requestBy);

	@Query(value = "SELECT a FROM HastReqStatus a WHERE a.nature=:nature")
	List<HastReqStatus> fetchByNature(String nature);

	@Query(value = "SELECT a FROM HastReqStatus a WHERE a.requestBy=:requestBy")
	List<HastReqStatus> fetchByRequest(String requestBy);

	/*
	 * @Query(value =
	 * "SELECT a FROM HastReqStatus a WHERE a.policyNo=:policyNo AND a.approver1=:empId OR a.approver2=:empId OR a.approver3=:empId OR a.approver4=:empId OR a.approver5=:empId"
	 * ) List<HastReqStatus> fetchBySearchApr(String policyNo, String empId);
	 */
	@Query(value = "SELECT a FROM HastReqStatus a WHERE a.applictioNO=:applicationNo AND a.approver1=:empId OR a.approver2=:empId OR a.approver3=:empId OR a.approver4=:empId OR a.approver5=:empId")
	List<HastReqStatus> fetchBySearchApr(String applicationNo, String empId);

	@Query(value = "SELECT a FROM HastReqStatus a WHERE a.status=:status AND a.requestBy=:empId")
	List<HastReqStatus> fetchByStatusReq(String status, String empId);

	@Query(value = "SELECT a FROM HastReqStatus a WHERE a.status=:status AND a.approver1=:empId OR a.approver2=:empId OR a.approver3=:empId OR a.approver4=:empId OR a.approver5=:empId")
	List<HastReqStatus> fetchByStatusApr(String status, String empId);

	@Query(value = "SELECT a.status FROM HastReqStatus a WHERE a.applictioNO=:applictioNO")
	List<String> fetchStatusByApplicationNo(String applictioNO);

	@Modifying
	@Transactional
	@Query(value = "UPDATE HastReqStatus set status =:status where applictioNO =:applictioNO AND requestBy=:empId")
	void updateStatusReq(String status, String empId, String applictioNO);

	@Modifying
	@Transactional
	@Query(value = "UPDATE HastReqStatus set status =:status where applictioNO =:applictioNO")
	void updateStatusApr(String status, String applictioNO);

	@Modifying
	@Transactional
	@Query(value = "UPDATE HastReqStatus set approver1 =:approver1 where applictioNO =:applictioNO AND status=:Approvestatus")
	void reassignApprover1(String approver1, String applictioNO, String Approvestatus);

	@Modifying
	@Transactional
	@Query(value = "UPDATE HastReqStatus set approver2 =:approver2 where applictioNO =:applictioNO AND status=:Approvestatus")
	void reassignApprover2(String approver2, String applictioNO, String Approvestatus);

	@Modifying
	@Transactional
	@Query(value = "UPDATE HastReqStatus set approver3 =:approver3 where applictioNO =:applictioNO AND status=:Approvestatus")
	void reassignApprover3(String approver3, String applictioNO, String Approvestatus);

	@Modifying
	@Transactional
	@Query(value = "UPDATE HastReqStatus set approver4 =:approver4 where applictioNO =:applictioNO AND status=:Approvestatus")
	void reassignApprover4(String approver4, String applictioNO, String Approvestatus);

	@Modifying
	@Transactional
	@Query(value = "UPDATE HastReqStatus set approver5 =:approver5 where applictioNO =:applictioNO AND status=:Approvestatus")
	void reassignApprover5(String approver5, String applictioNO, String Approvestatus);

	@Modifying
	@Transactional
	@Query(value = "UPDATE HastReqStatus set status =:status where applictioNO =:applictioNO")
	void updateStatusOps(String status, String applictioNO);

	@Query(value = "SELECT a FROM HastReqStatus a WHERE a.status=:status AND a.createDate between :s and :e order by a.createDate desc ")
	List<HastReqStatus> fetchBydate(@Param("s") LocalDateTime startdate, @Param("e") LocalDateTime enddate,
			String status);

	@Query(value = "SELECT a FROM HastReqStatus a WHERE a.applictioNO=:applicationNo AND a.transactionTypes=:txnType")
	List<HastReqStatus> fetchByTxnTyeAndApplicationNo(String applicationNo, String txnType);
	/*
	 * @Query(value =
	 * "SELECT a FROM HastReqStatus a WHERE a.applictioNO=:applictiono OR a.nature=:nature OR a.requestBy=:requestBy OR a.transactionTypes=:transactionTypes"
	 * ) List<HastReqStatus> checkByAppandTranxtype(LocalDateTime enddate,String
	 * applictiono, String nature, String requestBy, String transactionTypes);
	 */

	@Query(value = "SELECT a FROM HastReqStatus a WHERE (a.createDate >= :endDate OR a.updateDate >= :endDate) AND (a.applictioNO=:applictiono OR a.nature=:nature OR a.requestBy=:requestBy OR a.transactionTypes=:transactionTypes OR a.department=:department OR a.keyword=:keyword OR a.lan=:lan OR a.policyNo=:policy OR a.CIF=:cif OR a.accountNo=:accountno)")
	List<HastReqStatus> checkByAppandTranxtype(LocalDateTime endDate, String applictiono, String nature,
			String requestBy, String transactionTypes, String department, String keyword, String lan, String policy,
			String cif, String accountno);

	@Query(value = "SELECT a FROM HastReqStatus a WHERE (a.createDate >= :endDate OR a.updateDate >= :endDate) AND (a.applictioNO = :applicationNo OR a.nature = :nature OR a.requestBy = :requestBy OR a.transactionTypes = :transactionTypes OR a.department = :department) AND a.keyword = :keyword AND a.lan = :lan")
	List<HastReqStatus> checkByLanTranxtype(@Param("endDate") LocalDateTime endDate,
			@Param("applicationNo") String applicationNo, @Param("nature") String nature,
			@Param("requestBy") String requestBy, @Param("transactionTypes") String transactionTypes,
			@Param("department") String department, @Param("keyword") String keyword, @Param("lan") String lan);

	@Query(value = "SELECT a FROM HastReqStatus a WHERE (a.createDate >= :endDate OR a.updateDate >= :endDate) AND (a.applictioNO = :applicationNo OR a.nature = :nature OR a.requestBy = :requestBy OR a.transactionTypes = :transactionTypes OR a.department = :department) AND a.keyword = :keyword AND a.accountNo = :accountno")
	List<HastReqStatus> checkByAccountTranxtype(@Param("endDate") LocalDateTime endDate,
			@Param("applicationNo") String applicationNo, @Param("nature") String nature,
			@Param("requestBy") String requestBy, @Param("transactionTypes") String transactionTypes,
			@Param("department") String department, @Param("keyword") String keyword,
			@Param("accountno") String accountno);

	@Query(value = "SELECT a FROM HastReqStatus a WHERE (a.createDate >= :endDate OR a.updateDate >= :endDate) AND (a.applictioNO = :applicationNo OR a.nature = :nature OR a.requestBy = :requestBy OR a.transactionTypes = :transactionTypes OR a.department = :department) AND a.keyword = :keyword AND a.policyNo =:policy")
	List<HastReqStatus> checkByPolicyTranxtype(@Param("endDate") LocalDateTime endDate,
			@Param("applicationNo") String applicationNo, @Param("nature") String nature,
			@Param("requestBy") String requestBy, @Param("transactionTypes") String transactionTypes,
			@Param("department") String department, @Param("keyword") String keyword, @Param("policy") String policy);

	@Query(value = "SELECT a FROM HastReqStatus a WHERE (a.createDate >= :endDate OR a.updateDate >= :endDate) AND (a.applictioNO = :applicationNo OR a.nature = :nature OR a.requestBy = :requestBy OR a.transactionTypes = :transactionTypes OR a.department = :department) AND a.keyword = :keyword AND a.CIF =:cif")
	List<HastReqStatus> checkByCifTranxtype(@Param("endDate") LocalDateTime endDate,
			@Param("applicationNo") String applicationNo, @Param("nature") String nature,
			@Param("requestBy") String requestBy, @Param("transactionTypes") String transactionTypes,
			@Param("department") String department, @Param("keyword") String keyword, @Param("cif") String cif);

	@Query("SELECT a FROM HastReqStatus a WHERE a.tat48Date =:currentDateHour")
	public List<HastReqStatus> getListForSendEmail(String currentDateHour);

	public HastReqStatus findByApplictioNO(String string);

	@Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM branch_d_holiday_master WHERE holiday_date =:date", nativeQuery = true)
	public Integer existsHolidayByHolidayDate(LocalDate date);

	@Query(value = "SELECT hru2.user_email FROM tbl_hr_user_data AS hru1 JOIN tbl_hr_user_data AS hru2 ON hru1.reporting_manager_id = hru2.user_id WHERE hru1.user_id = '10002'", nativeQuery = true)
	public String getReportingManagerEmailByUserId(String userId);

	@Query(value = "SELECT user_email FROM tbl_hr_user_data where user_id =:userId", nativeQuery = true)
	public String getUserEmailByUserId(String userId);
}
