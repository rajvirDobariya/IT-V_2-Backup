package com.suryoday.collections.serviceImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.collections.pojo.ReceiptPojo;
import com.suryoday.collections.pojo.ReceiptResponse;
import com.suryoday.collections.repository.RecieptRepository;
import com.suryoday.collections.service.ReceiptService;

@Service
public class ReceiptServiceImpl implements ReceiptService {

	@Autowired
	RecieptRepository recieptRepository;

	@Override
	public String saveReciept(ReceiptPojo pojo) {

		Optional<ReceiptPojo> optional = recieptRepository.getByCustomer(pojo.getCustomerID());
		if (optional.isPresent()) {
			ReceiptPojo rcptpojo = optional.get();
			pojo.setId(rcptpojo.getId());
		}
		recieptRepository.save(pojo);

		return "Saved Record Successfully";
	}

	@Override
	public List<ReceiptResponse> findTopReceiptList(String branchId) {

		List<ReceiptPojo> list = recieptRepository.findTopReceipt(branchId);
		if (list.isEmpty()) {
			throw new NoSuchElementException("list is empty");
		}
		List<ReceiptResponse> list1 = new ArrayList<>();
		for (ReceiptPojo receiptpojo : list) {
			LocalDate currentDate = receiptpojo.getCurrentDate();
			DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			String currentDate1 = currentDate.format(formatter1);
			ReceiptResponse response = new ReceiptResponse(currentDate1, receiptpojo.getReceiptNo(),
					receiptpojo.getTotalCount(), receiptpojo.getAccountNo(), receiptpojo.getCustName(),
					receiptpojo.getPaymanetStatus());
			list1.add(response);
		}
		return list1;
	}

	@Override
	public List<ReceiptPojo> findAllDetails(String branchId) {

		List<ReceiptPojo> list = recieptRepository.findDetailsReceipt(branchId);
		if (list.isEmpty()) {
			throw new NoSuchElementException("list is empty");
		}
		return list;
	}

	@Override
	public String getReceiptNumber(String branchId) {

		String receiptNumber = recieptRepository.getByReceiptNumber(branchId);
		return receiptNumber;
	}

	@Override
	public List<ReceiptResponse> findAllDetailsByDate(String branchId, LocalDate startDate, LocalDate endDate) {

		List<ReceiptPojo> list = recieptRepository.findDetailsByDate(branchId, startDate, endDate);
		if (list.isEmpty()) {
			throw new NoSuchElementException("list is empty");
		}
		List<ReceiptResponse> list1 = new ArrayList<>();
		for (ReceiptPojo receiptpojo : list) {
			LocalDate currentDate = receiptpojo.getCurrentDate();
			DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			String currentDate1 = currentDate.format(formatter1);
			ReceiptResponse response = new ReceiptResponse(currentDate1, receiptpojo.getReceiptNo(),
					receiptpojo.getTotalCount(), receiptpojo.getAccountNo(), receiptpojo.getCustName(),
					receiptpojo.getPaymanetStatus());
			list1.add(response);
		}
		return list1;
	}

	@Override
	public List<ReceiptPojo> getReceiptPDFDetails(String receiptNo) {

		List<ReceiptPojo> list = recieptRepository.findPDFDetailsReceipt(receiptNo);
		if (list.isEmpty()) {
			throw new NoSuchElementException("list is empty");
		}
		return list;
	}

}
