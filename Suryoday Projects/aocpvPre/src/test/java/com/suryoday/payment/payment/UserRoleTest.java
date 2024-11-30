package com.suryoday.payment.payment;

public class UserRoleTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		

		 String a="12345683,12345684";
		 
		 String b="select * from aocp_table where application_no in("+a+")";
		 System.out.println(b);
	}

}
