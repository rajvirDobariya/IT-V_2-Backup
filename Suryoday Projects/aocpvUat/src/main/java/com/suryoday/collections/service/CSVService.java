package com.suryoday.collections.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.suryoday.collections.pojo.CSVModel;
import com.suryoday.collections.pojo.CSVModelResponse;

@Service
public interface CSVService {

	public void saveAllCSV(MultipartFile file);

	List<CSVModelResponse> findByStatusAll(String status, String branchId);

	List<CSVModel> findByAllLoan(String customerNO);

	List<CSVModel> findByAccountNo(String accountNo);

	public List<CSVModelResponse> findAllCustomerByDate(String branchId, LocalDate startdate, LocalDate enddate,
			String status);
}
