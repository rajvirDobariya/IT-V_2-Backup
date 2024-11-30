package com.suryoday.aocpv.pojo;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PreApprovedListVikasLoan {
	@Id
	private Long customerID;
	private long mobilePhone;
	private String referenceNo;
	private String memberName;
	private String state;
	private long branchId;
	private double amount;
	private String status;
	private LocalDate createDate;
	private String productCode;
	private LocalDate updatedate;
	private String Branch_Name;
	private String isActive;
	private String CITY;
	private String AccountNo;
	private String obligation;
	private double ELIGIBLE_LOAN_VALUE_2YEARS;
	private String SSFB_DPD_BUCKETS;
	private int MFI_VINTAGE_SSFB;
	private double sumInstallmentAmountopen;
	private int MFI_VINTAGE;
	private int TOTAL_NUM_MFI_ACCTS;
	private int numOpenAccount;
	private int NUM_CLOSED_ACCTS;
	private String LATEST_ACCOUNTSTATUS_MFI;
	private double TOTAL_MFI_DISBURSEMENT;
	private double sumOutstandingBalance;
	private int MFI_VINTAGE_MARKET;
	private double MAX_MFI_EMI;;
	private double MAX_LOAN_AMOUNT_MFI;
	private int Current_CycleNo;
	private int BranchCode;
	private String CentreName;
	private String DISTRICT_NAME;

	public int getBranchCode() {
		return BranchCode;
	}

	public void setBranchCode(int branchCode) {
		BranchCode = branchCode;
	}

	public String getCentreName() {
		return CentreName;
	}

	public void setCentreName(String centreName) {
		CentreName = centreName;
	}

	public String getDISTRICT_NAME() {
		return DISTRICT_NAME;
	}

	public void setDISTRICT_NAME(String dISTRICT_NAME) {
		DISTRICT_NAME = dISTRICT_NAME;
	}

	public double getELIGIBLE_LOAN_VALUE_2YEARS() {
		return ELIGIBLE_LOAN_VALUE_2YEARS;
	}

	public void setELIGIBLE_LOAN_VALUE_2YEARS(double eLIGIBLE_LOAN_VALUE_2YEARS) {
		ELIGIBLE_LOAN_VALUE_2YEARS = eLIGIBLE_LOAN_VALUE_2YEARS;
	}

	public String getSSFB_DPD_BUCKETS() {
		return SSFB_DPD_BUCKETS;
	}

	public void setSSFB_DPD_BUCKETS(String sSFB_DPD_BUCKETS) {
		SSFB_DPD_BUCKETS = sSFB_DPD_BUCKETS;
	}

	public int getMFI_VINTAGE_SSFB() {
		return MFI_VINTAGE_SSFB;
	}

	public void setMFI_VINTAGE_SSFB(int mFI_VINTAGE_SSFB) {
		MFI_VINTAGE_SSFB = mFI_VINTAGE_SSFB;
	}

	public double getSumInstallmentAmountopen() {
		return sumInstallmentAmountopen;
	}

	public void setSumInstallmentAmountopen(double sumInstallmentAmountopen) {
		this.sumInstallmentAmountopen = sumInstallmentAmountopen;
	}

	public int getMFI_VINTAGE() {
		return MFI_VINTAGE;
	}

	public void setMFI_VINTAGE(int mFI_VINTAGE) {
		MFI_VINTAGE = mFI_VINTAGE;
	}

	public int getTOTAL_NUM_MFI_ACCTS() {
		return TOTAL_NUM_MFI_ACCTS;
	}

	public void setTOTAL_NUM_MFI_ACCTS(int tOTAL_NUM_MFI_ACCTS) {
		TOTAL_NUM_MFI_ACCTS = tOTAL_NUM_MFI_ACCTS;
	}

	public int getNumOpenAccount() {
		return numOpenAccount;
	}

	public void setNumOpenAccount(int numOpenAccount) {
		this.numOpenAccount = numOpenAccount;
	}

	public int getNUM_CLOSED_ACCTS() {
		return NUM_CLOSED_ACCTS;
	}

	public void setNUM_CLOSED_ACCTS(int nUM_CLOSED_ACCTS) {
		NUM_CLOSED_ACCTS = nUM_CLOSED_ACCTS;
	}

	public String getLATEST_ACCOUNTSTATUS_MFI() {
		return LATEST_ACCOUNTSTATUS_MFI;
	}

	public void setLATEST_ACCOUNTSTATUS_MFI(String lATEST_ACCOUNTSTATUS_MFI) {
		LATEST_ACCOUNTSTATUS_MFI = lATEST_ACCOUNTSTATUS_MFI;
	}

	public double getTOTAL_MFI_DISBURSEMENT() {
		return TOTAL_MFI_DISBURSEMENT;
	}

	public void setTOTAL_MFI_DISBURSEMENT(double tOTAL_MFI_DISBURSEMENT) {
		TOTAL_MFI_DISBURSEMENT = tOTAL_MFI_DISBURSEMENT;
	}

	public double getSumOutstandingBalance() {
		return sumOutstandingBalance;
	}

	public void setSumOutstandingBalance(double sumOutstandingBalance) {
		this.sumOutstandingBalance = sumOutstandingBalance;
	}

	public int getMFI_VINTAGE_MARKET() {
		return MFI_VINTAGE_MARKET;
	}

	public void setMFI_VINTAGE_MARKET(int mFI_VINTAGE_MARKET) {
		MFI_VINTAGE_MARKET = mFI_VINTAGE_MARKET;
	}

	public double getMAX_MFI_EMI() {
		return MAX_MFI_EMI;
	}

	public void setMAX_MFI_EMI(double mAX_MFI_EMI) {
		MAX_MFI_EMI = mAX_MFI_EMI;
	}

	public double getMAX_LOAN_AMOUNT_MFI() {
		return MAX_LOAN_AMOUNT_MFI;
	}

	public void setMAX_LOAN_AMOUNT_MFI(double mAX_LOAN_AMOUNT_MFI) {
		MAX_LOAN_AMOUNT_MFI = mAX_LOAN_AMOUNT_MFI;
	}

	public int getCurrent_CycleNo() {
		return Current_CycleNo;
	}

	public void setCurrent_CycleNo(int current_CycleNo) {
		Current_CycleNo = current_CycleNo;
	}

	public String getObligation() {
		return obligation;
	}

	public void setObligation(String obligation) {
		this.obligation = obligation;
	}

	public String getAccountNo() {
		return AccountNo;
	}

	public void setAccountNo(String accountNo) {
		AccountNo = accountNo;
	}

	public String getCITY() {
		return CITY;
	}

	public void setCITY(String cITY) {
		CITY = cITY;
	}

	public Long getCustomerID() {
		return customerID;
	}

	public void setCustomerID(Long customerID) {
		this.customerID = customerID;
	}

	public long getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(long mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
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

	public long getBranchId() {
		return branchId;
	}

	public void setBranchId(long branchId) {
		this.branchId = branchId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDate getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public LocalDate getUpdatedate() {
		return updatedate;
	}

	public void setUpdatedate(LocalDate updatedate) {
		this.updatedate = updatedate;
	}

	public String getBranch_Name() {
		return Branch_Name;
	}

	public void setBranch_Name(String branch_Name) {
		Branch_Name = branch_Name;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

}
