package com.suryoday.Counterfeit.dao;

import java.time.YearMonth;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.suryoday.Counterfeit.Exception.DenominationException;
import com.suryoday.Counterfeit.Pojo.Counterfeit;
import com.suryoday.Counterfeit.Utils.DateUtils;

@Repository
@Transactional(rollbackOn = Exception.class)
public class DenominationDAO {

	@Autowired
	private EntityManager entityManager;

	public List<Object[]> findDenominationByFilter(JSONObject requestJson, Counterfeit counterfeit) {

		// MAKE QUERY
		StringBuilder jpql = new StringBuilder();

		// Denomination
		String denominationPrefix = "SELECT d.id, d.createdDate, d.detectDate, d.denominationNote, d.tendererAccountNumber, d.tendererCustomerName, d.serialNumber, d.securityFeatureBreached,"
				+ " c.id, c.dailyMonthly, c.branchCode, c.branchName"
				+ " FROM Counterfeit c INNER JOIN Denomination d ON c.id = d.counterfeitId";
		String denominationSuffix = " ORDER BY c.dailyMonthly, d.id";

		TypedQuery<Object[]> denominationQuery = null;
		jpql.append(" WHERE");

		// Branch Code
		jpql.append(" AND c.branchCode =:branchCode");

		// Date Type option : ["DATE", "MONTHLY", "COUNTERFEIT"]
		String dateType = requestJson.optString("dateType", null);
		String monthString = requestJson.optString("month");
		String branchCode = requestJson.optString("branchCode");
		YearMonth month = null;

		if (counterfeit == null) {
			dateType = "MONTHLY";
			jpql.append(" AND c.status ='Submitted'");
			// validate month and branchCode
			if (monthString == null || monthString.isEmpty() || branchCode == null || branchCode.isEmpty()) {
				throw new DenominationException("month is :" + monthString + " and branchCode is " + branchCode + "!");
			}
			month = DateUtils.convertMonthStringIntoYearMonth(monthString);

		} else {
			if (counterfeit.getDailyMonthly().equals("Monthly")) {
				jpql.append(" AND c.status ='Submitted'");
				dateType = "MONTHLY";
				month = counterfeit.getMonth();
			}
		}
		if (dateType != null) {
			switch (dateType) {
			case "COUNTERFEIT": {
				jpql.append(" AND c.id =:counterfeitId");
				break;
			}
			case "MONTHLY": {
				jpql.append(" AND c.createdDate BETWEEN :fromDate AND :toDate");
				break;
			}
			}
		}

		// Remove WHERE AND
		int whereAndIndex = jpql.indexOf("WHERE AND");
		if (whereAndIndex != -1) {
			jpql.replace(whereAndIndex, whereAndIndex + 9, "WHERE");
		} else if (jpql.toString().trim().endsWith("WHERE")) {
			// Remove the "WHERE" clause
			int index = jpql.lastIndexOf("WHERE");
			jpql.delete(index, jpql.length());
		}

		// CREATE QUERY
		if (entityManager != null) {
			String jpqlString = denominationPrefix + jpql.toString() + denominationSuffix;
			denominationQuery = entityManager.createQuery(jpqlString, Object[].class);
		}

		// SET PARAMETER'S

		// Branch Code
		if (counterfeit == null) {
			denominationQuery.setParameter("branchCode", branchCode);
		} else {
			denominationQuery.setParameter("branchCode", counterfeit.getBranchCode());
		}

		// Date Type
		if (dateType != null) {
			switch (dateType) {
			case "COUNTERFEIT": {
				denominationQuery.setParameter("counterfeitId", counterfeit.getId());
				break;
			}
			case "MONTHLY": {
				Date startDateOfMonth = DateUtils.getStartDateOfMonth(month);
				Date endDateOfMonth = DateUtils.getEndDateOfMonth(month);
				denominationQuery.setParameter("fromDate", startDateOfMonth);
				denominationQuery.setParameter("toDate", endDateOfMonth);
				break;
			}
			}
		}

		return denominationQuery.getResultList();
	}

}