package com.suryoday.aocpv.service;

import com.suryoday.aocpv.pojo.UserRoleMaster;

public interface LoginLdapService {

	UserRoleMaster getByuserId(String userId);

}
