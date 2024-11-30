package com.suryoday.twowheeler.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TwowheelerBankCode {
	@Id
	private long slNo;
	private String name;
	private String bankCode;
	private String routionCode;

	public long getSlNo() {
		return slNo;
	}

	public void setSlNo(long slNo) {
		this.slNo = slNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getRoutionCode() {
		return routionCode;
	}

	public void setRoutionCode(String routionCode) {
		this.routionCode = routionCode;
	}

}
