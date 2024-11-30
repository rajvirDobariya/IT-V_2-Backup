package com.suryoday.travelvisit.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.suryoday.travelvisit.model.OdCustomerVisit;
import com.suryoday.travelvisit.model.VisitTo;

public interface OdCustomerVisitRepository extends JpaRepository<OdCustomerVisit, Long> {

	// FOR RO
	public List<OdCustomerVisit> findByCreatedDateBetween(Date startDate, Date endDate);

	// FOR CREDIT_OPS
	public List<OdCustomerVisit> findByCreatedDateBetweenAndCreatedBy(Date startDate, Date endDate, Long userId);

}
