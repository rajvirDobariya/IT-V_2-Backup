package com.suryoday.twowheeler.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class NegativeLocalityMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String city;
	private String negativeArea;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getNegativeArea() {
		return negativeArea;
	}

	public void setNegativeArea(String negativeArea) {
		this.negativeArea = negativeArea;
	}

}
