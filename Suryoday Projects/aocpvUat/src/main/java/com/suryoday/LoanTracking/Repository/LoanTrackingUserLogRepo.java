package com.suryoday.LoanTracking.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import com.suryoday.LoanTracking.Pojo.LoanTrackingUserLog;

@Component
public interface LoanTrackingUserLogRepo extends JpaRepository<LoanTrackingUserLog, Long> {

	@Query(value = "SELECT top 1 a.id from tbl_loantracking_userlog a order by a.id desc", nativeQuery = true)
	Optional<Long> fetchLastId();

	@Query("Select a from LoanTrackingUserLog a where a.applicationNo=:application order by a.id desc")
	List<LoanTrackingUserLog> fetchByApplication(long application);

	@Query("Select a from LoanTrackingUserLog a where a.applicationNo=:appNo and a.fromStatus=:role1 and a.toStatus=:role2")
	List<LoanTrackingUserLog> fetchByDiscrepancy(long appNo, String role1, String role2);

	@Query("Select a from LoanTrackingUserLog a where a.applicationNo=:applicationNo and a.fromStatus=:fromStatus and a.toStatus=:toStatus")
	Optional<LoanTrackingUserLog> getByApplication(long applicationNo, String fromStatus, String toStatus);

}
