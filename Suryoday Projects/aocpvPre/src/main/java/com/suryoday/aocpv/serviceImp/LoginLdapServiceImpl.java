package com.suryoday.aocpv.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.aocpv.pojo.UserRoleMaster;
import com.suryoday.aocpv.repository.LoginLdapRepository;
import com.suryoday.aocpv.service.LoginLdapService;

@Service
public class LoginLdapServiceImpl implements LoginLdapService{

	@Autowired
	LoginLdapRepository ldapRepository;
	
	@Override
	public UserRoleMaster getByuserId(String userId) {
		return null;
		//Optional<UserRoleMaster> user =ldapRepository.getByUid(userId);
		//if(user.isPresent()) {
		//	return user.get();
		//}
		//throw new NoSuchElementException("You are Not authorized to View");
		
	}

}
