package com.suryoday.goNogo.serviceImpl;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.suryoday.goNogo.repository.GoNoGoImagesRepository;
import com.suryoday.goNogo.service.GoNoGoImagesService;

@Service
public class GoNoGoImagesServiceImpl implements GoNoGoImagesService {

	@Autowired
	GoNoGoImagesRepository repository;

	@Override
	public void saveImage(MultipartFile[] files, String applicationNo, JSONArray document, String string) {
		// TODO Auto-generated method stub

	}

}
