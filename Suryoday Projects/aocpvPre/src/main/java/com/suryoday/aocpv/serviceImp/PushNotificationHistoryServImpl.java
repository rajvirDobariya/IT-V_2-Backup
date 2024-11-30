package com.suryoday.aocpv.serviceImp;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suryoday.aocpv.pojo.NotificatonUserHistory;
import com.suryoday.aocpv.repository.PushNotificationHistoryRepo;
import com.suryoday.aocpv.service.PushNotificationHistoryService;

@Component
public class PushNotificationHistoryServImpl implements PushNotificationHistoryService{

	@Autowired
	PushNotificationHistoryRepo pushnotificationhistoryrepo;
	
	@Override
	public List<NotificatonUserHistory> fetchCount(String branchId, String role) {
		List<NotificatonUserHistory> list=pushnotificationhistoryrepo.fetchTop10(branchId,role);
		if(list.isEmpty())
		{
			throw new NoSuchElementException("List is empty");
		}
		return list;
	}

	@Override
	public String save(NotificatonUserHistory notificationUserHistory) {
		pushnotificationhistoryrepo.save(notificationUserHistory);
		return "Data Saved Successfully";
	}

	@Override
	public void update(int read, String empId, LocalDateTime updatedAt) {
		pushnotificationhistoryrepo.update(read,empId,updatedAt);
		
	}

	@Override
	public List<NotificatonUserHistory> fetchTop10(String branchId, String role, int read) {
		List<NotificatonUserHistory> list=pushnotificationhistoryrepo.fetchTop10Record(branchId,role,read);
		if(list.isEmpty())
		{
			throw new NoSuchElementException("List is empty");
		}
		return list;
	}

}
