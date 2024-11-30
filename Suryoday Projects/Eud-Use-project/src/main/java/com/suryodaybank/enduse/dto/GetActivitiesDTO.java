package com.suryodaybank.enduse.dto;

import java.time.LocalDate;

public class GetActivitiesDTO {

	private String fetchType;
	private Long accountNo;
	private LocalDate startDate;
	private LocalDate endDate;
	
	public String getFetchType() {
		return fetchType;
	}
	public void setFetchType(String fetchType) {
		this.fetchType = fetchType;
	}
	
	public Long getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(Long accountNo) {
		this.accountNo = accountNo;
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	
}
