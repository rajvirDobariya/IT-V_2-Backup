package com.suryoday.aocpv.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.suryoday.aocpv.pojo.AocpCustomer;

@Repository
public interface AocpCustomerDataRepo extends JpaRepository<AocpCustomer, Long>{

	@Query(value = "SELECT a from AocpCustomer a where a.applicationNo =:applicationNoInLong and a.isActive ='YES'")
	Optional<AocpCustomer> getByApplicationNo(long applicationNoInLong);
	
	@Query(value = "SELECT a from AocpCustomer a where a.customerId =:customerIdInLong and a.isActive ='YES'")
	Optional<AocpCustomer> getByCustomerId(long customerIdInLong);
	
	@Query(value = "SELECT a from AocpCustomer a where a.branchid =:b and a.Status =:s and a.isActive ='YES'")
	Optional<List<AocpCustomer>> getByIdAndStatus(@Param("b") String branchId ,@Param("s") String status);
	
	@Query(value = "SELECT a from AocpCustomer a where a.Status =:s and a.isActive ='YES'")
	Optional<List<AocpCustomer>> findByStatus(@Param ("s") String status);
	
	@Query(value = "SELECT a from AocpCustomer a where a.isActive ='YES' and a.updatedate between :s and :e order by a.timestamp desc ")
	Optional<List<AocpCustomer>> findByDate(@Param ("s") LocalDate startdate ,@Param ("e") LocalDate enddate);

	@Query(value = "SELECT a from AocpCustomer a where a.customerId =:customerIdInLong and a.isActive ='YES'")
	List<AocpCustomer> getByCustomerid(long customerIdInLong);

	@Query(value = "SELECT a from AocpCustomer a where a.name like :cutomesearch+'%' and a.branchid =:branchId and a.isActive ='YES'")
	List<AocpCustomer> getByName(String cutomesearch, String branchId);

	//@Query(value = "SELECT a from AocpCustomer a where a.branchid =:branchId and a.mobileNo =:mobile")
	@Query(value = "SELECT a from AocpCustomer a where a.mobileNo like :mobile+'%'  and a.branchid =:branchId and a.isActive ='YES'")
	List<AocpCustomer> getByMobile(String mobile, String branchId);
	
	@Query(value = "SELECT a from AocpCustomer a where a.customerId like :customerIdInLong+'%' and a.branchid =:branchId and a.isActive ='YES'")
	List<AocpCustomer> getByCustomerID(String customerIdInLong, String branchId);
	
	@Query(value = "SELECT a from AocpCustomer a where a.applicationNo like :appId+'%' and a.branchid =:branchId and a.isActive ='YES'")
	List<AocpCustomer> getByAppID(String appId, String branchId);
	
	@Modifying
	@Transactional
	@Query(value = "update AocpCustomer set Status =:status where applicationNo =:applicationNoInLong")
	void updateStatus(String status,long applicationNoInLong);
	
	@Query(value = "SELECT application_no from aocp_table where customer_id =:s",nativeQuery = true)
    public long getByCustomerIDAppln(@Param ("s") long customerNO);

	@Query(value ="SELECT TOP 10 * from aocp_table u where u.is_active ='YES' and u.updatedate between :s and :e order by u.timestamp desc",nativeQuery = true)
	List<AocpCustomer> findTopTenByDate(@Param ("s") LocalDate startdate ,@Param ("e") LocalDate enddate);

	@Query(value ="SELECT TOP 10 * from aocp_table a where a.is_active ='YES' and a.status =:s order by a.timestamp desc",nativeQuery = true)
	Optional<List<AocpCustomer>> getTopTenByIdAndStatus(@Param("s") String status);

	@Query(value = "SELECT a from AocpCustomer a where a.applicationNo =:applicationNoInLong")
	Optional<AocpCustomer> getByApp(long applicationNoInLong);

	//@Query(value ="SELECT * from aocp_table a where a.status =:s order by a.updatedate desc",nativeQuery = true)
	//List<AocpCustomer> findByDateAndBranchId(LocalDate startdate, LocalDate enddate, String branchId);
	@Procedure("aocpvhistory")
	void aocpvhistory(String applicationNo);

	@Query(value ="SELECT * from [aocp_table_data_history]  a where a.application_no =:applicationNoInLong and a.[versioncode] =:versioncode",nativeQuery = true)
	Optional<AocpCustomer> getByApplicationAndVersionCode(long applicationNoInLong, int versioncode);
	
	@Query(value = "SELECT a from AocpCustomer a where a.applicationNo =:appln and a.isActive ='YES'")
	List<AocpCustomer> findDetailsByApplnPDF(Long appln);

	@Query(value = "SELECT a from AocpCustomer a where a.isActive ='YES' and a.updatedate between :s and :e and a.branchid =:branch order by a.timestamp desc ")
	List<AocpCustomer> findByDateAndBranchID(@Param ("s") LocalDate startdate ,@Param ("e") LocalDate enddate, String branch);
	

    @Modifying
    @Transactional
    @Query(value="update AocpCustomer set productCode =:productCode where applicationNo =:applicationNo and isActive ='YES'")
    void updateProductCode(long applicationNo, String productCode);
    
    @Query(value = "SELECT a from AocpCustomer a where a.isActive ='YES'and a.updatedate between :s and :e and a.Status =:status order by a.timestamp desc ")
	List<AocpCustomer> findByDateAndStatus(@Param ("s") LocalDate startdate ,@Param ("e") LocalDate enddate, String status);
	
	@Query(value = "SELECT a from AocpCustomer a where a.isActive ='YES'and a.updatedate between :s and :e and a.Status =:status and a.branchid =:branch order by a.timestamp desc ")
	List<AocpCustomer> findByDateStatusAndBranchId(@Param ("s") LocalDate startdate ,@Param ("e") LocalDate enddate, String status, String branch);

	@Query(value = "select * from [aocp_table] where [is_active] ='YES' and [updatedate] between :s and :e and [status] in ('APPROVED','REFERBACK','COMPLETED','REJECTED') order by [timestamp] desc",nativeQuery = true)
	List<AocpCustomer> findByDateWeb(@Param ("s") LocalDate startdate ,@Param ("e") LocalDate enddate);

	@Query(value = "select * from [aocp_table] where [is_active] ='YES' and [updatedate] between :s and :e and [branchid] =:branchId and [status] in ('APPROVED','REFERBACK','COMPLETED','REJECTED') order by [timestamp] desc",nativeQuery = true)
	List<AocpCustomer> findByDateAndBranchIdWeb(@Param ("s") LocalDate startdate ,@Param ("e") LocalDate enddate,String branchId);
	
	@Procedure("usp_Fetch_By_Date")
	List<Object[]> getRetriveReportData(@Param("Start_Date") String startDate, @Param("End_Date") String endDate,@Param("status") String status,@Param("state") String state,@Param("city") String city,@Param("area") String area);
}
