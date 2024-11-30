package com.digitisation.branchreports.mapper;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.digitisation.branchreports.model.Filetable;
import com.digitisation.branchreports.utils.FileUtils;

@Service
public class FiletableMapper {

	public JSONObject convertToJson(Filetable filetable) {
		JSONObject report = new JSONObject();
		int i = 0;
		report.put("id", filetable.getId());
		report.put("fileName", filetable.getName());
		report.put("fileType", filetable.getType());
		byte[] fileBytes = FileUtils.convertFileToBytes(filetable.getBase64());
		report.put("fileBytes", fileBytes);
		return report;
	}

}
