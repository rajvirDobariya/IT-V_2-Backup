package com.suryoday.aocpv.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import com.suryoday.aocpv.pojo.HRUsersData;

@Component
public interface HRUserDataRepo extends JpaRepository<HRUsersData, Long> {
	@Query(value = "select a from HRUsersData a where a.userId =:userId1")
	Optional<HRUsersData> fetchUserFromHr(String userId1);
}
