package com.suryoday.aocpv.serviceImp;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.aocpv.pojo.AocpCustomer;
import com.suryoday.aocpv.pojo.AocpvIncomeDetails;
import com.suryoday.aocpv.service.AocpvIncomeService;
import com.suryoday.aocpv.service.ClientCreationService;

@Service
public class ClientCreationServiceImpl implements ClientCreationService {

	@Autowired
	AocpvIncomeService aocpvIncomeService;

	@Override
	public JSONObject clientCreation(AocpCustomer applicationNo) {
		// TODO Auto-generated method stub
		return null;
	}

	private JSONObject getRequest(AocpCustomer aocpCustomer) {

		AocpvIncomeDetails member = aocpvIncomeService.getByApplicationNoAndmember(aocpCustomer.getApplicationNo(),
				"SELF");

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("method", "postgroupclientcreation");

		JSONObject data = new JSONObject();
		data.put("BranchId", "010");
		data.put("ClientID", "01000322");
		data.put("ClientTypeID", "G");
		data.put("UpdateCount", "1");

		JSONObject PersonalDetails = new JSONObject();
		PersonalDetails.put("OurBranchID", aocpCustomer.getBranchid());
		PersonalDetails.put("ClientID", "01000322");
		PersonalDetails.put("ClientTypeID", "G");
		PersonalDetails.put("Name", aocpCustomer.getName());
		PersonalDetails.put("TitleID", member.getTitle() != null ? member.getTitle() : "");
		PersonalDetails.put("FirstName", member.getFirstName() != null ? member.getFirstName() : "");
		PersonalDetails.put("MiddleName", "");
		PersonalDetails.put("LastName", member.getLastName() != null ? member.getLastName() : "");
		PersonalDetails.put("GenderID", member.getGender() != null ? member.getGender() : "");
		DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("d/MM/yyyy");
		if (member.getDob() == null) {
			PersonalDetails.put("IsDOBGiven", "0");
		} else {
			PersonalDetails.put("IsDOBGiven", "1");
			String dobInString = member.getDob().format(formatter1);
			PersonalDetails.put("DateOfBirth", dobInString);
		}
		PersonalDetails.put("Age", Integer.toString(member.getAge()));
		PersonalDetails.put("AgeAsOn", "");
		PersonalDetails.put("NationalityID", "");
		PersonalDetails.put("ResidentID", "");
		PersonalDetails.put("PassportNo", "");
		PersonalDetails.put("PassportIssuedCityID", "");
		PersonalDetails.put("PassportExpiryDate", "");
		PersonalDetails.put("LiteracyLevelID", "");
		PersonalDetails.put("MaritalStatusID", member.getMarried());
		PersonalDetails.put("NumberOfHouseMembers", "");
		PersonalDetails.put("NumberOfChildren", "");
		PersonalDetails.put("NumberOfDependents", "");
		PersonalDetails.put("BloodGroupID", "");
		PersonalDetails.put("CanDonateBlood", "0");
		PersonalDetails.put("OpenedBy", "");
		PersonalDetails.put("OpenedDate", LocalDate.now().format(formatter1));
		PersonalDetails.put("ID1CaptionID", "01");
		PersonalDetails.put("ID1", "711028096982");
		PersonalDetails.put("ID2CaptionID", "02");
		PersonalDetails.put("ID2", "4567897564");
		PersonalDetails.put("ID3CaptionID", "");
		PersonalDetails.put("ID3", "");
		PersonalDetails.put("ID4CaptionID", "");
		PersonalDetails.put("ID4", "");
		PersonalDetails.put("CenterID", "01000322");
		PersonalDetails.put("GroupID", "02");
		data.put("PersonalDetails", PersonalDetails);

		JSONObject dt_AddressDetails = new JSONObject();
		JSONArray AddressDetails = new JSONArray();
		if (aocpCustomer.getAddress() == null || aocpCustomer.getAddress().isEmpty()) {

		} else {
			org.json.JSONArray addressInJson = new org.json.JSONArray(aocpCustomer.getAddress());
			String landmark_P = "";
			String address_Line1_P = "";
			String address_Line2_P = "";
			String city_P = "";
			String pincode_P = "";
			String district_P = "";
			String state_P = "";
			String landmark_R = "";
			String address_Line1_R = "";
			String address_Line2_R = "";
			String city_R = "";
			String pincode_R = "";
			String district_R = "";
			String state_R = "";
			for (int n = 0; n < addressInJson.length(); n++) {
				JSONObject jsonObject2 = addressInJson.getJSONObject(n);

				if (jsonObject2.getString("ADDRESSTYPE").equalsIgnoreCase("")) {
					address_Line1_P = jsonObject2.getString("ADDRESS_LINE1");
					address_Line2_P = jsonObject2.getString("ADDRESS_LINE2");
					city_P = jsonObject2.getString("CITY");
					pincode_P = jsonObject2.getString("PINCODE");
					district_P = jsonObject2.getString("DISTRICT");
					state_P = jsonObject2.getString("STATE");
					if (jsonObject2.has("LANDMARK")) {
						landmark_P = jsonObject2.getString("LANDMARK");
					}
				} else if (jsonObject2.getString("ADDRESSTYPE").equalsIgnoreCase("")) {
					address_Line1_R = jsonObject2.getString("ADDRESS_LINE1");
					address_Line2_R = jsonObject2.getString("ADDRESS_LINE2");
					city_R = jsonObject2.getString("CITY");
					pincode_R = jsonObject2.getString("PINCODE");
					district_R = jsonObject2.getString("DISTRICT");
					state_P = jsonObject2.getString("STATE");
					if (jsonObject2.has("LANDMARK")) {
						landmark_R = jsonObject2.getString("LANDMARK");
					}
				}

			}

			JSONObject AddressDetails1 = new JSONObject();
			AddressDetails1.put("AddressTypeID", "R");
			AddressDetails1.put("ZIPCode", pincode_R);
			AddressDetails1.put("Address1", address_Line1_R);
			AddressDetails1.put("Address2", address_Line2_R);
			AddressDetails1.put("LandMark", landmark_R);
			AddressDetails1.put("CityID", "10150158");
			AddressDetails1.put("Country", "IN");
			AddressDetails1.put("StateID", "82");
			AddressDetails1.put("DistrictID", "");
			AddressDetails1.put("SubDistrictID", "");
			AddressDetails1.put("VillageID", "");
			AddressDetails1.put("PlaceID", "");
			AddressDetails1.put("AreaCategoryID", "");
			AddressDetails1.put("Phone1", "");
			AddressDetails1.put("Phone2", "");
			AddressDetails1.put("Mobile", "");
			AddressDetails1.put("Fax", "");
			AddressDetails1.put("Email", "");
			AddressDetails1.put("IsMailingAddress", "0");
			AddressDetails.put(AddressDetails1);
			JSONObject AddressDetails2 = new JSONObject();
			AddressDetails2.put("AddressTypeID", "P");
			AddressDetails2.put("ZIPCode", pincode_P);
			AddressDetails2.put("Address1", address_Line1_P);
			AddressDetails2.put("Address2", address_Line2_P);
			AddressDetails2.put("LandMark", landmark_P);
			AddressDetails2.put("CityID", "10150158");
			AddressDetails2.put("Country", "IN");
			AddressDetails2.put("StateID", "82");
			AddressDetails2.put("DistrictID", "");
			AddressDetails2.put("SubDistrictID", "");
			AddressDetails2.put("VillageID", "");
			AddressDetails2.put("PlaceID", "");
			AddressDetails2.put("AreaCategoryID", "");
			AddressDetails2.put("Phone1", "");
			AddressDetails2.put("Phone2", "");
			AddressDetails2.put("Mobile", "5555555555");
			AddressDetails2.put("Fax", "");
			AddressDetails2.put("Email", "");
			AddressDetails2.put("IsMailingAddress", "1");
			AddressDetails.put(AddressDetails2);
		}

		dt_AddressDetails.put("AddressDetails", AddressDetails);
		data.put("dt_AddressDetails", dt_AddressDetails);

		JSONObject dt_ClientOtherDetails = new JSONObject();
		dt_ClientOtherDetails.put("IsReplacementCust", "");
		dt_ClientOtherDetails.put("DropOutCustID", "");
		dt_ClientOtherDetails.put("ReligionID", "");
		dt_ClientOtherDetails.put("CasteID", "");
		dt_ClientOtherDetails.put("HealthID", "");
		dt_ClientOtherDetails.put("MotherTongueID", "");
		dt_ClientOtherDetails.put("IsEarningMember", "");
		dt_ClientOtherDetails.put("OccupationID1", "");
		dt_ClientOtherDetails.put("OccupationID2", "");
		dt_ClientOtherDetails.put("RationCardTypeID", "");
		dt_ClientOtherDetails.put("CustHasBankAc", "");
		dt_ClientOtherDetails.put("CustBankName", "");
		dt_ClientOtherDetails.put("CustBankAccountID", "");
		dt_ClientOtherDetails.put("SavingsRangeID", "");
		dt_ClientOtherDetails.put("NoYearsCustStay", "");
		dt_ClientOtherDetails.put("NoYearsSpouseStay", "");
		dt_ClientOtherDetails.put("HouseTypeID", "");
		dt_ClientOtherDetails.put("SquareFeetofHouse", "");
		dt_ClientOtherDetails.put("AdvDeposit", "");
		dt_ClientOtherDetails.put("SourceOfDepositID", "");
		dt_ClientOtherDetails.put("HouseValue", "");
		dt_ClientOtherDetails.put("RentLeaseYears", "");
		dt_ClientOtherDetails.put("PattaHolder", "");
		dt_ClientOtherDetails.put("PattaHolderRelnID", "");
		dt_ClientOtherDetails.put("ContractExpiryDate", "");
		dt_ClientOtherDetails.put("Rent", "");
		dt_ClientOtherDetails.put("FloorTypeID", "");
		dt_ClientOtherDetails.put("RoofTypeID", "");
		dt_ClientOtherDetails.put("WallTypeID", "");
		dt_ClientOtherDetails.put("ToiletTypeID", "");
		dt_ClientOtherDetails.put("NoOfRoomsID", "");
		dt_ClientOtherDetails.put("HouseholdAsset", "");
		dt_ClientOtherDetails.put("NoOfHouseholdAsset", "");
		data.put("dt_ClientOtherDetails", dt_ClientOtherDetails);

		JSONObject dt_RelationDetails = new JSONObject();
		JSONArray RelationDetails = new JSONArray();
		JSONObject RelationDetails1 = new JSONObject();
		RelationDetails1.put("IsExistingClient", "0");
		RelationDetails1.put("RelatedClientID", "");
		RelationDetails1.put("RelationRefNo", "1");
		RelationDetails1.put("TitleID", "MS");
		RelationDetails1.put("ClientName", "monikamma");
		RelationDetails1.put("Address", "banaglore");
		RelationDetails1.put("Phone", "");
		RelationDetails1.put("Mobile", "8974542545");
		RelationDetails1.put("Email", "moni.d@gmail.com");
		RelationDetails1.put("GenderID", "F");
		RelationDetails1.put("IsDOBGiven", "1");
		RelationDetails1.put("DateOfBirth", "01/02/2016");
		RelationDetails1.put("Age", "");
		RelationDetails1.put("AgeAsOn", "");
		RelationDetails1.put("RelationID", "M");
		RelationDetails1.put("IsFamilyMember", "1");
		RelationDetails1.put("IsNominee", "1");
		RelationDetails1.put("RelationRoleID", "");
		RelationDetails1.put("ID1CaptionID", "");
		RelationDetails1.put("ID1", "");
		RelationDetails1.put("ID2CaptionID", "");
		RelationDetails1.put("ID2", "");
		RelationDetails1.put("Remarks", "");
		RelationDetails1.put("ButtonMark", "A");
		RelationDetails.put(RelationDetails1);
		JSONObject RelationDetails2 = new JSONObject();
		RelationDetails2.put("IsExistingClient", "0");
		RelationDetails2.put("RelatedClientID", "");
		RelationDetails2.put("RelationRefNo", "2");
		RelationDetails2.put("TitleID", "MS");
		RelationDetails2.put("ClientName", "Meenama");
		RelationDetails2.put("Address", "banaglore");
		RelationDetails2.put("Phone", "");
		RelationDetails2.put("Mobile", "");
		RelationDetails2.put("Email", "");
		RelationDetails2.put("GenderID", "F");
		RelationDetails2.put("IsDOBGiven", "0");
		RelationDetails2.put("DateOfBirth", "");
		RelationDetails2.put("Age", "3");
		RelationDetails2.put("AgeAsOn", "01/01/2015");
		RelationDetails2.put("RelationID", "F");
		RelationDetails2.put("IsFamilyMember", "1");
		RelationDetails2.put("IsNominee", "1");
		RelationDetails2.put("RelationRoleID", "G");
		RelationDetails2.put("ID1CaptionID", "");
		RelationDetails2.put("ID1", "");
		RelationDetails2.put("ID2CaptionID", "");
		RelationDetails2.put("ID2", "");
		RelationDetails2.put("Remarks", "");
		RelationDetails2.put("ButtonMark", "A");
		RelationDetails.put(RelationDetails2);
		dt_RelationDetails.put("RelationDetails", RelationDetails);
		data.put("dt_RelationDetails", dt_RelationDetails);

		JSONObject dt_BankAccountDetails = new JSONObject();
		dt_BankAccountDetails.put("EffectiveDate", "01/01/2016");
		JSONArray AccountDetails = new JSONArray();
		JSONObject AccountDetails1 = new JSONObject();
		AccountDetails1.put("SerialID", "1");
		AccountDetails1.put("ProductTypeID", "SB");
		AccountDetails1.put("InstitutionTypeID", "M");
		AccountDetails1.put("IFSCCode", "");
		AccountDetails1.put("BankID", "");
		AccountDetails1.put("InstitutionName", "WER");
		AccountDetails1.put("BranchID", "");
		AccountDetails1.put("BranchName", "WERTTTuuuuu");
		AccountDetails1.put("AccountID", "12345888");
		AccountDetails1.put("LoanForID", "");
		AccountDetails1.put("ConfirmAccountID", "12345");
		AccountDetails1.put("AccountName", "WER");
		AccountDetails1.put("AdvanceAmount", "");
		AccountDetails1.put("Balance", "");
		AccountDetails1.put("MonthlyPayment", "");
		AccountDetails1.put("Term", "");
		AccountDetails1.put("InsterestRate", "");
		AccountDetails1.put("BorrowPurposeID", "");
		AccountDetails1.put("AverageSavingAmount", "");
		AccountDetails1.put("BorrowRepayID", "");
		AccountDetails1.put("SavingsAccountOpenedBy", "");
		AccountDetails1.put("Remarks", "");
		AccountDetails1.put("ButtonMark", "A");
		AccountDetails.put(AccountDetails1);
		JSONObject AccountDetails2 = new JSONObject();
		AccountDetails2.put("SerialID", "2");
		AccountDetails2.put("ProductTypeID", "SB");
		AccountDetails2.put("InstitutionTypeID", "M");
		AccountDetails2.put("IFSCCode", "");
		AccountDetails2.put("BankID", "");
		AccountDetails2.put("InstitutionName", "SD");
		AccountDetails2.put("BranchID", "");
		AccountDetails2.put("BranchName", "SDF");
		AccountDetails2.put("AccountID", "1234");
		AccountDetails2.put("LoanForID", "");
		AccountDetails2.put("ConfirmAccountID", "1234");
		AccountDetails2.put("AccountName", "blahhhh");
		AccountDetails2.put("AdvanceAmount", "");
		AccountDetails2.put("Balance", "");
		AccountDetails2.put("MonthlyPayment", "");
		AccountDetails2.put("Term", "");
		AccountDetails2.put("InsterestRate", "");
		AccountDetails2.put("BorrowPurposeID", "");
		AccountDetails2.put("AverageSavingAmount", "");
		AccountDetails2.put("BorrowRepayID", "");
		AccountDetails2.put("SavingsAccountOpenedBy", "");
		AccountDetails2.put("Remarks", "");
		AccountDetails2.put("ButtonMark", "A");
		AccountDetails.put(AccountDetails2);
		JSONObject AccountDetails3 = new JSONObject();
		AccountDetails3.put("SerialID", "3");
		AccountDetails3.put("ProductTypeID", "SB");
		AccountDetails3.put("InstitutionTypeID", "M");
		AccountDetails3.put("IFSCCode", "");
		AccountDetails3.put("BankID", "");
		AccountDetails3.put("InstitutionName", "SD");
		AccountDetails3.put("BranchID", "");
		AccountDetails3.put("BranchName", "SDFfff");
		AccountDetails3.put("AccountID", "1234");
		AccountDetails3.put("LoanForID", "");
		AccountDetails3.put("ConfirmAccountID", "1234");
		AccountDetails3.put("AccountName", "");
		AccountDetails3.put("AdvanceAmount", "");
		AccountDetails3.put("Balance", "");
		AccountDetails3.put("MonthlyPayment", "");
		AccountDetails3.put("Term", "");
		AccountDetails3.put("InsterestRate", "");
		AccountDetails3.put("BorrowPurposeID", "");
		AccountDetails3.put("AverageSavingAmount", "");
		AccountDetails3.put("BorrowRepayID", "");
		AccountDetails3.put("SavingsAccountOpenedBy", "");
		AccountDetails3.put("Remarks", "");
		AccountDetails3.put("ButtonMark", "A");
		AccountDetails.put(AccountDetails3);
		dt_BankAccountDetails.put("AccountDetails", AccountDetails);
		data.put("dt_BankAccountDetails", dt_BankAccountDetails);

		JSONObject dt_FamilyFinInfoDetails = new JSONObject();
		dt_FamilyFinInfoDetails.put("AsOnDate", "");
		dt_FamilyFinInfoDetails.put("FamilyIncome", "2000");
		dt_FamilyFinInfoDetails.put("SelfIncome", "");
		dt_FamilyFinInfoDetails.put("OtherIncomeSource", "");
		dt_FamilyFinInfoDetails.put("OtherIncome", "");
		dt_FamilyFinInfoDetails.put("TotalIncome", "2000");
		dt_FamilyFinInfoDetails.put("FoodExp", "");
		dt_FamilyFinInfoDetails.put("MedicalExp", "");
		dt_FamilyFinInfoDetails.put("CookingFuelExp", "");
		dt_FamilyFinInfoDetails.put("ElectricityExp", "");
		dt_FamilyFinInfoDetails.put("TransporationExp", "");
		dt_FamilyFinInfoDetails.put("WaterExp", "");
		dt_FamilyFinInfoDetails.put("EducationExp", "");
		dt_FamilyFinInfoDetails.put("OtherExpSrc1", "");
		dt_FamilyFinInfoDetails.put("OtherExp1", "");
		dt_FamilyFinInfoDetails.put("OtherExpSrc2", "");
		dt_FamilyFinInfoDetails.put("OtherExp2", "");
		dt_FamilyFinInfoDetails.put("OtherExpSrc3", "");
		dt_FamilyFinInfoDetails.put("OtherExp3", "");
		dt_FamilyFinInfoDetails.put("OtherExpSrc4", "");
		dt_FamilyFinInfoDetails.put("OtherExp4", "");
		dt_FamilyFinInfoDetails.put("OtherExpSrc5", "");
		dt_FamilyFinInfoDetails.put("OtherExp5", "");
		dt_FamilyFinInfoDetails.put("OtherExpSrc6", "");
		dt_FamilyFinInfoDetails.put("OtherExp6", "");
		dt_FamilyFinInfoDetails.put("MonthlyInstallment", "");
		dt_FamilyFinInfoDetails.put("TotalExp", "");
		JSONArray FamilyFinInfDetails = new JSONArray();
		JSONObject FamilyFinInfDetails1 = new JSONObject();
		FamilyFinInfDetails1.put("RelnRefNo", "1");
		FamilyFinInfDetails1.put("RelnOccupationID", "");
		FamilyFinInfDetails1.put("RelnEducationID", "");
		FamilyFinInfDetails1.put("EducationInstTypeID", "");
		FamilyFinInfDetails1.put("RelnHealth", "");
		FamilyFinInfDetails1.put("IsEarningMember", "1");
		FamilyFinInfDetails1.put("RelnIncome", "2000");
		FamilyFinInfDetails1.put("ButtonMark", "A");
		FamilyFinInfDetails.put(FamilyFinInfDetails1);
		JSONObject FamilyFinInfDetails2 = new JSONObject();
		FamilyFinInfDetails2.put("RelnRefNo", "2");
		FamilyFinInfDetails2.put("RelnOccupationID", "");
		FamilyFinInfDetails2.put("RelnEducationID", "");
		FamilyFinInfDetails2.put("EducationInstTypeID", "");
		FamilyFinInfDetails2.put("RelnHealth", "");
		FamilyFinInfDetails2.put("IsEarningMember", "1");
		FamilyFinInfDetails2.put("RelnIncome", "5000");
		FamilyFinInfDetails2.put("ButtonMark", "A");
		FamilyFinInfDetails.put(FamilyFinInfDetails2);
		dt_FamilyFinInfoDetails.put("FamilyFinInfDetails", FamilyFinInfDetails);
		data.put("dt_FamilyFinInfoDetails", dt_FamilyFinInfoDetails);

		JSONObject dt_EmploymentDetails = new JSONObject();
		dt_EmploymentDetails.put("IsSalaried", "1");
		dt_EmploymentDetails.put("IsSelfEmployed", "0");
		dt_EmploymentDetails.put("OccupationID", "");
		dt_EmploymentDetails.put("DesignationID", "");
		dt_EmploymentDetails.put("CompanyTypeID", "");
		dt_EmploymentDetails.put("WorkingSince", "");
		dt_EmploymentDetails.put("EmployerName", "");
		dt_EmploymentDetails.put("WorkPermitNo", "");
		dt_EmploymentDetails.put("Salary", "10000");
		dt_EmploymentDetails.put("RentExpense", "");
		dt_EmploymentDetails.put("FamilyIncome", "");
		dt_EmploymentDetails.put("OtherExpenses", "");
		dt_EmploymentDetails.put("OtherIncome", "");
		dt_EmploymentDetails.put("TotalExpenses", "");
		dt_EmploymentDetails.put("TotalIncome", "10000");
		dt_EmploymentDetails.put("NetSavings", "");
		dt_EmploymentDetails.put("BusinessOwnershipID", "");
		dt_EmploymentDetails.put("BusinessLineID", "");
		dt_EmploymentDetails.put("BusinessStartedYear", "");
		dt_EmploymentDetails.put("NoOfEmployee", "");
		dt_EmploymentDetails.put("Comments", "");
		data.put("dt_EmploymentDetails", dt_EmploymentDetails);

		jsonObject.put("Data", data);
		return jsonObject;

	}

}
