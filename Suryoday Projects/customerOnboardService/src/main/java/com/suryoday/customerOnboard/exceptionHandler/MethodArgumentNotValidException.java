package com.suryoday.customerOnboard.exceptionHandler;

public class MethodArgumentNotValidException extends RuntimeException{

	public MethodArgumentNotValidException () {
		super("No Record Found In Database");
	}
	
	public MethodArgumentNotValidException (String message) {
		super(message);
	}
}
