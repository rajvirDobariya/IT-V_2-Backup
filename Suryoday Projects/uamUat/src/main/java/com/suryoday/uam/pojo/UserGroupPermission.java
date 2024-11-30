package com.suryoday.uam.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_user_group_permission")
public class UserGroupPermission implements Serializable{

	private static final long serialVersionUID = -5333317264593097634L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	
	@Column(name="user_permission")
	private String userPermission;
	
	@Column(name="module_group_name")
	private String modelGroupName;
	
	@ManyToOne
    @JoinColumn(name="user_id")
	private User user;
	
	@ManyToOne
    @JoinColumn(name="user_role_id")
	private UserRole userRole;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserPermission() {
		return userPermission;
	}

	public void setUserPermission(String userPermission) {
		this.userPermission = userPermission;
	}

	public String getModelGroupName() {
		return modelGroupName;
	}

	public void setModelGroupName(String modelGroupName) {
		this.modelGroupName = modelGroupName;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "UserGroupPermission [id=" + id + ", userPermission=" + userPermission + ", modelGroupName="
				+ modelGroupName + ", user=" + user + ", userRole=" + userRole + "]";
	}
	
}
