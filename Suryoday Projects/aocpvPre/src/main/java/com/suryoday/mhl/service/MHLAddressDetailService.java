package com.suryoday.mhl.service;

import org.json.JSONArray;

public interface MHLAddressDetailService {

	void saveAddress(JSONArray jsonArray, String applicationNo, String applicationRole, String memberId);

	void savePropertyAddress(JSONArray jsonArray, String applicationNo);

}
