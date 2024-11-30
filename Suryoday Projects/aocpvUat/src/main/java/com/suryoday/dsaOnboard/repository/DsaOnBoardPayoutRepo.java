package com.suryoday.dsaOnboard.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import com.suryoday.dsaOnboard.pojo.PayoutSchemeMaster;

@Component
public interface DsaOnBoardPayoutRepo extends JpaRepository<PayoutSchemeMaster, String> {

	@Query(value = "Select a from PayoutSchemeMaster a where a.product=:product and a.agencyType=:agencyType")
	Optional<PayoutSchemeMaster> fetchByProductAndAgency(String product, String agencyType);

}
