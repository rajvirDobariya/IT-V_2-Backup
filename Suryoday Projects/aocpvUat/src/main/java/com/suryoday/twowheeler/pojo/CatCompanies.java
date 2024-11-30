package com.suryoday.twowheeler.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CatCompanies {

	@Id
	private int id;
	private String symbol;
	private String companyName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public CatCompanies(int id, String symbol, String companyName) {
		super();
		this.id = id;
		this.symbol = symbol;
		this.companyName = companyName;
	}

	public CatCompanies() {
		super();
		// TODO Auto-generated constructor stub
	}

}
