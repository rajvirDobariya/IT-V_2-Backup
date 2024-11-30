package com.suryoday.dsaOnboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.suryoday.dsaOnboard.pojo.DsaOnBoardLeegalityInfo;

@Repository
public interface DsaOnBoardLeegalityRepository extends JpaRepository<DsaOnBoardLeegalityInfo, Integer> {

	@Query(value = "Select a from DsaOnBoardLeegalityInfo a where a.applicationNo=:applicationNo and a.documentType=:documentType")
	List<DsaOnBoardLeegalityInfo> getByApplicationNoAndDocument(String applicationNo, String documentType);

	List<DsaOnBoardLeegalityInfo> getByApplicationNo(String applicationNo);

}
