package com.suryoday.collections.pojo;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "UPDATE_USERLOG")
public class UserlogUpdate {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String loanNo;
	private String userId;
	private LocalDate createdDate;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLoanNo() {
		return loanNo;
	}
	public void setLoanNo(String loanNo) {
		this.loanNo = loanNo;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public LocalDate getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}
	
	@Override
	public String toString() {
		return "UserlogUpdate [id=" + id + ", loanNo=" + loanNo + ", userId=" + userId
				+ ", createdDate=" + createdDate + "]";
	}
}
