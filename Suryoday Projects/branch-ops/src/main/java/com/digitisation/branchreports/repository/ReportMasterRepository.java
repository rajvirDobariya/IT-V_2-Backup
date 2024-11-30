package com.digitisation.branchreports.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.digitisation.branchreports.model.RepMaster;

@Transactional
@Repository
public interface ReportMasterRepository extends JpaRepository<RepMaster, Long> {

	@Query("SELECT r FROM RepMaster r WHERE reportfrequency =:reportfrequency")
	List<RepMaster> findByReportfrequency(String reportfrequency);
}
