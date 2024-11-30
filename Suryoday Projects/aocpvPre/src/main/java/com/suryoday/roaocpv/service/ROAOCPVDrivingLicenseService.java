package com.suryoday.roaocpv.service;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public interface ROAOCPVDrivingLicenseService {

//	JSONObject authenticateDrivingLicense(JSONObject jsonObject, JSONObject header);

	JSONObject authenticateDrivingLicense(String drivingLicenseNo, JSONObject header);

	JSONObject getDrivingLicenseDetails(String drivingLicenseNo,String dob);

}
