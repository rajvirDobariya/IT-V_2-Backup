package com.suryodaybank.enduse.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.suryodaybank.enduse.model.EndUseActivity;

public interface EndUseActivityRepo extends JpaRepository<EndUseActivity, Long> {

	@Query(value = "SELECT SESSION_ID from SPRING_SESSION where SESSION_ID =:sessionId", nativeQuery = true)
	public String getAllSessionIds(String sessionId);

	public List<EndUseActivity> findByAccountNo(Long accountNo);

	public List<EndUseActivity> findByDisbursedDateBetween(LocalDate startDate, LocalDate endDate);

}
