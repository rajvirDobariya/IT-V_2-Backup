package com.suryoday.collections.serviceImpl;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.collections.pojo.UpdateTrailBean;
import com.suryoday.collections.repository.UpdateTrailRepository;
import com.suryoday.collections.repository.UserLogUpdateRepository;
import com.suryoday.collections.service.UpdateTrailService;

@Service
public class UpdateTrailServiceImpl implements UpdateTrailService{

	@Autowired
	UpdateTrailRepository updateTrailRepository;
	
	@Autowired
	UserLogUpdateRepository userLogUpdateRepository;
	
	@Override
	public String updateTrailSave(UpdateTrailBean pojo) {
		
		Optional<UpdateTrailBean> updateOptional = updateTrailRepository.getLoanNumber(pojo.getAccountNo());
		if(updateOptional.isPresent()) {
			UpdateTrailBean updateTrailBean = updateOptional.get();
			LocalDate now = LocalDate.now();
			updateTrailBean.setUpdatedDate(now);
			updateTrailRepository.save(updateTrailBean);
		}
		else {
			updateTrailRepository.save(pojo);
		}	
		return "Updated Trail Saved Successfully";
	}

}
