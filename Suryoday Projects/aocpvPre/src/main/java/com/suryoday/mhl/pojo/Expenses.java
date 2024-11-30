package com.suryoday.mhl.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_EXPENSES_MHL")
public class Expenses {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private String applicationNo;
	
	private String status;
	
	private double foodAndUtility;
	
	private double rent;
	
	private double transportation;
	
	private double medical;
	
	private double education;
	
	private double other;
	
	private double total;
	
	private double monthlyBalance;
	
	private double insurance;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getFoodAndUtility() {
		return foodAndUtility;
	}

	public void setFoodAndUtility(double foodAndUtility) {
		this.foodAndUtility = foodAndUtility;
	}

	public double getRent() {
		return rent;
	}

	public void setRent(double rent) {
		this.rent = rent;
	}

	public double getTransportation() {
		return transportation;
	}

	public void setTransportation(double transportation) {
		this.transportation = transportation;
	}

	public double getMedical() {
		return medical;
	}

	public void setMedical(double medical) {
		this.medical = medical;
	}

	public double getEducation() {
		return education;
	}

	public void setEducation(double education) {
		this.education = education;
	}

	public double getOther() {
		return other;
	}

	public void setOther(double other) {
		this.other = other;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public double getMonthlyBalance() {
		return monthlyBalance;
	}

	public void setMonthlyBalance(double monthlyBalance) {
		this.monthlyBalance = monthlyBalance;
	}

	public double getInsurance() {
		return insurance;
	}

	public void setInsurance(double insurance) {
		this.insurance = insurance;
	}

	@Override
	public String toString() {
		return "Expenses [id=" + id + ", applicationNo=" + applicationNo + ", status=" + status + ", foodAndUtility="
				+ foodAndUtility + ", rent=" + rent + ", transportation=" + transportation + ", medical=" + medical
				+ ", education=" + education + ", other=" + other + ", total=" + total + ", monthlyBalance="
				+ monthlyBalance + ", insurance=" + insurance + "]";
	}
	
}
