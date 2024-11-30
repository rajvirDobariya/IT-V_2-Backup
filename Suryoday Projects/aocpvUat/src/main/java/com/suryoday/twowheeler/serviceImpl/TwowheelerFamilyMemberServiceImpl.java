package com.suryoday.twowheeler.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.twowheeler.pojo.AddressDetails;
import com.suryoday.twowheeler.pojo.GuardianDetails;
import com.suryoday.twowheeler.pojo.TWNominee;
import com.suryoday.twowheeler.pojo.TwoWheelerFamilyMember;
import com.suryoday.twowheeler.pojo.TwoWheelerFamilyMemberResponse;
import com.suryoday.twowheeler.pojo.TwowheelerDetailesTable;
import com.suryoday.twowheeler.repository.TwowheelerFamilyMemberRepository;
import com.suryoday.twowheeler.service.TwowheelerDetailsService;
import com.suryoday.twowheeler.service.TwowheelerFamilyMemberService;

@Service
public class TwowheelerFamilyMemberServiceImpl implements TwowheelerFamilyMemberService {

	@Autowired
	TwowheelerFamilyMemberRepository familyMemberRepository;

	@Autowired
	TwowheelerDetailsService twowheelerDetailsService;

	@Override
	public String saveData(TwoWheelerFamilyMember familyMember) {
		if (familyMember.getId() == 0) {
			int id = 1;
			Optional<Integer> optional1 = familyMemberRepository.fetchLastId();
			if (optional1.isPresent()) {
				id = optional1.get();
				id++;
			}
			familyMember.setId(id);
			Optional<TwoWheelerFamilyMember> optional = familyMemberRepository
					.findBymember(familyMember.getApplicationNo(), familyMember.getMember());
			if (optional.isPresent()) {
				TwoWheelerFamilyMember twoWheelerFamilyMember = optional.get();
				familyMember.setId(twoWheelerFamilyMember.getId());
			}
		}

		familyMemberRepository.save(familyMember);
		return "done";
	}

	@Override
	public List<TwoWheelerFamilyMember> getByApplicationNo(String applicationNo) {
		List<TwoWheelerFamilyMember> list = familyMemberRepository.getByApplicationNo(applicationNo);
		if (list.size() == 0) {
			throw new NoSuchElementException("No Record Found");
		}
		return list;
	}

	@Override
	public Optional<TwoWheelerFamilyMember> fetchByApplicationNo(String applicationNo) {
		Optional<TwoWheelerFamilyMember> optional = familyMemberRepository.fetchByApplicationId(applicationNo);
		return optional;
	}

	@Override
	public void saveResponse(String proof, String proofId, String applicationNo, String response, String member) {
		if (member.equals("")) {
			member = "APPLICANT";
		}
		Optional<TwoWheelerFamilyMember> findBymember = familyMemberRepository.findBymember(applicationNo, member);
		TwoWheelerFamilyMember familyMember = new TwoWheelerFamilyMember();
		if (findBymember.isPresent()) {
			familyMember = findBymember.get();
		} else {
			int id = 1;
			Optional<Integer> optional1 = familyMemberRepository.fetchLastId();
			if (optional1.isPresent()) {
				id = optional1.get();
				id++;
			}
			familyMember.setApplicationNo(applicationNo);
			familyMember.setId(id);
			familyMember.setMember(member);
		}
		if (proof.equalsIgnoreCase("panCard")) {
			familyMember.setPanCard(proofId);
			JSONObject json = new JSONObject(response);
			familyMember.setPancardNoVerify(json.getString("isVerify"));
			familyMember.setPanCardResponse(response);
			familyMember.setMember(member);
			familyMember.setNameOnPan(json.getString("NameOnCard"));
			familyMember.setDobOnPancard(json.getString("dobOnCard"));
		} else if (proof.equalsIgnoreCase("voterId")) {
			familyMember.setVoterId(proofId);
			familyMember.setVoterIdVerify("YES");

		} else if (proof.equalsIgnoreCase("ekyc")) {
			TwowheelerDetailesTable detailesTable = twowheelerDetailsService.getByApplication(applicationNo);
			familyMember.setEkycResponse(response);
			familyMember.setEkycVerify("YES");
			familyMember.setEkycDoneBy(proofId);
			JSONObject ekyc = new JSONObject(response);
			JSONObject PoiResponse = ekyc.getJSONObject("Response").getJSONObject("KycRes").getJSONObject("UidData")
					.getJSONObject("Poi");
			familyMember.setNameOnAAdhar(PoiResponse.getString("name"));
			if (ekyc.has("referenceNo")) {
				familyMember.setAadharCard(ekyc.getString("referenceNo"));
				familyMember.setAadharNoVerify("YES");
			}
			if (detailesTable.getName() == null) {
				detailesTable.setName(PoiResponse.getString("name"));
				detailesTable.setDateOfBirth(PoiResponse.get("dob").toString());
				twowheelerDetailsService.saveData(detailesTable);
			}

		} else if (proof.equalsIgnoreCase("aadharCard")) {
			JSONObject jsonObject = new JSONObject(response);
			Long referenceNo1 = jsonObject.getJSONArray("Data").getJSONObject(0).getLong("ReferenceNumber");
			String referenceNo = referenceNo1.toString();
			familyMember.setAadharCard(proofId);
			familyMember.setAadharNoVerify("YES");
		}
		familyMemberRepository.save(familyMember);
	}

	@Override
	public TwoWheelerFamilyMember getByApplicationNoAndMember(String applicationNo, String member) {

		return familyMemberRepository.findBymember(applicationNo, member)
				.orElseThrow(() -> new NoSuchElementException("No Record Found"));
	}

	@Override
	public void save(TwoWheelerFamilyMember familyMember) {
		familyMemberRepository.save(familyMember);

	}

	@Override
	public TwoWheelerFamilyMember fetchByApplicationNoAndMember(String applicationNo, String member) {

		return familyMemberRepository.findBymember(applicationNo, member).orElseGet(() -> new TwoWheelerFamilyMember());
	}

	@Override
	public List<TWNominee> getNomineeDetails(String applicationNo) {
		Optional<TwoWheelerFamilyMember> optional = familyMemberRepository.findBymember(applicationNo, "Nominee");
		List<TWNominee> list = new ArrayList<>();
		if (optional.isPresent()) {
			TWNominee nominee = new TWNominee();
			TwoWheelerFamilyMember twoWheelerFamilyMember = optional.get();
			nominee.setFirstName(twoWheelerFamilyMember.getFirstName());
			nominee.setMiddleName(twoWheelerFamilyMember.getMiddleName());
			nominee.setLastName(twoWheelerFamilyMember.getLastName());
			nominee.setDob(twoWheelerFamilyMember.getDob());
			nominee.setAge(twoWheelerFamilyMember.getAge());
			nominee.setAddress(twoWheelerFamilyMember.getAddress());
			nominee.setNomineeRelationship(twoWheelerFamilyMember.getNomineeRelationship());
			nominee.setMobile(twoWheelerFamilyMember.getMobile());
			nominee.setMember(twoWheelerFamilyMember.getMember());
			nominee.setMarried(twoWheelerFamilyMember.getMarried());
			nominee.setApplicationNo(twoWheelerFamilyMember.getApplicationNo());
			String guardian = twoWheelerFamilyMember.getGuardian();
			if (guardian != null) {
				org.json.JSONObject guardiandetails = new org.json.JSONObject(guardian);
				GuardianDetails guardianDetails = new GuardianDetails();
				guardianDetails.setGuardianName(guardiandetails.getString("guardianName"));
				guardianDetails.setGuardianRelationship(guardiandetails.getString("guardianRelationship"));
				guardianDetails.setGuardianAddress(guardiandetails.getString("guardianAddress"));
				guardianDetails.setGuardianDob(guardiandetails.getString("guardianDob"));
				guardianDetails.setGuardianAge(guardiandetails.getString("guardianAge"));
				nominee.setGuardianDetails(guardianDetails);
			}
			list.add(nominee);
			return list;
		}

		throw new NoSuchElementException("No Record Found");
	}

	@Override
	public List<TwoWheelerFamilyMemberResponse> getFamilyDetails(String applicationNo) {
		List<TwoWheelerFamilyMember> list = familyMemberRepository.fetchByApplication(applicationNo);
		if (list.size() == 0) {
			throw new NoSuchElementException("No Record Found");
		} else {
			List<TwoWheelerFamilyMemberResponse> listOffamilyMember = new ArrayList<>();
			for (TwoWheelerFamilyMember familyMember : list) {
				TwoWheelerFamilyMemberResponse response = new TwoWheelerFamilyMemberResponse();
				response.setApplicationNo(familyMember.getApplicationNo());
				response.setId(familyMember.getId());
				response.setFirstName(familyMember.getFirstName());
				response.setMiddleName(familyMember.getMiddleName());
				response.setLastName(familyMember.getLastName());
				response.setTitle(familyMember.getTitle());
				response.setOccupation(familyMember.getOccupation());
				response.setGender(familyMember.getGender());
				response.setAge(familyMember.getAge());
				response.setDob(familyMember.getDob());
				response.setAppNoWithProductCode(familyMember.getAppNoWithProductCode());
				response.setCreatedTimestamp(familyMember.getCreatedTimestamp());
				response.setMobile(familyMember.getMobile());
				response.setMobileVerify(familyMember.getMobileVerify());
				response.setMarried(familyMember.getMarried());
				response.setAadharCard(familyMember.getAadharCard());
				response.setAadharNoVerify(familyMember.getAadharNoVerify());
				response.setPanCard(familyMember.getPanCard());
				response.setPancardNoVerify(familyMember.getPancardNoVerify());
				response.setVoterId(familyMember.getVoterId());
				response.setVoterIdVerify(familyMember.getVoterId());
				response.setEarning(familyMember.getEarning());
				response.setMember(familyMember.getMember());
				response.setEkycVerify(familyMember.getEkycVerify());
				response.setEkycDoneBy(familyMember.getEkycDoneBy());
				response.setUpdatedTimestamp(familyMember.getUpdatedTimestamp());
				response.setCategory(familyMember.getCategory());
				response.setGuardian(familyMember.getGuardian());
				response.setNomineeRelationship(familyMember.getNomineeRelationship());
				response.setFatherName(familyMember.getFatherName());
				response.setMotherName(familyMember.getMotherName());
				response.setCaste(familyMember.getCaste());
				response.setReligion(familyMember.getReligion());
				response.setEducation(familyMember.getEducation());
				response.setNoOfDependent(familyMember.getNoOfDependent());
				response.setNoOfFamilyMember(familyMember.getNoOfFamilyMember());
				response.setAnnualIncome(familyMember.getAnnualIncome());
				response.setCompanyName(familyMember.getCompanyName());
				response.setForm60(familyMember.getForm60());
				response.setSelectedIdentityProof(familyMember.getSelectedIdentityProof());
				response.setIdentityProof(familyMember.getIdentityProof());
				response.setIdentityProofVerify(familyMember.getIdentityProofVerify());
				response.setOccCode(familyMember.getOccCode());
				response.setReligionCode(familyMember.getReligionCode());
				response.setCasteCode(familyMember.getCasteCode());
				response.setEducationCode(familyMember.getEducationCode());
				response.setMarriedCode(familyMember.getMarriedCode());
				response.setGenderCode(familyMember.getGenderCode());
				response.setTitleCode(familyMember.getTitle());
				response.setCategoryCode(familyMember.getCategoryCode());
				response.setEmailId(familyMember.getEmailId());
				response.setEmploymentType(familyMember.getEmploymentType());
				response.setWorkStability(familyMember.getWorkStability());
				response.setNegativeLocality(familyMember.getNegativeLocality());
				response.setLocalityName(familyMember.getLocalityName());
				response.setDistanceFromBranch(familyMember.getDistanceFromBranch());
				response.setNameOnPan(familyMember.getNameOnPan());
				response.setDobOnPancard(familyMember.getDobOnPancard());
				response.setAmlScore(familyMember.getAmlScore());

				if (familyMember.getAddressArray() != null) {
					ObjectMapper objectMapper = new ObjectMapper();
					try {
						List<AddressDetails> reference = objectMapper.readValue(familyMember.getAddressArray(),
								new TypeReference<List<AddressDetails>>() {
								});
						response.setAddressArray(reference);
					} catch (Exception e) {

					}
				}
				listOffamilyMember.add(response);
			}
			return listOffamilyMember;
		}

	}

	@Override
	public TwoWheelerFamilyMember fetchByAppAndPan(String applicationNo, String pancard) {
		Optional<TwoWheelerFamilyMember> optional = familyMemberRepository.fetchByAppAndPan(applicationNo, pancard);

		TwoWheelerFamilyMember twoWheelerFamilyMember = new TwoWheelerFamilyMember();
		twoWheelerFamilyMember.setApplicationNo(applicationNo);
		twoWheelerFamilyMember.setMember("APPLICANT");
		return optional.orElse(twoWheelerFamilyMember);
	}

	@Override
	public TwoWheelerFamilyMember findById(int id) {
		return familyMemberRepository.findById(id).orElseGet(() -> new TwoWheelerFamilyMember());
	}

	@Override
	public void updateMember(String member, String applicationNo, String bureauaScore) {
		Optional<TwoWheelerFamilyMember> optional = familyMemberRepository.findBymember(applicationNo, member);
		if (optional.isPresent()) {
			TwoWheelerFamilyMember twoWheelerFamilyMember = optional.get();
			twoWheelerFamilyMember.setCibilScore(bureauaScore);
			familyMemberRepository.save(twoWheelerFamilyMember);

		}
	}

}
