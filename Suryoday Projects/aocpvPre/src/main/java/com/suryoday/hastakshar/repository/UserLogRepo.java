package com.suryoday.hastakshar.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.suryoday.hastakshar.pojo.HastUserLog;

public interface UserLogRepo extends JpaRepository<HastUserLog, Long> {

	@Query(value = "SELECT a from HastUserLog a where a.applictioNO =:applictioNO")
	List<HastUserLog> fetchUserLog(String applictioNO);

	@Query(value = "SELECT a from HastUserLog a where a.applictioNO =:applictioNO AND a.empId =:empId ORDER BY a.updateDate desc")
	List<HastUserLog> fetchStatusByEmpID(String applictioNO, String empId);

	@Query(value = "SELECT a from HastUserLog a where a.applictioNO =:applictioNO AND a.status =:status ORDER BY a.updateDate desc")
	List<HastUserLog> fetchBystatusCount(String applictioNO, String status);

	@Modifying
	@Transactional
	@Query(value = "UPDATE HastUserLog set status =:status where applictioNO =:applictioNO and status=:Astatus")
	void updateUserLogStatus(String status, String applictioNO, String Astatus);
}
