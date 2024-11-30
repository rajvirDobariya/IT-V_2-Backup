package com.suryoday.Counterfeit.Exception;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@ControllerAdvice
public class GlobalExceptionHandler {
	Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(CounterfeitException.class)
	public ResponseEntity<String> handleCounterfeitException(CounterfeitException ex) {
		JSONObject error = Error.createError(ex.getMessage(), HttpStatus.BAD_REQUEST);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.toString());
	}

	@ExceptionHandler(DenominationException.class)
	public ResponseEntity<String> handleDenominationException(DenominationException ex) {
		JSONObject error = Error.createError(ex.getMessage(), HttpStatus.BAD_REQUEST);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.toString());

	}

	@ExceptionHandler(HttpClientErrorException.BadRequest.class)
	public ResponseEntity<String> handleBadRequestException(HttpClientErrorException ex) {
		JSONObject error = Error.createError(ex.toString(), HttpStatus.BAD_REQUEST);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.toString());
	}

	@ExceptionHandler(HttpServerErrorException.InternalServerError.class)
	public ResponseEntity<String> handleInternalServerError(HttpServerErrorException ex) {
		JSONObject error = Error.createError(ex.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error.toString());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleOtherExceptions(Exception ex) {
		LOG.debug("Exception message ::{}", ex);
		JSONObject error = Error.createError(ex.toString(), HttpStatus.INTERNAL_SERVER_ERROR);

		JSONObject errorJson = new JSONObject();
		JSONObject errorDetails = new JSONObject();
		errorDetails.put("message", ex.getMessage()); // Include the message from the exception
		errorJson.put("Error", errorDetails);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorJson.toString());
	}
}
