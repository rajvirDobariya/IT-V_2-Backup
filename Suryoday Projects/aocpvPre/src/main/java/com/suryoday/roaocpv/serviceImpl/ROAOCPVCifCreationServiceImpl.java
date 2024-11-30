package com.suryoday.roaocpv.serviceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.aocpv.pojo.AocpCustomer;
import com.suryoday.aocpv.pojo.AocpvIncomeDetails;
import com.suryoday.aocpv.repository.AocpCustomerDataRepo;
import com.suryoday.aocpv.repository.AocpvIncomeDetailsRepository;
import com.suryoday.roaocpv.others.ROAOCPVGenerateProperty;
import com.suryoday.roaocpv.pojo.ApplicationDetails;
import com.suryoday.roaocpv.repository.ApplicationDetailsRepository;
import com.suryoday.roaocpv.service.ROAOCPVCifCreationService;

@Service
public class ROAOCPVCifCreationServiceImpl implements ROAOCPVCifCreationService {

	private static Logger logger = LoggerFactory.getLogger(ROAOCPVCifCreationServiceImpl.class);
	@Autowired
	ApplicationDetailsRepository appDetailsRepo;

	@Autowired
	AocpCustomerDataRepo aocpvcustomerdatarepo;

	@Autowired
	AocpvIncomeDetailsRepository aocpvincomedetailsrepo;

	@Override
	public JSONObject cifCreation(JSONObject jsonObject, JSONObject header) {
		JSONObject sendResponse = new JSONObject();

		String applicationNo = jsonObject.getJSONObject("Data").getString("AppliationID");
		
		Long applicationNo1 = Long.parseLong(applicationNo);
		JSONObject request = getRequest(applicationNo1);

		URL obj = null;
		try {

			ROAOCPVGenerateProperty x = ROAOCPVGenerateProperty.getInstance();
			x.getappprop();
			// GenerateProperty x = GenerateProperty.getInstance();
			x.bypassssl();
			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
			logger.debug(x.BASEURL + "customers?api_key=" + x.api_key);

			obj = new URL(x.BASEURL + "customers?api_key=" + x.api_key);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("X-Request-ID", header.getString("X-Request-ID"));
			con.setRequestProperty("X-User-ID", header.getString("X-User-ID"));
			con.setRequestProperty("X-From-ID", header.getString("X-From-ID"));
			con.setRequestProperty("X-To-ID", header.getString("X-To-ID"));
			con.setRequestProperty("X-Transaction-ID", header.getString("X-Transaction-ID"));

			sendResponse = getResponseData(request, sendResponse, con, "POST");
			String Data2 = sendResponse.getString("data");
			JSONObject Data1 = new JSONObject(Data2);
			String cifNumber = Data1.getJSONObject("Data").getString("UCIC");
			Optional<ApplicationDetails> findById = appDetailsRepo.findById(applicationNo1);
			if (findById.isPresent()) {
				ApplicationDetails applicationDetails = findById.get();
				applicationDetails.setCifNumber(cifNumber);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return sendResponse;
	}

	private static JSONObject getResponseData(JSONObject parent, JSONObject sendAuthenticateResponse,
			HttpURLConnection con, String MethodType) throws IOException {

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
			logger.debug("POST request not worked");

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

	private JSONObject getRequest(Long applicationNo1) {
		Optional<AocpCustomer> byApplicationNo = aocpvcustomerdatarepo.getByApplicationNo(applicationNo1);
		Optional<AocpvIncomeDetails> find = aocpvincomedetailsrepo.find(applicationNo1, "SELF");
		JSONObject Data = new JSONObject();
		if (byApplicationNo.isPresent()) {
			AocpCustomer aocpCustomer = byApplicationNo.get();
			AocpvIncomeDetails aocpvIncomeDetails = find.get();
			String branchid = aocpCustomer.getBranchid();

			JSONObject data = new JSONObject();
			org.json.simple.JSONObject individual = new org.json.simple.JSONObject();

			individual.put("Type", "Retail Business");
			individual.put("BranchId", branchid);
			individual.put("NamePrefix", "1");
			individual.put("FirstName", aocpvIncomeDetails.getFirstName());
			individual.put("MiddleName", "D");
			individual.put("LastName", aocpvIncomeDetails.getLastName());
			individual.put("MotherNamePrefix", "Mrs.");
			individual.put("MotherMaidenName", " ");
			individual.put("MotherMiddleName", "");
			individual.put("MotherLastName", "");
			individual.put("FatherNamePrefix", "");
			individual.put("FatherName", "");
			individual.put("FatherMiddleName", "");
			individual.put("FatherLastName", "");
			individual.put("Caste", "6");
			individual.put("Religion", "8");
//		String MaritalStatus ="NA";
//			if(aocpvIncomeDetails.getMarried().equalsIgnoreCase("YES")) {
//				MaritalStatus ="M";
//			}
//			else {
//				MaritalStatus ="S";
//			}
			individual.put("MaritalStatus", aocpvIncomeDetails.getMarried());
			individual.put("SpouseNamePrefix", "");
			individual.put("SpouseName", "");
			individual.put("SpouseMiddleName", "");
			individual.put("SpouseLastName", "");
			//char charAt = aocpvIncomeDetails.getGender().charAt(0);
			individual.put("Gender", aocpvIncomeDetails.getGender());
			org.json.simple.JSONObject dateandplaceofbirth = new org.json.simple.JSONObject();
			dateandplaceofbirth.put("BirthDt", aocpvIncomeDetails.getDob());
			dateandplaceofbirth.put("CityOfBirth", "");
			dateandplaceofbirth.put("CountryOfBirth", "IN");
			individual.put("DateAndPlaceOfBirth", dateandplaceofbirth);
			individual.put("ResidentialStatus", "1");
			org.json.simple.JSONObject ccm = new org.json.simple.JSONObject();
			ccm.put("MinorityCode", "8");
			individual.put("CCM", ccm);
			org.json.simple.JSONObject verifier = new org.json.simple.JSONObject();
			verifier.put("Name", "");
			verifier.put("Id", "");
			verifier.put("OrganizationName", "");
			verifier.put("Designation", "");
			verifier.put("VerificationDate", "");
			verifier.put("CKYCApplicationType", "");
			verifier.put("OrganizationCode", "");
			verifier.put("CKYCAccountType", "");
			verifier.put("VerificationDoctype", "");
			individual.put("Verifier", verifier);
			org.json.simple.JSONArray document = new org.json.simple.JSONArray();
			org.json.simple.JSONObject document1 = new org.json.simple.JSONObject();
			document1.put("Type", "PAN");
			document1.put("IdentityNumber", aocpvIncomeDetails.getPanCard());
			document.add(document1);
			org.json.simple.JSONObject document2 = new org.json.simple.JSONObject();
			document2.put("Type", "AADHAR");
			document2.put("IdentityNumber", aocpvIncomeDetails.getAadharCard());
			document.add(document2);
			individual.put("Document", document);
			org.json.simple.JSONObject contactDetails = new org.json.simple.JSONObject();
			org.json.simple.JSONArray contactdetails = new org.json.simple.JSONArray();
			org.json.simple.JSONObject contactDetails1 = new org.json.simple.JSONObject();
			
			org.json.simple.JSONObject postalAddress = new org.json.simple.JSONObject();
			org.json.simple.JSONObject contactDetails2 = new org.json.simple.JSONObject();
			org.json.simple.JSONObject postalAddress1 = new org.json.simple.JSONObject();
			String address1 = aocpCustomer.getAddress();
			if(address1 != null) 
			 {
			org.json.JSONArray addressInJson =new org.json.JSONArray(address1);
  			for(int n=0;n<addressInJson.length();n++) {
  			JSONObject jsonObject2 = addressInJson.getJSONObject(n);
  			String	addressType=jsonObject2.getString("ADDRESSTYPE");
  			if(addressType.equalsIgnoreCase("CURRENT ADDRESS")) {
  				addressType="R";
  				postalAddress.put("Type", addressType);
  				postalAddress.put("Address1", jsonObject2.getString("ADDRESS_LINE1"));
  				postalAddress.put("Address2", jsonObject2.getString("ADDRESS_LINE2"));
  				postalAddress.put("Address3", jsonObject2.getString("ADDRESS_LINE3"));
  				postalAddress.put("Address4", "");
  				postalAddress.put("Taluka", "");
  				postalAddress.put("Landmark", jsonObject2.getString("LANDMARK"));
  				postalAddress.put("District", jsonObject2.getString("DISTRICT"));
  				postalAddress.put("PostCode", jsonObject2.getString("PINCODE"));
  				postalAddress.put("City", jsonObject2.getString("CITY"));
  				postalAddress.put("State", jsonObject2.getString("STATE"));
  				postalAddress.put("Country","IN");
  				org.json.simple.JSONObject geoLocation = new org.json.simple.JSONObject();
  				geoLocation.put("Latitude", "");
  				geoLocation.put("Longitude", "");
  				postalAddress.put("GeoLocation", geoLocation);
  			}
  			else if(addressType.equalsIgnoreCase("PERMANENT ADDRESS")) {
  				addressType="P";
  				postalAddress1.put("Type", addressType);
  				postalAddress1.put("Address1", jsonObject2.getString("ADDRESS_LINE1"));
  				postalAddress1.put("Address2", jsonObject2.getString("ADDRESS_LINE2"));
  				postalAddress1.put("Address3", jsonObject2.getString("ADDRESS_LINE3"));
  				postalAddress1.put("Address4", "");
  				postalAddress1.put("Taluka", "");
  				postalAddress1.put("Landmark", jsonObject2.getString("LANDMARK"));
  				postalAddress1.put("District", jsonObject2.getString("DISTRICT"));
  				postalAddress1.put("PostCode", jsonObject2.getString("PINCODE"));
  				postalAddress1.put("City", jsonObject2.getString("CITY"));
  				postalAddress1.put("State", jsonObject2.getString("STATE"));
  				postalAddress1.put("Country", "IN");
  				org.json.simple.JSONObject geoLocation1 = new org.json.simple.JSONObject();
  				geoLocation1.put("Latitude", "");
  				geoLocation1.put("Longitude", "");
  				postalAddress1.put("GeoLocation", geoLocation1);
  	             
  			}
  			
  			}
			}
			contactDetails1.put("PostalAddress", postalAddress);
			contactDetails2.put("PostalAddress", postalAddress1);
			contactdetails.add(contactDetails1);

			contactdetails.add(contactDetails2);
			
			contactDetails.put("ContactDetails", contactdetails);
			individual.put("ContactDetails", contactDetails);
			individual.put("CKYCDateOfDeclaration", "");
			individual.put("SMSRegistration", "");
			individual.put("SMSCustomer", "0");
			individual.put("EducationQualification", "11");
			individual.put("Form60", "60");
			individual.put("KYCType", "29");
			org.json.simple.JSONObject party = new org.json.simple.JSONObject();
			party.put("EmailAddress", "");
			party.put("Occupation", aocpvIncomeDetails.getOccCode());
			party.put("AnnualNetIncome", "");
			party.put("MobileNumber", aocpCustomer.getMobileNo());
			party.put("CommunicationAddressType", "P");
			party.put("EmployeeId", "");
			individual.put("Party", party);
			org.json.simple.JSONObject introducer = new org.json.simple.JSONObject();
			org.json.simple.JSONArray postaladdress = new org.json.simple.JSONArray();
			introducer.put("PostalAddress", postaladdress);
			introducer.put("CountryOfResidence", "");
			introducer.put("IdentificaitonType", "");
			individual.put("Introducer", introducer);
			individual.put("CustomerCode", "1");
			individual.put("InternetBanking", "0");
			individual.put("MobileBankingFlag", "0");
			individual.put("EmailStatement", "");
			individual.put("AddressProof",  aocpvIncomeDetails.getAadharCard());
			individual.put("KYCLastModifiedDate", "");
			individual.put("PermanentAddressType", "");
			individual.put("PermanentAddressProofType", "");
			individual.put("PermanentAddressProofNumber", "");
			individual.put("MailingAddressType", "");
			individual.put("MailingPermanentAddressFlag", "");
			individual.put("MailingAddressProofType", "");
			individual.put("MailingAddressProofNumber", "");
			individual.put("FATCACompliance", "0");
			individual.put("Source", "IEXCEED");
			individual.put("PoliticallyExposedPerson", "2");
			individual.put("CountryOfResidence", "IN");
			individual.put("SBUCode", "3");
			individual.put("EmailStatementFrequency", "");
			org.json.simple.JSONArray identificationType = new org.json.simple.JSONArray();
			org.json.simple.JSONObject identificationType1 = new org.json.simple.JSONObject();
			identificationType1.put("IdentityType", "PAN");
			identificationType1.put("IdentityNumber", aocpvIncomeDetails.getPanCard());
			identificationType.add(identificationType1);
			org.json.simple.JSONObject identificationType2 = new org.json.simple.JSONObject();
			identificationType2.put("IdentityType", "AADHAR");
			identificationType2.put("IdentityNumber", aocpvIncomeDetails.getAadharCard());
			identificationType.add(identificationType2);
			org.json.simple.JSONObject identificationType3 = new org.json.simple.JSONObject();
			identificationType3.put("Category", "ADDRESSPROOF");
			identificationType3.put("IdentityType", "AADRCD");
			identificationType.add(identificationType3);
			individual.put("IdentificationType", identificationType);
			individual.put("UserId", aocpCustomer.getCreatedby());
			individual.put("DND", "");
			data.put("Individual", individual);
			Data.put("Data", data);
            System.out.println(Data);
            logger.debug("cif request"+Data.toString());
		} else {
			throw new NoSuchElementException("list is empty");
		}
		return Data;
	}

}
