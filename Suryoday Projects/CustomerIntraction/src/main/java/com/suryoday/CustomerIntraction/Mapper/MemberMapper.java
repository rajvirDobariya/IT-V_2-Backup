package com.suryoday.CustomerIntraction.Mapper;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.suryoday.CustomerIntraction.Pojo.CustomerIntractionMember;

@Service
public class MemberMapper {

	public CustomerIntractionMember convertToEntity(JSONObject jsonData) {

		CustomerIntractionMember memberData = new CustomerIntractionMember();
		JSONObject getJsonData = jsonData.getJSONObject("Data");
		memberData.setMemberName(getJsonData.getString("name"));
		memberData.setRole(getJsonData.getString("memberRole"));

		return memberData;

	}

}
