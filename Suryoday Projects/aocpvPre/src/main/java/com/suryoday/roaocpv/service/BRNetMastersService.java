package com.suryoday.roaocpv.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.suryoday.roaocpv.pojo.BRNetMasters;

@Component
public interface BRNetMastersService {

	BRNetMasters fetchBrNetMasters(String categotyType, String param);

	List<BRNetMasters> fetchMaritalStatus(String id);

	public void saveData(String string,String ApplicationNo);


}
