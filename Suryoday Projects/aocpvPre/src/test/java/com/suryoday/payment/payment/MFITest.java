package com.suryoday.payment.payment;

import org.json.JSONObject;

public class MFITest {

	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		

		String a="{\r\n" + 
				"    \"result\": {\r\n" + 
				"        \"name\": \"Navnita Sudhakar Nevare\",\r\n" + 
				"        \"rln_name\": \"Sudhakar Gajane\",\r\n" + 
				"        \"rln_type\": \"H\",\r\n" + 
				"        \"gender\": \"F\",\r\n" + 
				"        \"district\": \"Bhandara\",\r\n" + 
				"        \"ac_name\": \"Sakoli\",\r\n" + 
				"        \"pc_name\": \"Bhandara-Gondiya\",\r\n" + 
				"        \"state\": \"Maharashtra\",\r\n" + 
				"        \"epic_no\": \"HXR0828301\",\r\n" + 
				"        \"dob\": \"\",\r\n" + 
				"        \"age\": 42,\r\n" + 
				"        \"part_no\": \"16\",\r\n" + 
				"        \"slno_inpart\": \"36\",\r\n" + 
				"        \"ps_name\": \"Z.P. Upper primary school Ghanod Middle Zone\",\r\n" + 
				"        \"part_name\": \"Ghanod\",\r\n" + 
				"        \"last_update\": \"03-01-2023\",\r\n" + 
				"        \"ps_lat_long\": \"0.0,0.0\",\r\n" + 
				"        \"rln_name_v1\": \"सुधाकर गजणे\",\r\n" + 
				"        \"rln_name_v2\": \"\",\r\n" + 
				"        \"rln_name_v3\": \"\",\r\n" + 
				"        \"section_no\": \"1\",\r\n" + 
				"        \"id\": \"S130620016010036\",\r\n" + 
				"        \"name_v1\": \"नवनिता सुधाकर नेवारे\",\r\n" + 
				"        \"name_v2\": \"\",\r\n" + 
				"        \"name_v3\": \"\",\r\n" + 
				"        \"ac_no\": \"62\",\r\n" + 
				"        \"st_code\": \"S13\",\r\n" + 
				"        \"house_no\": \"\"\r\n" + 
				"    },\r\n" + 
				"    \"request_id\": \"30e4dfa9-9501-4855-8cb1-8471f693b682\",\r\n" + 
				"    \"status-code\": \"101\"\r\n" + 
				"}";
		 
		 
		 
		 System.out.println(a);
		 
		 
		 
		 JSONObject jm  = new JSONObject(a);
		
		 if(jm.has("result"))
		 {
			 jm.getJSONObject("result").getString("ac_name");
			 jm.getJSONObject("result").remove("ac_name");
			 jm.getJSONObject("result").put("ac_name",jm.getJSONObject("result").getString("name"));
			 
			 System.out.println(jm);
		 }
	}
	
	
}
