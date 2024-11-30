package com.suryoday.CustomerIntraction.Repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.suryoday.CustomerIntraction.Pojo.MOMSummaryDetails;

@Repository
public interface MOMSummaryRepository extends JpaRepository<MOMSummaryDetails, Long>{

	@Query
	List<MOMSummaryDetails> findByMeetingNumber(String meetingNumber);

	
}
