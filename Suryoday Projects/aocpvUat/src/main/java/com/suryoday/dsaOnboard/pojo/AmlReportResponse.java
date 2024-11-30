package com.suryoday.dsaOnboard.pojo;

public class AmlReportResponse {
	private String status;
	private String finalScore;
	private String transactionCode;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFinalScore() {
		return finalScore;
	}

	public void setFinalScore(String finalScore) {
		this.finalScore = finalScore;
	}

	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

}
