package com.suryoday.customerOnboard.serviceImpl;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.suryoday.customerOnboard.entity.CustomerOnboardAddress;
import com.suryoday.customerOnboard.service.CustomerOnboardAddressService;

@Service
public class CustomerOnboardAddressServiceImpl implements CustomerOnboardAddressService {

	@Override
	public JSONObject addAddress(JSONObject data, String applicationNo, JSONObject header) {

		String addressType = data.optString("addressType");
		String addressLine1 = data.optString("address_Line1");
		String addressLine2 = data.optString("address_Line2");
		String addressLine3 = data.optString("address_Line3");
		String city = data.optString("city");
		String pincode = data.optString("pincode");
		String district = data.optString("district");
		String state = data.optString("state");
		String country = data.optString("country");
		String landmark = data.optString("landmark");
		CustomerOnboardAddress address = new CustomerOnboardAddress();
		return null;
	}
}
