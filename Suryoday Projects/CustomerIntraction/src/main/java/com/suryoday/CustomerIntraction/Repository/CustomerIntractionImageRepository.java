package com.suryoday.CustomerIntraction.Repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.suryoday.CustomerIntraction.Pojo.MeetingImage;

@Repository
public interface CustomerIntractionImageRepository extends JpaRepository<MeetingImage, Long>{

	@Query
	List<MeetingImage> findByMeetingNumber(String meetingNumber);

	
}
