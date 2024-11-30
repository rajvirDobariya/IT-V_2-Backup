package com.suryoday.roaocpv.serviceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suryoday.aocpv.pojo.AocpCustomer;
import com.suryoday.aocpv.pojo.AocpvIncomeDetails;
import com.suryoday.aocpv.repository.AocpCustomerDataRepo;
import com.suryoday.aocpv.repository.AocpvIncomeDetailsRepository;
import com.suryoday.roaocpv.others.ROAOCPVGenerateProperty;
import com.suryoday.roaocpv.service.BRNetMastersService;
import com.suryoday.roaocpv.service.ClientCreationService;
@Component
public class ClientCreationServImpl implements ClientCreationService{
	@Autowired
	AocpCustomerDataRepo aocpcustomerdatarepo;
	
	@Autowired 
	AocpvIncomeDetailsRepository aocpincomedetailsrepo;
	
	@Autowired
	BRNetMastersService brmastersservice;
	
	private static Logger logger = LoggerFactory.getLogger(ClientCreationServImpl.class);
	

	@Override
	public JSONObject clientCreation(String applicationNo , JSONObject header) {

		JSONObject sendResponse = new JSONObject();
		JSONObject request=getRequest(applicationNo);
		System.out.println(request);
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
			 obj = new URL(x.BrNetconnect+"BRConnectClientNew/v1/BrNetconnect");
				logger.debug(x.BrNetconnect+"BRConnectClientNew/v1/BrNetconnect");
//			 obj = new URL("https://brn.suryodaybank.co.in/BRConnectClientNew/v1/BrNetconnect");
//			logger.debug("https://brn.suryodaybank.co.in/BRConnectClientNew/v1/BrNetconnect");
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Authorization", header.getString("Authorization"));

			sendResponse = getResponse(request, sendResponse, con, "POST");
//				
//				getHeadersRequestInfo(con);

		} catch (Exception e) {

			e.printStackTrace();
		}

		return sendResponse;
	}
	
	private JSONObject getRequest(String applicationNo) {
		long appNo = Long.parseLong(applicationNo);
		Optional<AocpCustomer> getByApp = aocpcustomerdatarepo.getByApp(appNo);
		AocpCustomer aocpCustomer = getByApp.get();
		Optional<AocpvIncomeDetails> findByAppNo = aocpincomedetailsrepo.find(appNo,"SELF");
		AocpvIncomeDetails aocpvIncomeDetails = findByAppNo.get();
		String address = aocpCustomer.getAddress();
		String addressTypeP = "",addressTypeR= "";
		String addressPLine1 = "",addressRLine1= "";
		String addressPLine2= "",addressRLine2= "";
		String addressPLine3= "",addressRLine3= "";
		String cityP= "",cityR= "";
		String pincodeP= "",pincodeR= "";
		String districtP= "",districtR= "";
		String stateP= "",stateR= "";
		String countryP= "",countryR= "";
		String landmarkP= "",landmarkR= "";
		org.json.JSONArray addressInJson =new org.json.JSONArray(address);
			for(int n=0;n<addressInJson.length();n++) {
					JSONObject jsonObject2 = addressInJson.getJSONObject(n);
					String addressType = jsonObject2.getString("ADDRESSTYPE");
					
					if(addressType.equalsIgnoreCase("CURRENT ADDRESS")) {
						addressTypeR="R";
						addressRLine1=jsonObject2.getString("ADDRESS_LINE1");
						addressRLine2 = jsonObject2.getString("ADDRESS_LINE2");
	  					addressRLine3 = jsonObject2.getString("ADDRESS_LINE3");
	  					cityR = jsonObject2.getString("CITY");
	  					pincodeR = jsonObject2.getString("PINCODE");
	  					districtR = jsonObject2.getString("DISTRICT");
	  					stateR = jsonObject2.getString("STATE");
	  					countryR = jsonObject2.getString("COUNTRY");	
	  					landmarkR=jsonObject2.getString("LANDMARK");
						
					}
					else if(addressType.equalsIgnoreCase("PERMANENT ADDRESS"))
					{
						addressTypeP="P";
						addressPLine1=jsonObject2.getString("ADDRESS_LINE1");
						addressPLine2 = jsonObject2.getString("ADDRESS_LINE2");
	  					addressPLine3 = jsonObject2.getString("ADDRESS_LINE3");
	  					cityP = jsonObject2.getString("CITY");
	  					pincodeP = jsonObject2.getString("PINCODE");
	  					districtP = jsonObject2.getString("DISTRICT");
	  					stateP = jsonObject2.getString("STATE");
	  					countryP = jsonObject2.getString("COUNTRY");	
	  					landmarkP=jsonObject2.getString("LANDMARK");
					}
			}
			String branchid = aocpCustomer.getBranchid().substring(2);
			LocalDate dateOfBirth = aocpCustomer.getDateOfBirth();
			LocalDate now = LocalDate.now();
			 DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("MM/dd/yyyy");
			   	String dob= dateOfBirth.format(formatter1);
			   	String asondate=now.format(formatter1);
			   String categotyType="ZipCodeID";
			    String middleName = aocpCustomer.getName().split(" ")[1];
			    String gender = aocpvIncomeDetails.getGender();
//			    BRNetMasters fetchBrNetMasters = brmastersservice.fetchBrNetMasters("GenderID", gender);
//			    String genderId = fetchBrNetMasters.getSubCodeID();
//			    System.out.println(fetchBrNetMasters);
//			    BRNetMasters fetchBrNetMasters2 = brmastersservice.fetchBrNetMasters("CityID",cityP);
//			    String citypId = fetchBrNetMasters2.getSubCodeID();
//			    BRNetMasters fetchBrNetMasters3 = brmastersservice.fetchBrNetMasters("StateID",stateP);
//			    String statepId = fetchBrNetMasters3.getSubCodeID();
//			    BRNetMasters fetchBrNetMasters4 = brmastersservice.fetchBrNetMasters("DistrictID",districtP);
//			    String districtpId = fetchBrNetMasters4.getSubCodeID();
	//		   	BRNetMasters brNetMasters=brmastersservice.fetchBrNetMasters(categotyType,pincodeP);
		JSONObject Data=new JSONObject();
		Data.put("Method","POSTINDIVIDUALCLIENTCREATION");
		Data.put("BranchId",branchid);
		Data.put("ClientID",Long.toString(aocpCustomer.getCustomerId()));
		Data.put("ClientTypeID","I");
		Data.put("PersonalDetails","<dt_PersonalDetails><OurBranchID>"+branchid+"</OurBranchID><ClientID>"+aocpCustomer.getCustomerId()+"</ClientID><ClientTypeID>I</ClientTypeID><Name>"+aocpCustomer.getName()+"</Name><TitleID>"+aocpvIncomeDetails.getTitle()+"</TitleID><FirstName>"+aocpvIncomeDetails.getFirstName()+"</FirstName><MiddleName>"+middleName+"</MiddleName><LastName>"+aocpvIncomeDetails.getLastName()+"</LastName><GenderID>"+gender+"</GenderID><IsDOBGiven>0</IsDOBGiven><DateOfBirth></DateOfBirth><Age>"+aocpvIncomeDetails.getAge()+"</Age><AgeAsOn>01/01/2020</AgeAsOn><NationalityID>I</NationalityID><ResidentID>R</ResidentID><PassportNo></PassportNo><PassportIssuedCityID></PassportIssuedCityID><PassportExpiryDate>03/31/2024</PassportExpiryDate><LiteracyLevelID>PG</LiteracyLevelID><MaritalStatusID>"+aocpvIncomeDetails.getMarried()+"</MaritalStatusID><NumberOfHouseMembers>5</NumberOfHouseMembers><NumberOfChildren>1</NumberOfChildren><NumberOfDependents>1</NumberOfDependents><BloodGroupID>B+</BloodGroupID><CanDonateBlood></CanDonateBlood><OpenedBy></OpenedBy><OpenedDate>2022-02-12</OpenedDate><ID1CaptionID>06</ID1CaptionID><ID1>"+aocpvIncomeDetails.getAadharCard()+"</ID1><ID2CaptionID>02</ID2CaptionID><ID2>"+aocpvIncomeDetails.getPanCard()+"</ID2><ID3CaptionID>11</ID3CaptionID><ID3>"+aocpvIncomeDetails.getMobile()+"</ID3><ID4CaptionID>12</ID4CaptionID><ID4></ID4><CenterID>00801529</CenterID><ExternalID1>E0001</ExternalID1><ExternalID2>E0002</ExternalID2><IsEKYC>1</IsEKYC><POA>06</POA><POI></POI><MainClientID>00000012</MainClientID><ClientRoleID>A</ClientRoleID><IsMobileNumberVerifed>1</IsMobileNumberVerifed><ZIPCode>233227</ZIPCode><ZIPCode1>233227</ZIPCode1></dt_PersonalDetails>");	
		Data.put("AddressDetails","<dt_AddressDetails><AddressDetails><AddressTypeID>"+addressTypeP+"</AddressTypeID><ZIPCode>233001</ZIPCode><Address1>"+addressPLine1+"</Address1><Address2>"+addressPLine2+"</Address2><LandMark>"+landmarkP+"</LandMark><CityID>"+cityP+"</CityID><Country>IN</Country><StateID>"+stateP+"</StateID><DistrictID>"+districtP+"</DistrictID><SubDistrictID></SubDistrictID><PlaceID></PlaceID><AreaCategoryID></AreaCategoryID><Phone1></Phone1><Phone2></Phone2><Mobile>"+aocpCustomer.getMobileNo()+"</Mobile><Fax></Fax><Email></Email><IsMailingAddress>1</IsMailingAddress><SameAsAddressTypeID>P</SameAsAddressTypeID></AddressDetails><AddressDetails><AddressTypeID>"+addressTypeR+"</AddressTypeID><ZIPCode>233001</ZIPCode><Address1>"+addressRLine1+"</Address1><Address2>"+addressRLine2+"</Address2><LandMark>"+landmarkR+"</LandMark><CityID>01010008</CityID><Country>IN</Country><StateID>15</StateID><DistrictID>0101</DistrictID><SubDistrictID></SubDistrictID><PlaceID>01010002</PlaceID><AreaCategoryID>1</AreaCategoryID><Phone1>8888888888</Phone1><Phone2>8888888888</Phone2><Mobile>8888888888</Mobile><Fax>9999999999</Fax><Email>test@gmail.com</Email><IsMailingAddress>0</IsMailingAddress><SameAsAddressTypeID>P</SameAsAddressTypeID></AddressDetails></dt_AddressDetails>");
		Data.put("ClientOtherDetails","<dt_ClientOtherDetails><IsReplacementCust>1</IsReplacementCust><DropOutCustID>DOC001</DropOutCustID><ReligionID>CR</ReligionID><CasteID>GEN</CasteID><HealthID>G</HealthID><MotherTongueID>01</MotherTongueID><IsEarningMember>1</IsEarningMember><OccupationID1>100</OccupationID1><OccupationID2>101</OccupationID2><RationCardTypeID>APL</RationCardTypeID><CustHasBankAc>1</CustHasBankAc><CustBankName>HDFC Bank</CustBankName><CustBankAccountID>9856325874</CustBankAccountID><SavingsRangeID>01</SavingsRangeID><NoYearsCustStay>4</NoYearsCustStay><NoYearsSpouseStay>6</NoYearsSpouseStay><HouseTypeID>02</HouseTypeID><SquareFeetofHouse>1000</SquareFeetofHouse><AdvDeposit>5000</AdvDeposit><SourceOfDepositID>07</SourceOfDepositID><HouseValue>6000</HouseValue><RentLeaseYears>2</RentLeaseYears><PattaHolder>Yes</PattaHolder><PattaHolderRelnID>F</PattaHolderRelnID><ContractExpiryDate>03/31/2024</ContractExpiryDate><Rent>5000</Rent><FloorTypeID>05</FloorTypeID><RoofTypeID>08</RoofTypeID><WallTypeID>08</WallTypeID><ToiletTypeID>01</ToiletTypeID><NoOfRoomsID></NoOfRoomsID><HouseholdAsset></HouseholdAsset><NoOfHouseholdAsset>100</NoOfHouseholdAsset></dt_ClientOtherDetails>");
		Data.put("RelationDetails","<dt_RelationDetails><RelationDetails><IsExistingClient>0</IsExistingClient><RelatedClientID /><TitleID>MS</TitleID><FirstName>Lalit</FirstName><MiddleName>Raj</MiddleName><LastName>F</LastName><Address>Vastrapur</Address><Phone>5555555555</Phone><Mobile>8596742563</Mobile><Email>relation@gmail.com</Email><GenderID>F</GenderID><IsDOBGiven>0</IsDOBGiven><DateOfBirth></DateOfBirth><Age>32</Age><AgeAsOn>01/01/2009</AgeAsOn><RelationID>S</RelationID><IsFamilyMember>1</IsFamilyMember><IsNominee>1</IsNominee><RelationRoleID>G</RelationRoleID><ID1CaptionID>01</ID1CaptionID><ID1>1234567890</ID1><ID2CaptionID>02</ID2CaptionID><ID2>0987654321</ID2><ID3CaptionID>DL</ID3CaptionID><ID3>1234567890</ID3><ID4CaptionID>VID</ID4CaptionID><ID4>1234567890</ID4><AppointeeName>Pradeep </AppointeeName><AppointeeRelation>C</AppointeeRelation><Remarks>Test</Remarks></RelationDetails><RelationDetails><IsExistingClient>0</IsExistingClient><RelatedClientID /><TitleID>MS</TitleID><FirstName>Ketan</FirstName><MiddleName>singh</MiddleName><LastName>Ramani</LastName><Address>New Ranip</Address><Phone>4444444444</Phone><Mobile>4444444445</Mobile><Email>test@gmail.com</Email><GenderID>F</GenderID><IsDOBGiven>0</IsDOBGiven><DateOfBirth></DateOfBirth><Age>33</Age><AgeAsOn>01/01/2009</AgeAsOn><RelationID>D</RelationID><IsFamilyMember>1</IsFamilyMember><IsNominee>0</IsNominee><RelationRoleID>C</RelationRoleID><ID1CaptionID>02</ID1CaptionID><ID1>1234567890</ID1><ID2CaptionID>03</ID2CaptionID><ID2>0987654321</ID2><ID3CaptionID></ID3CaptionID><ID3>1234567890</ID3><ID4CaptionID></ID4CaptionID><ID4>1234567890</ID4><AppointeeName>Mohan</AppointeeName><AppointeeRelation>C</AppointeeRelation><Remarks>Test</Remarks></RelationDetails></dt_RelationDetails>");
		Data.put("BankAccountDetails","<dt_BankAccountDetails><EffectiveDate>2022-02-01</EffectiveDate><AccountDetails><ProductTypeID>SB</ProductTypeID><InstitutionTypeID>C</InstitutionTypeID><IFSCCode>SBIN0031862</IFSCCode><BankID>SBI</BankID><InstitutionName>StateBankofTesting</InstitutionName><BranchID>"+aocpCustomer.getBranchid()+"</BranchID><BranchName>SGhighway</BranchName><AccountID>A0000000003</AccountID><LoanForID>S</LoanForID><ConfirmAccountID>A0000000003</ConfirmAccountID><AccountName>TEstaccountone</AccountName><AdvanceAmount>10000</AdvanceAmount><Balance>1000</Balance><MonthlyPayment>500</MonthlyPayment><Term>12</Term><InsterestRate>5</InsterestRate><BorrowPurposeID></BorrowPurposeID><AverageSavingAmount>7500</AverageSavingAmount><BorrowRepayID></BorrowRepayID><SavingsAccountOpenedBy>Ofc1Dhr</SavingsAccountOpenedBy><Remarks>ACRemark111111111</Remarks><IsTrxAccount>1</IsTrxAccount><ButtonMark>N</ButtonMark></AccountDetails><AccountDetails><ProductTypeID>LN</ProductTypeID><InstitutionTypeID>C</InstitutionTypeID><IFSCCode>SBIN0031862</IFSCCode><BankID>SBI</BankID><InstitutionName>StateBankofTesting</InstitutionName><BranchID>031862</BranchID><BranchName>Prahladnagar</BranchName><AccountID>200000000004</AccountID><LoanForID>O</LoanForID><ConfirmAccountID>200000000004</ConfirmAccountID><AccountName>Testaccounttwo</AccountName><AdvanceAmount>20000</AdvanceAmount><Balance>2000</Balance><MonthlyPayment>1000</MonthlyPayment><Term>24</Term><InsterestRate>10</InsterestRate><BorrowPurposeID></BorrowPurposeID><AverageSavingAmount>15000</AverageSavingAmount><BorrowRepayID></BorrowRepayID><SavingsAccountOpenedBy>SelfSBI</SavingsAccountOpenedBy><Remarks>ACRemark222222222</Remarks><IsTrxAccount>0</IsTrxAccount><ButtonMark>N</ButtonMark></AccountDetails></dt_BankAccountDetails>");
		Data.put("FamilyFinInfDetails","<dt_FamilyFinInfoDetails><AsOnDate>02/01/2021</AsOnDate><FamilyIncome></FamilyIncome><SelfIncome>"+aocpvIncomeDetails.getMonthlyIncome()+"</SelfIncome><OtherIncomeSource></OtherIncomeSource><OtherIncome></OtherIncome><TotalIncome>"+aocpCustomer.getTotalMonthlyIncome()+"</TotalIncome><FoodExp>"+aocpCustomer.getFoodAndUtility()+"</FoodExp><MedicalExp>"+aocpCustomer.getMedical()+"</MedicalExp><CookingFuelExp>"+aocpCustomer.getRent()+"</CookingFuelExp><ElectricityExp></ElectricityExp><TransporationExp>"+aocpCustomer.getTransportation()+"</TransporationExp><WaterExp>"+aocpCustomer.getOther()+"</WaterExp><EducationExp>"+aocpCustomer.getEducation()+"</EducationExp><OtherExpSrc1></OtherExpSrc1><OtherExp1></OtherExp1><OtherExpSrc2></OtherExpSrc2><OtherExp2></OtherExp2><OtherExpSrc3></OtherExpSrc3><OtherExp3></OtherExp3><OtherExpSrc4></OtherExpSrc4><OtherExp4></OtherExp4><OtherExpSrc5></OtherExpSrc5><OtherExp5></OtherExp5><OtherExpSrc6/><OtherExp6/><MonthlyInstallment></MonthlyInstallment><TotalExp>"+aocpCustomer.getTotal()+"</TotalExp><FamilyFinInfDetails><RelnOccupationID>100</RelnOccupationID><RelnEducationID>PG</RelnEducationID><EducationInstTypeID></EducationInstTypeID><RelnHealth>2</RelnHealth><IsEarningMember></IsEarningMember><RelnIncome>1234</RelnIncome><ButtonMark>N</ButtonMark></FamilyFinInfDetails><FamilyFinInfDetails><RelnOccupationID>101</RelnOccupationID><RelnEducationID>PG</RelnEducationID><EducationInstTypeID></EducationInstTypeID><RelnHealth>1</RelnHealth><IsEarningMember></IsEarningMember><RelnIncome>123456789012.12</RelnIncome><ButtonMark>N</ButtonMark></FamilyFinInfDetails></dt_FamilyFinInfoDetails>");
		Data.put("EmploymentDetails","");
		Data.put("DocumentDetails","<dt_DocumentDetails><DocumentDetails><ImagetypeID>ADP</ImagetypeID><ImageNumber>0123456789</ImageNumber><Image></Image><ImageClassificationID>POA</ImageClassificationID></DocumentDetails><DocumentDetails><ImagetypeID>ADF</ImagetypeID><ImageNumber>123</ImageNumber><Image></Image><ImageClassificationID>POI</ImageClassificationID></DocumentDetails></dt_DocumentDetails>");
		Data.put("UserDefinedField","");
		Data.put("UpdateCount","1");
		System.out.println(Data);
		return Data;
		
	}

	private static JSONObject getResponse(JSONObject parent, JSONObject sendAuthenticateResponse, HttpURLConnection con,
			String MethodType) throws IOException {

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

}
