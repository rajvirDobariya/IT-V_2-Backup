package com.suryoday.twowheeler.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.suryoday.twowheeler.pojo.TwowheelerLoanCreation;

@Repository
public interface TwowheelerLoanCreationRepository extends JpaRepository<TwowheelerLoanCreation, String>{

	Optional<TwowheelerLoanCreation> getByApplicationNo(String applicationNo);

}
