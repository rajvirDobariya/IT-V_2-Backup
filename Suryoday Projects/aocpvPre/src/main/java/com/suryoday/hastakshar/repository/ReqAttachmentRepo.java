package com.suryoday.hastakshar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.suryoday.hastakshar.pojo.HastReqAttachment;

@Repository
public interface ReqAttachmentRepo extends JpaRepository<HastReqAttachment, String> {

	@Query(value = "SELECT a from HastReqAttachment a where a.applictioNO =:applictioNO ORDER BY createDate desc")
	List<HastReqAttachment> fetchAttachment(String applictioNO);

}
