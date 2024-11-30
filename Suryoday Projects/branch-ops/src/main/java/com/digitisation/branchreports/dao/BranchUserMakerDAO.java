package com.digitisation.branchreports.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

//import org.apache.tomcat.util.bcel.classfile.Constant;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.digitisation.branchreports.enums.UserRole;
import com.digitisation.branchreports.exception.DigitizationException;
import com.digitisation.branchreports.utils.Constants;
import com.digitisation.branchreports.utils.DateUtils;

@Repository
@Transactional(rollbackOn = Exception.class)
public class BranchUserMakerDAO {

	Logger LOG = LoggerFactory.getLogger(BranchUserMakerDAO.class);

	@Autowired
	private EntityManager entityManager;

	public List<Object[]> findBranchUserMakerModelByFilter(JSONObject requestJson, String userRole) {

		// Make Query
		StringBuilder jpql = new StringBuilder("SELECT"
				+ " r.id, r.action, r.createdDate, r.updatedDate, r.reportdate, r.branchname, r.branchcode, r.status, r.submissiondate"
				// REPORT
				+ " ,r.reportId, rm.reportfrequency, rm.reportname"
				// RO
				+ " ,r.roId, r.roRemarks, rdoc.id, rdoc.name, rdoc.type, rdoc.base64"
				// BM
				+ " ,r.bmId, r.bmRemarks"
				// HO
				+ " ,r.hoId, r.hoRemarks"
				// AUDIT
				+ " ,r.auditorId, r.auditorRemarks, adoc.id, adoc.name, adoc.type, adoc.base64"
				+ " FROM BranchUserMakerModel r LEFT JOIN RepMaster rm ON r.reportId = rm.reportid"
				+ " LEFT JOIN Filetable rdoc ON r.roDocument=rdoc.id"
				+ " LEFT JOIN Filetable adoc ON r.auditorDocument=adoc.id" + " WHERE");
		TypedQuery<Object[]> query = null;
		boolean isTop10 = true;

		// Status
		List<String> statuses = new ArrayList();
		jpql.append(" AND r.status IN (:statuses)");

		// Branch Code
		String branchCode = requestJson.optString("branchCode");
		if (branchCode != null && !branchCode.isEmpty()) {
			jpql.append(" AND r.branchcode =:branchCode");
		}

		// FETCH_TYPE
		String fetchType = requestJson.optString("fetchType", null);
		String status = requestJson.optString("status", null);
		String dateString = requestJson.optString("date", null);
		String fromDateString = requestJson.optString("fromDate", null);
		String toDateString = requestJson.optString("toDate", null);
		String monthString = requestJson.optString("month", null);

		if (fetchType != null) {
			switch (fetchType) {
			case "DATE": {
				isTop10 = false;
				jpql.append(" AND r.reportdate :=date");
				break;
			}
			case "DATE_RANGE": {
				isTop10 = false;
				jpql.append(" AND r.reportdate BETWEEN :fromDate AND :toDate");
				break;
			}
			case "MONTH": {
				isTop10 = false;
				jpql.append(" AND r.reportdate BETWEEN :fromDate AND :toDate");
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
		String jpqlString = jpql.toString() + " ORDER BY r.id DESC";
		query = entityManager.createQuery(jpqlString, Object[].class);

		if (userRole.equals(UserRole.BRANCH_MAKER.getRole()) || userRole.equals(UserRole.BRANCH_CHECKER.getRole())) {

			// Validate Status
			if (!Constants.getReportStatusMap().containsKey(status)) {
				throw new DigitizationException("Enter valid report status!");
			}

			String statusValue = Constants.getReportStatusMap().get(status);
			statuses = Arrays.asList(statusValue);
			query.setParameter("statuses", statuses);

		} else if (userRole.equals(UserRole.BRANCH_HO.getRole())
				|| userRole.equals(UserRole.BRANCH_AUDITOR.getRole())) {
			String statusValue = Constants.getReportStatusMap().get("Approved Reports");
			statuses = Arrays.asList(statusValue);
			query.setParameter("statuses", statuses);
		}

		// Branch Code
		if (branchCode != null) {
			query.setParameter("branchCode", branchCode);
		}

		// FETCH_TYPE
		if (fetchType != null) {
			switch (fetchType) {
			case "DATE": {
				query.setParameter("date", DateUtils.getDateStringIntoLocalDate(dateString));
				break;
			}
			case "DATE_RANGE": {
				query.setParameter("fromDate", DateUtils.getDateStringIntoLocalDate(fromDateString));
				query.setParameter("toDate", DateUtils.getDateStringIntoLocalDate(toDateString));
				break;
			}
			case "MONTH": {
				query.setParameter("fromDate", DateUtils.getStartDateOfMonth(monthString));
				query.setParameter("toDate", DateUtils.getEndDateOfMonth(monthString));
				break;
			}
			}
		}

		// Set Top 10
		if (isTop10) {
			query.setMaxResults(10);
		}
		List<Object[]> resultList = query.getResultList();
		return resultList;
	}
}
