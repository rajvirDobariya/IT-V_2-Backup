package com.suryoday.uam.exceptionhandling;

public class DuplicateEntryException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public DuplicateEntryException(String message) {
		super(message);
		
	}
}
