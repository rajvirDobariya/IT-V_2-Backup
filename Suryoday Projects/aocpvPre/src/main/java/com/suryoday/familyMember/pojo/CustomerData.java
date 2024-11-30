package com.suryoday.familyMember.pojo;


import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "customer_data")
public class CustomerData {
	
	@Id
	private String customerId;
	private String referenceNo;
	private String mobilePhone;
	private String memberName;
	private String state;
	private String branchId;
	private String status;
	private LocalDateTime createDate;
	private LocalDateTime updataDate;
	
	
	public LocalDateTime getUpdataDate() {
		return updataDate;
	}
	public void setUpdataDate(LocalDateTime updataDate) {
		this.updataDate = updataDate;
	}
	public String getReferenceNo() {
		return referenceNo;
	}
	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerID(String customerId) {
		this.customerId = customerId;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public LocalDateTime getCreateDate() {
		return createDate;
	}
	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}
	@Override
	public String toString() {
		return "CustomerData [customerId=" + customerId + ", referenceNo=" + referenceNo + ", mobilePhone="
				+ mobilePhone + ", memberName=" + memberName + ", state=" + state + ", branchId=" + branchId
				+ ", status=" + status + ", createDate=" + createDate + ", updataDate=" + updataDate + "]";
	}
	
	
	
	
	
}
