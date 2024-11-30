package com.suryoday.collections.serviceImpl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.collections.others.CSVHelper;
import com.suryoday.collections.pojo.CSVModel;
import com.suryoday.collections.pojo.CSVModelResponse;
import com.suryoday.collections.repository.CSVRepository;
import com.suryoday.collections.service.CSVService;

@Service
public class CSVServiceImpl implements CSVService {

	@Autowired
	CSVRepository csvRepository;

	@Override
	public void saveAllCSV(MultipartFile file) {

		try {
			List<CSVModel> tutorials = CSVHelper.csvToTutorials(file.getInputStream());
			csvRepository.saveAll(tutorials);
		} catch (IOException e) {
			throw new RuntimeException("fail to store csv data: " + e.getMessage());
		}
	}

	@Override
	public List<CSVModelResponse> findByStatusAll(String status, String branchId) {

		List<CSVModelResponse> list = csvRepository.findByStatus(status, branchId);
		if (list.isEmpty()) {
			throw new NoSuchElementException("list is empty");
		}
		return list;
	}

	@Override
	public List<CSVModel> findByAllLoan(String customerNO) {

		List<CSVModel> list = csvRepository.findByCustomerNO(customerNO);
		if (list.isEmpty()) {
			throw new NoSuchElementException("list is empty");
		}
		return list;
	}

	@Override
	public List<CSVModel> findByAccountNo(String accountNo) {

		List<CSVModel> list = csvRepository.findByAccountNO(accountNo);
		if (list.size() == 0) {
			String customerID = accountNo;
			List<CSVModel> list1 = csvRepository.findByCustomerNO(customerID);
			if (list1.size() == 0) {
				String mobileNO = accountNo;
				List<CSVModel> list2 = csvRepository.findByMobileNo(mobileNO);
				if (list2.size() == 0) {
					throw new NoSuchElementException("NO Record found");
				} else {
					return list2;
				}
			} else {
				return list1;
			}
		} else {
			return list;
		}
	}

	@Override
	public List<CSVModelResponse> findAllCustomerByDate(String branchId, LocalDate startdate, LocalDate enddate,
			String status) {

		List<CSVModel> list = csvRepository.findByDateCustomer(status, branchId, startdate, enddate);
		if (list.isEmpty()) {
			throw new NoSuchElementException("list is empty");
		}
		List<CSVModelResponse> list1 = new ArrayList<>();
		for (CSVModel csv : list) {
			CSVModelResponse csvModelResponse = new CSVModelResponse(csv.getCustomerName(), csv.getCity(),
					csv.getCurrentPOS(), csv.getAggrementID(), csv.getCustomerID(), csv.getChasisnum(),
					csv.getDisbursalDate());
			list1.add(csvModelResponse);
		}
		return list1;
	}
}
