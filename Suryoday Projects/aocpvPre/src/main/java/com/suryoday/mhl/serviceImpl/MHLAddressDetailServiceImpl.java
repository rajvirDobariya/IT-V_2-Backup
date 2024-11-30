package com.suryoday.mhl.serviceImpl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.mhl.pojo.AddressDetails;
import com.suryoday.mhl.repository.MHLAddressDetailRepository;
import com.suryoday.mhl.service.MHLAddressDetailService;

@Service
public class MHLAddressDetailServiceImpl implements MHLAddressDetailService{

	@Autowired
	MHLAddressDetailRepository addressDetailRepository;
	
	@Override
	public void saveAddress(JSONArray jsonArray, String applicationNo, String applicationRole, String memberId) {
		
			String currentAddress =null;
			String	permanentAddress=null;
		for(int n=0 ; n<jsonArray.length();n++) {
			JSONObject object = jsonArray.getJSONObject(n);
			currentAddress=object.getJSONObject("currentAddress").toString();
			permanentAddress=object.getJSONObject("permanentAddress").toString(); 
		}
		saveaddress(currentAddress,  applicationNo,  applicationRole,  memberId ,"CURRENT_ADDRESS");
		saveaddress(permanentAddress,  applicationNo,  applicationRole,  memberId,"PERMANENT_ADDRESS");
			 
	}
	
	public void saveaddress(String address , String applicationNo, String applicationRole, String memberId,String add) {	
		JSONObject jsonObject=new JSONObject(address); 
		String addressLine1 = jsonObject.getString("addressLine1");
		String addressLine2 = jsonObject.getString("addressLine2");
		String city = jsonObject.getString("city");
		String dist = jsonObject.getString("dist");
		String state = jsonObject.getString("state");
		String pincode = jsonObject.getString("pincode");
		String landmark = jsonObject.getString("landmark");
		String distanceFromBranch = jsonObject.getString("distanceFromBranch");
		String owenershipStatus = jsonObject.getString("owenershipStatus");
		String stabiltyYear = jsonObject.getString("stabiltyYear");
		
		AddressDetails addressDetails=new AddressDetails();
		addressDetails.setAddressLine1(addressLine1);
		addressDetails.setAddressLine2(addressLine2);
		addressDetails.setCity(city);
		addressDetails.setDist(dist);
		addressDetails.setState(state);
		addressDetails.setPincode(pincode);
		addressDetails.setLandmark(landmark);
		addressDetails.setDistanceFromBranch(distanceFromBranch);
		addressDetails.setOwenershipStatus(owenershipStatus);
		addressDetails.setStabiltyYear(stabiltyYear);
		LocalDateTime now = LocalDateTime.now();
		addressDetails.setCreationDate(now);
		addressDetails.setUpdatedDate(now);
		addressDetails.setApplicationNo(applicationNo);
		addressDetails.setApplicationRole(applicationRole);
		addressDetails.setMemberId(memberId);
		addressDetails.setAddressType(add);
		
	Optional<AddressDetails> optional=addressDetailRepository.getByApplicationNo(applicationNo,applicationRole,add);
			if(optional.isPresent()) {
				AddressDetails addressdetails = optional.get();
				addressDetails.setId(addressdetails.getId());
			}
			addressDetailRepository.save(addressDetails);
	}

	@Override
	public void savePropertyAddress(JSONArray jsonArray, String applicationNo) {
		String propertyAddress =null;
		String applicationRole = "APPLICANT";
		String memberId="1";
		for(int n=0 ; n<jsonArray.length();n++) {
			JSONObject object = jsonArray.getJSONObject(n);
			propertyAddress=object.getJSONObject("PropertyAddress").toString();
		}
		saveaddress(propertyAddress,  applicationNo,  applicationRole,  memberId ,"PROPERTY_ADDRESS");
	}

}
