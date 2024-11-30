package com.suryoday.collections.service;

import org.springframework.stereotype.Service;

import com.suryoday.collections.pojo.UpdateTrailBean;

@Service
public interface UpdateTrailService {

	String updateTrailSave(UpdateTrailBean pojo);

}
