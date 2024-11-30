package com.suryoday.CustomerIntraction.Service;

import java.util.List;

import org.json.JSONArray;

import com.suryoday.CustomerIntraction.Pojo.MeetingImage;

public interface ImageService {
	
	public void saveImageWeb(JSONArray imageArray,String meetingNumber, List<MeetingImage> image);

}
