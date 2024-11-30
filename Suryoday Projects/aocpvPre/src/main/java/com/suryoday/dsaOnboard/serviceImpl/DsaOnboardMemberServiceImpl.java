package com.suryoday.dsaOnboard.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.dsaOnboard.pojo.Address;
import com.suryoday.dsaOnboard.pojo.DeviationDetailsResponse;
import com.suryoday.dsaOnboard.pojo.DsaOnboardMember;
import com.suryoday.dsaOnboard.pojo.DsaOnboardMemberResponse;
import com.suryoday.dsaOnboard.pojo.GstResponse;
import com.suryoday.dsaOnboard.repository.DsaOnboardMemberRepository;
import com.suryoday.dsaOnboard.service.DsaOnboardMemberService;

@Service
public class DsaOnboardMemberServiceImpl implements DsaOnboardMemberService {

	@Autowired
	DsaOnboardMemberRepository dsaOnboardMemberRepository;

	@Override
	public DsaOnboardMember getByApplicationNoAndMember(String applicationNo, String member) {
		Optional<DsaOnboardMember> optional = dsaOnboardMemberRepository
				.getByApplicationNoAndMember(Long.parseLong(applicationNo), member);
		if (optional.isPresent()) {
			return optional.get();
		}
		return new DsaOnboardMember();
	}

	@Override
	public String saveData(DsaOnboardMember dsaOnboardMember) {
		dsaOnboardMemberRepository.save(dsaOnboardMember);
		return "update Successfully";
	}

	@Override
	public DsaOnboardMember getByApplicationnoAndMember(String applicationNo, String member) {
		Optional<DsaOnboardMember> optional = dsaOnboardMemberRepository
				.getByApplicationNoAndMember(Long.parseLong(applicationNo), member);
		if (optional.isPresent()) {
			return optional.get();
		}
		throw new NoSuchElementException("No Record Found");
	}

	@Override
	public List<DsaOnboardMemberResponse> getByApplicationNo(long applicationno) {
		List<DsaOnboardMember> list = dsaOnboardMemberRepository.getByApplicationNo(applicationno);
		List<DsaOnboardMemberResponse> list2 = new ArrayList<>();
		for (DsaOnboardMember dsaOnboardMember : list) {
			DsaOnboardMemberResponse dsaOnboardMemberResponse = new DsaOnboardMemberResponse();
			dsaOnboardMemberResponse.setApplicationNo(applicationno);
			dsaOnboardMemberResponse.setPanCardNo(dsaOnboardMember.getPanCardNo());
			dsaOnboardMemberResponse.setPanCardVerify(dsaOnboardMember.isPanCardVerify());
			dsaOnboardMemberResponse.setName(dsaOnboardMember.getName());
			dsaOnboardMemberResponse.setGender(dsaOnboardMember.getGender());
			dsaOnboardMemberResponse.setPresentOccupation(dsaOnboardMember.getPresentOccupation());
			dsaOnboardMemberResponse.setAlternateMobileNo(dsaOnboardMember.getAlternateMobileNo());
			dsaOnboardMemberResponse.setAlternateMobileNoVerify(dsaOnboardMember.isAlternateMobileNoVerify());
			dsaOnboardMemberResponse.setAadharNo(dsaOnboardMember.getAadharNo());
			dsaOnboardMemberResponse.setAadharNoVerify(dsaOnboardMember.isAadharNoVerify());
			dsaOnboardMemberResponse.setMobLinkwithAadhar(dsaOnboardMember.getMobLinkwithAadhar());
			dsaOnboardMemberResponse.setDateOfBirth(dsaOnboardMember.getDateOfBirth());
			dsaOnboardMemberResponse.setMember(dsaOnboardMember.getMember());
			dsaOnboardMemberResponse.setMobile(dsaOnboardMember.getMobile());
			dsaOnboardMemberResponse.setMobileNoVerify(dsaOnboardMember.isMobileNoVerify());
			dsaOnboardMemberResponse.setEntityName(dsaOnboardMember.getEntityName());
			dsaOnboardMemberResponse.setIsMsmeRegister(dsaOnboardMember.getIsMsmeRegister());
			dsaOnboardMemberResponse.setGstNo(dsaOnboardMember.getGstNo());
			dsaOnboardMemberResponse.setEmailId(dsaOnboardMember.getEmailId() != null ? dsaOnboardMember.getEmailId():"NA");
			dsaOnboardMemberResponse.setGstNoVerify(dsaOnboardMember.getGstNoVerify());
			dsaOnboardMemberResponse.setCompanyName(dsaOnboardMember.getCompanyName());
			dsaOnboardMemberResponse.setNameOnPanCard(dsaOnboardMember.getNameOnPanCard());
			dsaOnboardMemberResponse.setDobOnPanCard(dsaOnboardMember.getDobOnPanCard());
			dsaOnboardMemberResponse.setAadharPanLinkStatus(dsaOnboardMember.getAadharPanLinkStatus());
			dsaOnboardMemberResponse.setFatherName(dsaOnboardMember.getFatherName());
			dsaOnboardMemberResponse.setIsPrimaryMember(dsaOnboardMember.getIsPrimaryMember() != null ? dsaOnboardMember.getIsPrimaryMember():"NO");
			dsaOnboardMemberResponse.setAddressForCommunication(dsaOnboardMember.getAddressForCommunication());
			if (dsaOnboardMember.getAddress() != null) {
				String address = dsaOnboardMember.getAddress();
				org.json.JSONArray addressInJson = new org.json.JSONArray(address);
				ObjectMapper objectMapper = new ObjectMapper();
				try {
					List<Address> personList = objectMapper.readValue(address, new TypeReference<List<Address>>() {
					});
					dsaOnboardMemberResponse.setAddress(personList);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}if (dsaOnboardMember.getSecondaryGst() != null) {
				String secondaryGst = dsaOnboardMember.getSecondaryGst();
				ObjectMapper objectMapper = new ObjectMapper();
				try {
					List<GstResponse> gstList = objectMapper.readValue(secondaryGst, new TypeReference<List<GstResponse>>() {
					});
					dsaOnboardMemberResponse.setSecondaryGst(gstList);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			list2.add(dsaOnboardMemberResponse);

		}

		return list2;
	}

	@Override
	public List<DsaOnboardMember> getByApplication(long applicationno) {
		List<DsaOnboardMember> list = dsaOnboardMemberRepository.getByApplicationNo(applicationno);
		return list;
	}

	@Override
	public void saveResponse(String proof, String proofIdNo, String applicationNo, String response, String member) {
		DsaOnboardMember dsaOnboardMember =new DsaOnboardMember();
		Optional<DsaOnboardMember> optional = dsaOnboardMemberRepository
				.getByApplicationNoAndMember(Long.parseLong(applicationNo), member);
		
		if (optional.isPresent()) {
			dsaOnboardMember=optional.get();
		}
		else {
			dsaOnboardMember.setApplicationNo(Long.parseLong(applicationNo));
			dsaOnboardMember.setMember(member);	
		}
			if (proof.equals("panCard")) {
				dsaOnboardMember.setPanCardNo(proofIdNo);
				dsaOnboardMember.setPancardResponse(response);
				JSONObject json=new JSONObject(response);
				dsaOnboardMember.setPanCardVerify(json.getBoolean("isVerify"));
				dsaOnboardMember.setNameOnPanCard(json.getString("NameOnCard"));
				dsaOnboardMember.setDobOnPanCard(json.getString("dobOnCard"));
				dsaOnboardMember.setAadharPanLinkStatus(json.getString("SeedingStatus"));
				
			} else if (proof.equals("gst")) {
				dsaOnboardMember.setGstNo(proofIdNo);
				dsaOnboardMember.setGstNoVerify("YES");
				dsaOnboardMember.setGstResponse(response);
				JSONObject Data = new JSONObject(response);
				String name = Data.getJSONObject("result").getString("lgnm");
				dsaOnboardMember.setCompanyName(name);
//				System.out.println(dsaOnboardMember.getApplicationNo());
			} else if (proof.equals("ekyc")) {
				dsaOnboardMember.setEkycDoneBy(proofIdNo);
				dsaOnboardMember.setEkycVerify("YES");
				dsaOnboardMember.setEkycResponse(response);
				JSONObject ekyc=new JSONObject(response);
				JSONObject PoiResponse = ekyc.getJSONObject("Response").getJSONObject("KycRes").getJSONObject("UidData").getJSONObject("Poi");
				dsaOnboardMember.setNameOnAadhar(PoiResponse.getString("name"));
			}
			dsaOnboardMemberRepository.save(dsaOnboardMember);
		
	}

	@Override
	public List<DeviationDetailsResponse> getDeviationDetails(String applicationNo) {
		List<DeviationDetailsResponse> list=dsaOnboardMemberRepository.getDeviationDetails(Long.parseLong(applicationNo));
		if(list.size()==0) {
			throw new NoSuchElementException("No Record Found");	
		}
		return list;
	}

	@Override
	public DsaOnboardMember findByApplicationNoAndIsPrimary(long applicationNo, String isPrimary) {

		return dsaOnboardMemberRepository.findByApplicationNoAndIsPrimary(applicationNo,isPrimary)
										 .orElseThrow(()-> new NoSuchElementException("No Record Found in DsaOnboardMember"));
		
	}
	
}
