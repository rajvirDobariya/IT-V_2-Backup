package com.suryoday.aocpv.exceptionhandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalEexceptionHandler {

	@ExceptionHandler(EmptyInputException.class)
	public ResponseEntity<String> handleEmptyInput(EmptyInputException emptyInputException){
		
		return new ResponseEntity<String>("input field is empty",HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException noSuchElementException){
		return new ResponseEntity<String>("no such element present in DB",HttpStatus.BAD_REQUEST);
		
	}
	@ExceptionHandler(TimeOutException.class)
	public ResponseEntity<String> handleTimeOutException(TimeOutException timeOutException){
		return new ResponseEntity<String>("time out ",HttpStatus.GATEWAY_TIMEOUT);
		
	}
	
}
