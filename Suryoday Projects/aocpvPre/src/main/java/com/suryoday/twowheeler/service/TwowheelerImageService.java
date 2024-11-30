package com.suryoday.twowheeler.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import com.suryoday.aocpv.pojo.Image;
import com.suryoday.twowheeler.pojo.TwowheelerImage;

public interface TwowheelerImageService {

	void savePhoto(MultipartFile[] files, String applicationNo, JSONArray document, String flowStatus, String member);

	List<TwowheelerImage> getByApplicationNoAndDocument(String applicationNo, String string);

	List<Image> getByAppNoAndMember(String applicationNo, String member, String documentType);

	void saveAadharPhoto(Map<String, String> map, String applicationNo, JSONArray jsonArray, String string);

	void savepdf(byte[] inFileBytes, JSONObject sanctionLetter, String applicationNo);

	void saveImage(MultipartFile[] files, String applicationNo, JSONArray document, String flowStatus, String member);

	void saveImageWeb(byte[] image, JSONArray document, String applicationNo, String member);

	List<String> getDocumentTypes(String applicationNo);

	Set<String> getDocumentTypesWeb(String applicationNo);

}
