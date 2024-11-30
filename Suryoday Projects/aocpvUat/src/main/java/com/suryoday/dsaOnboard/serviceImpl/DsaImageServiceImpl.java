package com.suryoday.dsaOnboard.serviceImpl;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.aocpv.pojo.GeoLcation;
import com.suryoday.aocpv.pojo.Image;
import com.suryoday.dsaOnboard.pojo.DsaImage;
import com.suryoday.dsaOnboard.repository.DsaImageRepository;
import com.suryoday.dsaOnboard.service.DsaImageService;

@Service
public class DsaImageServiceImpl implements DsaImageService {

	@Autowired
	DsaImageRepository imageRepository;

	@Override
	public void saveImage(MultipartFile[] files, String applicationNo, JSONArray document, String member) {

		for (MultipartFile file : files) {
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			String fileNameInRequest = null;
			String Documenttype = null;
			int id = 1;
			DsaImage dsaImage = new DsaImage();
			if (!document.isEmpty()) {
				for (int n = 0; n < document.length(); n++) {
					JSONObject object = document.getJSONObject(n);
					fileNameInRequest = object.getString("image");
					if (fileName.equals(fileNameInRequest)) {
						dsaImage.setGeoLocation(object.toString());
						Documenttype = object.getString("documentName");
						break;
					}
				}
				if (fileName.equals(fileNameInRequest)) {
					try {
						dsaImage.setApplicationNo(applicationNo);
						dsaImage.setImageName(fileName);
						dsaImage.setType(file.getContentType());
						dsaImage.setImages(file.getBytes());
						dsaImage.setDocumenttype(Documenttype);
						dsaImage.setSize(file.getSize());
						dsaImage.setMember(member);

						Optional<DsaImage> optional = imageRepository.getByApplicationNoAndDocumentType(applicationNo,
								Documenttype);
						if (optional.isPresent()) {
							DsaImage aocpvImage = optional.get();
							dsaImage.setId(aocpvImage.getId());

						}
						if (dsaImage.getGeoLocation() != null) {
							imageRepository.save(dsaImage);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					throw new NoSuchElementException("No Image Found");
				}

			}
		}

	}

	@Override
	public List<DsaImage> getByApplicationNo(String applicationNo) {
		List<DsaImage> list = imageRepository.getByApplicationNo(applicationNo);
		return list;
	}

	@Override
	public void saveAadharPhoto(Map<String, String> files, String applicationNo, JSONArray document, String member) {
		String fileName = "";
		String fileNameInRequest = null;
		String Documenttype = null;
		String content = "";
		String file = "";
		JSONObject jsonObject2 = null;
		int id = 1;
		JSONObject jsonObject = null;
		if (document.isEmpty()) {

		} else {
			for (int n = 0; n < document.length(); n++) {
				DsaImage dsaImage = new DsaImage();
				JSONObject object = document.getJSONObject(n);
				if (object.has("ekyc_photo")) {
					jsonObject2 = object.getJSONObject("ekyc_photo");
					String ekyc_photo = object.getJSONObject("ekyc_photo").toString();
					jsonObject = new JSONObject(ekyc_photo);
					fileName = "photo.jpg";
					content = "jpg/*";
					file = files.get("ekycPhoto");
					dsaImage.setGeoLocation(ekyc_photo);
					dsaImage.setMember(member);
					Documenttype = "ekyc_photo";
				} else if (object.has("ekyc_aadhar")) {
					jsonObject2 = object.getJSONObject("ekyc_aadhar");
					String ekyc_aadhar = object.getJSONObject("ekyc_aadhar").toString();
					jsonObject = new JSONObject(ekyc_aadhar);
					fileName = "photo.pdf";
					file = files.get("ekycAadhar");
					content = "pdf/*";
					dsaImage.setGeoLocation(ekyc_aadhar);
					dsaImage.setMember("SELF_COMPULSORY");
					Documenttype = "ekyc_aadhar";
				}

				fileNameInRequest = jsonObject.getString("image");
				byte[] bytes = Base64.getDecoder().decode(file);
				int length = bytes.length;
				if (fileName.equals(fileNameInRequest)) {
					try {
						dsaImage.setApplicationNo(applicationNo);
						dsaImage.setImageName(fileName);
						dsaImage.setType(content);
						dsaImage.setImages(bytes);
						dsaImage.setDocumenttype(Documenttype);
						dsaImage.setSize(length);

						Optional<DsaImage> optional = imageRepository
								.getByApplicationNoAndDocumentTypeAndMember(applicationNo, Documenttype, member);
						if (optional.isPresent()) {
							DsaImage dsaImage2 = optional.get();
							dsaImage.setId(dsaImage2.getId());

						}

						if (dsaImage.getGeoLocation() != null) {
							imageRepository.save(dsaImage);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		}

	}

	@Override
	public List<DsaImage> getByApplicationNoAndMember(String applicationNo, String member) {
		List<DsaImage> list = imageRepository.getByApplicationNoAndMember(applicationNo, member);
		return list;
	}

	@Override
	public List<Image> fetchByDocumentType(String applicationNo, String documentType) {
		List<DsaImage> list = imageRepository.fetchByDocumentType(applicationNo, documentType);
		if (list.size() == 0) {
			throw new NoSuchElementException("No Image Found");
		}
		List<Image> listOfImages = new ArrayList<>();
		for (DsaImage dsaImage : list) {

			String geoLocation = dsaImage.getGeoLocation();

			JSONObject jsonObjectImage = new JSONObject(geoLocation);

			String pimage = jsonObjectImage.getString("image");
			String pLat = jsonObjectImage.getString("Lat");
			String pLong = jsonObjectImage.getString("Long");
			String pAddress = jsonObjectImage.getString("Address");
			String ptimestamp = jsonObjectImage.getString("timestamp");

			GeoLcation geolocation = new GeoLcation(pimage, pLat, pLong, pAddress, ptimestamp);

			String documenttype = dsaImage.getDocumenttype();
			String imageName = dsaImage.getImageName();
			String type = dsaImage.getType();
			long size = dsaImage.getSize();
			String member1 = dsaImage.getMember();
			byte[] images2 = dsaImage.getImages();
			String encoded = Base64.getEncoder().encodeToString(images2);

			Image images = new Image(documenttype, imageName, type, size, encoded, member1, geolocation);

			listOfImages.add(images);

		}
		return listOfImages;
	}

	@Override
	public List<String> getDocumentTypes(String applicationNo) {
		List<String> list = imageRepository.getDocumentTypes(applicationNo);
		return list;
	}

	@Override
	public void savepdf(byte[] image, JSONObject object, String applicationNo) {
		JSONObject jsonObject = null;
		String content = "";
		String Documenttype = null;
		int id = 1;

		DsaImage dsaImage = new DsaImage();
		if (object.has("connectorServiceAgreement")) {
			jsonObject = object.getJSONObject("connectorServiceAgreement");
			String connectorServiceAgreement = object.getJSONObject("connectorServiceAgreement").toString();
			content = "pdf/*";
			dsaImage.setGeoLocation(connectorServiceAgreement);
			Documenttype = "connectorServiceAgreementPdf";
		}

		else if (object.has("dsaAgreement")) {
			jsonObject = object.getJSONObject("dsaAgreement");
			String loanApplicationForm = object.getJSONObject("dsaAgreement").toString();
			content = "pdf/*";
			dsaImage.setGeoLocation(loanApplicationForm);
			Documenttype = "dsaAgreementPdf";
		}
		String fileNameInRequest = jsonObject.getString("image");
		int length = image.length;

		try {
			dsaImage.setApplicationNo(applicationNo);
			dsaImage.setImageName(fileNameInRequest);
			dsaImage.setType(content);
			dsaImage.setImages(image);
			dsaImage.setDocumenttype(Documenttype);
			dsaImage.setSize(length);
			dsaImage.setMember("SELF");

			Optional<DsaImage> optional = imageRepository.getByApplicationNoAndDocumentType(applicationNo,
					Documenttype);
			if (optional.isPresent()) {
				DsaImage aocpvImage = optional.get();
				dsaImage.setId(aocpvImage.getId());

			}

			if (dsaImage.getGeoLocation() != null) {
				imageRepository.save(dsaImage);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public Set<String> getDocumentTypesWeb(String applicationNo) {
		Set<String> list = imageRepository.getDocumentTypesWeb(applicationNo);
		if (list.size() == 0) {
			throw new NoSuchElementException("No Image Found");
		} else {
			return list;
		}
	}

	@Override
	public void saveImageWeb(byte[] image, JSONArray document, String applicationNo, String member) {
		String Documenttype = null;
		String content = "";
		String fileNameInRequest = null;
		int id = 1;
		DsaImage dsaImage = new DsaImage();
		for (int n = 0; n < document.length(); n++) {
			JSONObject object = document.getJSONObject(n);

			dsaImage.setGeoLocation(object.toString());
			Documenttype = object.getString("documentName");
			fileNameInRequest = object.getString("image");
			break;
		}

		int length = image.length;

		try {
			dsaImage.setApplicationNo(applicationNo);
			dsaImage.setImageName(fileNameInRequest);
			dsaImage.setType(content);
			dsaImage.setImages(image);
			dsaImage.setDocumenttype(Documenttype);
			dsaImage.setSize(length);
			dsaImage.setMember(member);
			Optional<DsaImage> optional = imageRepository.getByApplicationNoAndDocumentType(applicationNo,
					Documenttype);
			if (optional.isPresent()) {
				DsaImage aocpvImage = optional.get();
				dsaImage.setId(aocpvImage.getId());

			}

			if (dsaImage.getGeoLocation() != null) {
				imageRepository.save(dsaImage);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
