package com.suryoday.twowheeler.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.suryoday.twowheeler.pojo.NegativeLocalityMaster;

@Repository
public interface NegativeLocalityRepository extends JpaRepository<NegativeLocalityMaster, Integer> {

	@Query(value = "SELECT a from NegativeLocalityMaster a")
	List<NegativeLocalityMaster> getALL();

}
