package com.suryoday.hastakshar.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hastakshar_UserEmail")
public class HastUserName {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@Column(unique = true)
	private String empId;
	private String EmpName;
	private String EmpState;
	private String EmpEmailid;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getEmpName() {
		return EmpName;
	}

	public void setEmpName(String empName) {
		EmpName = empName;
	}

	public String getEmpState() {
		return EmpState;
	}

	public void setEmpState(String empState) {
		EmpState = empState;
	}

	public String getEmpEmailid() {
		return EmpEmailid;
	}

	public void setEmpEmailid(String empEmailid) {
		EmpEmailid = empEmailid;
	}

	@Override
	public String toString() {
		return "HastUserName [id=" + id + ", empId=" + empId + ", EmpName=" + EmpName + ", EmpState=" + EmpState
				+ ", EmpEmailid=" + EmpEmailid + "]";
	}
}
