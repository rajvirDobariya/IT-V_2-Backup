package com.suryoday.mhl.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.suryoday.mhl.pojo.Expenses;

@Repository
public interface ExpensesRepository extends JpaRepository<Expenses, Integer> {

	@Query(value = "Select a from Expenses a where a.applicationNo =:appln")
	Optional<Expenses> checkApplicationNo(String appln);
}