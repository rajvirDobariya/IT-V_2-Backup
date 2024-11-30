package com.suryoday.twowheeler.service;

import org.json.JSONObject;

import com.suryoday.twowheeler.pojo.TwoWheelerFamilyMember;

public interface TwowheelerDudupeService {

	public JSONObject dedupeCall(TwoWheelerFamilyMember familyMember, JSONObject header);

}
