package com.suryoday.loantrackingphase2.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.suryoday.loantrackingphase2.model.NOIUpdation;

@Repository
public interface NOIUpdationRepository extends JpaRepository<NOIUpdation, Long> {

	public List<NOIUpdation> findByLoanId(String loanId);

	public List<NOIUpdation> findByCreatedDateBetween(Date fromDate, Date toDate);

	public List<NOIUpdation> findByLoanIdAndCreatedDateBetween(String loanId, Date fromDate, Date toDate);

}
