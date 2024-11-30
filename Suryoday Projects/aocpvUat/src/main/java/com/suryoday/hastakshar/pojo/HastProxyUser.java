package com.suryoday.hastakshar.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hastakshar_ProxyUser")
public class HastProxyUser {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String proxyUser;
	private String approverUser;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProxyUser() {
		return proxyUser;
	}

	public void setProxyUser(String proxyUser) {
		this.proxyUser = proxyUser;
	}

	public String getApproverUser() {
		return approverUser;
	}

	public void setApproverUser(String approverUser) {
		this.approverUser = approverUser;
	}

	@Override
	public String toString() {
		return "HastProxyUser [id=" + id + ", proxyUser=" + proxyUser + ", approverUser=" + approverUser + "]";
	}
}
