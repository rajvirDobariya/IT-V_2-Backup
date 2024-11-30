package com.suryoday.CustomerIntraction.Repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.suryoday.CustomerIntraction.Pojo.CustomerIntractionMember;

@Repository
public interface CustomerIntractionMemberRepository extends JpaRepository<CustomerIntractionMember, Long>{

	@Query
	List<CustomerIntractionMember> findByMeetingNumber(String meetingNumber);

	
	
}
