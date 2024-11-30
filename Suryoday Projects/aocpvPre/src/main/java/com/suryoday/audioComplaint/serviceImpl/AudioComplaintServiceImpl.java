package com.suryoday.audioComplaint.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.suryoday.audioComplaint.entity.AudioComplaintDetails;
import com.suryoday.audioComplaint.entity.AudioComplaintResponse;
import com.suryoday.audioComplaint.entity.GeoLcation;
import com.suryoday.audioComplaint.exceptionHandler.ResourceNotFoundException;
import com.suryoday.audioComplaint.repository.AudioComplaintRepository;
import com.suryoday.audioComplaint.service.AudioComplaintService;

@Service
public class AudioComplaintServiceImpl implements AudioComplaintService{

	@Autowired
	private AudioComplaintRepository audioComplaintRepository;
	
	@Override
	public void saveAudioComplaint(MultipartFile[] files, String complaintNo, JSONArray document, String x_User_ID, String branchId) {
		
		
		for(MultipartFile file : files ) {
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			String fileNameInRequest=null;
			String Documenttype=null;
			int id=1;
			AudioComplaintDetails images=new AudioComplaintDetails();
			if(!document.isEmpty()) {
				for(int n=0 ; n<document.length();n++) {
					JSONObject object = document.getJSONObject(n);
					 fileNameInRequest = object.getString("image");
					 if(fileName.equals(fileNameInRequest)) {
						 images.setGeoLocation(object.toString());
						 Documenttype=object.getString("documentName");
						 break;
					 }
				}
				if(fileName.equals(fileNameInRequest)) {
				try {
					images.setComplaintNo(complaintNo);
					images.setImageName(fileName);
					images.setType(file.getContentType());	
					images.setImages(file.getBytes());
					images.setSize(file.getSize());
					images.setCreationDate(LocalDateTime.now());
					images.setUpdatedDate(LocalDateTime.now());
					images.setStatus("PROGRESS");
					images.setUserId(x_User_ID);
					images.setBranchId(branchId);
//			Optional<AudioComplaintDetails> optional=audioComplaintRepository.getBycomplaintNo(complaintNo, Documenttype);
//					if(optional.isPresent()) {
//						AudioComplaintDetails aocpvImage = optional.get();
//						images.setId(aocpvImage.getId());
//					}
					if(images.getGeoLocation() != null) {
						audioComplaintRepository.save(images);	
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				}
				else {
					throw new ResourceNotFoundException("No Record Found");
				}
				
			}
		}
	}

	@Override
	public List<AudioComplaintResponse> fetchByBranchId(String userId, String branchId) {
		List<AudioComplaintDetails> list=audioComplaintRepository.fetchByBranchId(userId,branchId);
		if(list.size()==0) {
			throw new ResourceNotFoundException("No Record Found");	
		}
		List<AudioComplaintResponse> listOfImages = new ArrayList<>();
		for(AudioComplaintDetails audioComplaintDetails:list) {
		
			String geoLocation = audioComplaintDetails.getGeoLocation();

			JSONObject jsonObjectImage = new JSONObject(geoLocation);

			String pimage = jsonObjectImage.getString("image");
			String pLat = jsonObjectImage.getString("Lat");
			String pLong = jsonObjectImage.getString("Long");
			String pAddress = jsonObjectImage.getString("Address");
			String ptimestamp = jsonObjectImage.getString("timestamp");
	
			GeoLcation geolocation = new GeoLcation(pimage, pLat, pLong, pAddress, ptimestamp);
		
			byte[] images2 = audioComplaintDetails.getImages();
			String encoded = Base64.getEncoder().encodeToString(images2);
			
			AudioComplaintResponse audioComplaintResponse=new AudioComplaintResponse();
			audioComplaintResponse.setAudioName(audioComplaintDetails.getImageName());
			audioComplaintResponse.setType(audioComplaintDetails.getType());
			audioComplaintResponse.setSize(audioComplaintDetails.getSize());
			audioComplaintResponse.setAudio(encoded);
			audioComplaintResponse.setGeoLocation(geolocation);
			audioComplaintResponse.setComplaintNo(audioComplaintDetails.getComplaintNo());
			audioComplaintResponse.setCreationDate(audioComplaintDetails.getCreationDate());
			audioComplaintResponse.setUpdatedDate(audioComplaintDetails.getUpdatedDate());
			audioComplaintResponse.setStatus(audioComplaintDetails.getStatus());
			audioComplaintResponse.setUserId(audioComplaintDetails.getUserId());
			
			listOfImages.add(audioComplaintResponse);
			
		}
		return listOfImages;
	}

	@Override
	public AudioComplaintDetails fetchByComplaintNo(String complaintNo) {
		
		Optional<AudioComplaintDetails> optional=audioComplaintRepository.getByComplaintNo(complaintNo);
		if(optional.isPresent()){
			return optional.get();
		}
		throw new ResourceNotFoundException("No Record Found");	
	}

	@Override
	public void save(AudioComplaintDetails audioComplaintDetails) {
		audioComplaintRepository.save(audioComplaintDetails);
		
	}

	@Override
	public List<AudioComplaintResponse> fetchByDate(LocalDateTime startdate, LocalDateTime enddate) {
		
		List<AudioComplaintDetails> list=audioComplaintRepository.fetchByDate(startdate,enddate);
		if(list.size()==0) {
			throw new ResourceNotFoundException("No Record Found");	
		}
		List<AudioComplaintResponse> listOfImages = new ArrayList<>();
		for(AudioComplaintDetails audioComplaintDetails:list) {
		
			String geoLocation = audioComplaintDetails.getGeoLocation();

			JSONObject jsonObjectImage = new JSONObject(geoLocation);

			String pimage = jsonObjectImage.getString("image");
			String pLat = jsonObjectImage.getString("Lat");
			String pLong = jsonObjectImage.getString("Long");
			String pAddress = jsonObjectImage.getString("Address");
			String ptimestamp = jsonObjectImage.getString("timestamp");
	
			GeoLcation geolocation = new GeoLcation(pimage, pLat, pLong, pAddress, ptimestamp);
		
			byte[] images2 = audioComplaintDetails.getImages();
			String encoded = Base64.getEncoder().encodeToString(images2);
			
			AudioComplaintResponse audioComplaintResponse=new AudioComplaintResponse();
			audioComplaintResponse.setAudioName(audioComplaintDetails.getImageName());
			audioComplaintResponse.setType(audioComplaintDetails.getType());
			audioComplaintResponse.setSize(audioComplaintDetails.getSize());
			audioComplaintResponse.setAudio(encoded);
			audioComplaintResponse.setGeoLocation(geolocation);
			audioComplaintResponse.setComplaintNo(audioComplaintDetails.getComplaintNo());
			audioComplaintResponse.setCreationDate(audioComplaintDetails.getCreationDate());
			audioComplaintResponse.setUpdatedDate(audioComplaintDetails.getUpdatedDate());
			audioComplaintResponse.setStatus(audioComplaintDetails.getStatus());
			audioComplaintResponse.setUserId(audioComplaintDetails.getUserId());
			audioComplaintResponse.setRemarks(audioComplaintDetails.getRemarks());
			audioComplaintResponse.setBranchId(audioComplaintDetails.getBranchId());
			
			
			listOfImages.add(audioComplaintResponse);
			
		}
		return listOfImages;

	}

	@Override
	public boolean validateSessionId(String x_Session_ID, HttpServletRequest request) {
		try {
			String allSessionIds = audioComplaintRepository.getAllSessionIds(x_Session_ID);
			
			if (!allSessionIds.isEmpty()) {

				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.getMessage();

			return false;
		}
	}

	@Override
	public String getSessionId(String id, HttpServletRequest request) {
		Object attribute = request.getSession().getAttribute(id);
		String sessionId = "";
		if (attribute != null) {
			  List<String> list = audioComplaintRepository.fetchUser(id);
			if (list.size() != 0) {
				
				for(String primaryID:list) {
				int deleteSession = audioComplaintRepository.deleteSession(primaryID);	
				}
					request.getSession().invalidate();
					request.getSession().setAttribute(id, id);
					sessionId = request.getSession().getId();
					return sessionId;
				
			}
			else {
				request.getSession().invalidate();
				request.getSession().setAttribute(id, id);
				sessionId = request.getSession().getId();
				return sessionId;
			}
		} else {
			request.getSession().invalidate();
			request.getSession().setAttribute(id, id);
			sessionId = request.getSession().getId();
			return sessionId;
		}
	
		
	}

}
