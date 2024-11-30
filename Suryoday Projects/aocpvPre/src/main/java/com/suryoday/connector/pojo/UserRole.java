package com.suryoday.connector.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_userPermission")
public class UserRole implements Serializable {

private static final long serialVersionUID = -5163141132755477465L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name="id")
	private Long id;
	
	@Column(name="user_name")
	private String userName;
	
	@Column(name="permission_page_id")
	private String pageGroupId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPageGroupId() {
		return pageGroupId;
	}

	public void setPageGroupId(String pageGroupId) {
		this.pageGroupId = pageGroupId;
	}

	@Override
	public String toString() {
		return "UserRole [id=" + id + ", userName=" + userName + ", pageGroupId=" + pageGroupId + "]";
	}

	
}
