package com.suryoday.twowheeler.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.suryoday.twowheeler.pojo.LeegalityInfo;

@Repository
public interface LeegalityInfoRepository extends JpaRepository<LeegalityInfo, Integer> {

	@Query(value = "Select a from LeegalityInfo a where a.applicationNo=:applicationNo and a.documentType=:documentType")
	List<LeegalityInfo> getByApplicationNoAndDocument(String applicationNo, String documentType);

	List<LeegalityInfo> getByApplicationNo(String applicationNo);

}
