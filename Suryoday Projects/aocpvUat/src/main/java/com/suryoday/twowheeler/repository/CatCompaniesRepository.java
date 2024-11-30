package com.suryoday.twowheeler.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.suryoday.twowheeler.pojo.CatCompanies;

@Repository
public interface CatCompaniesRepository extends JpaRepository<CatCompanies, Integer> {

	@Query(value = "Select a from CatCompanies a order by a.id")
	List<CatCompanies> getAll();

}
