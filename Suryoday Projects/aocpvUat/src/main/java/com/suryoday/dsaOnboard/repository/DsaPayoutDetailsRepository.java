package com.suryoday.dsaOnboard.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.suryoday.dsaOnboard.pojo.DsaOnboardPayoutDetails;

public interface DsaPayoutDetailsRepository extends JpaRepository<DsaOnboardPayoutDetails, Long> {

	Optional<DsaOnboardPayoutDetails> getByApplicationNo(long parseLong);

}
