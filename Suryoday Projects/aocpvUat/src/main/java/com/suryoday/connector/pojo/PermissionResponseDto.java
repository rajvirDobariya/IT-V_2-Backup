package com.suryoday.connector.pojo;

import java.util.List;

public class PermissionResponseDto {

	private Long id;

	private String userName;

	private List<UserRoleDto> permissions;

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

	public List<UserRoleDto> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<UserRoleDto> permissions) {
		this.permissions = permissions;
	}

	@Override
	public String toString() {
		return "PermissionResponseDto [id=" + id + ", userName=" + userName + ", permissions=" + permissions + "]";
	}

}
