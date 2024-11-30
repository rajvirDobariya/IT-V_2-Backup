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

import com.suryoday.aocpv.pojo.NotificationUser;

@Component
@Repository
public interface PushNotificationUserRepo extends JpaRepository<NotificationUser,Long>{

	@Query(value = "SELECT top 1 a.pid from tbl_notification_usertable a order by a.pid desc",nativeQuery = true)
	Optional<Long> fetchLastId();

	@Query(value="Select a from NotificationUser a where a.tokenId =:tokenId")
	Optional<NotificationUser> fetchToken(String tokenId);

	@Query(value="Select a from NotificationUser a where a.userId =:userId")
	Optional<NotificationUser> getByUserId(String userId);

	@Modifying
	@Transactional
	@Query(value="update NotificationUser set tokenId=:tokenId,updatedAt=:updatedAt where userId=:userId")
	void updateRecord(String userId, String tokenId, LocalDateTime updatedAt);

	@Query(value="Select a from NotificationUser a where a.branchId =:branchId")
	List<NotificationUser> fetchByBranchId(String branchId);

	@Query(value="Select a from NotificationUser a where a.userId =:empId")
	List<NotificationUser> fetchTokenByUserId(String empId);

	@Query(value="Select a from NotificationUser a where a.branchId =:branchId")
	List<NotificationUser> fetchTokenByBranchId(String branchId);

	@Query(value="Select a from NotificationUser a where a.userId =:empId And a.branchId =:branchId")
	List<NotificationUser> fetchTokenByUserIdAndBranchId(String empId, String branchId);

}
