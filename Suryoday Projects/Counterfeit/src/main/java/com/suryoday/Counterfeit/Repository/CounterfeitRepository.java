package com.suryoday.Counterfeit.Repository;

import java.time.YearMonth;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.suryoday.Counterfeit.Dto.BranchMasterDTO;
import com.suryoday.Counterfeit.Pojo.Counterfeit;

@Repository
public interface CounterfeitRepository extends JpaRepository<Counterfeit, Long> {

	@Query("SELECT COUNT(c) FROM Counterfeit c WHERE c.branchCode = :branchCode AND c.createdDate BETWEEN :fromDate AND :toDate")
	public Long findCountByBranchCodeAndToday(String branchCode, Date fromDate, Date toDate);
	
	@Query("SELECT COUNT(c) FROM Counterfeit c WHERE c.branchCode = :branchCode AND c.createdDate BETWEEN :fromDate AND :toDate AND status='Submitted'")
	public Long findCountByBranchCodeAndMonthAndStatusSubmitted(String branchCode, Date fromDate, Date toDate);

	public Counterfeit findById(long id);

	@Query(value = "SELECT c FROM Counterfeit c WHERE c.branchCode = :branchCode AND c.month = :month")
	public Counterfeit findByBranchCodeAndMonth(String branchCode, YearMonth month);

	@Query(value = "SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END"
			+ " FROM Counterfeit c WHERE c.branchCode = :branchCode AND c.month = :month AND status='Submitted'")
	public boolean existsByBranchCodeAndMonthAndStatusSubmitted(String branchCode, YearMonth month);

	public Counterfeit findByMonthAndBranchCode(YearMonth month, String branchCode);

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM counterfeit WHERE dailyMonthly =:dailyMonthly", nativeQuery = true)
	public int deleteByDailyMonthly(String dailyMonthly);

	@Query(value = "SELECT SESSION_ID FROM [suryodayaopcv].[dbo].[SPRING_SESSION] WHERE SESSION_ID = :sessionId", nativeQuery = true)
	String getAllSessionIds(@Param("sessionId") String sessionId);

	@Query(value = "SELECT [branch_id] AS branchId, [branch_name] AS branchName FROM [suryodayaopcv].[dbo].[branch_master]", nativeQuery = true)
	public List<BranchMasterDTO> findAllBranches();

	@Query(value = "SELECT [branch_name] AS branchName FROM [suryodayaopcv].[dbo].[branch_master]"
			+ " WHERE [branch_id] =:branchId", nativeQuery = true)
	public String findBranchNameByBranchId(String branchId);

	@Query("SELECT c FROM Counterfeit c WHERE c.dailyMonthly='Monthly' AND c.month =:month")
	public List<Counterfeit> findAllMonthlyCountrfeitsByMonth(YearMonth month);

	public void deleteByBranchCode(String branchCode);

	public List<Counterfeit> findByBranchCode(String branchCode);

}