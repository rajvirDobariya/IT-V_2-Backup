package com.suryoday.mhl.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.suryoday.mhl.pojo.MhlUserLog;

@Repository
public interface MhlUserLogRepository extends JpaRepository<MhlUserLog, Integer>{

	@Query(value = "SELECT a from MhlUserLog a where a.applicationNo =:applicationNo and a.status =:status")
	Optional<MhlUserLog> fetchByApplicationNoAndstatus(String applicationNo, String status);

}
