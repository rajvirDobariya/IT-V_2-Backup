package com.suryoday.aocpv.serviceImp;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.aocpv.pojo.AocpvPerposecategory;
import com.suryoday.aocpv.repository.AocpvPerposecategoryRepository;
import com.suryoday.aocpv.service.AocpvPerposecategoryService;

@Service
public class AocpvPerposecategoryServiceImpl implements AocpvPerposecategoryService {

	@Autowired
	AocpvPerposecategoryRepository aocpvPerposecategoryRepository;

	private static Logger logger = LoggerFactory.getLogger(AocpvPerposecategoryServiceImpl.class);

	@Override
	public List<AocpvPerposecategory> fetchByCategoryId(String categoryId) {
		List<AocpvPerposecategory> findByCategoryId = aocpvPerposecategoryRepository.findByCategoryId(categoryId);
		if (findByCategoryId.isEmpty()) {
			throw new NoSuchElementException("list is empty");
		}
		logger.debug("Response from the DB" + findByCategoryId);
		return findByCategoryId;

	}

}
