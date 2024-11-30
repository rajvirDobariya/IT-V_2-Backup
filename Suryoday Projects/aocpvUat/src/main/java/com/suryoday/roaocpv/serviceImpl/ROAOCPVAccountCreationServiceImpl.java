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
import com.suryoday.aocpv.pojo.AocpvLoanCreation;
import com.suryoday.aocpv.repository.AocpCustomerDataRepo;
import com.suryoday.aocpv.repository.AocpvIncomeDetailsRepository;
import com.suryoday.aocpv.service.AocpvLoanCreationService;
import com.suryoday.roaocpv.others.ROAOCPVGenerateProperty;
import com.suryoday.roaocpv.service.ApplicationDetailsService;
import com.suryoday.roaocpv.service.ROAOCPVAccountCreationService;

@Service
public class ROAOCPVAccountCreationServiceImpl implements ROAOCPVAccountCreationService {

	private static Logger logger = LoggerFactory.getLogger(ROAOCPVAccountCreationServiceImpl.class);
	@Autowired
	AocpCustomerDataRepo aocpvcustomerdatarepo;
	@Autowired
	AocpvIncomeDetailsRepository aocpvincomedetailsrepo;
	@Autowired
	ApplicationDetailsService applicationDetailsService;

	@Autowired
	AocpvLoanCreationService aocpvLoanCreationService;

	@Override
	public JSONObject accountCreation(JSONObject jsonObject, JSONObject header, AocpvLoanCreation findByApplicationNo) {
		JSONObject sendResponse = new JSONObject();
		String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
		Long applicationNo1 = Long.parseLong(applicationNo);
		JSONObject request = getRequest(applicationNo1, findByApplicationNo);

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
			logger.debug(x.BASEURL + "account/CASA/v2?api_key=" + x.api_key);
			// obj= new
			// URL("https://intramashery.suryodaybank.co.in/ssfbuat/account/CASA/v2?api_key=kyqak5muymxcrjhc5q57vz9v");
			obj = new URL(x.BASEURL + "account/CASA/v2?api_key=" + x.api_key);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("X-Request-ID", header.getString("X-Request-ID"));
			con.setRequestProperty("X-User-ID", header.getString("X-User-ID"));
			con.setRequestProperty("X-From-ID", header.getString("X-From-ID"));
			con.setRequestProperty("X-To-ID", header.getString("X-To-ID"));
			con.setRequestProperty("X-Transaction-ID", header.getString("X-Transaction-ID"));

			sendResponse = getResponseData(request, sendResponse, con, "POST");

		} catch (Exception e) {

			e.printStackTrace();
		}

		return sendResponse;
	}

	private JSONObject getRequest(Long applicationNo1, AocpvLoanCreation aocpvLoanCreation) {
		Optional<AocpCustomer> byApplicationNo = aocpvcustomerdatarepo.getByApplicationNo(applicationNo1);
		Optional<AocpvIncomeDetails> find = aocpvincomedetailsrepo.find(applicationNo1, "SELF");
		String applicationNoInString = Long.toString(applicationNo1);
//		ApplicationDetails fetchByApplicationId = applicationDetailsService.fetchByApplicationId(applicationNoInString);
		JSONObject Data = new JSONObject();
		if (byApplicationNo.isPresent()) {
			AocpCustomer aocpCustomer = byApplicationNo.get();
			AocpvIncomeDetails aocpvIncomeDetails = find.get();

			// AocpvLoanCreation aocpvLoanCreation =
			// aocpvLoanCreationService.findByApplicationNo(applicationNo1);
			JSONObject data = new JSONObject();
			org.json.simple.JSONObject product = new org.json.simple.JSONObject();
			product.put("Group", "SAV");
			product.put("Type", "1010");
			data.put("Product", product);
			data.put("BranchId", aocpCustomer.getBranchid());
			org.json.simple.JSONObject customer = new org.json.simple.JSONObject();
			customer.put("NamePrefix", aocpvIncomeDetails.getTitle()); // aocpCustomer.getTitle()

//			String personalDetailsReq = fetchByApplicationId.getPersonalDetailsReq();
//			if(personalDetailsReq != null) {
//				 JSONObject jsonObject=new JSONObject(personalDetailsReq);
//				 customer.put("FirstName", jsonObject.getString("FirstName"));
//				customer.put("MiddleName", jsonObject.getString("MiddleName"));
//				customer.put("FatherName", jsonObject.getString("FatherName"));
//				customer.put("MotherName", jsonObject.getString("MotherName"));
//				customer.put("LastName", jsonObject.getString("LastName"));
//			}	

			customer.put("FirstName", aocpvIncomeDetails.getFirstName());
			customer.put("MiddleName", "");
			customer.put("FatherName", "");
			customer.put("MotherName", "");
			customer.put("LastName", aocpvIncomeDetails.getLastName());

//			char gender = aocpvIncomeDetails.getGender().charAt(0);
			customer.put("Gender", aocpvIncomeDetails.getGender());
			org.json.simple.JSONObject dateandplaceofbirth = new org.json.simple.JSONObject();
			dateandplaceofbirth.put("BirthDt", aocpCustomer.getDateOfBirth());
			dateandplaceofbirth.put("CityOfBirth", "");
			dateandplaceofbirth.put("CountryOfBirth", "IN");
			customer.put("DateAndPlaceOfBirth", dateandplaceofbirth);
			customer.put("Occupation", aocpvIncomeDetails.getOccCode());
			customer.put("Community", "1");
//			customer.put("OrganizationName", "");
//			customer.put("AdultDependents", "");
//			org.json.simple.JSONObject TDSDetails = new org.json.simple.JSONObject();
//			TDSDetails.put("FormSubmitted", "");
//			customer.put("TDSDetails", TDSDetails);
//			customer.put("TurnOver", "");
//			customer.put("EmailStatementFlag", "");
//			customer.put("NoOfDependents", "");
//			customer.put("OfficePhoneNumber", "");
//			customer.put("MinorDependents", "");
//			customer.put("TurnOver", "");

			customer.put("CountryOfResidence", "IN");
			customer.put("CustomerCode", "IN");
			org.json.simple.JSONArray identificationType = new org.json.simple.JSONArray();
//			org.json.simple.JSONObject identificationType1 = new org.json.simple.JSONObject();
//			identificationType1.put("IdentityNumber", "");
//			identificationType1.put("IdentityType", "");
//			identificationType1.put("Category", "");
//			identificationType1.put("SubCategory", "");
//			identificationType1.put("EmbossedName", "");
//			identificationType1.put("IssuedBy", "");
//			identificationType1.put("PlaceOfIssue", "");
//			identificationType1.put("Country", "");
//			identificationType.add(identificationType1);
			customer.put("IdentificationType", identificationType);
			customer.put("Name", "");
			customer.put("Type", "");
			customer.put("AddressType", "P");

//			customer.put("CustomerEmployeeNumber", "");
//			String MaritalStatus ="NA";
//			if(aocpvIncomeDetails.getMarried().equalsIgnoreCase("YES")) {
//				MaritalStatus ="M";
//			}
//			else {
//				MaritalStatus ="S";
//			}
			String accountData = aocpCustomer.getAccountData();
			String debitcard = "1";
			String InternateBanking = "1";
			String MobileBanking = "1";
			String EStatement = "1";
			String SmsAlerts = "1";
			String ChequeBook = "1";
			String SweepFacility = "0";
			if (accountData == null || accountData.isEmpty()) {
			} else {
				org.json.JSONObject accountdetails = new org.json.JSONObject(accountData);
				if (accountdetails.getString("DebitCard").equalsIgnoreCase("Yes")) {
					debitcard = "1";
				} else {
					debitcard = "0";
				}
				if (accountdetails.getString("InternateBanking").equalsIgnoreCase("Yes")) {
					InternateBanking = "1";
				} else {
					InternateBanking = "0";
				}
				if (accountdetails.getString("MobileBanking").equalsIgnoreCase("Yes")) {
					MobileBanking = "1";
				} else {
					MobileBanking = "0";
				}
				if (accountdetails.getString("EStatement").equalsIgnoreCase("Yes")) {
					EStatement = "1";
				} else {
					EStatement = "0";
				}
				if (accountdetails.getString("SmsAlerts").equalsIgnoreCase("Yes")) {
					SmsAlerts = "1";
				} else {
					SmsAlerts = "0";
				}
				if (accountdetails.getString("ChequeBook").equalsIgnoreCase("Yes")) {
					ChequeBook = "1";
				} else {
					ChequeBook = "0";
				}
				if (accountdetails.getString("SweepFacility").equalsIgnoreCase("Yes")) {
					SweepFacility = "1";
				} else {
					SweepFacility = "0";
				}
			}

			customer.put("MaritalStatus", aocpvIncomeDetails.getMarried());
			customer.put("MobileNumber", Long.toString(aocpCustomer.getMobileNo()));
			customer.put("PhoneNumber", "");
			customer.put("EmailAddress", "");
			customer.put("IncomeCategory", "7");
			customer.put("FATCAFlag", "0");
			customer.put("Category", "17");
			customer.put("ResidentialType", "O");
			customer.put("SMSNotification", SmsAlerts);
			customer.put("Education", "06");
			customer.put("EmployeeNumber", "");
			customer.put("CustomerOrganisationName", "");
			customer.put("EmployeeSector", "");
			customer.put("EmailNotification", SmsAlerts);
			customer.put("CountryOfRegistration", "IN");
			customer.put("NatureOfBusiness", "101");

			customer.put("Designation", "");
			customer.put("MaidenFirstName", "");
			data.put("Customer", customer);
			data.put("ChequeBookRequired", ChequeBook);
			data.put("DebitCardRequired", debitcard);
			org.json.simple.JSONObject account = new org.json.simple.JSONObject();
			account.put("NickName", "");
			org.json.simple.JSONObject nominee = new org.json.simple.JSONObject();
			String nomineeDetail = aocpCustomer.getNomineeDetails();
			String nomineeName = "";
			String nomineeRelationship = "";
			String guardianName = "";
			String nomineeAge = "";
			String nomineedob = "";
			String guardianAge = "";
			String guardianRelationship = "";
			String address1 = "";
			String address2 = "";
			String address3 = "";
			String pincode = "";
			String city = "";
			String state = "";
			String district = "";
			if (nomineeDetail == null || nomineeDetail.isEmpty()) {
			} else {
				org.json.JSONObject nomineeDetails = new org.json.JSONObject(nomineeDetail);

				nomineeName = nomineeDetails.getString("name");
				nomineeRelationship = nomineeDetails.getString("nomineeRelationship");
				guardianName = nomineeDetails.getString("guardianName");
				nomineeAge = nomineeDetails.getString("nomineeAge");
				nomineedob = nomineeDetails.getString("dob");
				guardianAge = nomineeDetails.getString("guardianAge");
				guardianRelationship = nomineeDetails.getString("guardianRelationship");
				address1 = nomineeDetails.getString("address_Line1");
				address2 = nomineeDetails.getString("address_Line2");
				address3 = nomineeDetails.getString("address_Line3");
				pincode = nomineeDetails.getString("pincode");
				city = nomineeDetails.getString("city");
				state = nomineeDetails.getString("state");
				district = nomineeDetails.getString("district");

			}
			nominee.put("Name", nomineeName);
			nominee.put("Relationship", nomineeRelationship); // 35 nomineeRelationship
			nominee.put("GuardianName", guardianName);
			org.json.simple.JSONObject postalAddress = new org.json.simple.JSONObject();

			postalAddress.put("Type", "ADDR");
			postalAddress.put("StreetName", address1);
			postalAddress.put("BuildingNumber", address2);
			postalAddress.put("AddressLine", address3);
			postalAddress.put("Department", "");
			postalAddress.put("SubDepartment", "");
			postalAddress.put("TownName", "");
			postalAddress.put("Landmark", "");
// 				postalAddress.put("District", district);
			postalAddress.put("PostCode", pincode);
			postalAddress.put("City", city);
			// postalAddress.put("State", state);
			postalAddress.put("CountrySubDivision", "IN");
			postalAddress.put("Country", "IN");
			org.json.simple.JSONObject geoLocation = new org.json.simple.JSONObject();
			geoLocation.put("Latitude", "");
			geoLocation.put("Longitude", "");
			postalAddress.put("GeoLocation", geoLocation);
			nominee.put("PostalAddress", postalAddress);

			account.put("Nominee", nominee);
			account.put("InternetBankingStatus", InternateBanking);
			account.put("MobileBankingStatus", MobileBanking);
			account.put("Amount", 0);
			account.put("Address", "");
			account.put("SweepFacility", SweepFacility);
			account.put("DateofBirth", nomineedob);
			account.put("GuardianAge", guardianAge);
//			account.put("Relationship","");
			account.put("NomineeAge", nomineeAge);
			account.put("LeadNo", "VAULT1");
//			account.put("EmbossedName", "");
			account.put("AccountOpeningFormNumber", applicationNoInString);
			account.put("AccountRelationship", "A");
			org.json.simple.JSONArray reference = new org.json.simple.JSONArray();
			org.json.simple.JSONObject reference1 = new org.json.simple.JSONObject();
			reference1.put("PostalAddress", "");
			reference1.put("Name", "");
			reference.add(reference1);
			org.json.simple.JSONObject reference2 = new org.json.simple.JSONObject();
			reference2.put("PostalAddress", "");
			reference2.put("Name", "");
			reference.add(reference2);
			account.put("Reference", reference);
			account.put("ModeOfOperation", "1");
			data.put("Account", account);
			org.json.simple.JSONArray postaladdress = new org.json.simple.JSONArray();
			data.put("PostalAddress", postaladdress);

			data.put("AccountId", "");
			data.put("UCIC", aocpvLoanCreation.getCifNumber());
			Data.put("Data", data);
			System.out.println(Data.toString());
			logger.debug("account opneing request" + Data.toString());
		} else {
			throw new NoSuchElementException("list is empty");
		}
		return Data;
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
}
