package com.suryoday.aocpv.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.suryoday.aocpv.pojo.UserLog;

@Repository
public interface UserLogRepository extends JpaRepository<UserLog, Integer>{

	
	
	@Query(value = "SELECT a from UserLog a where a.applicationNo =:applicationNo and a.stages =:status")
	List<UserLog> findByApplicationNoAndStages(long applicationNo, String status);

	@Query(value = "SELECT a from UserLog a where a.applicationNo =:applicationNoInLong order by a.id desc ")
	List<UserLog> getByApplicationNo(long applicationNoInLong);

	
	@Query(value = "SELECT top 1 a.id from aocpv_userlog a order by a.id desc",nativeQuery = true)
	Optional<Integer> fetchLastId();

}
