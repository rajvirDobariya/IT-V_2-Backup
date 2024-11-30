package com.suryoday.collections.pojo;

import java.util.List;

public class WebReceiptResponse {
	
	private List<ReceiptPojo> receiptPojo;
	
	private List<ReceiptImageResponse> receiptImage;

	public List<ReceiptPojo> getReceiptPojo() {
		return receiptPojo;
	}

	public void setReceiptPojo(List<ReceiptPojo> receiptPojo) {
		this.receiptPojo = receiptPojo;
	}

	public List<ReceiptImageResponse> getReceiptImage() {
		return receiptImage;
	}

	public void setReceiptImage(List<ReceiptImageResponse> receiptImage) {
		this.receiptImage = receiptImage;
	}

	

}
