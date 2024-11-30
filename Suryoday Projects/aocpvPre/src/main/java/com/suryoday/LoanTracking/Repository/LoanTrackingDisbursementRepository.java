package com.suryoday.LoanTracking.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.suryoday.LoanTracking.Pojo.LoanTrackingDisbursement;


@Repository
@Component
public interface LoanTrackingDisbursementRepository extends JpaRepository<LoanTrackingDisbursement,Long>{

	@Query(value = "SELECT top 1 a.application_id from tbl_loantracking_disbursement a order by a.application_id desc",nativeQuery=true)
	Optional<String> fetchLastApplicationNo();

	@Query("Select a from LoanTrackingDisbursement a where a.applicationId=:applicationNo")
	List<LoanTrackingDisbursement> fetchByApplicationId(long applicationNo);

	@Query(value = "SELECT a from LoanTrackingDisbursement a where a.createdBy =:createdBy and a.startDate between :s and :e order by a.startDate desc ")
	List<LoanTrackingDisbursement> fetchByDate(@Param("s") LocalDateTime startdate,@Param("e") LocalDateTime enddate,String createdBy);

	@Query("Select a from LoanTrackingDisbursement a where a.assignRole IN (:userRole,:userRole1) and a.assignTo=:userId and a.startDate between :s and :e order by a.startDate desc")
	List<LoanTrackingDisbursement> fetchByUserRoleAndUserId(String userRole,String userRole1,String userId,@Param("s") LocalDateTime startdate,
			@Param("e")LocalDateTime enddate);

	@Query("Select a from LoanTrackingDisbursement a where a.assignRole=:userRole")
	List<LoanTrackingDisbursement> fetchByUserRole(String userRole);
	
	@Query("Select a from LoanTrackingDisbursement a where a.panNo=:panNo and a.productType=:productType")
	Optional<LoanTrackingDisbursement> getByPanCard(String panNo,String productType);

	@Query(value = "SELECT a from LoanTrackingDisbursement a where a.startDate between :s and :e order by a.startDate desc ")
	List<LoanTrackingDisbursement> fetchByDateAll(@Param("s") LocalDateTime startdate,@Param("e") LocalDateTime enddate);

	@Query("Select a from LoanTrackingDisbursement a where a.currentStage IN (:userRole,:userRole1,:userRole2) and a.assignTo=:userId and a.startDate between :s and :e order by a.startDate desc")
	List<LoanTrackingDisbursement> fetchMyTasks(String userRole,String userRole1,String userRole2, String userId,@Param("s") LocalDateTime startdate,
			@Param("e")LocalDateTime enddate);

	@Query(value = "SELECT a.state FROM LoanTrackingDisbursement a group by a.state")
	List<String> getAllStates();

	@Query("Select COUNT(*) from LoanTrackingDisbursement a where a.currentStage IN(:currentStage,:currentStage2,:currentStage3) And a.productType=:productType")
	int countPendingRecordsByProduct(String currentStage, String currentStage2, String currentStage3, String productType);

	@Query("Select COUNT(*) from LoanTrackingDisbursement a where a.currentStage IN(:currentStage,:currentStage2,:currentStage3) And a.productType IS Null")
	int countPendingRecordsByProductNull(String currentStage, String currentStage2, String currentStage3);

	@Query(value = "SELECT a from LoanTrackingDisbursement a where a.currentStage IN(:userRole,:userRole2,:userRole3) and a.startDate between :s and :e order by a.startDate desc")
	List<LoanTrackingDisbursement> fetchByDateAndUserRole(@Param("s")LocalDateTime startdate, @Param("e")LocalDateTime enddate,String userRole,String userRole2,String userRole3);

	@Query("Select a from LoanTrackingDisbursement a where a.applicationId=:applicationNo")
	Optional<LoanTrackingDisbursement> getByApplicationId(long applicationNo);

	@Query(value = "SELECT a.productType FROM LoanTrackingDisbursement a group by a.productType")
	List<String> getAllProducts();

	@Query(value = "SELECT Top 10 * from [tbl_loantracking_disbursement] a where a.[start_date] between :s and :e order by a.start_date desc",nativeQuery = true)
	List<LoanTrackingDisbursement> findTopTenByDate(@Param("s")LocalDateTime startdate,@Param("e") LocalDateTime enddate);

	@Query("Select a from LoanTrackingDisbursement a where a.name LIKE :name OR a.applicationId=:applicationId")
	List<LoanTrackingDisbursement> searchByNameOrApplication(String name, long applicationId);

}
