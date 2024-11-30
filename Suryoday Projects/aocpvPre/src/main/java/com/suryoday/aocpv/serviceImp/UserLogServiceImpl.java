package com.suryoday.aocpv.serviceImp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.aocpv.pojo.UserLog;
import com.suryoday.aocpv.repository.UserLogRepository;
import com.suryoday.aocpv.service.UserLogService;

@Service
public class UserLogServiceImpl implements UserLogService{

	@Autowired
	UserLogRepository userLogRepository;
	
	
	@Override
	public void save(String x_User_ID, String applicationNo, String status, String pendingWith, int versionCode) {
		UserLog userLog=new UserLog();
		Long applicationno=Long.parseLong(applicationNo);
		userLog.setApplicationNo(applicationno);
		userLog.setStages(status);
		userLog.setUserId(x_User_ID);
		LocalDateTime now = LocalDateTime.now();
		//System.out.println(now);
		int id=1;
		userLog.setCreatedDate(now);
		userLog.setTimestamp(now);
		userLog.setUserRole(pendingWith);
		userLog.setVersionCode(versionCode);
		Optional<Integer>optional1=userLogRepository.fetchLastId();
		if(optional1.isPresent()) {
			id=	optional1.get();
			id++;
		}
//	Optional<UserLog> optional=userLogRepository.findByApplicationNoAndStages(applicationno,status);
//		
//		if(optional.isPresent()) {
//			UserLog userLog2 = optional.get();
//			userLog.setId(userLog2.getId());
//			userLogRepository.save(userLog);
//		}
//		else {
			userLog.setId(id);
			userLogRepository.save(userLog);
//		}
		
	}


	@Override
	public List<UserLog> getByApplicationNo(long applicationNoInLong) {
		
			List<UserLog>  list=userLogRepository.getByApplicationNo(applicationNoInLong);
		
			if(list.isEmpty()) {
				throw new NoSuchElementException("list is empty");
			}
			
		return list;
	}


	@Override
	public List<UserLog> getByApplicationNoAndStatus(String status, long applicationNo) {
	
		List<UserLog> list=userLogRepository.findByApplicationNoAndStages(applicationNo,status);
		
		if(list.isEmpty()) {
			throw new NoSuchElementException("list is empty");
		}
		
	return list;
	}

}
