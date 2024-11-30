package com.suryoday.collections.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.suryoday.collections.pojo.ReceiptImage;

public interface ReceiptImageService {

	public String saveImageReceipt(ReceiptImage images, MultipartFile files);

	public List<ReceiptImage> findAllDetails2(String receiptNo);

}
