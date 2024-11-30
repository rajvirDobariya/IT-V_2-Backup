package com.suryoday.uam.exceptionhandling;

public class TimeOutException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public TimeOutException(String errorMessage) {
		super(errorMessage);
	}
}
