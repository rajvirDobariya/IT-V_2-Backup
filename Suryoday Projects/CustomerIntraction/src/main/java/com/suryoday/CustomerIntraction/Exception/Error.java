package com.suryoday.CustomerIntraction.Exception;


import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

public class Error {

	static Logger logger = LoggerFactory.getLogger(Error.class);

	public static JSONObject createError(String message, HttpStatus badRequest) {
		logger.debug("SessionId is expired or Invalid sessionId!");

		JSONObject data = new JSONObject();
		data.put("value", message);
		JSONObject data2 = new JSONObject();
		data2.put("Error", data);
		logger.debug("Erro response :: {}", data2.toString());
		return data2;

	}
}
