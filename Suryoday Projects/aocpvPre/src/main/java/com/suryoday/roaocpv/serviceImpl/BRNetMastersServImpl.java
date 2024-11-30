package com.suryoday.roaocpv.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suryoday.aocpv.pojo.AocpCustomer;
import com.suryoday.aocpv.repository.AocpCustomerDataRepo;
import com.suryoday.aocpv.serviceImp.NoSuchElementException;
import com.suryoday.roaocpv.pojo.BRNetMasters;
import com.suryoday.roaocpv.repository.BRNetMastersRepo;
import com.suryoday.roaocpv.service.BRNetMastersService;
@Component
public class BRNetMastersServImpl implements BRNetMastersService{
	
	@Autowired BRNetMastersRepo brmastersrepo;
	@Autowired
	AocpCustomerDataRepo aocpcustomerrepo;
	
	@Override
	public BRNetMasters fetchBrNetMasters(String categotyType, String param) {
		Optional<BRNetMasters> optional=brmastersrepo.fetchBrMaster(categotyType,param);
		if(optional.isPresent())
		{
			return optional.get();
		}
		throw new NoSuchElementException("No Record Found");
	}

	@Override
	public List<BRNetMasters> fetchMaritalStatus(String id) {
		List<BRNetMasters> list = brmastersrepo.fetchMaritalStatus(id);
		if(list.isEmpty())
		{
			throw new NoSuchElementException("List is empty");
		}
		return list;
	}

	@Override
	public void saveData(String string,String ApplicationNo) {
		// TODO Auto-generated method stub
		Optional<AocpCustomer> byAppNo = aocpcustomerrepo.getByApp(Long.parseLong(ApplicationNo));
		if(byAppNo.isPresent())
		{
			AocpCustomer aocpCustomer = byAppNo.get();
			aocpCustomer.setBrNetAddress(string);
			aocpcustomerrepo.save(aocpCustomer);
		}
		else
		{
		throw new NoSuchElementException("No record Found");
		}
	}






}
