package com.suryoday.dsaOnboard.serviceImpl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suryoday.dsaOnboard.pojo.Address;
import com.suryoday.dsaOnboard.pojo.DsaOnboardDetails;
import com.suryoday.dsaOnboard.pojo.DsaOnboardMember;
import com.suryoday.dsaOnboard.service.DsaCodeCreationService;
import com.suryoday.dsaOnboard.service.DsaOnBoardDetailsService;
import com.suryoday.dsaOnboard.service.DsaOnboardMemberService;
import com.suryoday.roaocpv.others.ROAOCPVGenerateProperty;
@Service
public class DsaCodeCreationServiceImpl implements DsaCodeCreationService{

	private static Logger logger = LoggerFactory.getLogger(DsaCodeCreationServiceImpl.class);
	
	@Autowired
	DsaOnBoardDetailsService dsaOnBoardDetailsService;
	
	@Autowired
	DsaOnboardMemberService dsaOnboardMemberService;
	
	@Override
	public JSONObject codeCreation(DsaOnboardDetails dsaOnboardDetails) {
		JSONObject sendResponse = new JSONObject();	
		JSONObject request =getRequest(dsaOnboardDetails);
		dsaOnboardDetails.setDsaCodeCreationRequest(request.toString());
		URL obj = null;
		
		try {

			ROAOCPVGenerateProperty x = ROAOCPVGenerateProperty.getInstance();
			x.getappprop();
			x.bypassssl();
			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
			 obj = new URL(x.BASEURL+"PAYOUT/DsaMaster");
				logger.debug(x.BASEURL+"PAYOUT/DsaMaster");

			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("api_key", x.api_key);
			sendResponse = getResponse(request, sendResponse, con, "POST");
			sendResponse.put("dsaCode", request.getString("AgencyId"));
		} catch (Exception e) {

			e.getMessage();
		}
		
		return sendResponse;
	}

	private JSONObject getResponse(JSONObject parent, JSONObject sendAuthenticateResponse, HttpURLConnection con, String MethodType) throws Exception {
		con.setDoOutput(true);
		 OutputStreamWriter os = new OutputStreamWriter(con.getOutputStream());
		 os.write(parent.toString());
		 os.flush();
		 os.close();

		int responseCode = con.getResponseCode();
		logger.debug("POST Response Code :: " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			JSONObject sendauthenticateResponse1 = new JSONObject();
			sendauthenticateResponse1.put("data", response.toString());
			sendAuthenticateResponse = sendauthenticateResponse1;
		} else {
			logger.debug("GET request not worked");

			JSONObject sendauthenticateResponse1 = new JSONObject();

			JSONObject errr = new JSONObject();
			errr.put("Description", "Server Error " + responseCode);

			JSONObject j = new JSONObject();
			j.put("Error", errr);

			sendauthenticateResponse1.put("data", "" + j);
			sendAuthenticateResponse = sendauthenticateResponse1;
		}

		return sendAuthenticateResponse;
	}

	private JSONObject getRequest(DsaOnboardDetails dsaOnboardDetails) {
		
		String code=null;
		String typeOfRelationship=null;
		if(dsaOnboardDetails.getProductType() != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			 try {
			  typeOfRelationship = dsaOnboardDetails.getTypeOfRelationship();
			 if(typeOfRelationship.equalsIgnoreCase("CORPORATE DSA")) {
				 typeOfRelationship="DSA";
			 }
//			 List<String> list = objectMapper.readValue(dsaOnboardDetails.getProductType(), new TypeReference<List<String>>() {});
//			 String productType = list.stream().sorted().collect(Collectors.joining());
//			 if(productType.startsWith("C") && list.size()>1 && typeOfRelationship.equals("DSA")) {
//				 code=productType+"QM";
//			 }else if(productType.startsWith("C")&& list.size()<=1 && typeOfRelationship.equals("DSA")){
//				 code=productType+"Q";
//			 }else {
//				 code=productType+"M";
//			 }
			 code=typeOfRelationship.substring(0, 3);
			 DateTimeFormatter formater=	DateTimeFormatter.ofPattern("yyMM");
			 String date = LocalDate.now().format(formater);
			 code=code+date;
			 } catch (Exception e) {
		            e.printStackTrace();
		        } 
		}
		logger.debug("DsaCode : "+code);
		code=dsaOnBoardDetailsService.getlastDsaCode(code);
		logger.debug("DsaCodeFROMDB : "+code);
		DsaOnboardMember dsaOnboardMember=dsaOnboardMemberService.findByApplicationNoAndIsPrimary(dsaOnboardDetails.getApplicationNo(),"YES");
		logger.debug("dsaOnboardMember: "+dsaOnboardMember);
		JSONObject data=new JSONObject();
		data.put("AgencyId", code);//dsa ID
		data.put("AgencyType", dsaOnboardDetails.getTypeOfRelationship()!=null?dsaOnboardDetails.getTypeOfRelationship():"");//DSA/Connector
		data.put("AgencyName", dsaOnboardDetails.getCompanyName()!=null?dsaOnboardDetails.getCompanyName():"");//companyName
		data.put("DsaLevel", "");
		data.put("ParentDsaId", "");
		data.put("BranchId", dsaOnboardDetails.getBranchId()!=null?dsaOnboardDetails.getBranchId():"");
		data.put("Region", "");
		data.put("MobileNumber", dsaOnboardDetails.getMobileNo()!=null?dsaOnboardDetails.getMobileNo():"");
		data.put("ResidenceLat", "");
		data.put("ResidenceLong", "");
		data.put("OfficeLat", "");
		data.put("OfficeLong", "");
		data.put("EmailId", dsaOnboardDetails.getEmailId()!=null?dsaOnboardDetails.getEmailId():"");
		data.put("TdsRate", "5");
		data.put("GSTIN", dsaOnboardMember.getGstNo()!=null?dsaOnboardMember.getGstNo():"");//gst nu
		data.put("SchemeCode", "");
		data.put("BranchName", dsaOnboardDetails.getBranchName()!=null?dsaOnboardDetails.getBranchName():"");//branchName
		data.put("AadharRefNo", dsaOnboardMember.getAadharNo()!=null?dsaOnboardMember.getAadharNo():"");
		data.put("AccountInSsfb", "");
		data.put("AccountHolderName", dsaOnboardDetails.getAccountholderName()!=null?dsaOnboardDetails.getAccountholderName():"");//SURY0BK0000
		data.put("BankAccountNo", dsaOnboardDetails.getBankAccountNo()!=null?dsaOnboardDetails.getBankAccountNo():"");
		data.put("IfscCode", dsaOnboardDetails.getIfscCode()!=null?dsaOnboardDetails.getIfscCode():"");
		data.put("BankName", dsaOnboardDetails.getBankName()!=null?dsaOnboardDetails.getBankName():"");
		data.put("CustomerId", "");
		data.put("CompanyName", dsaOnboardDetails.getCompanyName()!=null?dsaOnboardDetails.getCompanyName():"");
		data.put("createdDate", LocalDate.now());
		data.put("entity", dsaOnboardDetails.getEntity()!=null?dsaOnboardDetails.getEntity():"");
		data.put("updatedDate", LocalDate.now());
		data.put("age_of_proprietor", dsaOnboardDetails.getAgeOfProprietor()!=null?dsaOnboardDetails.getAgeOfProprietor():"");
		data.put("LeadId", "");
		data.put("ConstitutionType", dsaOnboardDetails.getConstitutionType()!=null?dsaOnboardDetails.getConstitutionType():"");
		data.put("LeadName", "");
		JSONArray j=new JSONArray(dsaOnboardDetails.getProductType()!=null?dsaOnboardDetails.getProductType():"");
		data.put("ProductType", j.toString());
		data.put("TypeOfRelationship", "");
		data.put("DOB", dsaOnboardMember.getDateOfBirth()!=null?dsaOnboardMember.getDateOfBirth():"");//dob
		data.put("DOI", "");
		
		if(dsaOnboardMember.getAddress() != null) {
			String address = dsaOnboardMember.getAddress();
			org.json.JSONArray addressInJson =new org.json.JSONArray(address);
			for(int i=0;i<addressInJson.length();i++) {
				JSONObject jsonObject = addressInJson.getJSONObject(i);
					String addressLine1 = jsonObject.getString("addressLine1");
					String addressLine2 = jsonObject.getString("addressLine2");
					String addressLine3 = jsonObject.getString("addressLine3");
					String city = jsonObject.getString("city");
					String district = jsonObject.getString("district");
					String pincode = jsonObject.getString("pincode");
					String addressLong = jsonObject.getString("addressLong");
					String addressLat = jsonObject.getString("addressLat");
					String addres=addressLine1+" "+addressLine2+" "+addressLine3+" "+city+" "+" "+district+" "+pincode;
				if(jsonObject.getString("addressType").equals("Current Address")) {
					data.put("CurrentAddress", addres);		
					data.put("CurrentAddressLat", addressLat);
					data.put("CurrentAddressLong", addressLong);
				}else if(jsonObject.getString("addressType").equals("Residential Address")) {
					data.put("PermanentAddress", addres);	
				}else if(jsonObject.getString("addressType").equals("Office Address")) {
					data.put("OfficeAddress", addres);
				}
			}
		}
		
		data.put("PermanentAddressLat", "");
		data.put("PermanentAddressLong", "");
		data.put("AgreementValidityTill", dsaOnboardDetails.getLeegalityDate()!=null?dsaOnboardDetails.getLeegalityDate().plusYears(3):"");
		data.put("NameAsPerBank", dsaOnboardDetails.getAccountholderName()!=null?dsaOnboardDetails.getAccountholderName():"");
		data.put("NameAsPerPan", dsaOnboardMember.getNameOnPanCard()!=null?dsaOnboardMember.getNameOnPanCard():"");
		data.put("PanNumber", dsaOnboardMember.getPanCardNo()!=null?dsaOnboardMember.getPanCardNo():"");
		data.put("RmId", "");
		data.put("AadharPanLinked", dsaOnboardMember.getAadharPanLinkStatus()!=null?dsaOnboardMember.getAadharPanLinkStatus():"");
		logger.debug("request"+data.toString());

		return data;	
	}

	@Override
	public JSONObject addDsaId(DsaOnboardDetails dsaOnboardDetails) {
		JSONObject sendResponse = new JSONObject();	
		JSONObject request =getRequestDsaId(dsaOnboardDetails);
		dsaOnboardDetails.setFinacalDsaMasterRequest(request.toString());
		URL obj = null;
		
		try {

			ROAOCPVGenerateProperty x = ROAOCPVGenerateProperty.getInstance();
			x.getappprop();
			x.bypassssl();
			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
			 obj = new URL(x.BASEURL+"DSA/Master");
				logger.debug(x.BASEURL+"DSA/Master");

			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("api_key", x.api_key);
			con.setRequestProperty("X-Request-ID", "COR");
			sendResponse = getResponse(request, sendResponse, con, "POST");
			
		} catch (Exception e) {

			e.getMessage();
		}
		
		return sendResponse;
	}

	private JSONObject getRequestDsaId(DsaOnboardDetails dsaOnboardDetails) {
		
		DsaOnboardMember dsaOnboardMember=dsaOnboardMemberService.findByApplicationNoAndIsPrimary(dsaOnboardDetails.getApplicationNo(),"YES");
		List<Address> addressList=new ArrayList<>();
		Address address=new Address();
		if(dsaOnboardMember.getAddress() != null) {
			 ObjectMapper objectMapper = new ObjectMapper();
			 try {
				 addressList = objectMapper.readValue(dsaOnboardMember.getAddress(), new TypeReference<List<Address>>() {});

			 } catch (Exception e) {
		            e.printStackTrace();
		        } 
		}
		
		for(Address add:addressList) {
			if(add.getAddressType().equalsIgnoreCase("Residential Address")) {
				address=add;
				break;
			}
		}
		List<String> list = new ArrayList<>();
		if(dsaOnboardDetails.getProductType() != null) {
			JSONArray jsonArray = new JSONArray(dsaOnboardDetails.getProductType());
	        for (int i = 0; i < jsonArray.length(); i++) {
	        	if(jsonArray.getString(i).equals("HL")) {
	        		list.add("MORTG");
	        	}else {
	        		list.add(jsonArray.getString(i));
	        	}
	            
	        }
			 
		}
		List<String> schemeList=dsaOnBoardDetailsService.getSchemeCode(list);
		
		JSONObject data=new JSONObject();
		JSONObject request=new JSONObject();
		data.put("DSAId", dsaOnboardDetails.getDsaCode());
		data.put("DSASolId", dsaOnboardDetails.getBranchId());
		data.put("DSAName", dsaOnboardMember.getName());
		data.put("Address1", address.getAddressLine1()!=null?address.getAddressLine1():"");
		data.put("Address2", address.getAddressLine2()!=null?address.getAddressLine2():"");
		data.put("Address3", address.getAddressLine3()!=null?address.getAddressLine3():"");
		data.put("City", address.getCity()!=null?address.getCity():"");
		data.put("State", address.getState()!=null?address.getState():"");
		data.put("PostalCode", address.getPincode()!=null?address.getPincode():"");
		data.put("MobileNumber", dsaOnboardDetails.getMobileNo());
		data.put("Country", "IN");
		data.put("DSALevel", "1");
		data.put("ParentDSAId", "");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		String current = LocalDate.now().format(formatter);
		data.put("DSAOpenedDate", current);
		data.put("CommConsolFlag", "N");
		data.put("CommPaymentMode", "O");
		data.put("CommCrAccountId", "10000590180005");
		data.put("CommCrAccountCurrency", "INR");
		data.put("DSAType", "B");
		data.put("EmailId", dsaOnboardDetails.getEmailId());
		data.put("LicenseNumber", "");
		data.put("Remarks", "");
		data.put("FreeField1", "");
		data.put("FreeField2", "");
		data.put("FreeField3", "");
		data.put("FreeField4", "");
		data.put("ReferenceDsaId", "");
		JSONArray array=new JSONArray();
		for(String schemeCode:schemeList) {
			JSONObject productDetail=new JSONObject();
			productDetail.put("Schemecode", schemeCode);
			productDetail.put("PayCommision", "N");
			productDetail.put("SubReCrPh", "800070015");
			productDetail.put("isMultiRec", "Y");
			array.put(productDetail);
		}
		
		data.put("ProductDetails", array);
		
		request.put("Data", data);
		logger.debug("request"+data.toString());

		return request;	
	}

	
}
