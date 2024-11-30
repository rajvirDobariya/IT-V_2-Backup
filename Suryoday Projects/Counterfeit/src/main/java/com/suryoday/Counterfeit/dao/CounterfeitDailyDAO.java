package com.suryoday.Counterfeit.dao;

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
import com.suryoday.Counterfeit.Utils.DateUtils;
import com.suryoday.Counterfeit.enums.UserRole;

@Repository
@Transactional(rollbackOn = Exception.class)
public class CounterfeitDailyDAO {

	Logger LOG = LoggerFactory.getLogger(CounterfeitDailyDAO.class);

	@Autowired
	private EntityManager entityManager;

	public List<Counterfeit> findCounterfeitsByFilter(JSONObject requestJson) {

		// Validate Role
		String role = requestJson.optString("role");
		if (role == null || role.isEmpty()) {
			LOG.debug("role is null or empty :: {}", role);
			throw new CounterfeitException("role is null or empty!");
		}
		// Validate Branch Code
		String branchCode = requestJson.optString("branchCode", null);
		if (role.equals(UserRole.COUNTERFEIT_MAKER.getRole()) || role.equals(UserRole.COUNTERFEIT_CHECKER.getRole())) {
			if (branchCode == null || branchCode.isEmpty()) {
				LOG.debug("branchCode is null or empty :: {}", role);
				throw new CounterfeitException("branchCode is null or empty!" + branchCode);
			}
		}

		// Make Query
		StringBuilder jpql = new StringBuilder("SELECT c FROM Counterfeit c");
		TypedQuery<Counterfeit> query = null;
		boolean isTop10 = true;
		jpql.append(" WHERE dailyMonthly = 'Daily'");

		// Status
		List<String> statuses = new ArrayList<>();
		jpql.append(" AND status IN (:statuses)");

		// Branch Code
		if (branchCode != null) {
			jpql.append(" AND branchCode =:branchCode");
		}

		String dateString = requestJson.optString("date", null);
		String fromDateString = requestJson.optString("fromDate", null);
		String toDateString = requestJson.optString("toDate", null);

		// DATE
		if (dateString != null) {
			isTop10 = false;
			jpql.append(" AND createdDate BETWEEN :fromDate AND :toDate");

		} else if (fromDateString != null && toDateString != null) {
			isTop10 = false;
			jpql.append(" AND createdDate BETWEEN :fromDate AND :toDate");
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
		if (entityManager != null) {
			String jpqlString = jpql.toString() + " ORDER BY c.id DESC";
			LOG.debug("QUERY :" + jpqlString);
			query = entityManager.createQuery(jpqlString, Counterfeit.class);
		}

		// Status
		if (role.equals(UserRole.COUNTERFEIT_MAKER.getRole())) {
			statuses = Arrays.asList("Pending at Checker", "Pending at Maker", "Submitted");
			query.setParameter("statuses", statuses);
		} else if (role.equals(UserRole.COUNTERFEIT_CHECKER.getRole())) {
			statuses = Arrays.asList("Pending at Checker", "Submitted");
			query.setParameter("statuses", statuses);
		} else if (role.equals(UserRole.COUNTERFEIT_HO.getRole())) {
			statuses = Arrays.asList("Submitted");
			query.setParameter("statuses", statuses);
		} else {
			throw new CounterfeitException(
					"Your role is " + role + " and You have not autCREDIT_OPSrity to get counterfeit's!");
		}

		// Branch Code
		if (branchCode != null) {
			query.setParameter("branchCode", branchCode);
		}

		// Date String
		if (dateString != null) {
			query.setParameter("fromDate", DateUtils.getStartOfDay(dateString));
			query.setParameter("toDate", DateUtils.getEndOfDay(dateString));

		} else if (fromDateString != null && toDateString != null) {
			query.setParameter("fromDate", DateUtils.getStartOfDay(fromDateString));
			query.setParameter("toDate", DateUtils.getEndOfDay(toDateString));
		}

		// Set Top 10
		if (isTop10) {
			query.setMaxResults(10);
		}
		return query.getResultList();
	}
}