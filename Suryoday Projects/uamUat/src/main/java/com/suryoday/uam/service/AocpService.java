package com.suryoday.uam.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.suryoday.uam.pojo.PermissionDto;

@Service
public interface AocpService {

	public List<PermissionDto> getPagesForOtherRole(String otherRole, String CHANNEL, String userAccess,
			List<PermissionDto> dtos, String moduleName);
}
