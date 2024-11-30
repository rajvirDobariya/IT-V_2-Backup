package com.suryoday.CustomerIntraction.Service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.suryoday.CustomerIntraction.Exception.Error;
import com.suryoday.CustomerIntraction.Pojo.CustomerIntractionMember;
import com.suryoday.CustomerIntraction.Repository.CustomerIntractionMemberRepository;
import com.suryoday.CustomerIntraction.Service.CustomerIntractionMemberService;

@Service
public class CustomerIntractionmemberServiceImpl implements CustomerIntractionMemberService {

	private static Logger logger = LoggerFactory.getLogger(CustomerIntractionmemberServiceImpl.class);

	@Autowired
	private CustomerIntractionMemberRepository memberRepo;

	@Override
	public JSONObject createMembers(JSONObject requestString, String meetingNumber, List<CustomerIntractionMember> memberData) {

		List<CustomerIntractionMember> customerInteractionMembers = new ArrayList<>();

		
		CustomerIntractionMember member = new CustomerIntractionMember();
		logger.debug("CustomerIntractionmemberServiceImpl :: Create Member List : "+requestString);
		
		JSONArray membersJsonArray = requestString.optJSONArray("memberList");
		
		if (membersJsonArray == null || membersJsonArray.length() < 0) {
			logger.debug("membersJsonArray JSON array is null or empty :: {}", HttpStatus.BAD_REQUEST);
			return Error.createError("membersJsonArray JSON array is null or empty!", HttpStatus.BAD_REQUEST);
		}

		for (int i = 0; i < membersJsonArray.length(); i++) {
			
			if (i < memberData.size()) {
				member = memberData.get(i);
		    } else {
		    	member = new CustomerIntractionMember();
		    }
			JSONObject memberJson = (JSONObject) membersJsonArray.get(i);
			if (meetingNumber == null || meetingNumber.isEmpty()) {

				logger.debug(" meetingNumber is null or empty in Data of Request :: {}", HttpStatus.BAD_REQUEST);
				return Error.createError("meetingNumber is null or empty in Data of Request ", HttpStatus.BAD_REQUEST);
			}

			String memberName = memberJson.optString("name");
			Long CIFNumber = memberJson.optLong("CIFNo");
			String memberRole = memberJson.optString("memberRole");
			
			logger.debug("memberName, CIFNumber, memberRoll : "+ memberName, CIFNumber, memberRole );

			if (memberName == null || memberName.isEmpty() || CIFNumber == null || CIFNumber == 0L || memberRole == null
					|| memberRole.isEmpty()) {

				logger.debug("One or more required fields are null or empty in MemberList at index :: {} : {}", i,
						HttpStatus.BAD_REQUEST);
				return Error.createError("One or more required fields are null or empty in MemberList at index :: " + i,
						HttpStatus.BAD_REQUEST);
			}

//			CustomerIntractionMember member = new CustomerIntractionMember();

			member.setMemberName(memberName);
			member.setMemberId(CIFNumber);
			member.setMeetingNumber(meetingNumber);
			member.setRole(memberRole);
			member.setCreatedBy(requestString.optLong("createdBy"));

			// Add the member to the list
			customerInteractionMembers.add(member);
			logger.debug("Member data : "+ customerInteractionMembers );
		}

		// save member deatils in db
		memberRepo.saveAll(customerInteractionMembers);

		return null;
	}

}
