package com.suryoday.twowheeler.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.suryoday.twowheeler.pojo.MandateDetails;

@Repository
public interface MandateDetailsRepo extends JpaRepository<MandateDetails, Long> {

	@Query(value = "Select a from MandateDetails a where a.applicationNo=:applicationno")
	List<MandateDetails> fetchMandateDetails(String applicationno);

	@Query(value = "Select a from MandateDetails a where a.applicationNo =:applicationno and referenceNo =:referenceno")
	Optional<MandateDetails> getByApplicationNoAndReferenceNo(String applicationno, String referenceno);

}
