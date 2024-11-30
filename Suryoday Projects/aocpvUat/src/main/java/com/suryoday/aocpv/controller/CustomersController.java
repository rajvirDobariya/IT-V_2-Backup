package com.suryoday.aocpv.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
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

import com.suryoday.aocpv.service.CustomerService;

@RestController
@RequestMapping("/customer")
public class CustomersController {

	Logger logger = LoggerFactory.getLogger(CustomersController.class);

	@Autowired
	CustomerService customerService;

	@RequestMapping(value = "/getData", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> getCustomerData(@RequestBody String bm,
			@RequestHeader(name = "X-Request-ID", required = true) String X_REQUEST_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_TRANSACTION_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_TO_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_FORM_ID,
			@RequestHeader(name = "Content-Type", required = true) String CONTEND_TYPE, HttpServletRequest req)
			throws Exception {
		logger.debug("getdata  start");
		logger.debug(" request Data" + bm);
		JSONObject Header = new JSONObject();
		Header.put("X-Request-ID", X_REQUEST_ID);
		Header.put("X-Transaction-ID", X_TRANSACTION_ID);
		Header.put("X-To-ID", X_TO_ID);
		Header.put("X-From-ID", X_FORM_ID);
		Header.put("Content-TypeD", CONTEND_TYPE);

		if (X_REQUEST_ID.equals("IBR")) {
			logger.debug("X_REQUEST_ID is IBR");
			JSONObject jsonObject = new JSONObject(bm);
			// System.out.println(jsonObject.toString());

			JSONObject getResponse = customerService.getData(jsonObject, Header);
			// hardcode Response Start

//			 org.json.simple.JSONObject   response2= new org.json.simple.JSONObject();
//			 response2.put("CIFNUMBER", "180268778");
//			 response2.put("CUSTOMERTYPE","R");
//			 response2.put("BRANCHCODE","10011");
//			 response2.put("TITLE","MR.");
//			 response2.put("FIRSTNAME","Chintan");
//			 response2.put("MIDDLENAME","");
//			 response2.put("LASTNAME","Takwani");
//			 response2.put("GENDER","M");
//			 response2.put("EMAILID","chintantakwani.gec@gmail.com");
//			 response2.put("DOB","22/09/1954");
//			 response2.put("MARITALSTATUS","S");
//			 response2.put("EDUCATION","");
//			 response2.put("RELIGION","1");
//			 response2.put("CASTE","");
//			 response2.put("FATHERNAME","");
//			 response2.put("MOTHERMAINDENNAME","Arunaben");
//			 response2.put("MAIDENNAME","Arunaben");
//			 response2.put("PLACEOFBIRTH","");
//			 response2.put("NATIONALITY","IN");
//			 response2.put("RESIDENTIALSTATUS","");
//			 response2.put("PARENTGUARDIANNAME","");
//			 response2.put("RELATIONSHIP","0");
//			 response2.put("GUARDIANCIFNUMBER","");
//			 response2.put("ADULTDEPENDENT","0");
//			 response2.put("CHILDDEPENDENT","0");
//			 response2.put("COMPANYCIF","");
//			 response2.put("EMPLOYEECODE","");
//			 response2.put("OCCUPATION","");
//			 response2.put("WORKINGCOMPANY","");
//			 response2.put("DESIGNATION","");
//			 response2.put("GROSSANNUALINCOME","0.0");
//			 response2.put("BUSINESSNATURE","");
//			 response2.put("ANNUALTURNOVER","");
//			 response2.put("STATUS","A");
//			 response2.put("REGISTEREDMOBILE","965349915");
//			 response2.put("PAN_NUMBER","BAAPT1965N");
//			 response2.put("AADHAR_NUMBER","123412341234");
//			 response2.put("INCORPORATION_DATE","");
//			 response2.put("DIN","");
//			 response2.put("BARCODE","");
//			 response2.put("FORM60","");
//			 response2.put("TAN","");
//			 response2.put("PLACE_OF_INCORPORATION","");
//			 response2.put("FATCACOMPLIANCE","");
//			 response2.put("CUSTOMERCODE","");
//			 response2.put("CUSTOMERRISKCODE","");
//			 response2.put("SBUCODE","");
//			 response2.put("SPOUSE_NAME","");
//			 response2.put("DND","");
//			 response2.put("SMS_CUSTOMER","N");
//			 response2.put("ELECTRONIC_BILLPAYMENT","");
//			 response2.put("MOBILE_BANKING","Y");
//			 response2.put("NEW_CIF","");
//			 response2.put("ADDRESS_PROOF","");
//			 response2.put("PHYSICALSTATUS","0");
//			 response2.put("EMPLOYEE_NAME","");
//			 response2.put("INTERNET_ACCESS","1");
//			 response2.put("SMS_REGISTRATION","0");
//			 response2.put("STATEMENTBYEMAIL","N");
//			 response2.put("HOBBIES","");
//			 response2.put("POLITICALLYEXPOSED","");
//			 response2.put("NAME_OF_DOCUMENT","");
//			 response2.put("ISSUING_AUTHORITY","");
//			 response2.put("PLACE_OF_ISSUE","");
//			 response2.put("DATE_OF_COMMENCEMENT","");
//			 response2.put("COUNTRY_OF_REGISTRATION","");
//			 response2.put("DATE_OF_BOARD","");
//			 response2.put("NATURE_OF_ACTIVITY","");
//			 response2.put("CORPORATE_GROUP","");
//			 response2.put("IBW_TAGGING","");
//			 response2.put("EMAILSTMTFREQUENCY","");
//			 response2.put("TRADE_LICENSE","");
//			 response2.put("SALES_TAX_NO","");
//			 response2.put("EXCISE_REGISTRATION","");
//			 response2.put("BSR4_LEVEL1","");
//			 response2.put("BSR4_LEVEL2","");
//			 response2.put("BSR4_LEVEL3","");
//			 response2.put("SSI_MSME_REGISTRATION","");
//			 response2.put("VISA_COUNTRY","");
//			 response2.put("VISA_DETAILS","");
//			 response2.put("VISA_EXPIRY","");
//			 response2.put("VISA_ISSUE_BY","");
//			 response2.put("VISA_ISSUE_PLACE","");
//			 response2.put("VISA_TYPE","");
//			 response2.put("VISA_NUMBER","");
//			 response2.put("INSURANCE_TYPE","");
//			 response2.put("INSURANCE_COMPANY","");
//			 response2.put("OTHERBANK_ACCOUNT","");
//			 response2.put("LOAN_FROM_OTHERBANK","");
//			 response2.put("LOAN_BANKNAME","");
//			 response2.put("LOAN_EMI","");
//			 response2.put("TYPE_OF_LOAN","");
//			 response2.put("YEAR_OF_CURRENTADDRESS","");
//			 response2.put("MONTHLY_INCOME","");
//			 response2.put("SELF_EMPLOYEED_MONTH","");
//			 response2.put("SELF_EMPLOYEED_YEARS","");
//			 response2.put("NET_WORTH","0.0");
//			 response2.put("KYC_TYPE","3");
//			 response2.put("TDS_SLAB", "");
//			 response2.put("REKYC_FLAG" ,"");
//			 response2.put("FORM15G","");
//			 response2.put("FORM15H","");
//			 response2.put("RE_KYC_DATE","2026-06-11T00:00:00.000");
//			 response2.put("OWNERSHIPSTATUS","");
//			 response2.put("DRIVERS_LICENSE_STATE","");
//			 response2.put("IDENTIFICATIONTYPE","");
//			 response2.put("OTHER_IDENTIFICATION","");
//			 response2.put("ISSUER","");
//			 response2.put("OTHER_EXPIRATIONDATE","");
//			 response2.put("FATCA_ADDRESS_TYPE","");
//			 response2.put("FAX_NUMBER","");
//			 response2.put("ATTENTION_MSG","");
//			 response2.put("MONEY_RISK_CODE","");
//			 response2.put("INTRODUCER_NAME","");
//			 response2.put("INTRODUCER_CIF","");
//			 response2.put("CUSTOMER_KNOWNSINCE","");
//			 response2.put("INTRODUCER_ADDRESS1","");
//			 response2.put("INTRODUCER_ADDRESS2","");
//			 response2.put("INTRODUCER_ADDRESS3","");
//			 response2.put("INTRODUCER_ADDRESS4","");
//			 response2.put("SMARTSERVICE","");
//			 response2.put("ADDITIONAL_RISKCODE","");
//			 response2.put("CARD_NAME","");
//			 response2.put("CUSTOMER_TAXDETAILS","");
//			 response2.put("COUNTRY_DOMICILE","");
//			 response2.put("INTRODUCER_RELATIONSHIP","");
//			 response2.put("INTRODUCER_RELATION_OTHERS","");
//			 response2.put("CCM_EMPLOYEE_NO","");
//			 response2.put("CCM_NAME","");
//			 response2.put("UIN","");
//			 response2.put("MONTHS_OF_EXPERIENCE","");
//			 response2.put("TIME_IN_RESIDENCE","");
//			 response2.put("CUSTOMER_FAR_LOCATION","");
//			 response2.put("REASON_FAR_LOCATION","");
//			 response2.put("CUSTOMER_BANK_EXPERIENCE","");
//			 response2.put("DND_EMAIL","");
//			 response2.put("FAMILY_MEMBERS","");
//			 response2.put("WEAKER_SECTOR_CODE","");
//			 response2.put("MINORITY_COMMUNITY_CODE","");
//			 response2.put("CORRESPONDENCE_ADDRESS","");
//			 response2.put("CORRESPONDENCE_POLICE","");
//			 response2.put("PERMANENT_ADDRESSPO","");
//			 response2.put("PERMANENT_ADDRESS_POLICE","");
//			 response2.put("RESIDING_CURRADDRESS","");
//			 response2.put("INTRODUCER_DATE","");
//			 response2.put("NAME_OF_AUTHORITY","");
//			 response2.put("FINANCIAL_ASSET1","");
//			 response2.put("FINANCIAL_ASSET2","");
//			 response2.put("FINANCIAL_ASSET3","");
//			 response2.put("FINANCIAL_ASSET4","");
//			 response2.put("FINANCIAL_ASSET5","");
//			 response2.put("INTRODUCER_ACCNO","");
//			 response2.put("CKYCR_LASTUPDATE","");
//			 response2.put("PREFIX_MAIDEN_NAME","");
//			 response2.put("MAIDEN_MIDDLE_NAME","");
//			 response2.put("MAIDEN_LAST_NAME","Arunaben");
//			 response2.put("PREFIX_MOTHER_NAME","");
//			 response2.put("MOTHER_FIRSTNAME","");
//			 response2.put("MOTHER_MIDDLENAME","");
//			 response2.put("MOTHER_LASTNAME","");
//			 response2.put("NETWORTH_DATE","");
//			 response2.put("OFFICE_ADDRESSPO","");
//			 response2.put("OFFICE_ADDRESS_POLICE","");
//			 response2.put("HOUSEHOLD_DETAILS","");
//			 response2.put("PPI_DETAILS","");
//			 response2.put("PAN_VALIDATION_DATE", "");
//			 response2.put("RATION_CARD_TYPE", "");
//			 response2.put("RESIDING_AREA", "");
//			 response2.put("NUMBER_EMPLOYEE_FAMILY","");
//			 response2.put( "PHYSICAL_STATUS_RELATION", "");
//			 response2.put("NAME_MOBILE_OWNER","");
//			 response2.put( "DOB_RELATION", "");
//			 response2.put("AML_SCREENING", "");
//			 response2.put( "PROOF_IDENTITY_NUMBER", "");
//			 response2.put("PROOF_IDENTITY_ISSUEDATE", "");
//			 response2.put("PROOF_IDENTITY_EXPIRYDATE", "");
//			 response2.put( "PROOF_IDENTITY_DOCDATE","");
//			 response2.put("PROOF_ADDRESSTYPE","");
//			 response2.put("PROOF_ADDRESS_ISSUEDATE","");
//			 response2.put("PROOF_ADDRESS_EXPIRYDATE", "");
//			 response2.put( "PROOF_DOC_DATE", "");
//			 response2.put( "PROOF_DOC_DATE","");
//			 response2.put("PROOF_DOC_DATE","");
//			 response2.put("PREFIX_FATHER_NAME","");
//			 response2.put( "FATHER_FIRSTNAME","");
//			 response2.put("FATHER_MIDDLENAME", "");
//			 response2.put( "FATHER_LASTNAME", "");
//			 response2.put("PREFIX_SPOUSE_NAME", "");
//			 response2.put("SPOUSE_FIRSTNAME", "");
//			 response2.put("SPOUSE_MIDDLENAME","");
//			 response2.put( "SPOUSE_LASTNAME", "");
//			 response2.put("KYC_CUSTOMER_DECLARATION","");
//			 response2.put("KYC_VERIFIER_NAME","");
//			 response2.put("KYC_VERIFIER_ID","");
//			 response2.put("KYC_VERIFIER_DESIGNATION","");
//			 response2.put("KYC_VERIFIER_ORG_NAME", "");
//			 response2.put( "KYC_VERIFICATION_DATE","");
//			 response2.put("KYC_VERIFICATION_BRANCH","");
//			 response2.put("CKYC_ACCOUNT_TYPE","");
//			 response2.put("CKYC_APPLICATION_TYPE","");
//			 response2.put("MAILING_PERMANENT_ADDDRESS_FLAG","");
//			 response2.put("CKYC_DATE_OF_DEC","");
//			 response2.put( "LAST_KYC_DATE","");
//			 response2.put("CUST_ONBOARD_DATE","");
//			 
//			 org.json.simple.JSONObject   AddressDetails= new org.json.simple.JSONObject();
//			 org.json.simple.JSONArray  AddressDet= new org.json.simple.JSONArray();
//			 org.json.simple.JSONObject   AddressDet1= new org.json.simple.JSONObject();
//			 AddressDet1.put( "CIFNUMBER", "");
//			 AddressDet1.put( "ADDRESSTYPE","R");
//			 AddressDet1.put("ADDRESS1", "101,om shree balaji niwas, sector 3");
//			 AddressDet1.put( "ADDRESS2", "Belpada, kharghar");
//			 AddressDet1.put( "ADDRESS3", "Navi mumbai");
//			 AddressDet1.put("ADDRESS4", "");
//			 AddressDet1.put( "LANDMARK", "");
//			 AddressDet1.put("TALUKA", "");
//			 AddressDet1.put("DISTRICT", "");
//			 AddressDet1.put("PINCODE","410210");
//			 AddressDet1.put("CITY","PANVE");
//			 AddressDet1.put( "STATE","MH");
//			 AddressDet1.put( "COUNTRY","IN");
//			 AddressDet1.put("RESPHONE", "");
//			 AddressDet1.put( "OFFICEPHONE", "");
//			 AddressDet1.put( "MOBILE","");
//			 AddressDet1.put("EMAILID","chintantakwani.gec@gmail.com");
//			 AddressDet1.put( "BUILDINGTYPE", "");
//			 AddressDet1.put(  "MAILING_ADDRESS","R");
//			 
//			 org.json.simple.JSONObject   AddressDet2= new org.json.simple.JSONObject();
//			 AddressDet2.put( "CIFNUMBER", "");
//			 AddressDet2.put( "ADDRESSTYPE","O");
//			 AddressDet2.put("ADDRESS1", "vadi plot M G road,");
//			 AddressDet2.put( "ADDRESS2", "opp dispensary hospital, Porbandar,");
//			 AddressDet2.put( "ADDRESS3", "");
//			 AddressDet2.put("ADDRESS4", "");
//			 AddressDet2.put( "LANDMARK", "");
//			 AddressDet2.put("TALUKA", "");
//			 AddressDet2.put("DISTRICT", "");
//			 AddressDet2.put("PINCODE","360575");
//			 AddressDet2.put("CITY","PORBA");
//			 AddressDet2.put( "STATE","GJ");
//			 AddressDet2.put( "COUNTRY","IN");
//			 AddressDet2.put("RESPHONE", "");
//			 AddressDet2.put( "OFFICEPHONE", "");
//			 AddressDet2.put( "MOBILE","");
//			 AddressDet2.put("EMAILID","chintantakwani.gec@gmail.com");
//			 AddressDet2.put( "BUILDINGTYPE", "");
//			 AddressDet2.put(  "MAILING_ADDRESS","");
//			 
//			 org.json.simple.JSONObject   AddressDet3= new org.json.simple.JSONObject();
//			 AddressDet3.put( "CIFNUMBER", "");
//			 AddressDet3.put( "ADDRESSTYPE","Permanent");
//			 AddressDet3.put("ADDRESS1", "vadi plot M G road,");
//			 AddressDet3.put( "ADDRESS2", "opp dispensary hospital, Porbandar,");
//			 AddressDet3.put( "ADDRESS3", "NA");
//			 AddressDet3.put("ADDRESS4", "");
//			 AddressDet3.put( "LANDMARK", "");
//			 AddressDet3.put("TALUKA", "");
//			 AddressDet3.put("DISTRICT", "");
//			 AddressDet3.put("PINCODE","360575");
//			 AddressDet3.put("CITY","PORBA");
//			 AddressDet3.put( "STATE","GJ");
//			 AddressDet3.put( "COUNTRY","IN");
//			 AddressDet3.put("RESPHONE", "");
//			 AddressDet3.put( "OFFICEPHONE", "");
//			 AddressDet3.put( "MOBILE","");
//			 AddressDet3.put("EMAILID","chintantakwani.gec@gmail.com");
//			 AddressDet3.put( "BUILDINGTYPE", "");
//			 AddressDet3.put(  "MAILING_ADDRESS","");
//             
//			 org.json.simple.JSONObject   KYCDetails= new org.json.simple.JSONObject();
//			 org.json.simple.JSONArray  KYCDet= new org.json.simple.JSONArray();
//             
//			 org.json.simple.JSONObject   KYCDet1= new org.json.simple.JSONObject();
//			 KYCDet1.put("CIFNUMBER", "180268778");
//			 KYCDet1.put( "DOCUMENTTYPE","6");
//			 KYCDet1.put("DOCUMENTNUMBER", "BAAPT1965N");
//			 KYCDet1.put( "ISSUEDATE", "2018-06-11T00:00:00.000");
//			 KYCDet1.put(   "EXPIRYDATE", "");
//			 
//			 org.json.simple.JSONObject   KYCDet2= new org.json.simple.JSONObject();
//			 KYCDet2.put("CIFNUMBER", "180268778");
//			 KYCDet2.put( "DOCUMENTTYPE","3");
//			 KYCDet2.put("DOCUMENTNUMBER", "123412341234");
//			 KYCDet2.put( "ISSUEDATE", "2018-06-11T00:00:00.000");
//			 KYCDet2.put(   "EXPIRYDATE", "");
//           
//            
//			 org.json.simple.JSONObject   GSTDetails= new org.json.simple.JSONObject();
//			 GSTDetails.put("GSTIN", "");
//            
//			 KYCDet.add(KYCDet2);
//			 KYCDet.add(KYCDet1);
//			 AddressDet.add(AddressDet1);
//			 AddressDet.add(AddressDet2);
//			 AddressDet.add(AddressDet3);
//			 KYCDetails.put("KYCDet", KYCDet);
//			 AddressDetails.put("AddressDet",AddressDet );
//			 response2.put("AddressDetails",AddressDetails);
//			 response2.put("KYCDetails",KYCDetails);
//			 response2.put("GSTDetails",GSTDetails);
//			// response2.put();
//			 
//			
//            
//         
//             
//            
//             
//             
//             
//             
//             
//            
//             
//			 
//			 org.json.simple.JSONArray  CRMCustDataResponse= new org.json.simple.JSONArray();
//			 CRMCustDataResponse.add(response2);
//			 
//			 
//			 org.json.simple.JSONObject   response5= new org.json.simple.JSONObject();
//				response5.put("CRMCustDataResponse", CRMCustDataResponse);
//				response5.put("TransactionCode", "00");
//				
//			 org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
//				response.put("Data", response5);

			// hardcode Response end

			HttpStatus h = HttpStatus.BAD_GATEWAY;
			if (getResponse != null) {

				String Data2 = getResponse.getString("data");
				JSONObject Data1 = new JSONObject(Data2);

				// JSONObject jsonObject2 = getResponse.getJSONObject("data");
				// JSONObject jsonObject3 = jsonObject2.getJSONObject("Data");
				if (Data1.has("Data")) {
					h = HttpStatus.OK;
					JSONArray jsonArray = Data1.getJSONObject("Data").getJSONArray("CRMCustDataResponse");
					JSONObject object = jsonArray.getJSONObject(0);

					if (jsonObject.getJSONObject("Data").getString("CustomerNo").equals("220160978")) {
						object.put("REGISTEREDMOBILE", "9987447004");
						object.put("AADHAR_NUMBER", "594957550563");
						object.put("PAN_NUMBER", "IIZPK2842F");
					} else {
						object.put("REGISTEREDMOBILE", "9987447004");
						object.put("AADHAR_NUMBER", "897783434820");
						object.put("PAN_NUMBER", "IIZPK2842F");
					}
				} else if (Data1.has("Error")) {
					h = HttpStatus.BAD_REQUEST;
				}

				// logger.debug(" final Data"+Data1.toString());
				return new ResponseEntity<Object>(Data1.toString(), h);
			}

			else {
				logger.debug("timeout" + HttpStatus.GATEWAY_TIMEOUT);
				return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
			}
		} else {
			logger.debug("Invalid Request ");
			return new ResponseEntity<Object>("Invalid Request ", HttpStatus.BAD_REQUEST);

		}

	}
}
