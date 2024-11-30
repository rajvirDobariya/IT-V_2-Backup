package com.suryoday.collections.pojo;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "collection_input")
public class CSVModel {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;

	@Column(name = "AGREEMENTID", length = 50)
	private String aggrementID;

	@Column(name = "CUSTOMERID", length = 50)
	private String customerID;

	@Column(name = "CUSTOMERNAME", length = 50)
	private String customerName;

	@Column(name = "ProductGroup", length = 50)
	private String productGroup;

	@Column(name = "Product", length = 50)
	private String product;

	@Column(name = "Sub product", length = 50)
	private String subProduct;

	@Column(name = "Country", length = 50)
	private String country;

	@Column(name = "Zone", length = 50)
	private String zone;

	@Column(name = "Region", length = 50)
	private String region;

	@Column(name = "State", length = 50)
	private String state;

	@Column(name = "City", length = 50)
	private String city;

	@Column(name = "District", length = 50)
	private String district;

	@Column(name = "Branch", length = 50)
	private String branch;

	@Column(name = "BRANCH_CODE", length = 50)
	private String branchCode;

	@Column(name = "CurrentDPD", length = 50)
	private String currentDPD;

	@Column(name = "Current_INT", length = 50)
	private String currentINT;

	@Column(name = "Current_POS", length = 50)
	private String currentPOS;

	@Column(name = "Disbursal_Date", length = 50)
	private String disbursalDate;

	@Column(name = "DisbursedAmount", length = 50)
	private String disbursedAmount;

	@Column(name = "EMIAmount", length = 50)
	private String eMIAmount;

	@Column(name = "BOM_POS", length = 50)
	private String bOMPOS;

	@Column(name = "EMI_OD_Amount", length = 50)
	private String eMIODAmount;

	@Column(name = "INTEREST_OD", length = 50)
	private String interestOD;

	@Column(name = "INTEREST_Paid", length = 50)
	private String interestPaid;

	@Column(name = "LOAN_STATUS", length = 50)
	private String loanStatus;

	@Column(name = "PENAL_PENDING", length = 50)
	private String penalPending;

	@Column(name = "PRINCIPAL_OD", length = 50)
	private String principalOD;

	@Column(name = "TOTAL_ARREARS", length = 150)
	private String totalAddress;

	@Column(name = "TOTAL_FORECLOSURE", length = 50)
	private String totalForcelosure;

	@Column(name = "TOTAL_OUTSTANDING", length = 50)
	private String totalOutstanding;

	@Column(name = "OTHER_CHARGES", length = 50)
	private String otherChanges;

	@Column(name = "ForeclosePrepayCharge", length = 50)
	private String foreclosePrepayCharge;

	@Column(name = "TENURE_MONTH", length = 50)
	private String tenureMonth;

	@Column(name = "Tenure_Days", length = 50)
	private String tenureDays;

	@Column(name = "MAILINGADDRESSLINE", length = 150)
	private String mailingAddressLine;

	@Column(name = "MAILINGLANDMARK", length = 50)
	private String mailingLandmark;

	@Column(name = "MAILINGMOBILE", length = 50)
	private String mailingMobile;

	@Column(name = "MAILINGPHONE1", length = 50)
	private String mailingPhone1;

	@Column(name = "MAILINGPHONE2", length = 50)
	private String mailingPhone2;

	@Column(name = "MAILINGZIPCODE", length = 50)
	private String mailingZipcode;

	@Column(name = "MAILING_ADD_LAT", length = 50)
	private String mailingAddLat;

	@Column(name = "MAILING_ADD_LONG", length = 50)
	private String mailingAddLong;

	@Column(name = "NONMAILINGADDRESSLINE", length = 150)
	private String noMailingAddressLine;

	@Column(name = "NONMAILINGCITY", length = 50)
	private String noMailingCity;

	@Column(name = "NONMAILINGLANDMARK", length = 50)
	private String noMailingLandmark;

	@Column(name = "NONMAILINGMOBILE", length = 50)
	private String noMailingMobile;

	@Column(name = "NONMAILINGPHONE1", length = 50)
	private String noMailingPhone1;

	@Column(name = "NONMAILINGPHONE2", length = 50)
	private String noMailingPhone2;

	@Column(name = "NONMAILINGSTATE", length = 50)
	private String noMailingState;

	@Column(name = "NONMAILINGZIPCODE", length = 50)
	private String noMailingZipcode;

	@Column(name = "NONMAILING_ADD_LAT", length = 50)
	private String noMailingAddLat;

	@Column(name = "NONMAILING_ADD_LONG", length = 50)
	private String noMailingAddLong;

	@Column(name = "CHASISNUM", length = 50)
	private String chasisnum;

	@Column(name = "ENGINENUM", length = 50)
	private String engineum;

	@Column(name = "Level_Desc", length = 50)
	private String levelDesc;

	@Column(name = "MAKE", length = 50)
	private String make;

	@Column(name = "MANUFACTURERDESC", length = 50)
	private String manufactureDesc;

	@Column(name = "MODELID", length = 50)
	private String modelid;

	@Column(name = "UNIT_DESC", length = 50)
	private String unitDesc;

	@Column(name = "YEAR", length = 50)
	private String year;

	@Column(name = "CO_APPLICANT_1_ID", length = 50)
	private String coApplicant1ID;

	@Column(name = "CO_APPLICANT_1_NAME", length = 50)
	private String coApplicant1Name;

	@Column(name = "CO_APPLICANT_1_PHONE", length = 50)
	private String coApplicant1Phone;

	@Column(name = "CO_APPLICANT_2_ID", length = 50)
	private String coApplicant2ID;

	@Column(name = "CO_APPLICANT_2_NAME", length = 50)
	private String coApplicant2Name;

	@Column(name = "CO_APPLICANT_2_PHONE ", length = 50)
	private String coApplicant2Phone;

	@Column(name = "CO_APPLICANT_3_ID", length = 50)
	private String coApplicant3ID;

	@Column(name = "CO_APPLICANT_3_NAME", length = 50)
	private String coApplicant3Name;

	@Column(name = "CO_APPLICANT_3_PHONE ", length = 50)
	private String coApplicant3Phone;

	@Column(name = "CO_APPLICANT_4_ID", length = 50)
	private String coApplicant4ID;

	@Column(name = "CO_APPLICANT_4_NAME", length = 50)
	private String coApplicant14Name;

	@Column(name = "CO_APPLICANT_4_PHONE ", length = 50)
	private String coApplicant4Phone;

	@Column(name = "CO_APPLICANT_5_ID", length = 50)
	private String coApplicant5ID;

	@Column(name = "CO_APPLICANT_5_NAME", length = 50)
	private String coApplicant5Name;

	@Column(name = "CO_APPLICANT_5_PHONE ", length = 50)
	private String coApplicant5Phone;

	@Column(name = "GUARANTOR_1_ID ", length = 50)
	private String gurantor1ID;

	@Column(name = "GUARANTOR_1_NAME ", length = 50)
	private String gurantor1Name;

	@Column(name = "GUARANTOR_1_PHONE", length = 50)
	private String gurantor1Phone;

	@Column(name = "GUARANTOR_2_ID ", length = 50)
	private String gurantor2ID;

	@Column(name = "GUARANTOR_2_NAME ", length = 50)
	private String gurantor2Name;

	@Column(name = "GUARANTOR_2_PHONE", length = 50)
	private String gurantor2Phone;

	@Column(name = "GUARANTOR_3_ID ", length = 50)
	private String gurantor3ID;

	@Column(name = "GUARANTOR_3_NAME ", length = 50)
	private String gurantor3Name;

	@Column(name = "GUARANTOR_3_PHONE", length = 50)
	private String gurantor3Phone;

	@Column(name = "GUARANTOR_4_ID ", length = 50)
	private String gurantor4ID;

	@Column(name = "GUARANTOR_4_NAME ", length = 50)
	private String gurantor4Name;

	@Column(name = "GUARANTOR_4_PHONE", length = 50)
	private String gurantor4Phone;

	@Column(name = "REF1_ADDRS", length = 150)
	private String ref1Addrs;

	@Column(name = "REF1_CONTACT", length = 50)
	private String ref1Contact;

	@Column(name = "REF2_ADDRS", length = 150)
	private String ref2Addrs;

	@Column(name = "REF2_CONTACT", length = 50)
	private String ref2Contact;

	@Column(name = "REFERENCE1_NAME", length = 50)
	private String reference1Name;

	@Column(name = "REFERENCE2_NAME", length = 50)
	private String reference2Name;

	@Column(name = "CenterName", length = 50)
	private String centerName;

	@Column(name = "ACC_OPEN_DATE", length = 50)
	private String accOpenDate;

	@Column(name = "NEXT_DUE_DATE", length = 50)
	private String nextDueDate;

	@Column(name = "LAST_PAY_DATE", length = 50)
	private String LastPayDate;

	@Column(name = "BUSINESS_NAME", length = 50)
	private String bussinessName;

	@Column(name = "BUSINESSADDRESSLINE", length = 100)
	private String bussinessAddressLine;

	@Column(name = "BUSINESSADDRESSCITY", length = 50)
	private String bussinessAddressCity;

	@Column(name = "BUSINESSADDRESSSTATE", length = 50)
	private String bussinessAddressState;

	@Column(name = "BUSINESSADDRESSLANDMARK", length = 50)
	private String bussinessAddressLandmark;

	@Column(name = "BUSINESSADDRESSZIPCODE", length = 50)
	private String bussinessAddressZipcode;

	@Column(name = "BUSINESS_ADD_LAT", length = 50)
	private String bussinessAddressLat;

	@Column(name = "BUSINESS_ADD_LONG", length = 50)
	private String bussinessAddressLong;

	@Column(name = "BUSINESSMOBILE", length = 50)
	private String bussinessMobile;

	@Column(name = "BUSINESSPHONE1", length = 50)
	private String bussinessPhone1;

	@Column(name = "Collector_ID", length = 50)
	private String collectorID;

	@Column(name = "Status", length = 50)
	private String status;

	@Column(name = "current_date", length = 50)
	private LocalDate currentDate;

	@Column(name = "updated_date", length = 50)
	private LocalDate updatedDate;

	public CSVModel() {

	}

	public CSVModel(long id, String aggrementID, String customerID, String customerName, String productGroup,
			String product, String subProduct, String country, String zone, String region, String state, String city,
			String district, String branch, String branchCode, String currentDPD, String currentINT, String currentPOS,
			String disbursalDate, String disbursedAmount, String eMIAmount, String bOMPOS, String eMIODAmount,
			String interestOD, String interestPaid, String loanStatus, String penalPending, String principalOD,
			String totalAddress, String totalForcelosure, String totalOutstanding, String otherChanges,
			String foreclosePrepayCharge, String tenureMonth, String tenureDays, String mailingAddressLine,
			String mailingLandmark, String mailingMobile, String mailingPhone1, String mailingPhone2,
			String mailingZipcode, String mailingAddLat, String mailingAddLong, String noMailingAddressLine,
			String noMailingCity, String noMailingLandmark, String noMailingMobile, String noMailingPhone1,
			String noMailingPhone2, String noMailingState, String noMailingZipcode, String noMailingAddLat,
			String noMailingAddLong, String chasisnum, String engineum, String levelDesc, String make,
			String manufactureDesc, String modelid, String unitDesc, String year, String coApplicant1ID,
			String coApplicant1Name, String coApplicant1Phone, String coApplicant2ID, String coApplicant2Name,
			String coApplicant2Phone, String coApplicant3ID, String coApplicant3Name, String coApplicant3Phone,
			String coApplicant4ID, String coApplicant14Name, String coApplicant4Phone, String coApplicant5ID,
			String coApplicant5Name, String coApplicant5Phone, String gurantor1id, String gurantor1Name,
			String gurantor1Phone, String gurantor2id, String gurantor2Name, String gurantor2Phone, String gurantor3id,
			String gurantor3Name, String gurantor3Phone, String gurantor4id, String gurantor4Name,
			String gurantor4Phone, String ref1Addrs, String ref1Contact, String ref2Addrs, String ref2Contact,
			String reference1Name, String reference2Name, String centerName, String accOpenDate, String nextDueDate,
			String lastPayDate, String bussinessName, String bussinessAddressLine, String bussinessAddressCity,
			String bussinessAddressState, String bussinessAddressLandmark, String bussinessAddressZipcode,
			String bussinessAddressLat, String bussinessAddressLong, String bussinessMobile, String bussinessPhone1,
			String collectorID, String status, LocalDate currentDate, LocalDate updatedDate) {
		super();
		this.id = id;
		this.aggrementID = aggrementID;
		this.customerID = customerID;
		this.customerName = customerName;
		this.productGroup = productGroup;
		this.product = product;
		this.subProduct = subProduct;
		this.country = country;
		this.zone = zone;
		this.region = region;
		this.state = state;
		this.city = city;
		this.district = district;
		this.branch = branch;
		this.branchCode = branchCode;
		this.currentDPD = currentDPD;
		this.currentINT = currentINT;
		this.currentPOS = currentPOS;
		this.disbursalDate = disbursalDate;
		this.disbursedAmount = disbursedAmount;
		this.eMIAmount = eMIAmount;
		this.bOMPOS = bOMPOS;
		this.eMIODAmount = eMIODAmount;
		this.interestOD = interestOD;
		this.interestPaid = interestPaid;
		this.loanStatus = loanStatus;
		this.penalPending = penalPending;
		this.principalOD = principalOD;
		this.totalAddress = totalAddress;
		this.totalForcelosure = totalForcelosure;
		this.totalOutstanding = totalOutstanding;
		this.otherChanges = otherChanges;
		this.foreclosePrepayCharge = foreclosePrepayCharge;
		this.tenureMonth = tenureMonth;
		this.tenureDays = tenureDays;
		this.mailingAddressLine = mailingAddressLine;
		this.mailingLandmark = mailingLandmark;
		this.mailingMobile = mailingMobile;
		this.mailingPhone1 = mailingPhone1;
		this.mailingPhone2 = mailingPhone2;
		this.mailingZipcode = mailingZipcode;
		this.mailingAddLat = mailingAddLat;
		this.mailingAddLong = mailingAddLong;
		this.noMailingAddressLine = noMailingAddressLine;
		this.noMailingCity = noMailingCity;
		this.noMailingLandmark = noMailingLandmark;
		this.noMailingMobile = noMailingMobile;
		this.noMailingPhone1 = noMailingPhone1;
		this.noMailingPhone2 = noMailingPhone2;
		this.noMailingState = noMailingState;
		this.noMailingZipcode = noMailingZipcode;
		this.noMailingAddLat = noMailingAddLat;
		this.noMailingAddLong = noMailingAddLong;
		this.chasisnum = chasisnum;
		this.engineum = engineum;
		this.levelDesc = levelDesc;
		this.make = make;
		this.manufactureDesc = manufactureDesc;
		this.modelid = modelid;
		this.unitDesc = unitDesc;
		this.year = year;
		this.coApplicant1ID = coApplicant1ID;
		this.coApplicant1Name = coApplicant1Name;
		this.coApplicant1Phone = coApplicant1Phone;
		this.coApplicant2ID = coApplicant2ID;
		this.coApplicant2Name = coApplicant2Name;
		this.coApplicant2Phone = coApplicant2Phone;
		this.coApplicant3ID = coApplicant3ID;
		this.coApplicant3Name = coApplicant3Name;
		this.coApplicant3Phone = coApplicant3Phone;
		this.coApplicant4ID = coApplicant4ID;
		this.coApplicant14Name = coApplicant14Name;
		this.coApplicant4Phone = coApplicant4Phone;
		this.coApplicant5ID = coApplicant5ID;
		this.coApplicant5Name = coApplicant5Name;
		this.coApplicant5Phone = coApplicant5Phone;
		gurantor1ID = gurantor1id;
		this.gurantor1Name = gurantor1Name;
		this.gurantor1Phone = gurantor1Phone;
		gurantor2ID = gurantor2id;
		this.gurantor2Name = gurantor2Name;
		this.gurantor2Phone = gurantor2Phone;
		gurantor3ID = gurantor3id;
		this.gurantor3Name = gurantor3Name;
		this.gurantor3Phone = gurantor3Phone;
		gurantor4ID = gurantor4id;
		this.gurantor4Name = gurantor4Name;
		this.gurantor4Phone = gurantor4Phone;
		this.ref1Addrs = ref1Addrs;
		this.ref1Contact = ref1Contact;
		this.ref2Addrs = ref2Addrs;
		this.ref2Contact = ref2Contact;
		this.reference1Name = reference1Name;
		this.reference2Name = reference2Name;
		this.centerName = centerName;
		this.accOpenDate = accOpenDate;
		this.nextDueDate = nextDueDate;
		LastPayDate = lastPayDate;
		this.bussinessName = bussinessName;
		this.bussinessAddressLine = bussinessAddressLine;
		this.bussinessAddressCity = bussinessAddressCity;
		this.bussinessAddressState = bussinessAddressState;
		this.bussinessAddressLandmark = bussinessAddressLandmark;
		this.bussinessAddressZipcode = bussinessAddressZipcode;
		this.bussinessAddressLat = bussinessAddressLat;
		this.bussinessAddressLong = bussinessAddressLong;
		this.bussinessMobile = bussinessMobile;
		this.bussinessPhone1 = bussinessPhone1;
		this.collectorID = collectorID;
		this.status = status;
		this.currentDate = currentDate;
		this.updatedDate = updatedDate;
	}

	@Override
	public String toString() {
		return "CSVModel [id=" + id + ", aggrementID=" + aggrementID + ", customerID=" + customerID + ", customerName="
				+ customerName + ", productGroup=" + productGroup + ", product=" + product + ", subProduct="
				+ subProduct + ", country=" + country + ", zone=" + zone + ", region=" + region + ", state=" + state
				+ ", city=" + city + ", district=" + district + ", branch=" + branch + ", branchCode=" + branchCode
				+ ", currentDPD=" + currentDPD + ", currentINT=" + currentINT + ", currentPOS=" + currentPOS
				+ ", disbursalDate=" + disbursalDate + ", disbursedAmount=" + disbursedAmount + ", eMIAmount="
				+ eMIAmount + ", bOMPOS=" + bOMPOS + ", eMIODAmount=" + eMIODAmount + ", interestOD=" + interestOD
				+ ", interestPaid=" + interestPaid + ", loanStatus=" + loanStatus + ", penalPending=" + penalPending
				+ ", principalOD=" + principalOD + ", totalAddress=" + totalAddress + ", totalForcelosure="
				+ totalForcelosure + ", totalOutstanding=" + totalOutstanding + ", otherChanges=" + otherChanges
				+ ", foreclosePrepayCharge=" + foreclosePrepayCharge + ", tenureMonth=" + tenureMonth + ", tenureDays="
				+ tenureDays + ", mailingAddressLine=" + mailingAddressLine + ", mailingLandmark=" + mailingLandmark
				+ ", mailingMobile=" + mailingMobile + ", mailingPhone1=" + mailingPhone1 + ", mailingPhone2="
				+ mailingPhone2 + ", mailingZipcode=" + mailingZipcode + ", mailingAddLat=" + mailingAddLat
				+ ", mailingAddLong=" + mailingAddLong + ", noMailingAddressLine=" + noMailingAddressLine
				+ ", noMailingCity=" + noMailingCity + ", noMailingLandmark=" + noMailingLandmark + ", noMailingMobile="
				+ noMailingMobile + ", noMailingPhone1=" + noMailingPhone1 + ", noMailingPhone2=" + noMailingPhone2
				+ ", noMailingState=" + noMailingState + ", noMailingZipcode=" + noMailingZipcode + ", noMailingAddLat="
				+ noMailingAddLat + ", noMailingAddLong=" + noMailingAddLong + ", chasisnum=" + chasisnum
				+ ", engineum=" + engineum + ", levelDesc=" + levelDesc + ", make=" + make + ", manufactureDesc="
				+ manufactureDesc + ", modelid=" + modelid + ", unitDesc=" + unitDesc + ", year=" + year
				+ ", coApplicant1ID=" + coApplicant1ID + ", coApplicant1Name=" + coApplicant1Name
				+ ", coApplicant1Phone=" + coApplicant1Phone + ", coApplicant2ID=" + coApplicant2ID
				+ ", coApplicant2Name=" + coApplicant2Name + ", coApplicant2Phone=" + coApplicant2Phone
				+ ", coApplicant3ID=" + coApplicant3ID + ", coApplicant3Name=" + coApplicant3Name
				+ ", coApplicant3Phone=" + coApplicant3Phone + ", coApplicant4ID=" + coApplicant4ID
				+ ", coApplicant14Name=" + coApplicant14Name + ", coApplicant4Phone=" + coApplicant4Phone
				+ ", coApplicant5ID=" + coApplicant5ID + ", coApplicant5Name=" + coApplicant5Name
				+ ", coApplicant5Phone=" + coApplicant5Phone + ", gurantor1ID=" + gurantor1ID + ", gurantor1Name="
				+ gurantor1Name + ", gurantor1Phone=" + gurantor1Phone + ", gurantor2ID=" + gurantor2ID
				+ ", gurantor2Name=" + gurantor2Name + ", gurantor2Phone=" + gurantor2Phone + ", gurantor3ID="
				+ gurantor3ID + ", gurantor3Name=" + gurantor3Name + ", gurantor3Phone=" + gurantor3Phone
				+ ", gurantor4ID=" + gurantor4ID + ", gurantor4Name=" + gurantor4Name + ", gurantor4Phone="
				+ gurantor4Phone + ", ref1Addrs=" + ref1Addrs + ", ref1Contact=" + ref1Contact + ", ref2Addrs="
				+ ref2Addrs + ", ref2Contact=" + ref2Contact + ", reference1Name=" + reference1Name
				+ ", reference2Name=" + reference2Name + ", centerName=" + centerName + ", accOpenDate=" + accOpenDate
				+ ", nextDueDate=" + nextDueDate + ", LastPayDate=" + LastPayDate + ", bussinessName=" + bussinessName
				+ ", bussinessAddressLine=" + bussinessAddressLine + ", bussinessAddressCity=" + bussinessAddressCity
				+ ", bussinessAddressState=" + bussinessAddressState + ", bussinessAddressLandmark="
				+ bussinessAddressLandmark + ", bussinessAddressZipcode=" + bussinessAddressZipcode
				+ ", bussinessAddressLat=" + bussinessAddressLat + ", bussinessAddressLong=" + bussinessAddressLong
				+ ", bussinessMobile=" + bussinessMobile + ", bussinessPhone1=" + bussinessPhone1 + ", collectorID="
				+ collectorID + ", status=" + status + ", currentDate=" + currentDate + ", updatedDate=" + updatedDate
				+ "]";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAggrementID() {
		return aggrementID;
	}

	public void setAggrementID(String aggrementID) {
		this.aggrementID = aggrementID;
	}

	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getProductGroup() {
		return productGroup;
	}

	public void setProductGroup(String productGroup) {
		this.productGroup = productGroup;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getSubProduct() {
		return subProduct;
	}

	public void setSubProduct(String subProduct) {
		this.subProduct = subProduct;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getCurrentDPD() {
		return currentDPD;
	}

	public void setCurrentDPD(String currentDPD) {
		this.currentDPD = currentDPD;
	}

	public String getCurrentINT() {
		return currentINT;
	}

	public void setCurrentINT(String currentINT) {
		this.currentINT = currentINT;
	}

	public String getCurrentPOS() {
		return currentPOS;
	}

	public void setCurrentPOS(String currentPOS) {
		this.currentPOS = currentPOS;
	}

	public String getDisbursalDate() {
		return disbursalDate;
	}

	public void setDisbursalDate(String disbursalDate) {
		this.disbursalDate = disbursalDate;
	}

	public String getDisbursedAmount() {
		return disbursedAmount;
	}

	public void setDisbursedAmount(String disbursedAmount) {
		this.disbursedAmount = disbursedAmount;
	}

	public String geteMIAmount() {
		return eMIAmount;
	}

	public void seteMIAmount(String eMIAmount) {
		this.eMIAmount = eMIAmount;
	}

	public String getbOMPOS() {
		return bOMPOS;
	}

	public void setbOMPOS(String bOMPOS) {
		this.bOMPOS = bOMPOS;
	}

	public String geteMIODAmount() {
		return eMIODAmount;
	}

	public void seteMIODAmount(String eMIODAmount) {
		this.eMIODAmount = eMIODAmount;
	}

	public String getInterestOD() {
		return interestOD;
	}

	public void setInterestOD(String interestOD) {
		this.interestOD = interestOD;
	}

	public String getInterestPaid() {
		return interestPaid;
	}

	public void setInterestPaid(String interestPaid) {
		this.interestPaid = interestPaid;
	}

	public String getLoanStatus() {
		return loanStatus;
	}

	public void setLoanStatus(String loanStatus) {
		this.loanStatus = loanStatus;
	}

	public String getPenalPending() {
		return penalPending;
	}

	public void setPenalPending(String penalPending) {
		this.penalPending = penalPending;
	}

	public String getPrincipalOD() {
		return principalOD;
	}

	public void setPrincipalOD(String principalOD) {
		this.principalOD = principalOD;
	}

	public String getTotalAddress() {
		return totalAddress;
	}

	public void setTotalAddress(String totalAddress) {
		this.totalAddress = totalAddress;
	}

	public String getTotalForcelosure() {
		return totalForcelosure;
	}

	public void setTotalForcelosure(String totalForcelosure) {
		this.totalForcelosure = totalForcelosure;
	}

	public String getTotalOutstanding() {
		return totalOutstanding;
	}

	public void setTotalOutstanding(String totalOutstanding) {
		this.totalOutstanding = totalOutstanding;
	}

	public String getOtherChanges() {
		return otherChanges;
	}

	public void setOtherChanges(String otherChanges) {
		this.otherChanges = otherChanges;
	}

	public String getForeclosePrepayCharge() {
		return foreclosePrepayCharge;
	}

	public void setForeclosePrepayCharge(String foreclosePrepayCharge) {
		this.foreclosePrepayCharge = foreclosePrepayCharge;
	}

	public String getTenureMonth() {
		return tenureMonth;
	}

	public void setTenureMonth(String tenureMonth) {
		this.tenureMonth = tenureMonth;
	}

	public String getTenureDays() {
		return tenureDays;
	}

	public void setTenureDays(String tenureDays) {
		this.tenureDays = tenureDays;
	}

	public String getMailingAddressLine() {
		return mailingAddressLine;
	}

	public void setMailingAddressLine(String mailingAddressLine) {
		this.mailingAddressLine = mailingAddressLine;
	}

	public String getMailingLandmark() {
		return mailingLandmark;
	}

	public void setMailingLandmark(String mailingLandmark) {
		this.mailingLandmark = mailingLandmark;
	}

	public String getMailingMobile() {
		return mailingMobile;
	}

	public void setMailingMobile(String mailingMobile) {
		this.mailingMobile = mailingMobile;
	}

	public String getMailingPhone1() {
		return mailingPhone1;
	}

	public void setMailingPhone1(String mailingPhone1) {
		this.mailingPhone1 = mailingPhone1;
	}

	public String getMailingPhone2() {
		return mailingPhone2;
	}

	public void setMailingPhone2(String mailingPhone2) {
		this.mailingPhone2 = mailingPhone2;
	}

	public String getMailingZipcode() {
		return mailingZipcode;
	}

	public void setMailingZipcode(String mailingZipcode) {
		this.mailingZipcode = mailingZipcode;
	}

	public String getMailingAddLat() {
		return mailingAddLat;
	}

	public void setMailingAddLat(String mailingAddLat) {
		this.mailingAddLat = mailingAddLat;
	}

	public String getMailingAddLong() {
		return mailingAddLong;
	}

	public void setMailingAddLong(String mailingAddLong) {
		this.mailingAddLong = mailingAddLong;
	}

	public String getNoMailingAddressLine() {
		return noMailingAddressLine;
	}

	public void setNoMailingAddressLine(String noMailingAddressLine) {
		this.noMailingAddressLine = noMailingAddressLine;
	}

	public String getNoMailingCity() {
		return noMailingCity;
	}

	public void setNoMailingCity(String noMailingCity) {
		this.noMailingCity = noMailingCity;
	}

	public String getNoMailingLandmark() {
		return noMailingLandmark;
	}

	public void setNoMailingLandmark(String noMailingLandmark) {
		this.noMailingLandmark = noMailingLandmark;
	}

	public String getNoMailingMobile() {
		return noMailingMobile;
	}

	public void setNoMailingMobile(String noMailingMobile) {
		this.noMailingMobile = noMailingMobile;
	}

	public String getNoMailingPhone1() {
		return noMailingPhone1;
	}

	public void setNoMailingPhone1(String noMailingPhone1) {
		this.noMailingPhone1 = noMailingPhone1;
	}

	public String getNoMailingPhone2() {
		return noMailingPhone2;
	}

	public void setNoMailingPhone2(String noMailingPhone2) {
		this.noMailingPhone2 = noMailingPhone2;
	}

	public String getNoMailingState() {
		return noMailingState;
	}

	public void setNoMailingState(String noMailingState) {
		this.noMailingState = noMailingState;
	}

	public String getNoMailingZipcode() {
		return noMailingZipcode;
	}

	public void setNoMailingZipcode(String noMailingZipcode) {
		this.noMailingZipcode = noMailingZipcode;
	}

	public String getNoMailingAddLat() {
		return noMailingAddLat;
	}

	public void setNoMailingAddLat(String noMailingAddLat) {
		this.noMailingAddLat = noMailingAddLat;
	}

	public String getNoMailingAddLong() {
		return noMailingAddLong;
	}

	public void setNoMailingAddLong(String noMailingAddLong) {
		this.noMailingAddLong = noMailingAddLong;
	}

	public String getChasisnum() {
		return chasisnum;
	}

	public void setChasisnum(String chasisnum) {
		this.chasisnum = chasisnum;
	}

	public String getEngineum() {
		return engineum;
	}

	public void setEngineum(String engineum) {
		this.engineum = engineum;
	}

	public String getLevelDesc() {
		return levelDesc;
	}

	public void setLevelDesc(String levelDesc) {
		this.levelDesc = levelDesc;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getManufactureDesc() {
		return manufactureDesc;
	}

	public void setManufactureDesc(String manufactureDesc) {
		this.manufactureDesc = manufactureDesc;
	}

	public String getModelid() {
		return modelid;
	}

	public void setModelid(String modelid) {
		this.modelid = modelid;
	}

	public String getUnitDesc() {
		return unitDesc;
	}

	public void setUnitDesc(String unitDesc) {
		this.unitDesc = unitDesc;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getCoApplicant1ID() {
		return coApplicant1ID;
	}

	public void setCoApplicant1ID(String coApplicant1ID) {
		this.coApplicant1ID = coApplicant1ID;
	}

	public String getCoApplicant1Name() {
		return coApplicant1Name;
	}

	public void setCoApplicant1Name(String coApplicant1Name) {
		this.coApplicant1Name = coApplicant1Name;
	}

	public String getCoApplicant1Phone() {
		return coApplicant1Phone;
	}

	public void setCoApplicant1Phone(String coApplicant1Phone) {
		this.coApplicant1Phone = coApplicant1Phone;
	}

	public String getCoApplicant2ID() {
		return coApplicant2ID;
	}

	public void setCoApplicant2ID(String coApplicant2ID) {
		this.coApplicant2ID = coApplicant2ID;
	}

	public String getCoApplicant2Name() {
		return coApplicant2Name;
	}

	public void setCoApplicant2Name(String coApplicant2Name) {
		this.coApplicant2Name = coApplicant2Name;
	}

	public String getCoApplicant2Phone() {
		return coApplicant2Phone;
	}

	public void setCoApplicant2Phone(String coApplicant2Phone) {
		this.coApplicant2Phone = coApplicant2Phone;
	}

	public String getCoApplicant3ID() {
		return coApplicant3ID;
	}

	public void setCoApplicant3ID(String coApplicant3ID) {
		this.coApplicant3ID = coApplicant3ID;
	}

	public String getCoApplicant3Name() {
		return coApplicant3Name;
	}

	public void setCoApplicant3Name(String coApplicant3Name) {
		this.coApplicant3Name = coApplicant3Name;
	}

	public String getCoApplicant3Phone() {
		return coApplicant3Phone;
	}

	public void setCoApplicant3Phone(String coApplicant3Phone) {
		this.coApplicant3Phone = coApplicant3Phone;
	}

	public String getCoApplicant4ID() {
		return coApplicant4ID;
	}

	public void setCoApplicant4ID(String coApplicant4ID) {
		this.coApplicant4ID = coApplicant4ID;
	}

	public String getCoApplicant14Name() {
		return coApplicant14Name;
	}

	public void setCoApplicant14Name(String coApplicant14Name) {
		this.coApplicant14Name = coApplicant14Name;
	}

	public String getCoApplicant4Phone() {
		return coApplicant4Phone;
	}

	public void setCoApplicant4Phone(String coApplicant4Phone) {
		this.coApplicant4Phone = coApplicant4Phone;
	}

	public String getCoApplicant5ID() {
		return coApplicant5ID;
	}

	public void setCoApplicant5ID(String coApplicant5ID) {
		this.coApplicant5ID = coApplicant5ID;
	}

	public String getCoApplicant5Name() {
		return coApplicant5Name;
	}

	public void setCoApplicant5Name(String coApplicant5Name) {
		this.coApplicant5Name = coApplicant5Name;
	}

	public String getCoApplicant5Phone() {
		return coApplicant5Phone;
	}

	public void setCoApplicant5Phone(String coApplicant5Phone) {
		this.coApplicant5Phone = coApplicant5Phone;
	}

	public String getGurantor1ID() {
		return gurantor1ID;
	}

	public void setGurantor1ID(String gurantor1id) {
		gurantor1ID = gurantor1id;
	}

	public String getGurantor1Name() {
		return gurantor1Name;
	}

	public void setGurantor1Name(String gurantor1Name) {
		this.gurantor1Name = gurantor1Name;
	}

	public String getGurantor1Phone() {
		return gurantor1Phone;
	}

	public void setGurantor1Phone(String gurantor1Phone) {
		this.gurantor1Phone = gurantor1Phone;
	}

	public String getGurantor2ID() {
		return gurantor2ID;
	}

	public void setGurantor2ID(String gurantor2id) {
		gurantor2ID = gurantor2id;
	}

	public String getGurantor2Name() {
		return gurantor2Name;
	}

	public void setGurantor2Name(String gurantor2Name) {
		this.gurantor2Name = gurantor2Name;
	}

	public String getGurantor2Phone() {
		return gurantor2Phone;
	}

	public void setGurantor2Phone(String gurantor2Phone) {
		this.gurantor2Phone = gurantor2Phone;
	}

	public String getGurantor3ID() {
		return gurantor3ID;
	}

	public void setGurantor3ID(String gurantor3id) {
		gurantor3ID = gurantor3id;
	}

	public String getGurantor3Name() {
		return gurantor3Name;
	}

	public void setGurantor3Name(String gurantor3Name) {
		this.gurantor3Name = gurantor3Name;
	}

	public String getGurantor3Phone() {
		return gurantor3Phone;
	}

	public void setGurantor3Phone(String gurantor3Phone) {
		this.gurantor3Phone = gurantor3Phone;
	}

	public String getGurantor4ID() {
		return gurantor4ID;
	}

	public void setGurantor4ID(String gurantor4id) {
		gurantor4ID = gurantor4id;
	}

	public String getGurantor4Name() {
		return gurantor4Name;
	}

	public void setGurantor4Name(String gurantor4Name) {
		this.gurantor4Name = gurantor4Name;
	}

	public String getGurantor4Phone() {
		return gurantor4Phone;
	}

	public void setGurantor4Phone(String gurantor4Phone) {
		this.gurantor4Phone = gurantor4Phone;
	}

	public String getRef1Addrs() {
		return ref1Addrs;
	}

	public void setRef1Addrs(String ref1Addrs) {
		this.ref1Addrs = ref1Addrs;
	}

	public String getRef1Contact() {
		return ref1Contact;
	}

	public void setRef1Contact(String ref1Contact) {
		this.ref1Contact = ref1Contact;
	}

	public String getRef2Addrs() {
		return ref2Addrs;
	}

	public void setRef2Addrs(String ref2Addrs) {
		this.ref2Addrs = ref2Addrs;
	}

	public String getRef2Contact() {
		return ref2Contact;
	}

	public void setRef2Contact(String ref2Contact) {
		this.ref2Contact = ref2Contact;
	}

	public String getReference1Name() {
		return reference1Name;
	}

	public void setReference1Name(String reference1Name) {
		this.reference1Name = reference1Name;
	}

	public String getReference2Name() {
		return reference2Name;
	}

	public void setReference2Name(String reference2Name) {
		this.reference2Name = reference2Name;
	}

	public String getCenterName() {
		return centerName;
	}

	public void setCenterName(String centerName) {
		this.centerName = centerName;
	}

	public String getAccOpenDate() {
		return accOpenDate;
	}

	public void setAccOpenDate(String accOpenDate) {
		this.accOpenDate = accOpenDate;
	}

	public String getNextDueDate() {
		return nextDueDate;
	}

	public void setNextDueDate(String nextDueDate) {
		this.nextDueDate = nextDueDate;
	}

	public String getLastPayDate() {
		return LastPayDate;
	}

	public void setLastPayDate(String lastPayDate) {
		LastPayDate = lastPayDate;
	}

	public String getBussinessName() {
		return bussinessName;
	}

	public void setBussinessName(String bussinessName) {
		this.bussinessName = bussinessName;
	}

	public String getBussinessAddressLine() {
		return bussinessAddressLine;
	}

	public void setBussinessAddressLine(String bussinessAddressLine) {
		this.bussinessAddressLine = bussinessAddressLine;
	}

	public String getBussinessAddressCity() {
		return bussinessAddressCity;
	}

	public void setBussinessAddressCity(String bussinessAddressCity) {
		this.bussinessAddressCity = bussinessAddressCity;
	}

	public String getBussinessAddressState() {
		return bussinessAddressState;
	}

	public void setBussinessAddressState(String bussinessAddressState) {
		this.bussinessAddressState = bussinessAddressState;
	}

	public String getBussinessAddressLandmark() {
		return bussinessAddressLandmark;
	}

	public void setBussinessAddressLandmark(String bussinessAddressLandmark) {
		this.bussinessAddressLandmark = bussinessAddressLandmark;
	}

	public String getBussinessAddressZipcode() {
		return bussinessAddressZipcode;
	}

	public void setBussinessAddressZipcode(String bussinessAddressZipcode) {
		this.bussinessAddressZipcode = bussinessAddressZipcode;
	}

	public String getBussinessAddressLat() {
		return bussinessAddressLat;
	}

	public void setBussinessAddressLat(String bussinessAddressLat) {
		this.bussinessAddressLat = bussinessAddressLat;
	}

	public String getBussinessAddressLong() {
		return bussinessAddressLong;
	}

	public void setBussinessAddressLong(String bussinessAddressLong) {
		this.bussinessAddressLong = bussinessAddressLong;
	}

	public String getBussinessMobile() {
		return bussinessMobile;
	}

	public void setBussinessMobile(String bussinessMobile) {
		this.bussinessMobile = bussinessMobile;
	}

	public String getBussinessPhone1() {
		return bussinessPhone1;
	}

	public void setBussinessPhone1(String bussinessPhone1) {
		this.bussinessPhone1 = bussinessPhone1;
	}

	public String getCollectorID() {
		return collectorID;
	}

	public void setCollectorID(String collectorID) {
		this.collectorID = collectorID;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDate getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(LocalDate currentDate) {
		this.currentDate = currentDate;
	}

	public LocalDate getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(LocalDate updatedDate) {
		this.updatedDate = updatedDate;
	}
}
