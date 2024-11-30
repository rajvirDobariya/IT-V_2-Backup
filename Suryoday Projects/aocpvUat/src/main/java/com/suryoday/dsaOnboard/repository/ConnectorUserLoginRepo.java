package com.suryoday.dsaOnboard.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import com.suryoday.dsaOnboard.pojo.ConnectorUserLogin;

@Component
public interface ConnectorUserLoginRepo extends JpaRepository<ConnectorUserLogin, Long> {
	@Query(value = "SELECT a.emailId from ConnectorUserLogin a where a.emailId =:emailId")
	Optional<String> checkEmailId(String emailId);

	@Query(value = "SELECT a.mobileNo from ConnectorUserLogin a where a.mobileNo =:mobileNo")
	Optional<String> checkMobileNo(String mobileNo);

	@Query(value = "SELECT a from ConnectorUserLogin a where a.emailId =:emailId and a.password =:encryptPassword")
	Optional<ConnectorUserLogin> getByMobAndPass(String emailId, String encryptPassword);

}
