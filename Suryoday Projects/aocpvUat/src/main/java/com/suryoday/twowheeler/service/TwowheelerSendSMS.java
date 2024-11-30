package com.suryoday.twowheeler.service;

import org.json.JSONObject;

import com.suryoday.twowheeler.pojo.TwowheelerDetailesTable;

public interface TwowheelerSendSMS {

	JSONObject sendSms(TwowheelerDetailesTable twowheelerDetails, String stage);
}
