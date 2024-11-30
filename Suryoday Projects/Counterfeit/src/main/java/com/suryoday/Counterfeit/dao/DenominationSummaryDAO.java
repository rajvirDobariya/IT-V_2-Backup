package com.suryoday.Counterfeit.dao;

import java.time.YearMonth;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.suryoday.Counterfeit.Exception.DenominationException;
import com.suryoday.Counterfeit.Pojo.Counterfeit;
import com.suryoday.Counterfeit.Utils.DateUtils;

@Repository
@Transactional(rollbackOn = Exception.class)
public class DenominationSummaryDAO {

	Logger LOG = LoggerFactory.getLogger(DenominationSummaryDAO.class);

	@Autowired
	private EntityManager entityManager;

	public List<Object[]> findSummryByFilter(JSONObject requestJson, Counterfeit counterfeit) {

		// MAKE QUERY
		StringBuilder jpql = new StringBuilder();

		// Denomination
		String summaryPrefix = "SELECT d.denominationNote, COUNT(d.denominationNote), SUM(d.denominationNote)"
				+ " FROM Counterfeit c INNER JOIN Denomination d ON c.id = d.counterfeitId";
		String summarySuffix = " GROUP BY d.denominationNote ORDER BY d.denominationNote DESC";

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
				dateType = "MONTHLY";
				jpql.append(" AND c.status ='Submitted'");
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
			String jpqlString = summaryPrefix + jpql.toString() + summarySuffix;
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