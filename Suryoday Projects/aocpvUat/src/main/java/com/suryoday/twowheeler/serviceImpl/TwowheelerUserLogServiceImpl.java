package com.suryoday.twowheeler.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.twowheeler.pojo.TwowheelerUserLog;
import com.suryoday.twowheeler.repository.TwowheelerUserLogRepository;
import com.suryoday.twowheeler.service.TwowheelerUserLogService;

@Service
public class TwowheelerUserLogServiceImpl implements TwowheelerUserLogService {

	@Autowired
	TwowheelerUserLogRepository userLogRepository;

	@Override
	public void saveUserLog(String applicationNo, String stage, String x_User_ID, String userRole) {

		TwowheelerUserLog twowheelerUserLog = new TwowheelerUserLog(applicationNo, stage, x_User_ID,
				LocalDateTime.now(), userRole);
		userLogRepository.save(twowheelerUserLog);
	}

	@Override
	public List<TwowheelerUserLog> getByApplicationNo(String applicationNo) {
		List<TwowheelerUserLog> list = userLogRepository.getByApplicationNo(applicationNo);
		if (list.size() == 0) {
			throw new NoSuchElementException("No userLog Found");
		}
		return list;
	}

}
