package com.suryoday.loantrackingphase2.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.suryoday.loantrackingphase2.model.HealthCheckActivity;

@Repository
public interface HealthCheckActivityRepository extends JpaRepository<HealthCheckActivity, Long> {

	// Find All
	public List<HealthCheckActivity> findAll();

	// Find by PanNo
	public List<HealthCheckActivity> findByPanNo(String panNo);

	// Find by date range
	public List<HealthCheckActivity> findByCreatedDateBetween(Date startDate, Date endDate);

	// Find by date range and PanNo
	public List<HealthCheckActivity> findByPanNoAndCreatedDateBetween(String panNo, Date startDate, Date endDate);

	public Boolean existsByApplicationId(String applicationId);
}
