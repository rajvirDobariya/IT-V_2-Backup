package com.suryoday.roaocpv.service;

import java.io.IOException;

import org.json.JSONObject;

public interface DMSUploadService {

	public JSONObject dmsupload(long applicationNO) throws IOException;

}