package com.digitisation.branchreports.exception;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

public class Error {

	static Logger LOG = LoggerFactory.getLogger(Error.class);

	public static JSONObject createError(String message, HttpStatus httpStatus) {
		LOG.debug("SessionId is expired or Invalid sessionId!");

		JSONObject data = new JSONObject();
		data.put("value", message);
		JSONObject data2 = new JSONObject();
		data2.put("Error", data);
		LOG.debug("Erro response :: {}", data2.toString());
		return data2;

	}
}
