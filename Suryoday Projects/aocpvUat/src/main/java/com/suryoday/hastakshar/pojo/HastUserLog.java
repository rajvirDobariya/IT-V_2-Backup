package com.suryoday.hastakshar.pojo;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hastakshar_UserLog")
public class HastUserLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String applictioNO;
	private String remark;
	private String empId;
	private String status;
	private LocalDateTime updateDate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getApplictioNO() {
		return applictioNO;
	}

	public void setApplictioNO(String applictioNO) {
		this.applictioNO = applictioNO;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(LocalDateTime updateDate) {
		this.updateDate = updateDate;
	}

	@Override
	public String toString() {
		return "HastUserLog [id=" + id + ", applictioNO=" + applictioNO + ", remark=" + remark + ", empId=" + empId
				+ ", status=" + status + ", updateDate=" + updateDate + "]";
	}
}
