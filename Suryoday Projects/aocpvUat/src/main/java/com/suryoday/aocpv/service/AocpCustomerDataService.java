package com.suryoday.aocpv.service;

import java.time.LocalDate;
import java.util.List;

import com.suryoday.aocpv.pojo.AocpCustomer;
import com.suryoday.aocpv.pojo.CustomerWebResponse;
import com.suryoday.aocpv.pojo.FetchAllWebResponse;
import com.suryoday.aocpv.pojo.ResponseAocpCustomerData;
import com.suryoday.aocpv.pojo.ResponseAocpCustomerDataWeb;

public interface AocpCustomerDataService {

	AocpCustomer getByApplicationNo(long applicationNoInLong);

	String saveSingleData(AocpCustomer aocpCustomer);

	List<ResponseAocpCustomerData> getAllData();

	ResponseAocpCustomerData getByCustomerId(long customerIdInLong);

	List<ResponseAocpCustomerData> getByStatus(String status);

	List<FetchAllWebResponse> fetchAllByBranchIdAndStatus(String status);

	List<AocpCustomer> findByDate(LocalDate startdate, LocalDate enddate);

	ResponseAocpCustomerData getResponse(AocpCustomer aocpCustomer);

	ResponseAocpCustomerDataWeb getResponseWeb(AocpCustomer aocpCustomer, int versioncode);

	ResponseAocpCustomerData getResponseWithoutImage(AocpCustomer aocpCustomer);

	List<AocpCustomer> getAllByBranchIdAndStatus(String branchId, String status);

	ResponseAocpCustomerDataWeb fetchByApplicationNo(long applicationNoInLong, int versioncode);

	AocpCustomer fetchByApp(long applicationNoInLong);

	AocpCustomer fetchByCustNo(long customerIdInLong);

	List<AocpCustomer> getByName(String cutomesearch, String branchId);

	long statusChange(String applicationNo, String status);

	public long getByCustomerIDApplication(long customerNO);

	List<AocpCustomer> findTopTenByDate(LocalDate startdate, LocalDate enddate);

	AocpCustomer getByApp(long applicationNoInLong);

	public void createHistory(String applicationNo);

	List<AocpCustomer> fetchByApplicationNumberPDF(Long appln);

	void flowStatusChange(long applicationno);

	List<AocpCustomer> findByDateAndBranchID(LocalDate startdate, LocalDate enddate, String branchId);

	AocpCustomer getbycustomerId(long customerNo);

	List<AocpCustomer> findByDateAndStatus(LocalDate startdate, LocalDate enddate, String status);

	List<AocpCustomer> findByDateStatusAndBranchId(LocalDate startdate, LocalDate enddate, String status,
			String branchId);

	List<AocpCustomer> findByDateWeb(LocalDate startdate, LocalDate enddate);

	List<AocpCustomer> findByDateAndBranchIdWeb(LocalDate startdate, LocalDate enddate, String branchId);

	List<CustomerWebResponse> getRetriveReportData(String startDate, String endDate, String status, String state,
			String city, String area);
}
