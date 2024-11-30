package com.suryoday.twowheeler.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.json.XML;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.roaocpv.service.ROAOCPVEkycOtpService;
import com.suryoday.roaocpv.service.ROAOCPVEkycService;
import com.suryoday.roaocpv.service.ROAOCPVErrorResponseService;
import com.suryoday.roaocpv.service.ROAOCPVValidateEkycOtpService;
import com.suryoday.twowheeler.pojo.TwowheelerDetailesTable;
import com.suryoday.twowheeler.service.TwowheelerDetailsService;
import com.suryoday.twowheeler.service.TwowheelerFamilyMemberService;
import com.suryoday.twowheeler.service.TwowheelerImageService;

@RestController
@RequestMapping("/twowheelerDetails/uidauth")
public class TwowheelerEkycController {
	@Autowired
	ROAOCPVEkycService roaocpvEkycService;

	@Autowired
	ROAOCPVErrorResponseService errorResponseService;
	
	@Autowired
	ROAOCPVEkycOtpService otpservice;
	
	@Autowired
	ROAOCPVValidateEkycOtpService validateOtpService;
	
	@Autowired
	TwowheelerImageService imageService;
	
	@Autowired
	TwowheelerFamilyMemberService familyMemberService;
	
	@Autowired
	TwowheelerDetailsService twowheelerDetailsService;
	
	private static Logger logger = LoggerFactory.getLogger(TwowheelerEkycController.class);

	@RequestMapping(value = "/ekyc", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> ekycRequest(@RequestBody String parent,
			@RequestHeader(name = "applicationNo", required = true) String applicationNo,
			@RequestHeader(name = "member", required = true) String member) throws Exception {

//		parent = parent.replaceAll("\t", "");

		System.out.println(parent);

		logger.debug("POST Request : " + parent);

		JSONObject request = new JSONObject(parent);

		String uid = request.getJSONObject("kycRequest").getString("uid");
		System.out.println(uid);

		String latitude = request.getJSONObject("kycRequest").getString("latitude");
		String longitude = request.getJSONObject("kycRequest").getString("longitude");
		String address = request.getJSONObject("kycRequest").getString("address");

		String pidxml = request.getJSONObject("kycRequest").getString("pidxml");

		System.out.println(pidxml);
		String eKycRequest = roaocpvEkycService.getJsonRequest(pidxml, uid);
		logger.debug("Main Ekyc Request : " + eKycRequest);

		System.out.println("Main Ekyc Request : " + eKycRequest);
		String sendEkyc = roaocpvEkycService.sendEkyc(eKycRequest);
		logger.debug("Response From the API :  " + sendEkyc);

		org.json.JSONObject jsonConverted1 = XML.toJSONObject(sendEkyc);
		String response = jsonConverted1.toString();
		logger.debug("XML Data Converted To JSON : " + jsonConverted1);
		logger.debug("JSONString : " + response);
		JSONObject jsonAPIResponse = new JSONObject(response);
		System.out.println(jsonAPIResponse);
		if (jsonAPIResponse.has("Response")) {
			org.json.JSONObject jsonObject = jsonAPIResponse.getJSONObject("Response");
			String hsmData = jsonAPIResponse.getJSONObject("Response").getString("HsmData");
			logger.debug("hsmData : " + hsmData);
			if (!hsmData.equals("")) {
				String decryptString = roaocpvEkycService.decryptString(hsmData);
				logger.debug("decrypted String : " + decryptString);
				org.json.JSONObject jsonConverted2 = XML.toJSONObject(decryptString);
				String jsonStringResponse = jsonConverted2.toString();
				logger.debug("xmltoJsonConverted String : " + jsonStringResponse);
				org.json.JSONObject jsonResponse = new org.json.JSONObject(jsonStringResponse);
				logger.debug("Main Json Response : " + jsonResponse);
				org.json.JSONObject mainResponse = new org.json.JSONObject();
				if (!jsonResponse.equals(null)) {
					HttpStatus h = HttpStatus.BAD_GATEWAY;
					if (jsonResponse.has("KycRes")) {
						jsonObject.put("HsmData", jsonResponse);

						org.json.JSONObject responseObj = new org.json.JSONObject();
						org.json.JSONObject kycRes = new org.json.JSONObject();
						org.json.JSONObject uidData = jsonResponse.getJSONObject("KycRes").getJSONObject("UidData");
						String txn = jsonResponse.getJSONObject("KycRes").getString("txn");
						String ttl = jsonResponse.getJSONObject("KycRes").getString("ttl");
						String ts = jsonResponse.getJSONObject("KycRes").getString("ts");
						org.json.JSONObject responseData = jsonObject.getJSONObject("ResponseData");
						String responseCode = jsonObject.getString("ResponseCode");
						String responseMessage = jsonObject.getString("ResponseMessage");
						String response2 = jsonObject.getString("Response2");
						String response1 = jsonObject.getString("Response1");

						kycRes.put("txn", txn);
						kycRes.put("ttl", ttl);
						kycRes.put("ts", ts);
						responseObj.put("KycRes", kycRes);
						responseObj.put("ResponseData", responseData);
						responseObj.put("ResponseCode", responseCode);
						responseObj.put("ResponseMessage", responseMessage);
						responseObj.put("Response2", response2);
						responseObj.put("Response1", response1);

						String ekycPhoto = uidData.getString("Pht");
						String ekycAadhar = uidData.getJSONObject("Prn").getString("content");
						 Map<String, String> map=new HashMap(); 
						    map.put("ekycPhoto",ekycPhoto); 
						    map.put("ekycAadhar",ekycAadhar);

						org.json.JSONObject ekyc_photo = new org.json.JSONObject();
						ekyc_photo.put("image", "photo.jpg");
						ekyc_photo.put("Lat", latitude);
						ekyc_photo.put("Long", longitude);
						ekyc_photo.put("Address", address);
						LocalDateTime localDateTime = LocalDateTime.now();
						ekyc_photo.put("timestamp", localDateTime);

						org.json.JSONObject ekyc_aadhar = new org.json.JSONObject();
						ekyc_aadhar.put("image", "photo.pdf");
						ekyc_aadhar.put("Lat", latitude);
						ekyc_aadhar.put("Long", longitude);
						ekyc_aadhar.put("Address", address);
						ekyc_aadhar.put("timestamp", localDateTime);

						org.json.JSONObject ekyc_photo1 = new org.json.JSONObject();
						ekyc_photo1.put("ekyc_photo", ekyc_photo);
						org.json.JSONObject ekyc_aadhar1 = new org.json.JSONObject();
						ekyc_aadhar1.put("ekyc_aadhar", ekyc_aadhar);

						org.json.JSONArray jsonArray = new org.json.JSONArray();
						jsonArray.put(ekyc_aadhar1);
						jsonArray.put(ekyc_photo1);

	//					Long applicationNo1 = Long.parseLong(applicationNo);
						imageService.saveAadharPhoto(map, applicationNo, jsonArray, member);
						//uidData.remove("Pht");
						uidData.remove("Prn");
						kycRes.put("UidData", uidData);
						JSONObject poi = uidData.getJSONObject("Poi");
						mainResponse.put("Response", responseObj);
						
						org.json.JSONObject DBResponse= new JSONObject(mainResponse.toMap());
						
						DBResponse.getJSONObject("Response").getJSONObject("KycRes").getJSONObject("UidData").remove("Pht");
						
						familyMemberService.saveResponse("ekyc", "FINGERPRINT", applicationNo, DBResponse.toString(),member);
						TwowheelerDetailesTable twowheelerDetails = twowheelerDetailsService.getByApplication(applicationNo);
						twowheelerDetails.setAadharNo(uid);
						twowheelerDetails.setName(poi.getString("name"));
						twowheelerDetailsService.saveData(twowheelerDetails);
						h = HttpStatus.OK;

					} else if (jsonResponse.has("Description")) {
						h = HttpStatus.BAD_REQUEST;
					}
					logger.debug("Main Response : " + mainResponse.toString());
					return new ResponseEntity<>(mainResponse.toString(), h);
				} else {
					logger.debug("GATEWAY_TIMEOUT");
					return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
				}
			} else {
				logger.debug("If HmsData Empty Response : " + jsonAPIResponse.toString());
				String kycRes = jsonAPIResponse.getJSONObject("Response").getJSONObject("ResponseData").getJSONObject("KycResponse").getJSONObject("Resp").getString("kycRes");
				String errorResponse=errorResponseService.getError(kycRes,Long.parseLong(applicationNo));
				return new ResponseEntity<Object>(errorResponse, HttpStatus.BAD_REQUEST);
			}
		} else if (jsonAPIResponse.has("Description")) {
			logger.debug("Main Response : " + jsonAPIResponse.toString());
			
			return new ResponseEntity<>(jsonAPIResponse.toString(), HttpStatus.BAD_REQUEST);
		} else {
			logger.debug("GATEWAY_TIMEOUT");
			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}

	}
	
	@RequestMapping(value = "/faceEKyc", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> faceEKycReq(@RequestBody String parent,
			@RequestHeader(name = "applicationNo", required = true) String applicationNo,
			@RequestHeader(name = "member", required = true) String member
			) throws Exception {

		parent = parent.replaceAll("\t", "");

		System.out.println(parent);

		logger.debug("POST Request : " + parent);

		JSONObject request = new JSONObject(parent);

		String uid = request.getJSONObject("kycRequest").getString("uid");
		System.out.println(uid);

		String latitude = request.getJSONObject("kycRequest").getString("latitude");
		String longitude = request.getJSONObject("kycRequest").getString("longitude");
		String address = request.getJSONObject("kycRequest").getString("address");

		String pidxml = request.getJSONObject("kycRequest").getString("pidxml");

		System.out.println(pidxml);
		String eKycRequest = roaocpvEkycService.getJsonFaceRequest(pidxml, uid);

		logger.debug("Main Ekyc Request : " + eKycRequest);

		System.out.println("Main Ekyc Request : " + eKycRequest);
		String sendEkyc = roaocpvEkycService.sendEkyc(eKycRequest);
		logger.debug("Response From the API :  " + sendEkyc);

		org.json.JSONObject jsonConverted1 = XML.toJSONObject(sendEkyc);
		String response = jsonConverted1.toString();
		logger.debug("XML Data Converted To JSON : " + jsonConverted1);
		logger.debug("JSONString : " + response);
		JSONObject jsonAPIResponse = new JSONObject(response);
		System.out.println(jsonAPIResponse);
		if (jsonAPIResponse.has("Response")) {
			org.json.JSONObject jsonObject = jsonAPIResponse.getJSONObject("Response");
			String hsmData = jsonAPIResponse.getJSONObject("Response").getString("HsmData");
			logger.debug("hsmData : " + hsmData);
			if (!hsmData.equals("")) {
				String decryptString = roaocpvEkycService.decryptString(hsmData);
				logger.debug("decrypted String : " + decryptString);
				org.json.JSONObject jsonConverted2 = XML.toJSONObject(decryptString);
				String jsonStringResponse = jsonConverted2.toString();
				logger.debug("xmltoJsonConverted String : " + jsonStringResponse);
				org.json.JSONObject jsonResponse = new org.json.JSONObject(jsonStringResponse);
				logger.debug("Main Json Response : " + jsonResponse);
				org.json.JSONObject mainResponse = new org.json.JSONObject();
				if (!jsonResponse.equals(null)) {
					HttpStatus h = HttpStatus.BAD_GATEWAY;
					if (jsonResponse.has("KycRes")) {
						jsonObject.put("HsmData", jsonResponse);

						org.json.JSONObject responseObj = new org.json.JSONObject();
						org.json.JSONObject kycRes = new org.json.JSONObject();
						org.json.JSONObject uidData = jsonResponse.getJSONObject("KycRes").getJSONObject("UidData");
						String txn = jsonResponse.getJSONObject("KycRes").getString("txn");
						String ttl = jsonResponse.getJSONObject("KycRes").getString("ttl");
						String ts = jsonResponse.getJSONObject("KycRes").getString("ts");
						org.json.JSONObject responseData = jsonObject.getJSONObject("ResponseData");
						String responseCode = jsonObject.getString("ResponseCode");
						String responseMessage = jsonObject.getString("ResponseMessage");
						String response2 = jsonObject.getString("Response2");
						String response1 = jsonObject.getString("Response1");

						kycRes.put("txn", txn);
						kycRes.put("ttl", ttl);
						kycRes.put("ts", ts);
						responseObj.put("KycRes", kycRes);
						responseObj.put("ResponseData", responseData);
						responseObj.put("ResponseCode", responseCode);
						responseObj.put("ResponseMessage", responseMessage);
						responseObj.put("Response2", response2);
						responseObj.put("Response1", response1);

						String ekycPhoto = uidData.getString("Pht");
						String ekycAadhar = uidData.getJSONObject("Prn").getString("content");
						 Map<String, String> map=new HashMap(); 
						    map.put("ekycPhoto",ekycPhoto); 
						    map.put("ekycAadhar",ekycAadhar);

						org.json.JSONObject ekyc_photo = new org.json.JSONObject();
						ekyc_photo.put("image", "photo.jpg");
						ekyc_photo.put("Lat", latitude);
						ekyc_photo.put("Long", longitude);
						ekyc_photo.put("Address", address);
						LocalDateTime localDateTime = LocalDateTime.now();
						ekyc_photo.put("timestamp", localDateTime);

						org.json.JSONObject ekyc_aadhar = new org.json.JSONObject();
						ekyc_aadhar.put("image", "photo.pdf");
						ekyc_aadhar.put("Lat", latitude);
						ekyc_aadhar.put("Long", longitude);
						ekyc_aadhar.put("Address", address);
						ekyc_aadhar.put("timestamp", localDateTime);

						org.json.JSONObject ekyc_photo1 = new org.json.JSONObject();
						ekyc_photo1.put("ekyc_photo", ekyc_photo);
						org.json.JSONObject ekyc_aadhar1 = new org.json.JSONObject();
						ekyc_aadhar1.put("ekyc_aadhar", ekyc_aadhar);

						org.json.JSONArray jsonArray = new org.json.JSONArray();
						jsonArray.put(ekyc_aadhar1);
						jsonArray.put(ekyc_photo1);

						Long applicationNo1 = Long.parseLong(applicationNo);
						
						imageService.saveAadharPhoto(map, applicationNo, jsonArray, member);
						
	//					uidData.remove("Pht");
						uidData.remove("Prn");
						kycRes.put("UidData", uidData);
						JSONObject poi = uidData.getJSONObject("Poi");
						mainResponse.put("Response", responseObj);
						org.json.JSONObject DBResponse= new JSONObject(mainResponse.toMap());
						
						DBResponse.getJSONObject("Response").getJSONObject("KycRes").getJSONObject("UidData").remove("Pht");
						
						familyMemberService.saveResponse("ekyc", "fACEEKYC", applicationNo, DBResponse.toString(),member);	
						TwowheelerDetailesTable twowheelerDetails = twowheelerDetailsService.getByApplication(applicationNo);
						twowheelerDetails.setAadharNo(uid);
						twowheelerDetails.setName(poi.getString("name"));
						twowheelerDetailsService.saveData(twowheelerDetails);
						
						h = HttpStatus.OK;

					} else if (jsonResponse.has("Description")) {
						h = HttpStatus.BAD_REQUEST;
					}
					logger.debug("Main Response : " + mainResponse.toString());
					return new ResponseEntity<>(mainResponse.toString(), h);
				} else {
					logger.debug("GATEWAY_TIMEOUT");
					return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
				}
			} else {
				logger.debug("If HmsData Empty Response : " + jsonAPIResponse.toString());
				String kycRes = jsonAPIResponse.getJSONObject("Response").getJSONObject("ResponseData").getJSONObject("KycResponse").getJSONObject("Resp").getString("kycRes");
				String errorResponse=errorResponseService.getError(kycRes,Long.parseLong(applicationNo));
				return new ResponseEntity<Object>(errorResponse, HttpStatus.BAD_REQUEST);
			}
		} else if (jsonAPIResponse.has("Description")) {
			logger.debug("Main Response : " + jsonAPIResponse.toString());
			
			return new ResponseEntity<>(jsonAPIResponse.toString(), HttpStatus.BAD_REQUEST);
		} else {
			logger.debug("GATEWAY_TIMEOUT");
			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}

	}
	
	@RequestMapping(value = "/ekycSendOtp", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> ekycOtp(@RequestBody String bm,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID)
			throws Exception {	
			JSONObject jsonObject=new JSONObject(bm);
			String aadharNo = jsonObject.getJSONObject("Data").getString("AadharNo");
			int max = 899999;
			int min = 800000;
			int random_int = (int) Math.floor(Math.random() * (max - min + 1) + min);
			String stan = Integer.toString(random_int);
			
		 	String xmlRequest = otpservice.getXmlRequest(aadharNo,stan);
		 	
		 	String sendEkyc = otpservice.sendEkyc(xmlRequest);
		 	
		 	org.json.JSONObject jsonConverted1 = XML.toJSONObject(sendEkyc);
			String response = jsonConverted1.toString();
			JSONObject jsonAPIResponse = new JSONObject(response);
			System.out.println(jsonAPIResponse);
			String responseCode = jsonAPIResponse.getJSONObject("Response").getJSONObject("ResponseData").getJSONObject("OtpResponse").getJSONObject("OtpRes").getString("ret");
			org.json.simple.JSONObject   pdresponse= new org.json.simple.JSONObject();
			if(responseCode.equalsIgnoreCase("y")) {
		 		pdresponse.put("message","otp send successfully");
		 		pdresponse.put("UKC",stan);
		 		return new ResponseEntity<Object>(pdresponse,HttpStatus.OK);
			}
			else {
				pdresponse.put("message","please enter valid aadharnumber");
		 		return new ResponseEntity<Object>(pdresponse,HttpStatus.OK);
			}
	}
	
	@RequestMapping(value = "/ekycValidateOtp", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> validateOtp(@RequestBody String bm,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "applicationNo", required = true) String applicationNo,
			@RequestHeader(name = "member", required = true) String member)
			throws Exception {
		JSONObject request=new JSONObject(bm);
		String otp = request.getJSONObject("Data").getString("Otp");
		String aadharNo = request.getJSONObject("Data").getString("AadharNo");
		String UKC = request.getJSONObject("Data").getString("UKC");
		String xmlRequest = validateOtpService.getXmlRequest(otp,aadharNo,UKC);
		System.out.println(xmlRequest);
		String sendEkyc = validateOtpService.sendEkyc(xmlRequest);
		
		org.json.JSONObject jsonConverted1 = XML.toJSONObject(sendEkyc);
		String response = jsonConverted1.toString();
		JSONObject jsonAPIResponse = new JSONObject(response);
		System.out.println("jsonAPIResponse"+jsonAPIResponse);
		if (jsonAPIResponse.has("Response")) {
		org.json.JSONObject jsonObject = jsonAPIResponse.getJSONObject("Response");
		String HsmData = jsonAPIResponse.getJSONObject("Response").getString("HsmData");
		if (!HsmData.equals("")) {
		String decryptString =validateOtpService.decryptString(HsmData);
		System.out.println(decryptString);
		org.jsoup.nodes.Document doc = Jsoup.parse(decryptString, "", Parser.xmlParser());
		
		 Elements select = doc.select("KycRes");
		System.out.println(select.toString());
		String string = select.toString();
		org.json.JSONObject jsonConverted2 = XML.toJSONObject(string);
		String jsonStringResponse = jsonConverted2.toString();
		org.json.JSONObject jsonResponse = new org.json.JSONObject(jsonStringResponse);
		System.out.println("JSONStringResponse"+jsonStringResponse);
		org.json.JSONObject mainResponse = new org.json.JSONObject();
		String responseCode = jsonAPIResponse.getJSONObject("Response").getString("ResponseCode");
		if(responseCode.equals("99"))
		{
			String responseMesage = jsonAPIResponse.getJSONObject("Response").getString("ResponseMessage");
			mainResponse.put("ResponseCode",responseCode); 
			mainResponse.put("ResponseMessage",responseMesage);
			return new ResponseEntity<Object>(mainResponse.toString(),HttpStatus.OK);
		}
		if (!jsonResponse.equals(null)) {
			HttpStatus h = HttpStatus.BAD_GATEWAY;
			if (jsonResponse.has("KycRes")) {	
				jsonObject.put("HsmData", jsonResponse);

				org.json.JSONObject responseObj = new org.json.JSONObject();
				org.json.JSONObject kycRes = new org.json.JSONObject();
				
				org.json.JSONObject uidData = jsonResponse.getJSONObject("KycRes").getJSONObject("UidData");
				
				String txn = jsonResponse.getJSONObject("KycRes").getString("txn");
				String ttl = jsonResponse.getJSONObject("KycRes").getString("ttl");
				String ts = jsonResponse.getJSONObject("KycRes").getString("ts");
				org.json.JSONObject responseData = jsonObject.getJSONObject("ResponseData");
				String responseCode2 = jsonObject.getString("ResponseCode");
				String responseMessage = jsonObject.getString("ResponseMessage");
				String response2 = jsonObject.getString("Response2");
				String response1 = jsonObject.getString("Response1");

				kycRes.put("txn", txn);
				kycRes.put("ttl", ttl);
				kycRes.put("ts", ts);
				responseObj.put("KycRes", kycRes);
				responseObj.put("ResponseData", responseData);
				responseObj.put("ResponseCode", responseCode2);
				responseObj.put("ResponseMessage", responseMessage);
				responseObj.put("Response2", response2);
				responseObj.put("Response1", response1);

				String ekycPhoto = uidData.getString("Pht");
				String ekycAadhar = uidData.getJSONObject("Prn").getString("content");
				 Map<String, String> map=new HashMap(); 
				    map.put("ekycPhoto",ekycPhoto); 
				    map.put("ekycAadhar",ekycAadhar);

				org.json.JSONObject ekyc_photo = new org.json.JSONObject();
				ekyc_photo.put("image", "photo.jpg");
				ekyc_photo.put("Lat", "0.0");
				ekyc_photo.put("Long", "0.0");
				ekyc_photo.put("Address", "");
				LocalDateTime localDateTime = LocalDateTime.now();
				ekyc_photo.put("timestamp", localDateTime);

				org.json.JSONObject ekyc_aadhar = new org.json.JSONObject();
				ekyc_aadhar.put("image", "photo.pdf");
				ekyc_aadhar.put("Lat", "0.0");
				ekyc_aadhar.put("Long", "0.0");
				ekyc_aadhar.put("Address", "");
				ekyc_aadhar.put("timestamp", localDateTime);

				org.json.JSONObject ekyc_photo1 = new org.json.JSONObject();
				ekyc_photo1.put("ekyc_photo", ekyc_photo);
				org.json.JSONObject ekyc_aadhar1 = new org.json.JSONObject();
				ekyc_aadhar1.put("ekyc_aadhar", ekyc_aadhar);

				org.json.JSONArray jsonArray = new org.json.JSONArray();
				jsonArray.put(ekyc_aadhar1);
				jsonArray.put(ekyc_photo1);

				Long applicationNo1 = Long.parseLong(applicationNo);
				
				imageService.saveAadharPhoto(map, applicationNo, jsonArray, member);
				
//					uidData.remove("Pht");
				uidData.remove("Prn");
				kycRes.put("UidData", uidData);
				JSONObject poi = uidData.getJSONObject("Poi");
				mainResponse.put("Response", responseObj);
				org.json.JSONObject DBResponse= new JSONObject(mainResponse.toMap());
				
				DBResponse.getJSONObject("Response").getJSONObject("KycRes").getJSONObject("UidData").remove("Pht");
				
				familyMemberService.saveResponse("ekyc", "Otp", applicationNo, DBResponse.toString(),member);
				TwowheelerDetailesTable twowheelerDetails = twowheelerDetailsService.getByApplication(applicationNo);
				twowheelerDetails.setAadharNo(aadharNo);
				twowheelerDetails.setName(poi.getString("name"));
				twowheelerDetailsService.saveData(twowheelerDetails);
				h = HttpStatus.OK;

			}
			else if (jsonResponse.has("Description")) {
				h = HttpStatus.BAD_REQUEST;
			}
			logger.debug("Main Response : " + mainResponse.toString());
			return new ResponseEntity<>(mainResponse.toString(), h);
		}
		else {
			
			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
		}else{
			logger.debug("If HmsData Empty Response : " + jsonAPIResponse.toString());
			String kycRes = jsonAPIResponse.getJSONObject("Response").getJSONObject("ResponseData").getJSONObject("KycResponse").getJSONObject("Resp").getString("kycRes");
			String errorResponse=errorResponseService.getError(kycRes,Long.parseLong(applicationNo));
			return new ResponseEntity<Object>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		} else if (jsonAPIResponse.has("Description")) {
		
			return new ResponseEntity<>(jsonAPIResponse.toString(), HttpStatus.BAD_REQUEST);
		} else {
			
			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}
}
