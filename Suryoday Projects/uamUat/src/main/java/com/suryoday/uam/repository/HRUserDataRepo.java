package com.suryoday.uam.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import com.suryoday.uam.pojo.HRUsersData;

@Component
public interface HRUserDataRepo extends JpaRepository<HRUsersData, Integer>{
	@Query(value = "select a from HRUsersData a where a.userId =:userId1")
	Optional<HRUsersData> fetchUserFromHr(String userId1);
}
