package com.suryoday.twowheeler.service;

import java.util.List;

import org.json.JSONObject;

import com.suryoday.twowheeler.pojo.MandateDetails;

public interface NachMandateService {

	JSONObject mandateCreation(JSONObject jsonObject);

	JSONObject fetchMandate(JSONObject jsonObject);

	List<MandateDetails> fetchMandateDetails(String applicationno);

	void savemandate(JSONObject jsonObject, String applicationNo, String reference);

	MandateDetails getByApplicationNoAndReference(String applicationNo, String referenceNo);

	void save(MandateDetails mandateDetails);

	List<MandateDetails> getMandateDetails(String applicationNo);

}
