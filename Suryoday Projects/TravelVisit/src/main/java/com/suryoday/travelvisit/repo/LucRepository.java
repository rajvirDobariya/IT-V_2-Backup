package com.suryoday.travelvisit.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.suryoday.travelvisit.model.Luc;
import com.suryoday.travelvisit.model.VisitTo;

public interface LucRepository extends JpaRepository<Luc, Long> {

	// FOR RO
	public List<Luc> findByCreatedDateBetween(Date startDate, Date endDate);

	// FOR CREDIT_OPS
	public List<Luc> findByCreatedDateBetweenAndCreatedBy(Date startDate, Date endDate, Long userId);

}
