
package com.suryoday.roaocpv.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.roaocpv.pojo.ROAOCPVProductType;
import com.suryoday.roaocpv.repository.ROAOCPVProductTypeRepo;
import com.suryoday.roaocpv.service.ROAOCPVProductTypeService;

@Service
public class ROAOCPVProductTypeServiceImpl implements ROAOCPVProductTypeService{
	@Autowired
	ROAOCPVProductTypeRepo roaocpvProductTypeRepo;

	@Override
	public List<ROAOCPVProductType> fetchByCategoryType(String productType) {
		List<ROAOCPVProductType> roaocpvProductType = roaocpvProductTypeRepo.fetchByCategoryType(productType);
        if(roaocpvProductType.isEmpty()) {
            throw new NoSuchElementException("List is Empty");
        }
        return roaocpvProductType;
	}

	@Override
	public List<ROAOCPVProductType> fetchByAll() {
		List<ROAOCPVProductType> roaocpvProductType2 = roaocpvProductTypeRepo.fetchByAll();
        if(roaocpvProductType2.isEmpty()) {
            throw new NoSuchElementException("List is Empty");
        }
        return roaocpvProductType2;
	}

}
