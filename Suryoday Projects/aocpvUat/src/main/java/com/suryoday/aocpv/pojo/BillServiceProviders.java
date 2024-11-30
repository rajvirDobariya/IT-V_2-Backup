package com.suryoday.aocpv.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_billservice_providers")
public class BillServiceProviders {
	@Id
	private String serviceProvider;
	private String state;
	private String code;

	public String getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(String serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "BillServiceProviders [serviceProvider=" + serviceProvider + ", state=" + state + ", code=" + code + "]";
	}

}
