package com.suryoday.twowheeler.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.suryoday.twowheeler.pojo.AssetMaster;
import com.suryoday.twowheeler.pojo.DealerByPincode;
import com.suryoday.twowheeler.pojo.DealerMaster;
import com.suryoday.twowheeler.pojo.NtbSchemeMaster;
import com.suryoday.twowheeler.pojo.SchemeMaster;
import com.suryoday.twowheeler.pojo.TWModelMaster;

@Component
public interface TwoWheelerMastersService {

	List<TWModelMaster> fetchModelMaster();

	SchemeMaster fetchBySchemeCode(String schemeName);

//	List<DealerMaster> fetchByDealerCode(String dealerCode);

	List<TWModelMaster> fetchByVehicle(String manufacturerName, String vehicleType);

	List<SchemeMaster> fetchAllSchemes();

	List<DealerMaster> fetchAllDealers(String manufacturerName);

	AssetMaster fetchByVariant(String variantId);

	List<String> fetchManufacture();

	List<String> fetchSchemeDesc();

	List<String> fetchModel(String manufacturerName);

	List<DealerByPincode> fetchDealerBypincode(String pinCode);

	List<DealerMaster> fetchByDealerCode(String dealercode);

	List<String> fetchVarient(String modelName);

	List<DealerByPincode> fetchAllDealer(String branchId, String manufacturer);

	List<String> getSchemeNtb(String model, String breCustomerCategory);

	NtbSchemeMaster fetchBySchemeCodeNtb(String schemeName);

	String getLtv(String scheme, String breCustCatagory);

}
