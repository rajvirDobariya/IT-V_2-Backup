package com.suryoday.aocpv.serviceImp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suryoday.aocpv.pojo.NotificationUser;
import com.suryoday.aocpv.repository.PushNotificationUserRepo;
import com.suryoday.aocpv.service.PushNotificationUserService;
@Component
public class PushNotificationUserServImpl implements PushNotificationUserService{
	@Autowired
	PushNotificationUserRepo pushnotificationrepo;
	@Override
	public String save(NotificationUser notificationUser) {
		pushnotificationrepo.save(notificationUser);
		return "Data Saved";
	}
	@Override
	public NotificationUser fetchToken(String tokenId) {
		Optional<NotificationUser> optional=pushnotificationrepo.fetchToken(tokenId);
		NotificationUser notificationUser = optional.get();
		return notificationUser;
		
		
	}
	@Override
	public void update(String tokenId, String userId, LocalDateTime updatedAt) {
		Optional<NotificationUser> optional=pushnotificationrepo.getByUserId(userId);
		if(optional.isPresent())
		{
			pushnotificationrepo.updateRecord(userId,tokenId,updatedAt);
		}
		else {
			throw new NoSuchElementException("NO Record found");
			}
		
	}
	@Override
	public List<NotificationUser> fetchTokenByUserId(String empId) {
		List<NotificationUser> list=pushnotificationrepo.fetchTokenByUserId(empId);
		if(list.isEmpty())
		{
			throw new NoSuchElementException("NO Record found");
		}
		return list;
	}
	@Override
	public List<NotificationUser> fetchTokenByBranchId(String branchId) {
		List<NotificationUser> list=pushnotificationrepo.fetchTokenByBranchId(branchId);
		if(list.isEmpty())
		{
			throw new NoSuchElementException("NO Record found");
		}
		return list;
	}
	@Override
	public List<NotificationUser> fetchTokenByUserIdAndBranchId(String empId, String branchId) {
		List<NotificationUser> list=pushnotificationrepo.fetchTokenByUserIdAndBranchId(empId,branchId);
		if(list.isEmpty())
		{
			throw new NoSuchElementException("NO Record found");
		}
		return list;
	}

}
