package com.suryoday.dsaOnboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.suryoday.dsaOnboard.pojo.DsaUserLog;

@Repository
public interface DsaUserLogRepository extends JpaRepository<DsaUserLog, Long>{

	List<DsaUserLog> getByApplicationNo(long applicationno);

}
