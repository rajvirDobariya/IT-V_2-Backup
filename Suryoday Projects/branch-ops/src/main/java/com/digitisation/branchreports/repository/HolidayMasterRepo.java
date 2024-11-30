package com.digitisation.branchreports.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.digitisation.branchreports.model.HolidayMaster;

@Repository

public interface HolidayMasterRepo extends JpaRepository<HolidayMaster, Long> {

	public boolean existsByHolidayDate(LocalDate date);

	public HolidayMaster findByHolidayDate(LocalDate date);

}
