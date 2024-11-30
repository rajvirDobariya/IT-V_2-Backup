package com.suryoday.customerOnboard.exceptionHandler;

public class ResourceNotFoundException extends RuntimeException{

	public ResourceNotFoundException() {
		super("No Record Found In Database");
	}
	
	public ResourceNotFoundException(String message) {
		super(message);
	}
}
