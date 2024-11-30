package com.suryoday.uam.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.uam.repository.LoginLdapRepository;
import com.suryoday.uam.service.LoginLdapService;

@Service
public class LoginLdapServiceImpl implements LoginLdapService{

	@Autowired
	LoginLdapRepository ldapRepository;
	
	

}
