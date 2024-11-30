package com.suryoday.aocpv.service;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import com.suryoday.aocpv.pojo.AocpvImages;
import com.suryoday.aocpv.pojo.Image;

public interface AocpvImageService {

	String saveSingleData(AocpvImages aocpvImages);

	void saveAll(List<AocpvImages> list);

	AocpvImages getByApplicationNo(long applicationNo);

	List<AocpvImages> getByApplicationNoAndMember(long applicationNo,String member,String flowStatus);
	
	void saveCustomerPhoto(MultipartFile[] files, long applicationNoInLong, JSONArray document, String flowStatus);

	void savePhotos(MultipartFile[] files, long applicationNoInLong, JSONArray document, String flowStatus);

	void savePhotoIncome(MultipartFile[] files, long applicationNoInLong, JSONArray document, String flowStatus,
			String member);

	public String getGeoLoactionByApplication(long applicationNo);

	AocpvImages getImageByApplication(Long application);

	String saveMultipleImages(MultipartFile[] files, JSONArray document, long applicationNo, String member);

	void saveAadharPhoto(Map<String, String> map, long applicationNoInLong, JSONArray document, String flowStatus);

	List<Image> fetchByApplicationNoAndFlow(long applicationNoInLong, String flowStatus);

	void updateversionCode(long applicationNoInLong, int versioncode);

	List<AocpvImages> getByappAndVersion(long applicationNo, String member, int versioncode);

	List<AocpvImages> getByapplicationAndVersion(long applicationNo, int versioncode);

	String savePdf(byte[] image,JSONObject jsonObject2, long applicationNoInLong);

	List<AocpvImages> getByApplicationNoAll(long applicationNoInLong);

	List<AocpvImages> getByApplicationNo1(long applicationNo);

	List<AocpvImages> getImageBydocType(long applicationNO);

	List<AocpvImages> getImageForComparison(long applicationNoInLong);

	List<Image> fetchMemberImage(long applicationNoInLong, String member, String document);

	List<String> fetchMemberName(long applicationNoInLong);

	AocpvImages getByApplicationNoAndName(long applicationNoInLong, String string);

	List<AocpvImages> fetchImageforOnePager(long applicationNoInLong);

	String fetchBydocumenttype(long applicationNoInLong, String documentType);

	List<AocpvImages> getByApplicationNoAnddocument(long applicationNoInLong, String string);

	List<Image> getsanctionLetterAndagreement(String applicationNo);

	String fetchMemberImageByApplicationNo(long applicationNoInLong, String member, String string);

	String validateUDImage(long applicationNoInLong);

	void saveImageWeb(byte[] image, JSONArray document, long applicationno, String member);

	List<AocpvImages> getByApplicationId(long applicationNO);

}
