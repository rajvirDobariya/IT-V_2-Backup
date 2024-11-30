package com.suryoday.dsaOnboard.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.suryoday.dsaOnboard.pojo.DsaOnboardCPV;

@Repository
public interface DsaOnboardCPVRepository extends JpaRepository<DsaOnboardCPV, Integer> {

	@Query(value = "SELECT DISTINCT  a.state from DsaOnboardCPV a")
	Set<String> fetchState();

	@Query(value = "SELECT DISTINCT  a.district from DsaOnboardCPV a where a.state =:state")
	Set<String> fetchCity(String state);

	@Query(value = "SELECT a from DsaOnboardCPV a where a.district =:city")
	List<DsaOnboardCPV> getCPVList(String city);

}
