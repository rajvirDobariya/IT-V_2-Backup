package com.suryoday.aocpv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.suryoday.aocpv.pojo.NotificationUser;

@Component
@Repository
public interface PushNotificationRepo extends JpaRepository<NotificationUser,Long>{

	@Query(value="Select a from NotificationUser a where a.userId =:empId")
	List<NotificationUser> fetchByUserId(String empId);

	@Query(value="Select a from NotificationUser a where a.branchId =:branchId")
	List<NotificationUser> fetchByBranchId(String branchId);
	
}
