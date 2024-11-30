package com.suryoday.payment.payment;

import org.json.JSONObject;

public class IncomeDetailsTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		             //aadharBack
		String a="{\"aadhaarBack\":{\"image\":\"1661259876.jpg\",\"Address\":\"1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA\",\"Long\":\"-122.084\",\"Lat\":\"37.421998333333335\",\"timestamp\":\"08/23/2022 6:34:36 PM\"}}\r\n" + 
				"";
		System.out.println("new"+a);
		
		JSONObject object= new JSONObject(a);
		System.out.println(object.has("aadharBack"));
		
		if(object.has("panCardPhoto")) {
			object.getJSONObject("panCardPhoto").toString();
			
		}
		else if(object.has("aadharFront")) {
		object.getJSONObject("aadharFront").toString();
			
		}
		else if(object.has("aadharBack")) {
			String aadharBack=object.getJSONObject("aadharBack").toString();
		    System.out.println("hi"+aadharBack);
		}

	}

}
