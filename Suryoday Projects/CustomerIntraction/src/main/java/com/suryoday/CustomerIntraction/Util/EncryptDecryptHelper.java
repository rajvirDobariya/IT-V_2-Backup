package com.suryoday.CustomerIntraction.Util;

import org.json.JSONObject;

public class EncryptDecryptHelper {

	public static JSONObject decryptRequestString(String encryptResponseString, String channel, String X_Session_ID,
			String X_encode_ID) throws Exception {
		// 1 REQUEST
		JSONObject encryptJSONObject = new JSONObject(encryptResponseString);
		String decryptContainerString = "";
		String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
		if (channel.equalsIgnoreCase("WEB")) {
			decryptContainerString = Crypt.decrypt(encryptJSONObject.toString(), X_encode_ID, X_Session_ID);
			//decryptContainerString = Crypt.decrypt(encryptJSONObject.toString());
		} else if (channel.equalsIgnoreCase("MB")) {

			decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, X_Session_ID);
		}
		return new JSONObject(decryptContainerString);
	}

	public static JSONObject encryptResponseString(JSONObject responseJson, String channel, String X_Session_ID,
			String X_encode_ID) throws Exception {
		// 3 RESPONSE
		String data = responseJson.toString();
		String encryptString2 = "";
		if (channel.equalsIgnoreCase("WEB")) {
			encryptString2 = Crypt.encrypt(data, X_encode_ID);
		} else if (channel.equalsIgnoreCase("MB")) {
			encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, X_Session_ID);
		}
		JSONObject data2 = new JSONObject();
		data2.put("value", encryptString2);
		JSONObject data3 = new JSONObject();
		data3.put("Data", data2);
		return data3;
	}

	public static void main(String[] args) throws Exception {

		JSONObject getName = new JSONObject("{\"Data\":{\"CIFNo\":\"240000695\",\"memberRole\":\"Customer\"}}");
		
		JSONObject saveMeeting = new JSONObject("{\"Data\":{\"meetingDate\":\"2024-06-23\",\"branchCode\":\"HDFC0000466\",\"createdBy\":\"1234\",\"meetingStartTime\":\"10:00 AM\",\"meetingEndTime\":\"02:00 PM\",\"summaryMOM\":[{\"topicOfDiscussion\":\"Test\",\"branchActionable\":\"Test\",\"feedback\":\"test\",\"ETA\":\"2024-06-23\"},{\"topicOfDiscussion\":\"Test\",\"branchActionable\":\"Test\",\"feedback\":\"test\",\"ETA\":\"2020-06-23\"},{\"topicOfDiscussion\":\"Test\",\"branchActionable\":\"Test\",\"feedback\":\"test\",\"ETA\":\"202-06-23\"},{\"topicOfDiscussion\":\"Test\",\"branchActionable\":\"Test\",\"feedback\":\"test\",\"ETA\":\"2024-06-23\"},{\"topicOfDiscussion\":\"Test\",\"branchActionable\":\"Test\",\"feedback\":\"test\",\"ETA\":\"2024-06-23\"},{\"topicOfDiscussion\":\"Test\",\"branchActionable\":\"Test\",\"feedback\":\"test\",\"ETA\":\"2024-06-23\"},{\"topicOfDiscussion\":\"Test\",\"branchActionable\":\"Test\",\"feedback\":\"test\",\"ETA\":\"2024-06-23\"}],\"memberList\":[{\"name\":\"Nikhil Kumar\",\"CIFNo\":\"240000695\",\"memberRole\":\"Customer\"},{\"name\":\"Tanaji Kumar\",\"CIFNo\":\"240000695\",\"memberRole\":\"Staff\"},{\"name\":\"Navin Kumar\",\"CIFNo\":\"240000695\",\"memberRole\":\"Staff\"}],\"image\":[{\"base64Image\":\"RmFzZTY0IGVuY29kaW5nIHN0cmluZw==\",\"documentType\":\"jpg\"},{\"base64Image\":\"RmFzNNY0IGVuY29kaW5nIHN0cmluZw==\",\"documentType\":\"jpg\"},{\"base64Image\":\"RmFzTNY0IGVuY29kaW5nIHN0cmluZw==\",\"documentType\":\"pdf\"},{\"base64Image\":\"RmTzTNY0IGVuY29kaW5nIHN0cmluZw==\",\"documentType\":\"pdf\"}]}}");
		
		JSONObject getMeetingdata = new JSONObject("{\"Data\":{\"meetingNumber\":\"\",\"startDate\":\"2024-05-01\",\"endDate\":\"2024-08-25\"}}");
		
		JSONObject getById = new JSONObject("{\"Data\":{\"meetingNumber\":\"BLCSCM/10011/July/2024\"}}");
		
		JSONObject saveBM = new JSONObject("{\"Data\":{\"meetingNumber\":\"BLCSCM/10011/August/2024\",\"updatedBy\":\"1\",\"role\":\"BM\"}}");
		
		
		JSONObject downloadPDF = new JSONObject("{\"Data\":{\"meetingNumber\":\"BLCSCM/1101/July/2024\"}}");
		JSONObject encryptRequestString = encryptResponseString(downloadPDF, "WEB",
				"3c31c1c1-a732-4e20-b9bd-9537844e61cf", "123");
		System.out.println("EncryptRequestString : " + encryptRequestString);

		// 2
		String response = "{\"Data\":{\"value\":\"CO+P299vaKh3J9D8fAK1OMUghMMVI3hk+F5djxKFbmuOPZlttWSQjeYpxixyiPRBB/BGWm88/caq+9njCaycQw==\"}}";
		JSONObject decryptRequestString = decryptRequestString(response, "WEB", "4c6272b8-317e-4889-b7e2-3a92b4bb4812",
				"123");
		System.out.println("DecryptRequestString :" + decryptRequestString);
	}

}
