package com.suryoday.roaocpv.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suryoday.aocpv.serviceImp.NoSuchElementException;
import com.suryoday.roaocpv.pojo.BRNetMasters;
import com.suryoday.roaocpv.repository.BRNetMastersRepo;
import com.suryoday.roaocpv.service.BRNetMastersService;

@Component
public class BRNetMastersServImpl implements BRNetMastersService {

	@Autowired
	BRNetMastersRepo brmastersrepo;

	@Override
	public BRNetMasters fetchBrNetMasters(String categotyType, String param) {
		Optional<BRNetMasters> optional = brmastersrepo.fetchBrMaster(categotyType, param);
		if (optional.isPresent()) {
			return optional.get();
		}
		throw new NoSuchElementException("No Record Found");
	}

}
