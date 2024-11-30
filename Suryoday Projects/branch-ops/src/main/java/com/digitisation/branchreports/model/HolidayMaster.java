package com.digitisation.branchreports.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "branch_d_holiday_master")
public class HolidayMaster implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String holiday;

	@Column(columnDefinition = "varchar(255) default 'Holiday'")
	private String holidaystatus;

	@Column(name = "holiday_date")
	private LocalDate holidayDate;

	public String getHolidaystatus() {
		return holidaystatus;
	}

	public void setHolidaystatus(String holidaystatus) {
		this.holidaystatus = holidaystatus;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getHoliday() {
		return holiday;
	}

	public void setHoliday(String holiday) {
		this.holiday = holiday;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public LocalDate getHolidayDate() {
		return holidayDate;
	}

	public void setHolidayDate(LocalDate holidayDate) {
		this.holidayDate = holidayDate;
	}

}
