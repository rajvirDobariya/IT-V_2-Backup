package com.suryoday.connector.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.suryoday.connector.pojo.RegistrationPage;

@Repository
public interface RegistrationReository extends JpaRepository<RegistrationPage, Long> {

	@Query(value = "SELECT a from RegistrationPage a where a.applicationNo =:appln and a.flowStatus =:imageFlow")
	List<RegistrationPage> findByApplicationNoReview(Long appln, String imageFlow);

	@Query(value = "SELECT a from RegistrationPage a where a.mobile =:mobile")
	Optional<RegistrationPage> fetchBymobileNumber(String mobile);

}
