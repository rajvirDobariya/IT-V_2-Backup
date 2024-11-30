package com.suryoday.dsaOnboard.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import com.suryoday.aocpv.pojo.Image;
import com.suryoday.dsaOnboard.pojo.DsaImage;

public interface DsaImageService {

	void saveImage(MultipartFile[] files, String applicationNo, JSONArray document, String member);

	List<DsaImage> getByApplicationNo(String applicationNo);

	void saveAadharPhoto(Map<String, String> map, String applicationNo, JSONArray jsonArray, String member);

	List<DsaImage> getByApplicationNoAndMember(String applicationNo, String member);

	List<Image> fetchByDocumentType(String applicationNo, String documentType);

	List<String> getDocumentTypes(String applicationNo);

	void savepdf(byte[] image, JSONObject loanAgreement1, String applicationNo);

	Set<String> getDocumentTypesWeb(String applicationNo);

	void saveImageWeb(byte[] image, JSONArray document, String applicationNo, String member);

}
