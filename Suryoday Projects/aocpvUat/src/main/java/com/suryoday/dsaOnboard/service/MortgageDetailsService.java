package com.suryoday.dsaOnboard.service;

import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import com.suryoday.dsaOnboard.pojo.MortgageDetails;

public interface MortgageDetailsService {

	void saveMortgageDetails(JSONObject jsonObject, MultipartFile file) throws IOException;

	List<MortgageDetails> fetchByEmpId(JSONObject jsonObject);

	List<MortgageDetails> fetchByDate(JSONObject jsonObject);

	JSONObject writeExcel(JSONArray j);

}
