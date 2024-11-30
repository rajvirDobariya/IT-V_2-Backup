package com.suryoday.hastakshar.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.hastakshar.controller.HastaksharMasterController;
import com.suryoday.hastakshar.pojo.HastMaster;
import com.suryoday.hastakshar.repository.MasterRepo;
import com.suryoday.hastakshar.service.HastaksharMasterService;

@Service
public class HastaksharMasterServiceImpl implements HastaksharMasterService {

	@Autowired
	private MasterRepo masterRepo;

	Logger logger = LoggerFactory.getLogger(HastaksharMasterController.class);

	@Override
	public List<HastMaster> addHastMaster(List<HastMaster> hastMasters) {
		List<HastMaster> newHastMasters = new ArrayList<>();

		hastMasters.forEach(hastMaster -> {
			// Check if an entry with the same transactionType already exists
			HastMaster existingMaster = masterRepo.findByTransactionTypes(hastMaster.getTransactionTypes());
			if (existingMaster == null) {
				hastMaster.setId(UUID.randomUUID().toString());
				newHastMasters.add(hastMaster);
			}
		});

		// Save only the new (non-duplicate) entries to the database
		return masterRepo.saveAll(newHastMasters);
	}
}
