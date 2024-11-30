package com.digitisation.branchreports.exception;

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

	@ExceptionHandler(DigitizationException.class)
	public ResponseEntity<String> handleCounterfeitException(DigitizationException ex) {
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

}
