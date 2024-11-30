package com.suryoday.twowheeler.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.twowheeler.pojo.AssetMaster;
import com.suryoday.twowheeler.pojo.DealerByPincode;
import com.suryoday.twowheeler.pojo.DealerMaster;
import com.suryoday.twowheeler.pojo.SchemeMaster;
import com.suryoday.twowheeler.pojo.TWModelMaster;
import com.suryoday.twowheeler.repository.DealerMasterRepo;
import com.suryoday.twowheeler.repository.SchemeMasterRepo;
import com.suryoday.twowheeler.repository.TWAssetMasterRepository;
import com.suryoday.twowheeler.repository.TwoWheelerMastersRepo;
import com.suryoday.twowheeler.service.TwoWheelerMastersService;

@Component
public class TwoWheelerMastersServImpl implements TwoWheelerMastersService {

	@Autowired
	TwoWheelerMastersRepo twowheelermastersrepo;
	
	@Autowired
	SchemeMasterRepo schememastersrepo;
	
	@Autowired
	DealerMasterRepo dealermasterrepo;
	
	@Autowired
	TWAssetMasterRepository assetRepo;
	
	@Override
	public List<TWModelMaster> fetchModelMaster() {
		List<TWModelMaster> list = twowheelermastersrepo.fetchModelMaster();
		if (list.isEmpty()) {
			throw new NoSuchElementException("No Record Found");
		}
		return list;
	}

	@Override
	public SchemeMaster fetchBySchemeCode(String schemeName) {
		Optional<SchemeMaster> optional = schememastersrepo.fetchBySchemeCode(schemeName);
		if (optional.isPresent()) {
			return optional.get();
		}
		throw new NoSuchElementException("No Record Found");
	}

//	@Override
//	public List<DealerMaster> fetchByDealerCode(String dealerCode) {
//		
//		List<DealerMaster> list = dealermasterrepo.fetchByDealerCode(dealerCode);
//		if(list.isEmpty())
//		{
//			throw new NoSuchElementException("No Record Found");
//		}
//		return list;
//	}
	@Override
	public List<TWModelMaster> fetchByVehicle(String manufacturerName, String vehicleType) {
		List<TWModelMaster> list = twowheelermastersrepo.fetchByVehicel(manufacturerName, vehicleType);
		if (list.isEmpty()) {
			throw new NoSuchElementException("No Record Found");
		}
		return list;
	}

	@Override
	public List<SchemeMaster> fetchAllSchemes() {
		List<SchemeMaster> list = schememastersrepo.fetchAllSchemes();
		if (list.isEmpty()) {
			throw new NoSuchElementException("No Record Found");
		}
		return list;
	}

	@Override
	public List<DealerMaster> fetchAllDealers(String manufacturerName) {
		List<DealerMaster> list = dealermasterrepo.fetchAllDealers(manufacturerName);
		if (list.isEmpty()) {
			throw new NoSuchElementException("No Record Found");
		}
		return list;
	}

	@Override
	public AssetMaster fetchByVariant(String variantName) {
		Optional<AssetMaster> optional = assetRepo.fetchByVariant(variantName);
		if (optional.isPresent()) {
			return optional.get();
		}
		throw new NoSuchElementException("No Record Found");
	}

	@Override
	public List<String> fetchManufacture() {
		List<String> list = assetRepo.fetchManufacture();
		return list;
	}

	@Override
	public List<String> fetchSchemeDesc() {
		List<String> list = schememastersrepo.fetchSchemeDesc();
		if (list.isEmpty()) {
			throw new NoSuchElementException("No Record Found");
		}
		return list;
	}

	@Override
	public List<String> fetchModel(String manufacturerName) {
		List<String> list = assetRepo.fetchModel(manufacturerName);
		if (list.isEmpty()) {
			throw new NoSuchElementException("No Record Found");
		}
		return list;
	}

	@Override
	public List<DealerByPincode> fetchDealerBypincode(String pinCode) {
		List<DealerByPincode> list = dealermasterrepo.fetchDealerBypincode(pinCode);
		if (list.isEmpty()) {
			throw new NoSuchElementException("Please enter valid Pincode");
		}
		return list;
	}

	@Override
	public List<DealerMaster> fetchByDealerCode(String dealercode) {
		List<DealerMaster> list = dealermasterrepo.fetchByDealerCode(dealercode);
		if (list.isEmpty()) {
			throw new NoSuchElementException("No Record Found");
		}
		return list;
	}

	@Override
	public List<String> fetchVarient(String modelName) {
		List<String> list = assetRepo.fetchVarient(modelName);
		if (list.isEmpty()) {
			throw new NoSuchElementException("No Record Found");
		}
		return list;
	}

	@Override
	public List<DealerByPincode> fetchAllDealer(String branchId, String manufacturer) {
		List<DealerByPincode> list = dealermasterrepo.fetchAllDealer(manufacturer);
		if (list.isEmpty()) {
			throw new NoSuchElementException("Please enter valid Pincode");
		}
		return list;
	}
}
