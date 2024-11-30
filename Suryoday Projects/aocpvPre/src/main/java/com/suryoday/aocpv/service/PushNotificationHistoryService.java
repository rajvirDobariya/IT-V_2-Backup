package com.suryoday.aocpv.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.suryoday.aocpv.pojo.NotificatonUserHistory;

@Component
public interface PushNotificationHistoryService {


	String save(NotificatonUserHistory notificationUserHistory);

	List<NotificatonUserHistory> fetchCount(String branchId, String role);

	void update(int read, String empId, LocalDateTime updatedAt);

	List<NotificatonUserHistory> fetchTop10(String branchId, String role, int read);

}
