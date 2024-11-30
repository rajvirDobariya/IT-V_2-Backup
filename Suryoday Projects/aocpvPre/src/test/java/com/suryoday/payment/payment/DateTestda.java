package com.suryoday.payment.payment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateTestda {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
     String dateOfBirth = "1991-07-02";
	     
	     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	     //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
		 LocalDate dob = LocalDate.parse(dateOfBirth, formatter);	
		 System.out.println(dob);

		 
		
	}

}
