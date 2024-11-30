package com.suryoday.CustomerIntraction.Repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.suryoday.CustomerIntraction.Pojo.MOMSummaryDetailsAudit;

@Repository
public interface MOMSummaryAuditRepository extends JpaRepository<MOMSummaryDetailsAudit, Long> {

	@Query
	List<MOMSummaryDetailsAudit> findByMeetingNumber(String meetingNumber);

}
