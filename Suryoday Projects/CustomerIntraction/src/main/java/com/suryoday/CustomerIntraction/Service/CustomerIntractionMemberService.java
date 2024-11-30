package com.suryoday.CustomerIntraction.Service;

import java.util.List;

import org.json.JSONObject;

import com.suryoday.CustomerIntraction.Pojo.CustomerIntractionMember;

public interface CustomerIntractionMemberService {

	public JSONObject createMembers(JSONObject requestJsonDecrypt, String meetingNumber, List<CustomerIntractionMember> member);

}
