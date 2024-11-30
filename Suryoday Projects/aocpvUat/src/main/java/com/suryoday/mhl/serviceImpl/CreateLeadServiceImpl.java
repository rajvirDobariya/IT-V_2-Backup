package com.suryoday.mhl.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.mhl.pojo.CreateLead;
import com.suryoday.mhl.repository.CreateLeadRepository;
import com.suryoday.mhl.service.CreateLeadService;

@Service
public class CreateLeadServiceImpl implements CreateLeadService {

	@Autowired
	CreateLeadRepository createLeadRepository;

	@Override
	public String save(CreateLead createLead) {

		String applicationNo = null;
		LocalDateTime now = LocalDateTime.now();
		createLead.setCreatedDate(now);
		createLead.setUpdatedDate(now);
		createLead.setStatus("INITIATED");
		applicationNo = "HL-" + 1001;

		Optional<String> optional = createLeadRepository.fetchLastApplicationNo();

		if (optional.isPresent()) {
			String application_No = optional.get();
			String[] split = application_No.split("-");
			String string = split[1];
			long application = Long.parseLong(string);
			application++;
			applicationNo = "HL-" + application;
		}

		createLead.setApplicationNo(applicationNo);
		createLeadRepository.save(createLead);

		return applicationNo;

	}

	@Override
	public List<CreateLead> findByBranchIdAndStatus(String branchId, String status) {
		List<CreateLead> list = createLeadRepository.findByBranchIdAndStatus(branchId, status);
		if (list.isEmpty()) {
			throw new NoSuchElementException("list is empty");
		}
		return list;
	}

	@Override
	public List<CreateLead> findByDate(LocalDateTime startdate, LocalDateTime enddate) {

		List<CreateLead> list = createLeadRepository.findByDate(startdate, enddate);

		if (list.isEmpty()) {
			throw new NoSuchElementException("list is empty");
		}
		return list;
	}

	@Override
	public void updateStatus(String applicationNo, String status) {
		Optional<CreateLead> optional = createLeadRepository.getByApplicationNo(applicationNo);
		if (optional.isPresent()) {
			System.out.println(optional.get());
			createLeadRepository.updateStatus(applicationNo, status);
		} else {
			throw new NoSuchElementException("customer not present");
		}
	}

}
