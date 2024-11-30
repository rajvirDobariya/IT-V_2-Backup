package com.suryoday.LoanTracking.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.suryoday.LoanTracking.Pojo.LoanTrackingDisbursement;
import com.suryoday.connector.pojo.User;

@Component
public interface LoanTrackingDisbursementService {

	String createApplicationNo();

	void save(LoanTrackingDisbursement disbursement, String panNo, String productType);

	List<LoanTrackingDisbursement> fetchByApplicationId(String applicationId);

	List<LoanTrackingDisbursement> fetchByDate(LocalDateTime startdate, LocalDateTime enddate, String createdBy);

	List<User> fetchByCredit(String assignrole);

	void save(LoanTrackingDisbursement disbursement);

	List<LoanTrackingDisbursement> fetchByUserRoleAndUserId(String userRole, String userId, LocalDateTime startdate,
			LocalDateTime enddate);

	List<LoanTrackingDisbursement> fetchByDateAll(LocalDateTime startdate, LocalDateTime enddate);

	List<LoanTrackingDisbursement> fetchMyTasks(String userRole, String userId, LocalDateTime startdate,
			LocalDateTime enddate);

	String getUserName(String userId);

	List<String> getAllStates();

	int countPendingRecords2(String string, String state);

	List<LoanTrackingDisbursement> fetchByDateAndUserRole(LocalDateTime startdate, LocalDateTime enddate,
			String userRole);

	LoanTrackingDisbursement getByApplicationId(long applicationNo);

	void saveData(LoanTrackingDisbursement loanTracking);

	List<String> getAllProducts();

	JSONObject writeExcel(JSONArray j);

	List<LoanTrackingDisbursement> findTopTenByDate(LocalDateTime startdate, LocalDateTime enddate);

	List<LoanTrackingDisbursement> searchByNameOrApplication(String name, long applicationId);

}
