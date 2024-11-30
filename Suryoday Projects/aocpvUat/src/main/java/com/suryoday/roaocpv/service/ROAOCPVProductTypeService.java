package com.suryoday.roaocpv.service;

import java.util.List;

import com.suryoday.roaocpv.pojo.ROAOCPVProductType;

public interface ROAOCPVProductTypeService {
	List<ROAOCPVProductType> fetchByCategoryType(String productType);

	List<ROAOCPVProductType> fetchByAll();

}
