package com.suryoday.aocpv.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.suryoday.aocpv.pojo.NotificatonUserHistory;

@Component
@Repository
public interface PushNotificationHistoryRepo extends JpaRepository<NotificatonUserHistory, String>{

	

	@Query(value = "SELECT top 1 a.id from tbl_notification_userhistory a order by a.id desc",nativeQuery = true)
	Optional<Long> fetchLastId();

	@Query(value= "SELECT TOP 10 * from tbl_notification_userhistory a where a.branch_id=:branchId and a.role=:role order by a.id desc",nativeQuery = true)
	List<NotificatonUserHistory> fetchTop10(String branchId, String role);

	@Modifying
	@Transactional
	@Query(value = "update NotificatonUserHistory set readHistory=:read,updatedAt=:updatedAt where empId=:empId")
	void update(int read, String empId, LocalDateTime updatedAt);
	
	@Query(value= "SELECT TOP 10 * from tbl_notification_userhistory a where a.branch_id=:branchId and a.role=:role and a.read_history=:read order by a.id desc",nativeQuery = true)
	List<NotificatonUserHistory> fetchTop10Record(String branchId, String role, int read);

}
