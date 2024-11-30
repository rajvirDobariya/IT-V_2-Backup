package com.suryoday.loantrackingphase2.service;

import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import com.suryoday.loantrackingphase2.model.ProductMaster;

public interface ProductMasterService {

	public String addExcel(MultipartFile file);

	public JSONObject getAllProductMasters(String channel, String X_Session_ID, String X_User_ID, boolean isEncrypt);

	public JSONObject getProductMasterById(String request, String channel, String X_Session_ID, String X_User_ID,
			boolean isEncrypt);

	public ProductMaster findById(Long productCode);

	public String getEmployeeNameById(Integer empId);
}
