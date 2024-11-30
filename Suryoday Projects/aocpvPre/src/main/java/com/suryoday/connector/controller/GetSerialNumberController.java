package com.suryoday.connector.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/uidauth")
public class GetSerialNumberController {
	
	Logger logger = LoggerFactory.getLogger(GetSerialNumberController.class);
	
	@RequestMapping(value="/getSerialData", method = RequestMethod.POST)
	public ResponseEntity<Object> serviceRequestDispatcher(@RequestBody String jsonRequest)
			throws ClassNotFoundException {
		
		System.out.println(jsonRequest);
		
		JSONObject lResponse = new JSONObject(jsonRequest);
		String piddata =lResponse.getJSONObject("Data").getString("PidData");
		// Json Request from FE/Postman
		System.out.println(piddata);
	
	String jsonString = null;
    try  
    {  
    	// XML to JSON Conversion
    	JSONObject jsonConverted = XML.toJSONObject(piddata);   
    	  jsonString = jsonConverted.toString();  
         System.out.println(jsonString);         
    } catch (JSONException e) {  
        e.printStackTrace();  
    } 
   		
    // Taking a value from JsonObject
    JSONObject obj = new JSONObject(jsonString);
    JSONArray jsonArray = obj.getJSONObject("PidData").getJSONObject("DeviceInfo").getJSONObject("additional_info").getJSONArray("Param");
    System.out.println("Json Single Value"+jsonArray);
    
    // Taking Value through FOR LOOP
    String stringValue = "";
    String stringName = ""; 
    for(int i=0; i< jsonArray.length(); i++) 
    {
    	JSONObject jsonObject = jsonArray.getJSONObject(i);
    	stringName = jsonObject.getString("name");
    	if(stringName.equalsIgnoreCase("DeviceSerial")) 
    	{
    		 stringValue = jsonObject.getString("value");
    	}
    }
    System.out.println(stringValue);
    
    		JSONObject response2 = new JSONObject();
    		response2.put("DeviceID",stringValue);
			JSONObject response = new JSONObject();
			response.put("Data",response2);
			
			return new ResponseEntity<Object>(response.toString(),HttpStatus.OK);

	}

}
