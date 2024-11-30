package com.digitisation.branchreports.repository;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.digitisation.branchreports.model.BranchUserMakerModel;

@Repository
@Transactional
public interface BranchUserMakerRepository extends JpaRepository<BranchUserMakerModel, Long> {

	@Query(value = "select SESSION_ID from SPRING_SESSION where SESSION_ID =:sessionid", nativeQuery = true)
	public String getAllSessionIds(String sessionid);

	@Transactional
	@Modifying
	@Query("UPDATE BranchUserMakerModel b SET b.status = 'Pending' WHERE b.status ='Not Submitted'")
	public void updateStatusNotsubmittedToPending();

	@Query("SELECT bum.branchcode, bum.branchname, bum.reportdate, rm.reportfrequency, COUNT(bum.id) AS reports "
			+ "FROM BranchUserMakerModel bum " + "INNER JOIN RepMaster rm ON bum.reportId = rm.reportid"
			+ " WHERE bum.status='Pending'"
			+ " GROUP BY bum.branchcode, bum.branchname, bum.reportdate, rm.reportfrequency"
			+ " ORDER BY bum.branchcode ASC, bum.reportdate ASC")
	public List<Object[]> getPendingReportsData();

	@Transactional
	@Modifying
	@Query("DELETE FROM BranchUserMakerModel bum WHERE bum.branchcode NOT IN :branchCodes")
	public void deleteOtherBranchesReports(List<String> branchCodes);

	@Transactional
	public void deleteByReportdate(LocalDate date);

	@Query("SELECT COUNT(*) FROM BranchUserMakerModel bum WHERE bum.reportdate =:date")
	public Long getCountByReportDate(LocalDate date);

	@Query(value = "SELECT COUNT(bum.id) FROM branch_d_branch_user_maker AS bum"
			+ " INNER JOIN branch_d_rep_master AS rm ON bum.report_id=rm.reportid"
			+ " WHERE rm.reportfrequency =:reportFrequency AND reportdate =:reportdate", nativeQuery = true)
	public Long getCountByReportdateAndReporeFrequency(LocalDate reportdate, String reportFrequency);

	@Modifying
	@Query("DELETE FROM BranchUserMakerModel bum WHERE bum.id IN (:ids)")
	public void deleteByIds(List<Long> ids);

	public List<BranchUserMakerModel> findByReportdate(LocalDate reportDate);

	@Query(value = "SELECT COUNT(bum.id) FROM branch_d_branch_user_maker AS bum"
			+ " INNER JOIN branch_d_rep_master AS rm ON bum.report_id=rm.reportid"
			+ " WHERE rm.reportfrequency =:reportFrequency AND reportdate =:reportdate AND branchcode =:branchCode", nativeQuery = true)
	public Long getCountByReportdateAndReporeFrequencyAndByBranchCode(LocalDate reportdate, String reportFrequency,
			String branchCode);

	@Query(value = "SELECT DISTINCT bum.branchcode, bm.branch_name FROM branch_d_branch_user_maker AS bum INNER JOIN branch_master AS bm ON bum.branchcode = bm.branch_id", nativeQuery = true)
	public List<Object[]> getTodayBranchesList();

	@Query(value = "SELECT COUNT(bum.id) FROM branch_d_branch_user_maker AS bum INNER JOIN branch_d_rep_master AS rm ON bum.report_id=rm.reportid"
			+ " WHERE rm.reportfrequency =:reportFrequency AND bum.reportdate =:reportDate AND bum.branchcode =:branchCode AND rm.reportid=:reportId", nativeQuery = true)
	public Long getCountByReportdateAndReporeFrequencyAndByBranchCodeAndByReportId(LocalDate reportDate,
			String reportFrequency, String branchCode, Long reportId);

}
