package com.suryoday.audioComplaint.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.suryoday.audioComplaint.entity.ErrorResponse;



@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handlerResourceNotFoundException(ResourceNotFoundException ex){
		ErrorResponse errorResponse=new ErrorResponse();
		errorResponse.setMessage(ex.getMessage());
		errorResponse.setSuccess(false);
		errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(errorResponse,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	 @ResponseStatus(HttpStatus.BAD_REQUEST)
	 @ExceptionHandler(MethodArgumentNotValidException.class)
	 public ResponseEntity<ErrorResponse> handlerResourceNotFoundException(MethodArgumentNotValidException ex){
			ErrorResponse errorResponse=new ErrorResponse();
			errorResponse.setMessage(ex.getMessage());
			errorResponse.setSuccess(false);
			errorResponse.setStatus(HttpStatus.BAD_REQUEST);
			return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
		}
	 
	 @ResponseStatus(HttpStatus.BAD_REQUEST)
	 @ExceptionHandler(InproperImageException.class)
	 public ResponseEntity<ErrorResponse> handlerInproperImageException(InproperImageException ex){
			ErrorResponse errorResponse=new ErrorResponse();
			errorResponse.setMessage(ex.getMessage());
			errorResponse.setSuccess(false);
			errorResponse.setStatus(HttpStatus.BAD_REQUEST);
			return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
		}
	 
	 @ResponseStatus(HttpStatus.UNAUTHORIZED)
	 @ExceptionHandler(InvalidSessionException.class)
	 public ResponseEntity<ErrorResponse> handlerInvalidSessionException(InvalidSessionException ex){
			ErrorResponse errorResponse=new ErrorResponse();
			errorResponse.setMessage(ex.getMessage());
			errorResponse.setSuccess(false);
			errorResponse.setStatus(HttpStatus.UNAUTHORIZED);
			return new ResponseEntity<>(errorResponse,HttpStatus.UNAUTHORIZED);
		}
}
