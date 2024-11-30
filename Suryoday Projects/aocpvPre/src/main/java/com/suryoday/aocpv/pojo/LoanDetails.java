package com.suryoday.aocpv.pojo;


import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Star_Loan_Input_field")
public class LoanDetails {
			@Id
			@Column(length = 40)
			private String referenceNo;
			@Column(length = 50)
			private String memberName;
			@Column(length = 20)
			private String state;
			@Column(length = 24)
			private long mobilePhone;
			private int numOpenAccount;
//			private int numOpenAccountOwn;
//			private int numOpenAccountNonOwn;
			private double sumOutstandingBalance;
			private double sumOutstandingBalanceOwn;
//			private double sumOutstandingBalanceNonOwn;
			private double sumInstallmentAmountopen;
//			private double sumInstallmentAmountopenOwn;
//			private double sumInstallmentAmountOpenNonOwn;
//			@Column(length = 24)
//			private int rightOfChargeOfTag;
//			@Column(length = 24)
//			private int restructureTag;
//			@Column(length = 24)
//			private Date earliestOpenDate;
//			@Column(length = 24)
//			private Date latestOpenDate;
//			@Column(length = 24)
//			private Date earliestCloseDate;
			@Column(length = 24)
			private Date latestCloseDate;
//			@Column(length = 24)
//			private Date earliestOpenDateOwn;
//			@Column(length = 24)
//			private Date latestOpenDateOwn;
//			@Column(length = 24)
//			private Date earliestOpenDateNonOwn;
//			@Column(length = 24)
//			private Date latestOpenDateNonOwn;
//			@Column(length = 24)
//			private Date earliestCloseDateOwn;
//			@Column(length = 24)
//			private Date latestCloseDateOwn;
//			@Column(length = 24)
//			private Date earliestCloseDateNonOwn;
//			@Column(length = 24)
//			private Date latestCloseDateNonOwn;
//			@Column(length = 40)
//			private String latestMfiInstitution;
//			@Column(length = 24)
//			private int isActiveInMfi;
//			@Column(length = 24)
//			private int IS_ACTIVE_WITH_SSFB;
//			@Column(length = 24)
//			private int IS_WITH_SSFB_ONLY;
			@Column(length = 24)
			private int MFI_VINTAGE;
//			@Column(length = 24)
//			private int MFI_VINTAGE_MARKET;
			@Column(length = 24)
			private int MFI_VINTAGE_SSFB;
//			@Column(length = 24)
//			private int MFI_1_30DPD;
//			@Column(length = 24)
//			private int MFI_1_60DPD;
//			@Column(length = 24)
//			private int MFI_NPA_TAG;
//			@Column(length = 40)
//			private String MFI_MAX_DELQ_STATUS;
			@Column(length = 24)
			private int MFI_BUREAU_VINTAGE;
			private double MAX_MFI_EMI;
//			private double MAX_MFI_EMI_OPEN;
			private double MAX_LOAN_AMOUNT_MFI;
//			private double MAX_LOAN_AMOUNT_MFI_OPEN;
			@Column(length = 50)
			private String LATEST_ACCOUNTSTATUS_MFI;
//			@Column(length = 24)
//			private int RETAIL_RESTRUCTURED_TAG;
//			@Column(length = 24)
//			private int RETAIL_WRITEOFF_TAG;
//			@Column(length = 24)
//			private int RETAIL_DELQ_STATUS_RANKED;
//			@Column(length = 50)
//			private String RETAIL_MAX_DELQ_STATUS;
//			@Column(length = 24)
//			private double RETAIL_BALANCE;
//			@Column(length = 24)
//			private double RETAIL_sanctionamount;
//			@Column(length = 24)
//			private double SECURED_DISBURSED;
//			@Column(length = 24)
//			private double UNSECURED_DISBURSED;
			@Column(length = 24)
			private double SECURED_POS;
			@Column(length = 24)
			private double UNSECURED_POS;
			@Column(length = 24)
			private double HL_POS;
			@Column(length = 24)
			private double LAP_POS;
//			@Column(length = 24)
//			private int ACTIVE_IN_HL;
//			@Column(length = 24)
//			private int ACTIVE_IN_LAP;
//			@Column(length = 24)
//			private Date RETAIL_EARLIEST_OPEN_DATE;
//			@Column(length = 24)
//			private Date RETAIL_LATEST_OPEN_DATE;
//			@Column(length = 24)
//			private Date HL_LATEST_OPEN_DATE;
//			@Column(length = 24)
//			private Date LAP_LATEST_OPEN_DATE;
//			@Column(length = 24)
//			private double HL_DISBURSED;
//			@Column(length = 24)
//			private double LAP_DISBURSED;
			@Column(length = 24)
			private double RETAIL_IMPUTED_EMI_WO_CCOD_CURRENT;
//			@Column(length = 24)
//			private double RETAIL_IMPUTED_EMI_CURRENT;
//			@Column(length = 24)
//			private int NPA_TAG;
//			@Column(length = 24)
//			private int RETAIL_1_30DPD;
//			@Column(length = 24)
//			private int RETAIL_1_60DPD;
//			@Column(length = 24)
//			private double OTHERS_POS;
//			@Column(length = 24)
//			private int NUM_RETAIL_ACTIVE_ACCTS;
			@Column(length = 24)
			private int NUM_SECURED_ACCTS;
			@Column(length = 24)
			private int NUM_UNSECURED_ACCTS;
			@Column(length = 24)
			private int RETAIL_BUREAU_VINTAGE;
//			@Column(length = 24)
//			private int NUM_LIVE_RETAIL;
//			@Column(length = 24)
//			private int NUM_CLOSED_RETAIL;
//			@Column(length = 24)
//			private int NUM_ACCTS_RETAIL;
			private double MAX_CURRENT_EMI;
//			private double MAX_EMI;
//			private double MAX_LOAN_AMOUNT;
//			private double MAX_CURRENT_LOAN_AMOUNT;
//			@Column(nullable = true)
//			private int MAX_RETAIL_LOAN_TENURE;
//			private int MAX_RETAIL_LOAN_TENURE_CURRENT;
//			@Column(length = 24)
//			private Date RETAIL_LATEST_CLOSED_DATE;
			@Column(length = 24)
			private int NUM_SECURED_CLOSED_ACCTS;
			@Column(length = 24)
			private int NUM_UNSECURED_CLOSED_ACCTS;
			@Column(length = 24)
			private int NUM_SECURED_LIVE_ACCTS;
			@Column(length = 24)
			private int NUM_UNSECURED_LIVE_ACCTS;
			@Column(length = 24)
			private int NUM_PL_LIVE;
			@Column(length = 24)
			private int NUM_PL_CLOSED;
			@Column(length = 24)
			private int NUM_BL_LIVE;
			@Column(length = 24)
			private int NUM_BL_CLOSED;
			@Column(length = 24)
			private int NUM_HL_LAP_LIVE;
			@Column(length = 24)
			private int NUM_HL_LAP_CLOSED;
//			@Column(length = 40)
//			private String LATEST_ACCOUNTSTATUS_RETAIL;
			@Column(length = 40)
			private String LATEST_ACCOUNTSTATUS_PL;
			@Column(length = 40)
			private String LATEST_ACCOUNTSTATUS_BL;
			@Column(length = 50)
			private String LATEST_ACCOUNTSTATUS_HL_LAP;
			@Column(length = 24)
			private double TOTAL_DISB_PL;
			@Column(length = 24)
			private double TOTAL_DISB_BL;
			@Column(length = 24)
			private double TOTAL_DISB_HL_LAP;
			@Column(length = 24)
			private double BL_POS;
			@Column(length = 24)
			private double PL_POS;
			@Column(length = 24)
			private int BUREAU_VINTAGE_PL;
			@Column(length = 24)
			private int BUREAU_VINTAGE_BL;
			@Column(length = 24)
			private int BUREAU_VINTAGE_HL_LAP;
			@Column(length = 24)
			private Date LATEST_CLOSEDATE_PL;
			@Column(length = 24)
			private Date LATEST_CLOSEDATE_BL;
			@Column(length = 24)
			private Date LATEST_CLOSEDATE_HL_LAP;
			@Column(length = 24)
			private Date LATEST_CLOSEDATE_SECURED;
			@Column(length = 24)
			private Date LATEST_CLOSEDATE_UNSECURED;
			private double MAX_EMI_PL;
			private double MAX_EMI_BL;
			private double MAX_EMI_SECURED;
			private double MAX_EMI_UNSECURED;
			private double MAX_EMI_HL_LAP;
			private int MAX_LOAN_TENURE_HL_LAP;
			private int MAX_LOAN_TENURE_PL;
			private int MAX_LOAN_TENURE_BL;
			private int MAX_LOAN_TENURE_SECURED;
			private int MAX_LOAN_TENURE_UNSECURED;
			private double MAX_LOAN_AMOUNT_UNSECURED;
			private double MAX_LOAN_AMOUNT_SECURED;
			private double MAX_LOAN_AMOUNT_PL;
			private double MAX_LOAN_AMOUNT_BL;
			@Column(nullable = true)
			private double MAX_LOAN_AMOUNT_HL_LAP;
//			private int RETAIL_VINTAGE;
			@Column(length = 40)
			private long branchId;
			@Column(length = 24)
			private double amount;
			@Column(length = 24)
			private String status;
			private LocalDate createDate;
			private LocalDate updatedate;
			private Long customerID;
			private String AccountNo;
			private double ELIGIBLE_LOAN_VALUE_2YEARS;
			private double DisbursedAmt;
			private String SSFB_DPD_BUCKETS;
			private int TOTAL_NUM_MFI_ACCTS;
			private int NUM_HL_LAP_ACCTS;
			private int NUM_BL_ACCTS;
			private int NUM_PL_ACCTS;
			private int NUM_CLOSED_ACCTS;
			private String LATEST_ACCOUNTSTATUS_SECURED;
			private String LATEST_ACCOUNTSTATUS_UNSECURED;
			private double TOTAL_MFI_DISBURSEMENT;
			private double TOTAL_DISB_SECURED;
			private double TOTAL_DISB_UNSECURED;
			private int BUREAU_VINTAGE_SECURED;
			private int BUREAU_VINTAGE_UNSECURED;
			private int Current_CycleNo;
			private String Branch_Name;
			private String CITY;
			private String DISTRICT_NAME;
			private String CentreName;
			private int BranchCode;
			private String isActive;
			private String productCode;
			
			public String getProductCode() {
				return productCode;
			}
			public void setProductCode(String productCode) {
				this.productCode = productCode;
			}
			public String getIsActive() {
				return isActive;
			}
			public void setIsActive(String isActive) {
				this.isActive = isActive;
			}
			public int getBranchCode() {
				return BranchCode;
			}
			public void setBranchCode(int branchCode) {
				BranchCode = branchCode;
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
			public long getMobilePhone() {
				return mobilePhone;
			}
			public void setMobilePhone(long mobilePhone) {
				this.mobilePhone = mobilePhone;
			}
			public int getNumOpenAccount() {
				return numOpenAccount;
			}
			public void setNumOpenAccount(int numOpenAccount) {
				this.numOpenAccount = numOpenAccount;
			}
			public double getSumOutstandingBalance() {
				return sumOutstandingBalance;
			}
			public void setSumOutstandingBalance(double sumOutstandingBalance) {
				this.sumOutstandingBalance = sumOutstandingBalance;
			}
			public double getSumOutstandingBalanceOwn() {
				return sumOutstandingBalanceOwn;
			}
			public void setSumOutstandingBalanceOwn(double sumOutstandingBalanceOwn) {
				this.sumOutstandingBalanceOwn = sumOutstandingBalanceOwn;
			}
			public double getSumInstallmentAmountopen() {
				return sumInstallmentAmountopen;
			}
			public void setSumInstallmentAmountopen(double sumInstallmentAmountopen) {
				this.sumInstallmentAmountopen = sumInstallmentAmountopen;
			}
			public Date getLatestCloseDate() {
				return latestCloseDate;
			}
			public void setLatestCloseDate(Date latestCloseDate) {
				this.latestCloseDate = latestCloseDate;
			}
			public int getMFI_VINTAGE() {
				return MFI_VINTAGE;
			}
			public void setMFI_VINTAGE(int mFI_VINTAGE) {
				MFI_VINTAGE = mFI_VINTAGE;
			}
			public int getMFI_VINTAGE_SSFB() {
				return MFI_VINTAGE_SSFB;
			}
			public void setMFI_VINTAGE_SSFB(int mFI_VINTAGE_SSFB) {
				MFI_VINTAGE_SSFB = mFI_VINTAGE_SSFB;
			}
			public int getMFI_BUREAU_VINTAGE() {
				return MFI_BUREAU_VINTAGE;
			}
			public void setMFI_BUREAU_VINTAGE(int mFI_BUREAU_VINTAGE) {
				MFI_BUREAU_VINTAGE = mFI_BUREAU_VINTAGE;
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
			public String getLATEST_ACCOUNTSTATUS_MFI() {
				return LATEST_ACCOUNTSTATUS_MFI;
			}
			public void setLATEST_ACCOUNTSTATUS_MFI(String lATEST_ACCOUNTSTATUS_MFI) {
				LATEST_ACCOUNTSTATUS_MFI = lATEST_ACCOUNTSTATUS_MFI;
			}
			public double getSECURED_POS() {
				return SECURED_POS;
			}
			public void setSECURED_POS(double sECURED_POS) {
				SECURED_POS = sECURED_POS;
			}
			public double getUNSECURED_POS() {
				return UNSECURED_POS;
			}
			public void setUNSECURED_POS(double uNSECURED_POS) {
				UNSECURED_POS = uNSECURED_POS;
			}
			public double getHL_POS() {
				return HL_POS;
			}
			public void setHL_POS(double hL_POS) {
				HL_POS = hL_POS;
			}
			public double getLAP_POS() {
				return LAP_POS;
			}
			public void setLAP_POS(double lAP_POS) {
				LAP_POS = lAP_POS;
			}
			public double getRETAIL_IMPUTED_EMI_WO_CCOD_CURRENT() {
				return RETAIL_IMPUTED_EMI_WO_CCOD_CURRENT;
			}
			public void setRETAIL_IMPUTED_EMI_WO_CCOD_CURRENT(double rETAIL_IMPUTED_EMI_WO_CCOD_CURRENT) {
				RETAIL_IMPUTED_EMI_WO_CCOD_CURRENT = rETAIL_IMPUTED_EMI_WO_CCOD_CURRENT;
			}
			public int getNUM_SECURED_ACCTS() {
				return NUM_SECURED_ACCTS;
			}
			public void setNUM_SECURED_ACCTS(int nUM_SECURED_ACCTS) {
				NUM_SECURED_ACCTS = nUM_SECURED_ACCTS;
			}
			public int getNUM_UNSECURED_ACCTS() {
				return NUM_UNSECURED_ACCTS;
			}
			public void setNUM_UNSECURED_ACCTS(int nUM_UNSECURED_ACCTS) {
				NUM_UNSECURED_ACCTS = nUM_UNSECURED_ACCTS;
			}
			public int getRETAIL_BUREAU_VINTAGE() {
				return RETAIL_BUREAU_VINTAGE;
			}
			public void setRETAIL_BUREAU_VINTAGE(int rETAIL_BUREAU_VINTAGE) {
				RETAIL_BUREAU_VINTAGE = rETAIL_BUREAU_VINTAGE;
			}
			public double getMAX_CURRENT_EMI() {
				return MAX_CURRENT_EMI;
			}
			public void setMAX_CURRENT_EMI(double mAX_CURRENT_EMI) {
				MAX_CURRENT_EMI = mAX_CURRENT_EMI;
			}
			public int getNUM_SECURED_CLOSED_ACCTS() {
				return NUM_SECURED_CLOSED_ACCTS;
			}
			public void setNUM_SECURED_CLOSED_ACCTS(int nUM_SECURED_CLOSED_ACCTS) {
				NUM_SECURED_CLOSED_ACCTS = nUM_SECURED_CLOSED_ACCTS;
			}
			public int getNUM_UNSECURED_CLOSED_ACCTS() {
				return NUM_UNSECURED_CLOSED_ACCTS;
			}
			public void setNUM_UNSECURED_CLOSED_ACCTS(int nUM_UNSECURED_CLOSED_ACCTS) {
				NUM_UNSECURED_CLOSED_ACCTS = nUM_UNSECURED_CLOSED_ACCTS;
			}
			public int getNUM_SECURED_LIVE_ACCTS() {
				return NUM_SECURED_LIVE_ACCTS;
			}
			public void setNUM_SECURED_LIVE_ACCTS(int nUM_SECURED_LIVE_ACCTS) {
				NUM_SECURED_LIVE_ACCTS = nUM_SECURED_LIVE_ACCTS;
			}
			public int getNUM_UNSECURED_LIVE_ACCTS() {
				return NUM_UNSECURED_LIVE_ACCTS;
			}
			public void setNUM_UNSECURED_LIVE_ACCTS(int nUM_UNSECURED_LIVE_ACCTS) {
				NUM_UNSECURED_LIVE_ACCTS = nUM_UNSECURED_LIVE_ACCTS;
			}
			public int getNUM_PL_LIVE() {
				return NUM_PL_LIVE;
			}
			public void setNUM_PL_LIVE(int nUM_PL_LIVE) {
				NUM_PL_LIVE = nUM_PL_LIVE;
			}
			public int getNUM_PL_CLOSED() {
				return NUM_PL_CLOSED;
			}
			public void setNUM_PL_CLOSED(int nUM_PL_CLOSED) {
				NUM_PL_CLOSED = nUM_PL_CLOSED;
			}
			public int getNUM_BL_LIVE() {
				return NUM_BL_LIVE;
			}
			public void setNUM_BL_LIVE(int nUM_BL_LIVE) {
				NUM_BL_LIVE = nUM_BL_LIVE;
			}
			public int getNUM_BL_CLOSED() {
				return NUM_BL_CLOSED;
			}
			public void setNUM_BL_CLOSED(int nUM_BL_CLOSED) {
				NUM_BL_CLOSED = nUM_BL_CLOSED;
			}
			public int getNUM_HL_LAP_LIVE() {
				return NUM_HL_LAP_LIVE;
			}
			public void setNUM_HL_LAP_LIVE(int nUM_HL_LAP_LIVE) {
				NUM_HL_LAP_LIVE = nUM_HL_LAP_LIVE;
			}
			public int getNUM_HL_LAP_CLOSED() {
				return NUM_HL_LAP_CLOSED;
			}
			public void setNUM_HL_LAP_CLOSED(int nUM_HL_LAP_CLOSED) {
				NUM_HL_LAP_CLOSED = nUM_HL_LAP_CLOSED;
			}
			public String getLATEST_ACCOUNTSTATUS_PL() {
				return LATEST_ACCOUNTSTATUS_PL;
			}
			public void setLATEST_ACCOUNTSTATUS_PL(String lATEST_ACCOUNTSTATUS_PL) {
				LATEST_ACCOUNTSTATUS_PL = lATEST_ACCOUNTSTATUS_PL;
			}
			public String getLATEST_ACCOUNTSTATUS_BL() {
				return LATEST_ACCOUNTSTATUS_BL;
			}
			public void setLATEST_ACCOUNTSTATUS_BL(String lATEST_ACCOUNTSTATUS_BL) {
				LATEST_ACCOUNTSTATUS_BL = lATEST_ACCOUNTSTATUS_BL;
			}
			public String getLATEST_ACCOUNTSTATUS_HL_LAP() {
				return LATEST_ACCOUNTSTATUS_HL_LAP;
			}
			public void setLATEST_ACCOUNTSTATUS_HL_LAP(String lATEST_ACCOUNTSTATUS_HL_LAP) {
				LATEST_ACCOUNTSTATUS_HL_LAP = lATEST_ACCOUNTSTATUS_HL_LAP;
			}
			public double getTOTAL_DISB_PL() {
				return TOTAL_DISB_PL;
			}
			public void setTOTAL_DISB_PL(double tOTAL_DISB_PL) {
				TOTAL_DISB_PL = tOTAL_DISB_PL;
			}
			public double getTOTAL_DISB_BL() {
				return TOTAL_DISB_BL;
			}
			public void setTOTAL_DISB_BL(double tOTAL_DISB_BL) {
				TOTAL_DISB_BL = tOTAL_DISB_BL;
			}
			public double getTOTAL_DISB_HL_LAP() {
				return TOTAL_DISB_HL_LAP;
			}
			public void setTOTAL_DISB_HL_LAP(double tOTAL_DISB_HL_LAP) {
				TOTAL_DISB_HL_LAP = tOTAL_DISB_HL_LAP;
			}
			public double getBL_POS() {
				return BL_POS;
			}
			public void setBL_POS(double bL_POS) {
				BL_POS = bL_POS;
			}
			public double getPL_POS() {
				return PL_POS;
			}
			public void setPL_POS(double pL_POS) {
				PL_POS = pL_POS;
			}
			public int getBUREAU_VINTAGE_PL() {
				return BUREAU_VINTAGE_PL;
			}
			public void setBUREAU_VINTAGE_PL(int bUREAU_VINTAGE_PL) {
				BUREAU_VINTAGE_PL = bUREAU_VINTAGE_PL;
			}
			public int getBUREAU_VINTAGE_BL() {
				return BUREAU_VINTAGE_BL;
			}
			public void setBUREAU_VINTAGE_BL(int bUREAU_VINTAGE_BL) {
				BUREAU_VINTAGE_BL = bUREAU_VINTAGE_BL;
			}
			public int getBUREAU_VINTAGE_HL_LAP() {
				return BUREAU_VINTAGE_HL_LAP;
			}
			public void setBUREAU_VINTAGE_HL_LAP(int bUREAU_VINTAGE_HL_LAP) {
				BUREAU_VINTAGE_HL_LAP = bUREAU_VINTAGE_HL_LAP;
			}
			public Date getLATEST_CLOSEDATE_PL() {
				return LATEST_CLOSEDATE_PL;
			}
			public void setLATEST_CLOSEDATE_PL(Date lATEST_CLOSEDATE_PL) {
				LATEST_CLOSEDATE_PL = lATEST_CLOSEDATE_PL;
			}
			public Date getLATEST_CLOSEDATE_BL() {
				return LATEST_CLOSEDATE_BL;
			}
			public void setLATEST_CLOSEDATE_BL(Date lATEST_CLOSEDATE_BL) {
				LATEST_CLOSEDATE_BL = lATEST_CLOSEDATE_BL;
			}
			public Date getLATEST_CLOSEDATE_HL_LAP() {
				return LATEST_CLOSEDATE_HL_LAP;
			}
			public void setLATEST_CLOSEDATE_HL_LAP(Date lATEST_CLOSEDATE_HL_LAP) {
				LATEST_CLOSEDATE_HL_LAP = lATEST_CLOSEDATE_HL_LAP;
			}
			public Date getLATEST_CLOSEDATE_SECURED() {
				return LATEST_CLOSEDATE_SECURED;
			}
			public void setLATEST_CLOSEDATE_SECURED(Date lATEST_CLOSEDATE_SECURED) {
				LATEST_CLOSEDATE_SECURED = lATEST_CLOSEDATE_SECURED;
			}
			public Date getLATEST_CLOSEDATE_UNSECURED() {
				return LATEST_CLOSEDATE_UNSECURED;
			}
			public void setLATEST_CLOSEDATE_UNSECURED(Date lATEST_CLOSEDATE_UNSECURED) {
				LATEST_CLOSEDATE_UNSECURED = lATEST_CLOSEDATE_UNSECURED;
			}
			public double getMAX_EMI_PL() {
				return MAX_EMI_PL;
			}
			public void setMAX_EMI_PL(double mAX_EMI_PL) {
				MAX_EMI_PL = mAX_EMI_PL;
			}
			public double getMAX_EMI_BL() {
				return MAX_EMI_BL;
			}
			public void setMAX_EMI_BL(double mAX_EMI_BL) {
				MAX_EMI_BL = mAX_EMI_BL;
			}
			public double getMAX_EMI_SECURED() {
				return MAX_EMI_SECURED;
			}
			public void setMAX_EMI_SECURED(double mAX_EMI_SECURED) {
				MAX_EMI_SECURED = mAX_EMI_SECURED;
			}
			public double getMAX_EMI_UNSECURED() {
				return MAX_EMI_UNSECURED;
			}
			public void setMAX_EMI_UNSECURED(double mAX_EMI_UNSECURED) {
				MAX_EMI_UNSECURED = mAX_EMI_UNSECURED;
			}
			public double getMAX_EMI_HL_LAP() {
				return MAX_EMI_HL_LAP;
			}
			public void setMAX_EMI_HL_LAP(double mAX_EMI_HL_LAP) {
				MAX_EMI_HL_LAP = mAX_EMI_HL_LAP;
			}
			public int getMAX_LOAN_TENURE_HL_LAP() {
				return MAX_LOAN_TENURE_HL_LAP;
			}
			public void setMAX_LOAN_TENURE_HL_LAP(int mAX_LOAN_TENURE_HL_LAP) {
				MAX_LOAN_TENURE_HL_LAP = mAX_LOAN_TENURE_HL_LAP;
			}
			public int getMAX_LOAN_TENURE_PL() {
				return MAX_LOAN_TENURE_PL;
			}
			public void setMAX_LOAN_TENURE_PL(int mAX_LOAN_TENURE_PL) {
				MAX_LOAN_TENURE_PL = mAX_LOAN_TENURE_PL;
			}
			public int getMAX_LOAN_TENURE_BL() {
				return MAX_LOAN_TENURE_BL;
			}
			public void setMAX_LOAN_TENURE_BL(int mAX_LOAN_TENURE_BL) {
				MAX_LOAN_TENURE_BL = mAX_LOAN_TENURE_BL;
			}
			public int getMAX_LOAN_TENURE_SECURED() {
				return MAX_LOAN_TENURE_SECURED;
			}
			public void setMAX_LOAN_TENURE_SECURED(int mAX_LOAN_TENURE_SECURED) {
				MAX_LOAN_TENURE_SECURED = mAX_LOAN_TENURE_SECURED;
			}
			public int getMAX_LOAN_TENURE_UNSECURED() {
				return MAX_LOAN_TENURE_UNSECURED;
			}
			public void setMAX_LOAN_TENURE_UNSECURED(int mAX_LOAN_TENURE_UNSECURED) {
				MAX_LOAN_TENURE_UNSECURED = mAX_LOAN_TENURE_UNSECURED;
			}
			public double getMAX_LOAN_AMOUNT_UNSECURED() {
				return MAX_LOAN_AMOUNT_UNSECURED;
			}
			public void setMAX_LOAN_AMOUNT_UNSECURED(double mAX_LOAN_AMOUNT_UNSECURED) {
				MAX_LOAN_AMOUNT_UNSECURED = mAX_LOAN_AMOUNT_UNSECURED;
			}
			public double getMAX_LOAN_AMOUNT_SECURED() {
				return MAX_LOAN_AMOUNT_SECURED;
			}
			public void setMAX_LOAN_AMOUNT_SECURED(double mAX_LOAN_AMOUNT_SECURED) {
				MAX_LOAN_AMOUNT_SECURED = mAX_LOAN_AMOUNT_SECURED;
			}
			public double getMAX_LOAN_AMOUNT_PL() {
				return MAX_LOAN_AMOUNT_PL;
			}
			public void setMAX_LOAN_AMOUNT_PL(double mAX_LOAN_AMOUNT_PL) {
				MAX_LOAN_AMOUNT_PL = mAX_LOAN_AMOUNT_PL;
			}
			public double getMAX_LOAN_AMOUNT_BL() {
				return MAX_LOAN_AMOUNT_BL;
			}
			public void setMAX_LOAN_AMOUNT_BL(double mAX_LOAN_AMOUNT_BL) {
				MAX_LOAN_AMOUNT_BL = mAX_LOAN_AMOUNT_BL;
			}
			public double getMAX_LOAN_AMOUNT_HL_LAP() {
				return MAX_LOAN_AMOUNT_HL_LAP;
			}
			public void setMAX_LOAN_AMOUNT_HL_LAP(double mAX_LOAN_AMOUNT_HL_LAP) {
				MAX_LOAN_AMOUNT_HL_LAP = mAX_LOAN_AMOUNT_HL_LAP;
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
			public LocalDate getUpdatedate() {
				return updatedate;
			}
			public void setUpdatedate(LocalDate updatedate) {
				this.updatedate = updatedate;
			}
			public Long getCustomerID() {
				return customerID;
			}
			public void setCustomerID(Long customerID) {
				this.customerID = customerID;
			}
			public String getAccountNo() {
				return AccountNo;
			}
			public void setAccountNo(String accountNo) {
				AccountNo = accountNo;
			}
			public double getELIGIBLE_LOAN_VALUE_2YEARS() {
				return ELIGIBLE_LOAN_VALUE_2YEARS;
			}
			public void setELIGIBLE_LOAN_VALUE_2YEARS(double eLIGIBLE_LOAN_VALUE_2YEARS) {
				ELIGIBLE_LOAN_VALUE_2YEARS = eLIGIBLE_LOAN_VALUE_2YEARS;
			}
			public double getDisbursedAmt() {
				return DisbursedAmt;
			}
			public void setDisbursedAmt(double disbursedAmt) {
				DisbursedAmt = disbursedAmt;
			}
			public String getSSFB_DPD_BUCKETS() {
				return SSFB_DPD_BUCKETS;
			}
			public void setSSFB_DPD_BUCKETS(String sSFB_DPD_BUCKETS) {
				SSFB_DPD_BUCKETS = sSFB_DPD_BUCKETS;
			}
			public int getTOTAL_NUM_MFI_ACCTS() {
				return TOTAL_NUM_MFI_ACCTS;
			}
			public void setTOTAL_NUM_MFI_ACCTS(int tOTAL_NUM_MFI_ACCTS) {
				TOTAL_NUM_MFI_ACCTS = tOTAL_NUM_MFI_ACCTS;
			}
			public int getNUM_HL_LAP_ACCTS() {
				return NUM_HL_LAP_ACCTS;
			}
			public void setNUM_HL_LAP_ACCTS(int nUM_HL_LAP_ACCTS) {
				NUM_HL_LAP_ACCTS = nUM_HL_LAP_ACCTS;
			}
			public int getNUM_BL_ACCTS() {
				return NUM_BL_ACCTS;
			}
			public void setNUM_BL_ACCTS(int nUM_BL_ACCTS) {
				NUM_BL_ACCTS = nUM_BL_ACCTS;
			}
			public int getNUM_PL_ACCTS() {
				return NUM_PL_ACCTS;
			}
			public void setNUM_PL_ACCTS(int nUM_PL_ACCTS) {
				NUM_PL_ACCTS = nUM_PL_ACCTS;
			}
			public int getNUM_CLOSED_ACCTS() {
				return NUM_CLOSED_ACCTS;
			}
			public void setNUM_CLOSED_ACCTS(int nUM_CLOSED_ACCTS) {
				NUM_CLOSED_ACCTS = nUM_CLOSED_ACCTS;
			}
			public String getLATEST_ACCOUNTSTATUS_SECURED() {
				return LATEST_ACCOUNTSTATUS_SECURED;
			}
			public void setLATEST_ACCOUNTSTATUS_SECURED(String lATEST_ACCOUNTSTATUS_SECURED) {
				LATEST_ACCOUNTSTATUS_SECURED = lATEST_ACCOUNTSTATUS_SECURED;
			}
			public String getLATEST_ACCOUNTSTATUS_UNSECURED() {
				return LATEST_ACCOUNTSTATUS_UNSECURED;
			}
			public void setLATEST_ACCOUNTSTATUS_UNSECURED(String lATEST_ACCOUNTSTATUS_UNSECURED) {
				LATEST_ACCOUNTSTATUS_UNSECURED = lATEST_ACCOUNTSTATUS_UNSECURED;
			}
			public double getTOTAL_MFI_DISBURSEMENT() {
				return TOTAL_MFI_DISBURSEMENT;
			}
			public void setTOTAL_MFI_DISBURSEMENT(double tOTAL_MFI_DISBURSEMENT) {
				TOTAL_MFI_DISBURSEMENT = tOTAL_MFI_DISBURSEMENT;
			}
			public double getTOTAL_DISB_SECURED() {
				return TOTAL_DISB_SECURED;
			}
			public void setTOTAL_DISB_SECURED(double tOTAL_DISB_SECURED) {
				TOTAL_DISB_SECURED = tOTAL_DISB_SECURED;
			}
			public double getTOTAL_DISB_UNSECURED() {
				return TOTAL_DISB_UNSECURED;
			}
			public void setTOTAL_DISB_UNSECURED(double tOTAL_DISB_UNSECURED) {
				TOTAL_DISB_UNSECURED = tOTAL_DISB_UNSECURED;
			}
			public int getBUREAU_VINTAGE_SECURED() {
				return BUREAU_VINTAGE_SECURED;
			}
			public void setBUREAU_VINTAGE_SECURED(int bUREAU_VINTAGE_SECURED) {
				BUREAU_VINTAGE_SECURED = bUREAU_VINTAGE_SECURED;
			}
			public int getBUREAU_VINTAGE_UNSECURED() {
				return BUREAU_VINTAGE_UNSECURED;
			}
			public void setBUREAU_VINTAGE_UNSECURED(int bUREAU_VINTAGE_UNSECURED) {
				BUREAU_VINTAGE_UNSECURED = bUREAU_VINTAGE_UNSECURED;
			}
			public int getCurrent_CycleNo() {
				return Current_CycleNo;
			}
			public void setCurrent_CycleNo(int current_CycleNo) {
				Current_CycleNo = current_CycleNo;
			}
			public String getBranch_Name() {
				return Branch_Name;
			}
			public void setBranch_Name(String branch_Name) {
				Branch_Name = branch_Name;
			}
			public String getCITY() {
				return CITY;
			}
			public void setCITY(String cITY) {
				CITY = cITY;
			}
			public String getDISTRICT_NAME() {
				return DISTRICT_NAME;
			}
			public void setDISTRICT_NAME(String dISTRICT_NAME) {
				DISTRICT_NAME = dISTRICT_NAME;
			}
			public String getCentreName() {
				return CentreName;
			}
			public void setCentreName(String centreName) {
				CentreName = centreName;
			}
			@Override
			public String toString() {
				return "LoanDetails [referenceNo=" + referenceNo + ", memberName=" + memberName + ", state=" + state
						+ ", mobilePhone=" + mobilePhone + ", numOpenAccount=" + numOpenAccount
						+ ", sumOutstandingBalance=" + sumOutstandingBalance + ", sumOutstandingBalanceOwn="
						+ sumOutstandingBalanceOwn + ", sumInstallmentAmountopen=" + sumInstallmentAmountopen
						+ ", latestCloseDate=" + latestCloseDate + ", MFI_VINTAGE=" + MFI_VINTAGE
						+ ", MFI_VINTAGE_SSFB=" + MFI_VINTAGE_SSFB + ", MFI_BUREAU_VINTAGE=" + MFI_BUREAU_VINTAGE
						+ ", MAX_MFI_EMI=" + MAX_MFI_EMI + ", MAX_LOAN_AMOUNT_MFI=" + MAX_LOAN_AMOUNT_MFI
						+ ", LATEST_ACCOUNTSTATUS_MFI=" + LATEST_ACCOUNTSTATUS_MFI + ", SECURED_POS=" + SECURED_POS
						+ ", UNSECURED_POS=" + UNSECURED_POS + ", HL_POS=" + HL_POS + ", LAP_POS=" + LAP_POS
						+ ", RETAIL_IMPUTED_EMI_WO_CCOD_CURRENT=" + RETAIL_IMPUTED_EMI_WO_CCOD_CURRENT
						+ ", NUM_SECURED_ACCTS=" + NUM_SECURED_ACCTS + ", NUM_UNSECURED_ACCTS=" + NUM_UNSECURED_ACCTS
						+ ", RETAIL_BUREAU_VINTAGE=" + RETAIL_BUREAU_VINTAGE + ", MAX_CURRENT_EMI=" + MAX_CURRENT_EMI
						+ ", NUM_SECURED_CLOSED_ACCTS=" + NUM_SECURED_CLOSED_ACCTS + ", NUM_UNSECURED_CLOSED_ACCTS="
						+ NUM_UNSECURED_CLOSED_ACCTS + ", NUM_SECURED_LIVE_ACCTS=" + NUM_SECURED_LIVE_ACCTS
						+ ", NUM_UNSECURED_LIVE_ACCTS=" + NUM_UNSECURED_LIVE_ACCTS + ", NUM_PL_LIVE=" + NUM_PL_LIVE
						+ ", NUM_PL_CLOSED=" + NUM_PL_CLOSED + ", NUM_BL_LIVE=" + NUM_BL_LIVE + ", NUM_BL_CLOSED="
						+ NUM_BL_CLOSED + ", NUM_HL_LAP_LIVE=" + NUM_HL_LAP_LIVE + ", NUM_HL_LAP_CLOSED="
						+ NUM_HL_LAP_CLOSED + ", LATEST_ACCOUNTSTATUS_PL=" + LATEST_ACCOUNTSTATUS_PL
						+ ", LATEST_ACCOUNTSTATUS_BL=" + LATEST_ACCOUNTSTATUS_BL + ", LATEST_ACCOUNTSTATUS_HL_LAP="
						+ LATEST_ACCOUNTSTATUS_HL_LAP + ", TOTAL_DISB_PL=" + TOTAL_DISB_PL + ", TOTAL_DISB_BL="
						+ TOTAL_DISB_BL + ", TOTAL_DISB_HL_LAP=" + TOTAL_DISB_HL_LAP + ", BL_POS=" + BL_POS
						+ ", PL_POS=" + PL_POS + ", BUREAU_VINTAGE_PL=" + BUREAU_VINTAGE_PL + ", BUREAU_VINTAGE_BL="
						+ BUREAU_VINTAGE_BL + ", BUREAU_VINTAGE_HL_LAP=" + BUREAU_VINTAGE_HL_LAP
						+ ", LATEST_CLOSEDATE_PL=" + LATEST_CLOSEDATE_PL + ", LATEST_CLOSEDATE_BL="
						+ LATEST_CLOSEDATE_BL + ", LATEST_CLOSEDATE_HL_LAP=" + LATEST_CLOSEDATE_HL_LAP
						+ ", LATEST_CLOSEDATE_SECURED=" + LATEST_CLOSEDATE_SECURED + ", LATEST_CLOSEDATE_UNSECURED="
						+ LATEST_CLOSEDATE_UNSECURED + ", MAX_EMI_PL=" + MAX_EMI_PL + ", MAX_EMI_BL=" + MAX_EMI_BL
						+ ", MAX_EMI_SECURED=" + MAX_EMI_SECURED + ", MAX_EMI_UNSECURED=" + MAX_EMI_UNSECURED
						+ ", MAX_EMI_HL_LAP=" + MAX_EMI_HL_LAP + ", MAX_LOAN_TENURE_HL_LAP=" + MAX_LOAN_TENURE_HL_LAP
						+ ", MAX_LOAN_TENURE_PL=" + MAX_LOAN_TENURE_PL + ", MAX_LOAN_TENURE_BL=" + MAX_LOAN_TENURE_BL
						+ ", MAX_LOAN_TENURE_SECURED=" + MAX_LOAN_TENURE_SECURED + ", MAX_LOAN_TENURE_UNSECURED="
						+ MAX_LOAN_TENURE_UNSECURED + ", MAX_LOAN_AMOUNT_UNSECURED=" + MAX_LOAN_AMOUNT_UNSECURED
						+ ", MAX_LOAN_AMOUNT_SECURED=" + MAX_LOAN_AMOUNT_SECURED + ", MAX_LOAN_AMOUNT_PL="
						+ MAX_LOAN_AMOUNT_PL + ", MAX_LOAN_AMOUNT_BL=" + MAX_LOAN_AMOUNT_BL
						+ ", MAX_LOAN_AMOUNT_HL_LAP=" + MAX_LOAN_AMOUNT_HL_LAP + ", branchId=" + branchId + ", amount="
						+ amount + ", status=" + status + ", createDate=" + createDate + ", updatedate=" + updatedate
						+ ", customerID=" + customerID + ", AccountNo=" + AccountNo + ", ELIGIBLE_LOAN_VALUE_2YEARS="
						+ ELIGIBLE_LOAN_VALUE_2YEARS + ", DisbursedAmt=" + DisbursedAmt + ", SSFB_DPD_BUCKETS="
						+ SSFB_DPD_BUCKETS + ", TOTAL_NUM_MFI_ACCTS=" + TOTAL_NUM_MFI_ACCTS + ", NUM_HL_LAP_ACCTS="
						+ NUM_HL_LAP_ACCTS + ", NUM_BL_ACCTS=" + NUM_BL_ACCTS + ", NUM_PL_ACCTS=" + NUM_PL_ACCTS
						+ ", NUM_CLOSED_ACCTS=" + NUM_CLOSED_ACCTS + ", LATEST_ACCOUNTSTATUS_SECURED="
						+ LATEST_ACCOUNTSTATUS_SECURED + ", LATEST_ACCOUNTSTATUS_UNSECURED="
						+ LATEST_ACCOUNTSTATUS_UNSECURED + ", TOTAL_MFI_DISBURSEMENT=" + TOTAL_MFI_DISBURSEMENT
						+ ", TOTAL_DISB_SECURED=" + TOTAL_DISB_SECURED + ", TOTAL_DISB_UNSECURED="
						+ TOTAL_DISB_UNSECURED + ", BUREAU_VINTAGE_SECURED=" + BUREAU_VINTAGE_SECURED
						+ ", BUREAU_VINTAGE_UNSECURED=" + BUREAU_VINTAGE_UNSECURED + ", Current_CycleNo="
						+ Current_CycleNo + ", Branch_Name=" + Branch_Name + ", CITY=" + CITY + ", DISTRICT_NAME="
						+ DISTRICT_NAME + ", CentreName=" + CentreName + ", BranchCode=" + BranchCode + "]";
			}
			
}
