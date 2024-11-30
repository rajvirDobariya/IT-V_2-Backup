package com.suryoday.roaocpv.service;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public interface ROAOCPVDrivingLicenseService {

//	JSONObject authenticateDrivingLicense(JSONObject jsonObject, JSONObject header);

	JSONObject authenticateDrivingLicense(String drivingLicenseNo, String dob, JSONObject header);

}
