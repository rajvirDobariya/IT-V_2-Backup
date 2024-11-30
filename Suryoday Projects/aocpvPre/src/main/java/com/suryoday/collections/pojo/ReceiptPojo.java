package com.suryoday.collections.pojo;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "IISSUE_RECEIPT")
public class ReceiptPojo {
	 @Id
	 @GeneratedValue
	 @Column(name = "id")
	private int id;
	 
	private String customerID;
	private String accountNo;
	private String custName;
	private String ptpAmount;
	private String emiAmount;
	private String emiOverdue;
	private String charges;
	private String totalAmount;
	private String modeOfPayment; 
	private String receiptNo;
	private String totalCount;
	private String panNO;
	private String mobileNo;
	private String remarks;
	private String upiID;
	private LocalDate currentDate;
	private String branchId;
	private String paymanetStatus;
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	public String getCustomerID() {
		return customerID;
	}
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getPtpAmount() {
		return ptpAmount;
	}
	public void setPtpAmount(String ptpAmount) {
		this.ptpAmount = ptpAmount;
	}
	public String getEmiAmount() {
		return emiAmount;
	}
	public void setEmiAmount(String emiAmount) {
		this.emiAmount = emiAmount;
	}
	public String getEmiOverdue() {
		return emiOverdue;
	}
	public void setEmiOverdue(String emiOverdue) {
		this.emiOverdue = emiOverdue;
	}
	public String getCharges() {
		return charges;
	}
	public void setCharges(String charges) {
		this.charges = charges;
	}
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getModeOfPayment() {
		return modeOfPayment;
	}
	public void setModeOfPayment(String modeOfPayment) {
		this.modeOfPayment = modeOfPayment;
	}
	
	public String getReceiptNo() {
		return receiptNo;
	}
	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}
	public String getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}
	public String getPanNO() {
		return panNO;
	}
	public void setPanNO(String panNO) {
		this.panNO = panNO;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
		
	public String getUpiID() {
		return upiID;
	}
	public void setUpiID(String upiID) {
		this.upiID = upiID;
	}
	
	public LocalDate getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(LocalDate currentDate) {
		this.currentDate = currentDate;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getPaymanetStatus() {
		return paymanetStatus;
	}

	public void setPaymanetStatus(String paymanetStatus) {
		this.paymanetStatus = paymanetStatus;
	}

	@Override
	public String toString() {
		return "ReceiptPojo [id=" + id + ",customerID=" + customerID + ", accountNo=" + accountNo + ", custName=" + custName
				+ ", ptpAmount=" + ptpAmount + ", emiAmount=" + emiAmount + ", emiOverdue=" + emiOverdue + ", charges="
				+ charges + ", totalAmount=" + totalAmount + ", modeOfPayment=" + modeOfPayment + ", receiptNo="
				+ receiptNo + ", totalCount=" + totalCount + ", panNO=" + panNO + ", mobileNo=" + mobileNo
				+ ", remarks=" + remarks + ", upiID=" + upiID + ", currentDate=" + currentDate + ", branchId=" +branchId+ ", paymanetStatus=" +paymanetStatus+ "]";
	}		
}
