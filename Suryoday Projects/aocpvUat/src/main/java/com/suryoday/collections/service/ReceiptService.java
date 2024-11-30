package com.suryoday.collections.service;

import java.time.LocalDate;
import java.util.List;

import com.suryoday.collections.pojo.ReceiptPojo;
import com.suryoday.collections.pojo.ReceiptResponse;

public interface ReceiptService {

	String saveReciept(ReceiptPojo pojo);

	List<ReceiptResponse> findTopReceiptList(String branchId);

	List<ReceiptPojo> findAllDetails(String branchId);

	String getReceiptNumber(String branchId);

	List<ReceiptResponse> findAllDetailsByDate(String branchId, LocalDate startDate, LocalDate endDate);

	List<ReceiptPojo> getReceiptPDFDetails(String receiptNo);

}
