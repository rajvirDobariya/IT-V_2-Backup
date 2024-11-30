package com.suryoday.roaocpv.service;

import org.springframework.stereotype.Component;

import com.suryoday.roaocpv.pojo.BRNetMasters;

@Component
public interface BRNetMastersService {

	BRNetMasters fetchBrNetMasters(String categotyType, String param);

}
