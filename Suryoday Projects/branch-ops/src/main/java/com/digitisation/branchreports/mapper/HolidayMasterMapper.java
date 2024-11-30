package com.digitisation.branchreports.mapper;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.digitisation.branchreports.model.HolidayMaster;
import com.digitisation.branchreports.utils.DateUtils;

@Service
public class HolidayMasterMapper {

	public HolidayMaster convertJsonToRepMaster(JSONObject object) {
		HolidayMaster holiday = new HolidayMaster();
		holiday.setHoliday(object.getString("holiday"));
		holiday.setHolidayDate(DateUtils.getDateStringIntoLocalDate(object.getString("holidayDate")));
		holiday.setHolidaystatus(object.getString("holidaystatus"));
		return holiday;
	}
}
