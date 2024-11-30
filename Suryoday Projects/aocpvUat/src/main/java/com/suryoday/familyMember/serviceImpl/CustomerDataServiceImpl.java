package com.suryoday.familyMember.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.aocpv.exceptionhandling.DuplicateEntryException;
import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.familyMember.pojo.CustomerData;
import com.suryoday.familyMember.pojo.FamilyMember;
import com.suryoday.familyMember.pojo.FamilyMemberImages;
import com.suryoday.familyMember.pojo.FamilyMemberResponse;
import com.suryoday.familyMember.pojo.GeoLcation;
import com.suryoday.familyMember.pojo.Image;
import com.suryoday.familyMember.repository.CustomerDataRepository;
import com.suryoday.familyMember.repository.FamilyMemberRepository;
import com.suryoday.familyMember.repository.MemberImageRepository;
import com.suryoday.familyMember.service.CustomerDataService;

@Service
public class CustomerDataServiceImpl implements CustomerDataService {

	@Autowired
	CustomerDataRepository customerDataRepository;

	@Autowired
	FamilyMemberRepository familyMemberRepository;

	@Autowired
	MemberImageRepository imageRepository;

	@Override
	public CustomerData fetchBycustomerId(String customerId) {
		Optional<CustomerData> optional = customerDataRepository.getByCustomerId(customerId);
		if (optional.isPresent()) {
			return optional.get();
		}
		throw new NoSuchElementException("No Record Found ");
	}

	@Override
	public String saveFamilyMember(FamilyMember familyMember) {
		int id = 1;
		Optional<FamilyMember> optional = familyMemberRepository
				.fetchByCustomerIdAndMember(familyMember.getCustomerId(), familyMember.getMember());
		if (optional.isPresent()) {
			familyMember.setId(optional.get().getId());
		} else {
			Optional<Integer> optional1 = familyMemberRepository.fetchLastId();
			if (optional1.isPresent()) {
				id = optional1.get();
				id++;
				familyMember.setId(id);
			}
		}
		familyMemberRepository.save(familyMember);
		return "Data Saved";
	}

	@Override
	public List<FamilyMemberResponse> fetchFamilyMember(String customerId) {
		List<FamilyMember> list = familyMemberRepository.getByCustomerId(customerId);
		if (list.size() == 0) {
			throw new NoSuchElementException("List is empty");
		}
		List<FamilyMemberResponse> listofFamily = new ArrayList<>();

		for (FamilyMember familyMember : list) {
			FamilyMemberResponse familyMemberResponse = new FamilyMemberResponse();
			familyMemberResponse.setCustomerId(familyMember.getCustomerId());
			familyMemberResponse.setMember(familyMember.getMember());
			familyMemberResponse.setEarning(familyMember.getEarning());
			familyMemberResponse.setOccupation(familyMember.getOccupation());
			familyMemberResponse.setOccCode(familyMember.getOccCode());
			familyMemberResponse.setPrimarySourceOfIncome(familyMember.getPrimarySourceOfIncome());
			familyMemberResponse.setMonthlyIncome(familyMember.getMonthlyIncome());
			familyMemberResponse.setMonthlyLoanEmi(familyMember.getMonthlyLoanEmi());
			familyMemberResponse.setTitle(familyMember.getTitle());
			familyMemberResponse.setFirstName(familyMember.getFirstName());
			familyMemberResponse.setLastName(familyMember.getLastName());
			familyMemberResponse.setGender(familyMember.getGender());
			familyMemberResponse.setAge(familyMember.getAge());
			familyMemberResponse.setDob(familyMember.getDob());
			familyMemberResponse.setMobile(familyMember.getMobile());
			familyMemberResponse.setMobileVerify(familyMember.getMobileVerify());
			familyMemberResponse.setMarried(familyMember.getMarried());
			familyMemberResponse.setAadharCard(familyMember.getAadharCard());
			familyMemberResponse.setAadharNoVerify(familyMember.getAadharNoVerify());
			familyMemberResponse.setPanCard(familyMember.getPanCard());
			familyMemberResponse.setPancardNoVerify(familyMember.getPancardNoVerify());
			familyMemberResponse.setVoterId(familyMember.getVoterId());
			familyMemberResponse.setVoterIdVerify(familyMember.getVoterIdVerify());

			List<FamilyMemberImages> imageList = imageRepository.getByCustomerIdMember(customerId,
					familyMember.getMember());

			List<Image> listOfImages = new ArrayList<>();
			for (FamilyMemberImages familyMemberImages : imageList) {
				String geoLocation = familyMemberImages.getGeoLocation();
				JSONObject jsonObjectImage = new JSONObject(geoLocation);
				String pimage = jsonObjectImage.getString("image");
				String pLat = jsonObjectImage.getString("Lat");
				String pLong = jsonObjectImage.getString("Long");
				String pAddress = jsonObjectImage.getString("Address");
				String ptimestamp = jsonObjectImage.getString("timestamp");
				GeoLcation geolocation = new GeoLcation(pimage, pLat, pLong, pAddress, ptimestamp);

				String documenttype = familyMemberImages.getDocumenttype();
				String imageName = familyMemberImages.getImageName();
				String type = familyMemberImages.getType();
				long size = familyMemberImages.getSize();
				String member = familyMemberImages.getMember();
				byte[] images2 = familyMemberImages.getImages();
				String encoded = Base64.getEncoder().encodeToString(images2);

				Image images = new Image(documenttype, imageName, type, size, encoded, geolocation, member);
				System.out.println(images);
				listOfImages.add(images);

			}
			familyMemberResponse.setImage(listOfImages);
			listofFamily.add(familyMemberResponse);
		}
		return listofFamily;

	}

	@Override
	public String deleteMember(String customerId, String member) {
		Optional<FamilyMember> optional = familyMemberRepository.fetchByCustomerIdAndMember(customerId, member);
		if (optional.isPresent()) {
			familyMemberRepository.delete(optional.get());
		} else {
			throw new NoSuchElementException("member Not present");
		}
		return "member deleted";
	}

	@Override
	public String statuschange(String customerId, String status) {
		Optional<CustomerData> optional = customerDataRepository.getByCustomerId(customerId);
		if (optional.isPresent()) {
			CustomerData customerData = optional.get();
			LocalDateTime now = LocalDateTime.now();
			customerData.setStatus(status);
			customerData.setUpdataDate(now);
			customerDataRepository.save(customerData);
			return "status updated";
		}
		throw new NoSuchElementException("No Record Found ");
	}

	@Override
	public void validatePancard(String panCardNo) {
		Optional<FamilyMember> optional = familyMemberRepository.getByPanCard(panCardNo);
		if (optional.isPresent()) {
			throw new DuplicateEntryException("pancard no is already exist");
		}
	}

	@Override
	public void validateAadharCard(String referenceNo) {
		Optional<FamilyMember> optional = familyMemberRepository.getByAadharCard(referenceNo);
		if (optional.isPresent()) {
			throw new DuplicateEntryException("Aadhar no is already exist");
		}

	}

}
