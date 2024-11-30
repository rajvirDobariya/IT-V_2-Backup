package com.suryoday.Counterfeit.dao;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.suryoday.Counterfeit.Exception.CounterfeitException;
import com.suryoday.Counterfeit.Pojo.Counterfeit;
import com.suryoday.Counterfeit.Utils.Constants;
import com.suryoday.Counterfeit.enums.UserRole;

@Repository
@Transactional(rollbackOn = Exception.class)
public class CounterfeitMonthlyDAO {

	Logger LOG = LoggerFactory.getLogger(CounterfeitMonthlyDAO.class);
	@Autowired
	private EntityManager entityManager;

	public List<Counterfeit> findCounterfeitsByFilter(JSONObject requestJson) {

		// Validate Role
		String role = requestJson.optString("role");

		// Validate Branch Code
		String branchCode = requestJson.optString("branchCode", null);
		if (role.equals(UserRole.COUNTERFEIT_MAKER.getRole()) || role.equals(UserRole.COUNTERFEIT_CHECKER.getRole())) {
			if (branchCode == null || branchCode.isEmpty()) {
				LOG.debug("Branchcode is null or empty :: {}", role);
				throw new CounterfeitException("Branchcode is null or empty!" + branchCode);
			}
		}

		// Make Query
		StringBuilder jpql = new StringBuilder("SELECT c FROM Counterfeit c");
		TypedQuery<Counterfeit> query = null;
		boolean isTop10 = true;
		jpql.append(" WHERE dailyMonthly = 'Monthly'");

		// Status
		List<String> statuses = new ArrayList<>();
		jpql.append(" AND status IN (:statuses)");

		// Branch Code
		if (branchCode != null) {
			jpql.append(" AND branchCode =:branchCode");
		}

		// Date Type
		String monthString = requestJson.optString("month", null);
		YearMonth month = null;
		if (monthString != null) {
			isTop10 = false;
			// Check if MONTH matches the year-month format
			if (!monthString.matches(Constants.YEAR_MONTH_PATTERN)) {
				throw new CounterfeitException("Month must be in 'yyyy-MM' format.");
			}
			month = YearMonth.parse(monthString, DateTimeFormatter.ofPattern("yyyy-MM"));
			jpql.append(" AND month =:month");
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

		// Create Query
		String jpqlString = jpql.toString() + " ORDER BY c.id DESC";
		query = entityManager.createQuery(jpqlString, Counterfeit.class);

		// Set Parameter

		// Status
		if (role.equals(UserRole.COUNTERFEIT_MAKER.getRole())) {
			statuses = Arrays.asList("Pending at Checker", "Pending at Maker", "Submitted");
			query.setParameter("statuses", statuses);
		} else if (role.equals(UserRole.COUNTERFEIT_CHECKER.getRole())) {
			statuses = Arrays.asList("Pending at Checker", "Submitted");
			query.setParameter("statuses", statuses);
		} else if (role.equals(UserRole.COUNTERFEIT_HO.getRole())) {
			statuses = Arrays.asList("Pending", "Pending at Checker", "Pending at Maker", "Submitted");
			query.setParameter("statuses", statuses);
		} else {
			throw new CounterfeitException(
					"Your role is " + role + " and You have not CREDIT_OPSrity to get counterfeit's!");
		}

		// Branch Code
		if (branchCode != null) {
			query.setParameter("branchCode", branchCode);
		}

		// Date Type
		if (month != null) {
			query.setParameter("month", month);
		}
		// Set Top 10
		if (isTop10) {
			query.setMaxResults(10);
		}
		return query.getResultList();
	}
}