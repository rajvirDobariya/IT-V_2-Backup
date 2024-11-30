package com.suryoday.payment.payment;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RemakstestDemo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
	JSONArray j = new JSONArray();
	JSONObject jb = new JSONObject();
	jb.put("1", "\"1) RESIDENCE PHOTO \\\\\\nIS NOT PROPER (ROOF NOT VISIBLE INSIDE) can't change        \"");
	
	j.put(jb);
	j.put("2) REQUIRED LATEST[] UTILITY BILL RECEIPT  ");
	
	String m=" 1) RESIDENCE PHOTO IS NOT PROPER (ROOF NOT VISIBLE INSIDE)  \r\n" + 
			"\r\n" + 
			"  2) REQUIRED LATEST UTILITY BILL RECEIPT\r\n" + 
			"";
	//System.out.println(j.toString());
	
	         boolean b=false;
	         System.out.println(m.toString());
	        b = isJSONValid1(m.toString());
	      System.out.println(b);
	
			}

	
	public static boolean isJSONValid(String jsonInString ) {
	    try {
	       final ObjectMapper mapper = new ObjectMapper();
	       mapper.readTree(jsonInString);
	       return true;
	    } catch (IOException e) {
	       return false;
	    }
	  }
	
	public static boolean isJSONValid1(String test) {
	    try {
	        new JSONObject(test);
	    } catch (JSONException ex) {
	        // edited, to include @Arthur's comment
	        // e.g. in case JSONArray is valid as well...
	    	ex.printStackTrace();
	        try {
	            new JSONArray(test);
	        } catch (JSONException ex1) {
	        	ex1.printStackTrace();
	            return false;
	        }
	    }
	    return true;
	}
}
