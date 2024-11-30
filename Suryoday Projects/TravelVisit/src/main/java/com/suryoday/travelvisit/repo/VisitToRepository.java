package com.suryoday.travelvisit.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.suryoday.travelvisit.model.VisitTo;

public interface VisitToRepository extends JpaRepository<VisitTo, Long> {

	// FOR RO
	public List<VisitTo> findByCreatedDateBetween(Date startDate, Date endDate);

	// FOR CREDIT_OPS
	public List<VisitTo> findByCreatedDateBetweenAndCreatedBy(Date startDate, Date endDate, Long userId);

	@Query(value = "SELECT SESSION_ID from SPRING_SESSION where SESSION_ID =:sessionId", nativeQuery = true)
	public String getAllSessionIds(String sessionId);

}
