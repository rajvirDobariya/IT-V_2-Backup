package com.suryoday.aocpv.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.suryoday.aocpv.pojo.NotificationUser;

@Component
public interface PushNotificationUserService {

	String save(NotificationUser notificationUser);

	NotificationUser fetchToken(String tokenId);

	void update(String tokenId, String userId, LocalDateTime updatedAt);

	List<NotificationUser> fetchTokenByUserId(String empId);

	List<NotificationUser> fetchTokenByBranchId(String branchId);

	List<NotificationUser> fetchTokenByUserIdAndBranchId(String empId, String branchId);

}
