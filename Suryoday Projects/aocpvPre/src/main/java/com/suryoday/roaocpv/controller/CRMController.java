package com.suryoday.roaocpv.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.suryoday.aocpv.exceptionhandling.EmptyInputException;
import com.suryoday.aocpv.pojo.AocpCustomer;
import com.suryoday.aocpv.pojo.AocpvImages;
import com.suryoday.aocpv.pojo.AocpvIncomeDetails;
import com.suryoday.aocpv.pojo.AocpvLoanCreation;
import com.suryoday.aocpv.pojo.GeoLcation;
import com.suryoday.aocpv.pojo.Image;
import com.suryoday.aocpv.service.AocpCustomerDataService;
import com.suryoday.aocpv.service.AocpvImageService;
import com.suryoday.aocpv.service.AocpvIncomeService;
import com.suryoday.aocpv.service.AocpvLoanCreationService;
import com.suryoday.mhl.controller.ExcelController;
import com.suryoday.roaocpv.others.PDFAo;
import com.suryoday.roaocpv.others.PDFPassbook;
import com.suryoday.roaocpv.others.PDFSanction;
import com.suryoday.roaocpv.pojo.Address;
import com.suryoday.roaocpv.pojo.PersonalDetailResponse;
import com.suryoday.roaocpv.service.ApplicationDetailsService;
import com.suryoday.roaocpv.service.CRMService;



@Controller
@RequestMapping("/rocpv")
public class CRMController {
	
	@Autowired
	CRMService cRMService;
	
	@Autowired
    AocpCustomerDataService aocpCustomerDataService;
    
    @Autowired
    AocpvImageService aocpvImageService;
	
	@Autowired
	AocpvLoanCreationService aocpvLoanCreationService;
	
	@Autowired
	ApplicationDetailsService applicationDetailsService;
	
	@Autowired
	AocpvIncomeService aocpvIncomeService;
	
	Logger logger = LoggerFactory.getLogger(CRMController.class);

	@RequestMapping(value="/consumeCustomerData", method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<Object> getCustomerData(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_Correlation_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_REQUEST_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-From-ID", required = true) String X_FORM_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_TO_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_TRANSACTION_ID,			 
			 @RequestHeader(name = "Content-Type", required = true) String CONTEND_TYPE,HttpServletRequest req)  throws Exception{
		
		 JSONObject Header= new JSONObject();
		 Header.put("X-Correlation-ID",X_Correlation_ID );
		 Header.put("X-Request-ID",X_REQUEST_ID );
		 Header.put("X-User-ID",X_User_ID );
		 Header.put("X-From-ID",X_FORM_ID );
		 Header.put("X-To-ID",X_TO_ID );
		 Header.put("X-Transaction-ID",X_TRANSACTION_ID );
		 Header.put("Content-Type",CONTEND_TYPE );
		 
		 JSONObject jsonObject=new JSONObject(jsonRequest);
			logger.debug("Fetch All Details");
			logger.debug("Request"+jsonRequest);
			
			if(jsonRequest.isEmpty()) {
				logger.debug("Request is empty"+jsonRequest);
				throw new EmptyInputException("Input cannot be empty");
			}
		 
			String appln = jsonObject.getJSONObject("Data").getString("ApplicationNumber");
			
           /* String BranchCode = jsonObject.getJSONObject("Data").getString("BranchCode");
            String Title = jsonObject.getJSONObject("Data").getString("Title");
            String FirstName = jsonObject.getJSONObject("Data").getString("FirstName");
            String MiddleName = jsonObject.getJSONObject("Data").getString("MiddleName");
            String LastName = jsonObject.getJSONObject("Data").getString("LastName");
            String Gender = jsonObject.getJSONObject("Data").getString("Gender");
            String DOB = jsonObject.getJSONObject("Data").getString("DOB");
            String MaritalStatus = jsonObject.getJSONObject("Data").getString("MaritalStatus");
            String Education = jsonObject.getJSONObject("Data").getString("Education");
            String Religion = jsonObject.getJSONObject("Data").getString("Religion");
            String Caste = jsonObject.getJSONObject("Data").getString("Caste");
            String PrefixFatherName = jsonObject.getJSONObject("Data").getString("PrefixFatherName");
            String FatherName = jsonObject.getJSONObject("Data").getString("FatherName");
            String FatherMiddleName = jsonObject.getJSONObject("Data").getString("FatherMiddleName");
            String FatherLastName = jsonObject.getJSONObject("Data").getString("FatherLastName");
            String PrefixMotherName = jsonObject.getJSONObject("Data").getString("PrefixMotherName");
            String MotherMaidenName = jsonObject.getJSONObject("Data").getString("MotherMaidenName");
            String MotherMiddleName = jsonObject.getJSONObject("Data").getString("MotherMiddleName");
            String MotherLastName = jsonObject.getJSONObject("Data").getString("MotherLastName");
            String PrefixMaidenName = jsonObject.getJSONObject("Data").getString("PrefixMaidenName");
            String MaidenName = jsonObject.getJSONObject("Data").getString("MaidenName");
            String MaidenMiddleName = jsonObject.getJSONObject("Data").getString("MaidenMiddleName");
            String MaidenLastName = jsonObject.getJSONObject("Data").getString("MaidenLastName");
            String PlaceOfBirth = jsonObject.getJSONObject("Data").getString("PlaceOfBirth");
            String Nationality = jsonObject.getJSONObject("Data").getString("Nationality");
            String ResidentialStatus = jsonObject.getJSONObject("Data").getString("ResidentialStatus");
            String ParentGuardianName = jsonObject.getJSONObject("Data").getString("ParentGuardianName");
            String Relationship = jsonObject.getJSONObject("Data").getString("Relationship");
            String GuardianCIFNumber = jsonObject.getJSONObject("Data").getString("GuardianCIFNumber");
            String AdultDependent = jsonObject.getJSONObject("Data").getString("AdultDependent");
            String ChildDependent = jsonObject.getJSONObject("Data").getString("ChildDependent");
            String CompanyCIF = jsonObject.getJSONObject("Data").getString("CompanyCIF");
            String EmployeeCode = jsonObject.getJSONObject("Data").getString("EmployeeCode");
            String EmployeeNumber = jsonObject.getJSONObject("Data").getString("EmployeeNumber");
            String Occupation = jsonObject.getJSONObject("Data").getString("Occupation");
            String WorkingCompany = jsonObject.getJSONObject("Data").getString("WorkingCompany");
            String Designation = jsonObject.getJSONObject("Data").getString("Designation");
            String WorkingSince = jsonObject.getJSONObject("Data").getString("WorkingSince");
            String GrossAnnualIncome = jsonObject.getJSONObject("Data").getString("GrossAnnualIncome");
            String BusinessNature = jsonObject.getJSONObject("Data").getString("BusinessNature");
            String AnnualTurnover = jsonObject.getJSONObject("Data").getString("AnnualTurnover");
            String MonthlyIncome = jsonObject.getJSONObject("Data").getString("MonthlyIncome");
            String Status = jsonObject.getJSONObject("Data").getString("Status");
            String PrefferedAddressForCommunication = jsonObject.getJSONObject("Data").getString("PrefferedAddressForCommunication");
            String RegisteredMobile = jsonObject.getJSONObject("Data").getString("RegisteredMobile");
            String AlternateMobile = jsonObject.getJSONObject("Data").getString("AlternateMobile");
            String EmailAddress = jsonObject.getJSONObject("Data").getString("EmailAddress");
            String PANNumber = jsonObject.getJSONObject("Data").getString("PANNumber");
            String AadharNumber = jsonObject.getJSONObject("Data").getString("AadharNumber");
            String VoterIdNumber = jsonObject.getJSONObject("Data").getString("VoterIdNumber");
            String VoterIdIssueDate = jsonObject.getJSONObject("Data").getString("VoterIdIssueDate");
            String DrivingLicenseNumber = jsonObject.getJSONObject("Data").getString("DrivingLicenseNumber");
            String DrivingLicenseIssueDate = jsonObject.getJSONObject("Data").getString("DrivingLicenseIssueDate");
            String DrivingLicenseExpiryDate = jsonObject.getJSONObject("Data").getString("DrivingLicenseExpiryDate");
            String DrivingLicenseState = jsonObject.getJSONObject("Data").getString("DrivingLicenseState");            
            String RationCard = jsonObject.getJSONObject("Data").getString("RationCard");
            String RationCardIssueDate = jsonObject.getJSONObject("Data").getString("RationCardIssueDate");
            String RationCardExpiryDate = jsonObject.getJSONObject("Data").getString("RationCardExpiryDate");
            String NregaCard = jsonObject.getJSONObject("Data").getString("NregaCard");
            String NregaCardIssueDate = jsonObject.getJSONObject("Data").getString("NregaCardIssueDate");
            String NregaCardExpiryDate = jsonObject.getJSONObject("Data").getString("NregaCardExpiryDate");
            String PassportNumber = jsonObject.getJSONObject("Data").getString("PassportNumber");
            String PassportExpiryDate = jsonObject.getJSONObject("Data").getString("PassportExpiryDate");
            String PassportIssueDate = jsonObject.getJSONObject("Data").getString("PassportIssueDate");
            String VisaDetails = jsonObject.getJSONObject("Data").getString("VisaDetails");
            String VisaIssueBy = jsonObject.getJSONObject("Data").getString("VisaIssueBy");
            String VisaIssuePlace = jsonObject.getJSONObject("Data").getString("VisaIssuePlace");
            String VisaExpiry = jsonObject.getJSONObject("Data").getString("VisaExpiry");
            String VisaCountry = jsonObject.getJSONObject("Data").getString("VisaCountry");
            String VisaType = jsonObject.getJSONObject("Data").getString("VisaType");
            String VisaNumber = jsonObject.getJSONObject("Data").getString("VisaNumber");
            String InsuranceNumber = jsonObject.getJSONObject("Data").getString("InsuranceNumber");
            String branchID = jsonObject.getJSONObject("Data").getString("InsuranceType");
            String InsuranceType = jsonObject.getJSONObject("Data").getString("InsuranceCompany");
            String NewCIF = jsonObject.getJSONObject("Data").getString("NewCIF");
            String OtherBankAccount = jsonObject.getJSONObject("Data").getString("OtherBankAccount");
            String LoanFromOtherBank = jsonObject.getJSONObject("Data").getString("LoanFromOtherBank");
            String TypeOfLoan = jsonObject.getJSONObject("Data").getString("TypeOfLoan");
            String LoanEMI = jsonObject.getJSONObject("Data").getString("LoanEMI");
            String YearAtCurrentAddress = jsonObject.getJSONObject("Data").getString("YearAtCurrentAddress");
            String SelfEmployeedYears = jsonObject.getJSONObject("Data").getString("SelfEmployeedYears");
            String SelfEmployeedMonths = jsonObject.getJSONObject("Data").getString("SelfEmployeedMonths");
            String Hobbies = jsonObject.getJSONObject("Data").getString("Hobbies");
            String NetWorth = jsonObject.getJSONObject("Data").getString("NetWorth");
            String OwnerShipStatus = jsonObject.getJSONObject("Data").getString("OwnerShipStatus");
            String IdentificationType = jsonObject.getJSONObject("Data").getString("IdentificationType");
            String OtherIdentification = jsonObject.getJSONObject("Data").getString("OtherIdentification");
            String Issuer = jsonObject.getJSONObject("Data").getString("Issuer");
            String OtherExpirationDate = jsonObject.getJSONObject("Data").getString("OtherExpirationDate");
            String Barcode = jsonObject.getJSONObject("Data").getString("Barcode");
            String FatcaCompliance = jsonObject.getJSONObject("Data").getString("FatcaCompliance");
            String FatcaAddressType = jsonObject.getJSONObject("Data").getString("FatcaAddressType");
            String FaxNumber = jsonObject.getJSONObject("Data").getString("FaxNumber");
            String AttentionMsg = jsonObject.getJSONObject("Data").getString("AttentionMsg");
            String MoneyRiskCode = jsonObject.getJSONObject("Data").getString("MoneyRiskCode");
            String IntroducerName = jsonObject.getJSONObject("Data").getString("IntroducerName");
            String IntroducerCIF = jsonObject.getJSONObject("Data").getString("IntroducerCIF");
            String CustomerKnownSince = jsonObject.getJSONObject("Data").getString("CustomerKnownSince");
            String IntroducerAddress1 = jsonObject.getJSONObject("Data").getString("IntroducerAddress1");
            String IntroducerAddress2 = jsonObject.getJSONObject("Data").getString("IntroducerAddress2");
            String IntroducerAddress3 = jsonObject.getJSONObject("Data").getString("IntroducerAddress3");
            String IntroducerAddress4 = jsonObject.getJSONObject("Data").getString("IntroducerAddress4");
            String SmartService = jsonObject.getJSONObject("Data").getString("SmartService");
            String AdditionalRiskCode = jsonObject.getJSONObject("Data").getString("AdditionalRiskCode");
            String CardName = jsonObject.getJSONObject("Data").getString("CardName");
            String CustomerTaxDetails = jsonObject.getJSONObject("Data").getString("CustomerTaxDetails");
            String CountryDomicile = jsonObject.getJSONObject("Data").getString("CountryDomicile");
            String IntroducerRelationship = jsonObject.getJSONObject("Data").getString("IntroducerRelationship");
            String IntroducerRelationOtherS = jsonObject.getJSONObject("Data").getString("IntroducerRelationOtherS");
            String CCMEmployeeNo = jsonObject.getJSONObject("Data").getString("CCMEmployeeNo");
            String CCMName = jsonObject.getJSONObject("Data").getString("CCMName");           
            String UniqueIdentificationNumber = jsonObject.getJSONObject("Data").getString("UniqueIdentificationNumber");
            String MonthsOfExperience = jsonObject.getJSONObject("Data").getString("MonthsOfExperience");
            String TimeInResidence = jsonObject.getJSONObject("Data").getString("TimeInResidence");
            String CustomerFarLocation = jsonObject.getJSONObject("Data").getString("CustomerFarLocation");
            String ReasonForLocation = jsonObject.getJSONObject("Data").getString("ReasonForLocation");
            String CustomerBankExperiance = jsonObject.getJSONObject("Data").getString("CustomerBankExperiance");
            String DNDEmail = jsonObject.getJSONObject("Data").getString("DNDEmail");
            String FamilyMembers = jsonObject.getJSONObject("Data").getString("FamilyMembers");
            String WeakerSectorCode = jsonObject.getJSONObject("Data").getString("WeakerSectorCode");
            String MinorityCode = jsonObject.getJSONObject("Data").getString("MinorityCode");
            String CorrespondenceAddress = jsonObject.getJSONObject("Data").getString("CorrespondenceAddress");
            String CorrespondencePoliceStation = jsonObject.getJSONObject("Data").getString("CorrespondencePoliceStation");
            String PermanentAddressPO = jsonObject.getJSONObject("Data").getString("PermanentAddressPO");
            String PermanentAddressPoliceStation = jsonObject.getJSONObject("Data").getString("PermanentAddressPoliceStation");
            String ResidingCurrentAddress = jsonObject.getJSONObject("Data").getString("ResidingCurrentAddress");
            String IntroducerDate = jsonObject.getJSONObject("Data").getString("IntroducerDate");
            String NameOfAuthority = jsonObject.getJSONObject("Data").getString("NameOfAuthority");
            String FinancialAsset1 = jsonObject.getJSONObject("Data").getString("FinancialAsset1");
            String FinancialAsset2 = jsonObject.getJSONObject("Data").getString("FinancialAsset2");
            String FinancialAsset3 = jsonObject.getJSONObject("Data").getString("FinancialAsset3");
            String FinancialAsset4 = jsonObject.getJSONObject("Data").getString("FinancialAsset4");
            String FinancialAsset5 = jsonObject.getJSONObject("Data").getString("FinancialAsset5");
            String CKYCRLastUpdate = jsonObject.getJSONObject("Data").getString("CKYCRLastUpdate");
            String NetWorthDate = jsonObject.getJSONObject("Data").getString("NetWorthDate");
            String OfficeAddressPO = jsonObject.getJSONObject("Data").getString("OfficeAddressPO");
            String OfficeAddressPoliceStation = jsonObject.getJSONObject("Data").getString("OfficeAddressPoliceStation");
            String HouseholdDetails = jsonObject.getJSONObject("Data").getString("HouseholdDetails");
            String PPIDetails = jsonObject.getJSONObject("Data").getString("PPIDetails");
            String PanValidationDate = jsonObject.getJSONObject("Data").getString("PanValidationDate");
            String RationCardType = jsonObject.getJSONObject("Data").getString("RationCardType");
            String ResidingArea = jsonObject.getJSONObject("Data").getString("ResidingArea");
            String NoOfEmployeesInFamily = jsonObject.getJSONObject("Data").getString("NoOfEmployeesInFamily");
            String PhysicalStatusRelation = jsonObject.getJSONObject("Data").getString("PhysicalStatusRelation");
            String MobileNumberOwner = jsonObject.getJSONObject("Data").getString("MobileNumberOwner");
            String DOBRelation = jsonObject.getJSONObject("Data").getString("DOBRelation");
            String HouseRentedSince = jsonObject.getJSONObject("Data").getString("HouseRentedSince");
            String AMLScreening = jsonObject.getJSONObject("Data").getString("AMLScreening");
            String IdentityProofType = jsonObject.getJSONObject("Data").getString("IdentityProofType");
            String IdentityProofNumber = jsonObject.getJSONObject("Data").getString("IdentityProofNumber");
            String IdentityProofIssueDate = jsonObject.getJSONObject("Data").getString("IdentityProofIssueDate");
            String IdentityExpiryDate = jsonObject.getJSONObject("Data").getString("IdentityExpiryDate");
            String IdentityProofDocDate = jsonObject.getJSONObject("Data").getString("IdentityProofDocDate");
            String AddressProofType = jsonObject.getJSONObject("Data").getString("AddressProofType");
            String AddressProofIssueDate = jsonObject.getJSONObject("Data").getString("AddressProofIssueDate");
            String AddressProofExpiryDate = jsonObject.getJSONObject("Data").getString("AddressProofExpiryDate");
            String ProofDocumentDate = jsonObject.getJSONObject("Data").getString("ProofDocumentDate");
            String KYCCustomerDeclare = jsonObject.getJSONObject("Data").getString("KYCCustomerDeclare");
            String KYCVerifierName = jsonObject.getJSONObject("Data").getString("KYCVerifierName");
            String KYCVerifierId = jsonObject.getJSONObject("Data").getString("KYCVerifierId");
            String KYCVerifierDesignation = jsonObject.getJSONObject("Data").getString("KYCVerifierDesignation");
            String KYCVerifierOrgName = jsonObject.getJSONObject("Data").getString("KYCVerifierOrgName");
            String KYCVerifierOrgCode = jsonObject.getJSONObject("Data").getString("KYCVerifierOrgCode");
            String KYCVerificationDate = jsonObject.getJSONObject("Data").getString("KYCVerificationDate");
            String KYCVerificationBranch = jsonObject.getJSONObject("Data").getString("KYCVerificationBranch");
            String KYCVerificationDocType = jsonObject.getJSONObject("Data").getString("KYCVerificationDocType");
            String CKYCAccountType = jsonObject.getJSONObject("Data").getString("CKYCAccountType");            
            String CKYCApplicationType = jsonObject.getJSONObject("Data").getString("CKYCApplicationType");
            String MailingAddressType = jsonObject.getJSONObject("Data").getString("MailingAddressType");
            String MailingAndPermanentAddressFlag = jsonObject.getJSONObject("Data").getString("MailingAndPermanentAddressFlag");
            String MailingAddressProofNumber = jsonObject.getJSONObject("Data").getString("MailingAddressProofNumber");
            String MailingAddressProofType = jsonObject.getJSONObject("Data").getString("MailingAddressProofType");
            String PermanentAddressType = jsonObject.getJSONObject("Data").getString("PermanentAddressType");
            String PermanentAddressProofNumber = jsonObject.getJSONObject("Data").getString("PermanentAddressProofNumber");
            String PermanentAddressProofType = jsonObject.getJSONObject("Data").getString("PermanentAddressProofType");
            String OfficeAddressType = jsonObject.getJSONObject("Data").getString("OfficeAddressType");
            String CKYCDateOfDeclaration = jsonObject.getJSONObject("Data").getString("CKYCDateOfDeclaration");
            String CustomerCode = jsonObject.getJSONObject("Data").getString("CustomerCode");
            String PrefixSpouseName = jsonObject.getJSONObject("Data").getString("PrefixSpouseName");
            String SpouseName = jsonObject.getJSONObject("Data").getString("SpouseName");
            String SpouseMiddleName = jsonObject.getJSONObject("Data").getString("SpouseMiddleName");
            String SpouseLastName = jsonObject.getJSONObject("Data").getString("SpouseLastName");
            String SBUCode = jsonObject.getJSONObject("Data").getString("SBUCode");
            String CustomerRiskCode = jsonObject.getJSONObject("Data").getString("CustomerRiskCode");
            String ElectronicBillPayment = jsonObject.getJSONObject("Data").getString("ElectronicBillPayment");
            String MobileBanking = jsonObject.getJSONObject("Data").getString("MobileBanking");
            String AddressProof = jsonObject.getJSONObject("Data").getString("AddressProof");
            String PhysicalStatus = jsonObject.getJSONObject("Data").getString("PhysicalStatus");
            String InternetBanking = jsonObject.getJSONObject("Data").getString("InternetBanking");
            String EmployeeName = jsonObject.getJSONObject("Data").getString("EmployeeName");
            String SMSRegistration = jsonObject.getJSONObject("Data").getString("SMSRegistration");
            String SMSCustomer = jsonObject.getJSONObject("Data").getString("SMSCustomer");
            String StatementByEmail = jsonObject.getJSONObject("Data").getString("StatementByEmail");
            String CKYCRNumber = jsonObject.getJSONObject("Data").getString("CKYCRNumber");
            String PoliticallyExposedPerson = jsonObject.getJSONObject("Data").getString("PoliticallyExposedPerson");
            String ReKYCFlag = jsonObject.getJSONObject("Data").getString("ReKYCFlag");
            String KYC_Type = jsonObject.getJSONObject("Data").getString("KYC_Type");
            String ReKYCDate = jsonObject.getJSONObject("Data").getString("ReKYCDate");
            String LastKYCDate = jsonObject.getJSONObject("Data").getString("LastKYCDate");
            String CustomerOnBoardDate = jsonObject.getJSONObject("Data").getString("CustomerOnBoardDate");
            String TDS_Slab = jsonObject.getJSONObject("Data").getString("TDS_Slab");
            String Form60 = jsonObject.getJSONObject("Data").getString("Form60");
            String Form61 = jsonObject.getJSONObject("Data").getString("Form61");
            String Form15H = jsonObject.getJSONObject("Data").getString("Form15H");
            String Form15G = jsonObject.getJSONObject("Data").getString("Form15G");
            String DateOfIncorporation = jsonObject.getJSONObject("Data").getString("DateOfIncorporation");
            String RegistrationNumber = jsonObject.getJSONObject("Data").getString("RegistrationNumber");
            String CIN = jsonObject.getJSONObject("Data").getString("CIN");
            String DIN = jsonObject.getJSONObject("Data").getString("DIN");
            String EntityType = jsonObject.getJSONObject("Data").getString("EntityType");
            String DND = jsonObject.getJSONObject("Data").getString("DND");
            String BSR4Level1 = jsonObject.getJSONObject("Data").getString("BSR4Level1");
            String BSR4Level2 = jsonObject.getJSONObject("Data").getString("BSR4Level2");
            String BSR4Level3 = jsonObject.getJSONObject("Data").getString("BSR4Level3");
            String NameOfDocument = jsonObject.getJSONObject("Data").getString("NameOfDocument");
            String IssuingAuthority = jsonObject.getJSONObject("Data").getString("IssuingAuthority");
            String PlaceOfIssue = jsonObject.getJSONObject("Data").getString("PlaceOfIssue");
            String DateOfCommencement = jsonObject.getJSONObject("Data").getString("DateOfCommencement");
            String CountryOfRegistration = jsonObject.getJSONObject("Data").getString("CountryOfRegistration");
            String DateOfBoardResolution = jsonObject.getJSONObject("Data").getString("DateOfBoardResolution");
            String NatureOfActivity = jsonObject.getJSONObject("Data").getString("NatureOfActivity");
            String CorporateGroup = jsonObject.getJSONObject("Data").getString("CorporateGroup");
            String IBWTagging = jsonObject.getJSONObject("Data").getString("IBWTagging");            
            String EmailStatementFrequency = jsonObject.getJSONObject("Data").getString("EmailStatementFrequency");
            String TANNumber = jsonObject.getJSONObject("Data").getString("TANNumber");
            String TradeLicenseNo = jsonObject.getJSONObject("Data").getString("TradeLicenseNo");
            String SalesTaxNo = jsonObject.getJSONObject("Data").getString("SalesTaxNo");
            String ExciseRegistrationNo = jsonObject.getJSONObject("Data").getString("ExciseRegistrationNo");
            String SSIMSMERegistrationNumber = jsonObject.getJSONObject("Data").getString("SSIMSMERegistrationNumber");
            String LocaleOffice = jsonObject.getJSONObject("Data").getString("LocaleOffice");
            String AddressType = jsonObject.getJSONObject("Data").getString("AddressType");
            String Address1 = jsonObject.getJSONObject("Data").getString("Address1");
            String Address2 = jsonObject.getJSONObject("Data").getString("Address2");
            String Address3 = jsonObject.getJSONObject("Data").getString("Address3");
            String Address4 = jsonObject.getJSONObject("Data").getString("Address4");
            String Landmark = jsonObject.getJSONObject("Data").getString("Landmark");
            String Taluka = jsonObject.getJSONObject("Data").getString("Taluka");
            String District = jsonObject.getJSONObject("Data").getString("District");
            String PinCode = jsonObject.getJSONObject("Data").getString("PinCode");
            String City = jsonObject.getJSONObject("Data").getString("City");
            String State = jsonObject.getJSONObject("Data").getString("State");
            String Country = jsonObject.getJSONObject("Data").getString("Country");
            String ResPhone = jsonObject.getJSONObject("Data").getString("ResPhone");
            String OfficePhone = jsonObject.getJSONObject("Data").getString("OfficePhone");
            String Mobile = jsonObject.getJSONObject("Data").getString("Mobile");
            String EmailId = jsonObject.getJSONObject("Data").getString("EmailId");
            String BuildingType = jsonObject.getJSONObject("Data").getString("BuildingType");
            String AddressType1 = jsonObject.getJSONObject("Data").getString("AddressType1");
            String Address11 = jsonObject.getJSONObject("Data").getString("Address11");
            String Address12 = jsonObject.getJSONObject("Data").getString("Address12");
            String Address13 = jsonObject.getJSONObject("Data").getString("Address13");
            String Address14 = jsonObject.getJSONObject("Data").getString("Address14");
            String Landmark1 = jsonObject.getJSONObject("Data").getString("Landmark1");
            String Taluka1 = jsonObject.getJSONObject("Data").getString("Taluka1");
            String District1 = jsonObject.getJSONObject("Data").getString("District1");
            String PinCode1 = jsonObject.getJSONObject("Data").getString("PinCode1");
            String City1 = jsonObject.getJSONObject("Data").getString("City1");
            String State1 = jsonObject.getJSONObject("Data").getString("State1");
            String Country1 = jsonObject.getJSONObject("Data").getString("Country1");
            String ResPhone1 = jsonObject.getJSONObject("Data").getString("ResPhone1");
            String OfficePhone1 = jsonObject.getJSONObject("Data").getString("OfficePhone1");
            String Mobile1 = jsonObject.getJSONObject("Data").getString("Mobile1");
            String EmailId1 = jsonObject.getJSONObject("Data").getString("EmailId1");
            String BuildingType1 = jsonObject.getJSONObject("Data").getString("BuildingType1");
            String AddressType2 = jsonObject.getJSONObject("Data").getString("AddressType2");
            String Address21 = jsonObject.getJSONObject("Data").getString("Address21");
            String Address22 = jsonObject.getJSONObject("Data").getString("Address22");
            String Address23 = jsonObject.getJSONObject("Data").getString("Address23");
            String Address24 = jsonObject.getJSONObject("Data").getString("Address24");
            String Landmark2 = jsonObject.getJSONObject("Data").getString("Landmark2");
            String Taluka2 = jsonObject.getJSONObject("Data").getString("Taluka2");
            String District2 = jsonObject.getJSONObject("Data").getString("District2");
            String PinCode2 = jsonObject.getJSONObject("Data").getString("PinCode2");
            String City2 = jsonObject.getJSONObject("Data").getString("City2");
            String State2 = jsonObject.getJSONObject("Data").getString("State2");
            String Country2 = jsonObject.getJSONObject("Data").getString("Country2");
            String ResPhone2 = jsonObject.getJSONObject("Data").getString("ResPhone2");
            String OfficePhone2 = jsonObject.getJSONObject("Data").getString("OfficePhone2");
            String Mobile2 = jsonObject.getJSONObject("Data").getString("Mobile2");
            String EmailId2 = jsonObject.getJSONObject("Data").getString("EmailId2");
            String BuildingType2 = jsonObject.getJSONObject("Data").getString("BuildingType2");
            String DocumentType1 = jsonObject.getJSONObject("Data").getString("DocumentType1");
            String DocumentNumber1 = jsonObject.getJSONObject("Data").getString("DocumentNumber1");
            String IssueDate1 = jsonObject.getJSONObject("Data").getString("IssueDate1");
            String ExpiryDate1 = jsonObject.getJSONObject("Data").getString("ExpiryDate1");
            String DocumentType2 = jsonObject.getJSONObject("Data").getString("DocumentType2");
            String DocumentNumber2 = jsonObject.getJSONObject("Data").getString("DocumentNumber2");
            String IssueDate2 = jsonObject.getJSONObject("Data").getString("IssueDate2");
            String ExpiryDate2 = jsonObject.getJSONObject("Data").getString("ExpiryDate2");
            String DocumentType3 = jsonObject.getJSONObject("Data").getString("DocumentType3");            
            String DocumentNumber3 = jsonObject.getJSONObject("Data").getString("DocumentNumber3");
            String IssueDate3 = jsonObject.getJSONObject("Data").getString("IssueDate3");
            String ExpiryDate3 = jsonObject.getJSONObject("Data").getString("ExpiryDate3");
            String DocumentType4 = jsonObject.getJSONObject("Data").getString("DocumentType4");
            String DocumentNumber4 = jsonObject.getJSONObject("Data").getString("DocumentNumber4");
            String IssueDate4 = jsonObject.getJSONObject("Data").getString("IssueDate4");
            String ExpiryDate4 = jsonObject.getJSONObject("Data").getString("ExpiryDate4");
            String Source = jsonObject.getJSONObject("Data").getString("Source");
            String UserId = jsonObject.getJSONObject("Data").getString("UserId");
            String CRMUserId = jsonObject.getJSONObject("Data").getString("CRMUserId");
            String PlaceOfIncorporation = jsonObject.getJSONObject("Data").getString("PlaceOfIncorporation");
            String UdhyogAadhar = jsonObject.getJSONObject("Data").getString("UdhyogAadhar");*/
            
//            List<String> listCRM = new ArrayList<>();
//            listCRM.add(CustomerType);
            logger.debug("db Call start");
            System.out.println("List"+appln);  
            org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
            
            int max = 899999;
    		int min = 800000;
    		int random_int = (int) Math.floor(Math.random() * (max - min + 1) + min);
    		//String stan = Integer.toString(random_int);
    		if (random_int % 2 == 0) {
    			 Thread.sleep(5000);
            	 response.put("Success", "CRM Number Generated");    			
    		} else {    			
    			Thread.sleep(5000);
           	 	response.put("Error", "CRM Number Not Generated");        			
    		}        
            logger.debug("Final Response"+response.toString());
		 return new ResponseEntity<Object>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/CRM", method = RequestMethod.POST , produces = "application/json")
    public ResponseEntity<Object> crmData(@RequestBody String jsonRequest,
    		 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID
    		) throws IOException {
		 JSONObject jsonObject=new JSONObject(jsonRequest);
		 String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		logger.debug("your rest api is working");
    	JSONObject jsonObjectresponse= new JSONObject();   	
    	jsonObjectresponse = cRMService.crmData(applicationNo,X_User_ID);
   	
    	HttpStatus  h=HttpStatus.BAD_REQUEST;
    	if(jsonObjectresponse!=null) {
    		 String Data2= jsonObjectresponse.getString("data");
			 JSONObject Data1= new JSONObject(Data2);
			 if(Data1.has("Data")) {
				 h= HttpStatus.OK;
			 }
			 return new ResponseEntity<Object>(Data1.toString(), h);
    	}else {
    		 logger.debug("GATEWAY_TIMEOUT");
			 return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
    	   	
    }
	
	@RequestMapping(value = "/getBranchNameCode", method = RequestMethod.POST , produces = "application/json")
    public ResponseEntity<Object> getBranchNameCode(
    		@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_Correlation_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req
    		) throws IOException {
	
		JSONObject Header = new JSONObject();
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-To-ID", X_To_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("X-Correlation-ID", X_Correlation_ID);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Request-ID", X_Request_ID);
		
		logger.debug("your rest api is working"); 
    	JSONObject jsonObject = cRMService.getBranchNameCode(Header);
    	HttpStatus h = HttpStatus.BAD_GATEWAY;
		if (jsonObject != null) {
			String Data2 = jsonObject.getString("data");
			System.out.println("data2");
			JSONObject Data1 = new JSONObject(Data2);

			System.out.println(Data1);

			if (Data1.has("Data")) {
				h = HttpStatus.OK;

			} else if (Data1.has("Error")) {
				h = HttpStatus.BAD_REQUEST;

			}

			return new ResponseEntity<Object>(Data1.toString(), h);

		} else {

			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}	   	
    }
	
	@RequestMapping(value="/getSanctionLetter", method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<Object> getSanctionLetter(@RequestBody String jsonRequest,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_REQUEST_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_TRANSACTION_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_TO_ID,
			 @RequestHeader(name = "X-From-ID", required = true) String X_FORM_ID,
			 @RequestHeader(name = "Content-Type", required = true) String CONTEND_TYPE,HttpServletRequest req,HttpServletResponse res)  throws Exception{
		 res.setContentType("application/pdf");
		 res.setHeader("Content-Disposition", "attachment; filename=Receipt.pdf");
		 
		 JSONObject jsonObject=new JSONObject(jsonRequest);
			logger.debug("Fetch All Details");
			logger.debug("Request"+jsonRequest);
			
			if(jsonRequest.isEmpty()) {
				logger.debug("Request is empty"+jsonRequest);
				throw new EmptyInputException("Input cannot be empty");
			}
			
			String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
			Long applicationNoLong=Long.parseLong(applicationNo);
			AocpvLoanCreation aocpvLoanCreation = aocpvLoanCreationService.findByApplicationNo(applicationNoLong);
		
			AocpvImages imageCust = aocpvImageService.getImageByApplication(applicationNoLong);
			byte[] images2 = imageCust.getImages();
			
	 		String encoded = Base64.getEncoder().encodeToString(images2);
			
			
		//	List<ReceiptPojo> receiptPDF = receiptService.getReceiptPDFDetails(receiptNo);
			
        
        String sConfigFile = "props/sanction.properties";
        InputStream in = ExcelController.class.getClassLoader().getResourceAsStream(sConfigFile);
		if (in == null) {
		System.out.println("Blank File");
		}
		Properties prop = new Properties();
		prop.load(in);
		
		String title = prop.getProperty("title");
		String lefttable1 = prop.getProperty("table.left1");
		String lefttable2 = prop.getProperty("table.left2");
		String lefttable3 = prop.getProperty("table.left3");
		String lefttable4 = prop.getProperty("table.left4");
		String lefttable5 = prop.getProperty("table.left5");
		String lefttable6 = prop.getProperty("table.left6");
		String lefttable7 = prop.getProperty("table.left7");
		String lefttable8 = prop.getProperty("table.left8");
		String lefttable9 = prop.getProperty("table.left9");
		String lefttable10 = prop.getProperty("table.left10");
		String lefttable11 = prop.getProperty("table.left11");
		String lefttable12 = prop.getProperty("table.left12");
		String lefttable13 = prop.getProperty("table.left13");
		String lefttable14 = prop.getProperty("table.left14");
		String lefttable15 = prop.getProperty("table.left15");
		String lefttable16 = prop.getProperty("table.left16");
		String lefttable17 = prop.getProperty("table.left17");
		String lefttable18 = prop.getProperty("table.left18");
		String lefttable19 = prop.getProperty("table.left19");
		String lefttable20 = prop.getProperty("table.left20");
		String lefttable21 = prop.getProperty("table.left21");
		String lefttable22 = prop.getProperty("table.left22");
		String title2 = prop.getProperty("title2");
		String sign = prop.getProperty("sign");
		
		List<String> list = new ArrayList<String>();
		 
		  list.add(title);
		  list.add(lefttable1);
		  list.add(lefttable2);
		  list.add(lefttable3);
		  list.add(lefttable4);
		  list.add(lefttable5);
		  list.add(lefttable6);
		  list.add(lefttable7);
		  list.add(lefttable8);
		  list.add(lefttable9);
		  list.add(lefttable10);
		  list.add(lefttable11);
		  list.add(lefttable12);
		  list.add(lefttable13);
		  list.add(lefttable14);
		  list.add(lefttable15);
		  list.add(lefttable16);
		  list.add(lefttable17);
		  list.add(lefttable18);
		  list.add(lefttable19);
		  list.add(lefttable20);
		  list.add(lefttable21);
		  list.add(lefttable22);
		  list.add(title2);
		  list.add(sign);
		  list.add(encoded);
		  
		  PDFSanction sanction = new PDFSanction(list,aocpvLoanCreation);
		  byte[] pdfr =  sanction.exportSanctionLetter(res);
		  String lat = "70";
			String geoLong = "70";
		JSONObject jsonObject2 = new JSONObject();
			JSONObject sanctionLetter = new JSONObject();
			sanctionLetter.put("Lat", lat);
			sanctionLetter.put("image", "sanctionLetter.pdf");
			sanctionLetter.put("Long", geoLong);
			sanctionLetter.put("Address", "");
			sanctionLetter.put("timestamp", "");
			jsonObject2.put("sanctionLetter", sanctionLetter);

			String message =aocpvImageService.savePdf(pdfr,jsonObject2,applicationNoLong);
		  
		  String base64 = Base64.getEncoder().encodeToString(pdfr);
	      String result = new String(base64);
	      
	      
//	      System.out.println(result);
//	      File f = new File(result);
//	      PDDocument pdd = PDDocument.load(f);
//	      AccessPermission ap = new AccessPermission();
//	      StandardProtectionPolicy stpp = new StandardProtectionPolicy("abcd" , "abcd" , ap);
//	      stpp.setEncryptionKeyLength(128);
//	      stpp.setPermissions(ap);
//	      pdd.protect(stpp);
//	      String finalPdf=null;
//	      pdd.save(finalPdf);
//	      pdd.close();
	      org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
	      response.put("Data", result);
	     // response.put("Data", list);
		 return new ResponseEntity<Object>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value="/getPassbookPDF", method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<Object> getPassbookPDF(@RequestBody String jsonRequest,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_REQUEST_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_TRANSACTION_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_TO_ID,
			 @RequestHeader(name = "X-From-ID", required = true) String X_FORM_ID,
			 @RequestHeader(name = "Content-Type", required = true) String CONTEND_TYPE,HttpServletRequest req,HttpServletResponse res)  throws Exception{
		 res.setContentType("application/pdf");
		 res.setHeader("Content-Disposition", "attachment; filename=Passbook.pdf");
		 
		 JSONObject jsonObject=new JSONObject(jsonRequest);
			logger.debug("Print PDF");
			logger.debug("Request"+jsonRequest);
			
			if(jsonRequest.isEmpty()) {
				logger.debug("Request is empty"+jsonRequest);
				throw new EmptyInputException("Input cannot be empty");
			}
			String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
			Long applicationNoNOLong=Long.parseLong(applicationNo);
			AocpvLoanCreation loanCreation = aocpvLoanCreationService.findByApplicationNo(applicationNoNOLong);
			AocpvImages imageCust = aocpvImageService.getImageByApplication(applicationNoNOLong);
			byte[] images2 = imageCust.getImages();
			//JSONObject jsonObjectImage=new JSONObject(imageCust);
		//	String custImage = jsonObjectImage.getString("images");
			//byte[] byteArrray = custImage.getBytes();
	 		String encoded = Base64.getEncoder().encodeToString(images2);
        
	 		// Third Party API
//	 		JSONObject jsonObject2 = cRMService.getRepayment();
	 		
        String sConfigFile = "props/passbook.properties";
        InputStream in = ExcelController.class.getClassLoader().getResourceAsStream(sConfigFile);
		if (in == null) {
		System.out.println("Blank File");
		}
		Properties prop = new Properties();
		prop.load(in);
		
		String header = prop.getProperty("header");
		
		String left1 = prop.getProperty("left1");
		String left2 = prop.getProperty("left2");
		String left3 = prop.getProperty("left3");
		String left4 = prop.getProperty("left4");
		String left5 = prop.getProperty("left5");
		String left6 = prop.getProperty("left6");
		String left7 = prop.getProperty("left7");
		String left8 = prop.getProperty("left8");
		String left9 = prop.getProperty("left9");
		
		
		String downleft1 = prop.getProperty("downleft1");
		String downleft2 = prop.getProperty("downleft2");
		String downleft3 = prop.getProperty("downleft3");
		String downleft4 = prop.getProperty("downleft4");
		String downleft5 = prop.getProperty("downleft5");		
		String downright1 = prop.getProperty("downright1");
		String downright2 = prop.getProperty("downright2");
		String downright3 = prop.getProperty("downright3");
		String downright4 = prop.getProperty("downright4");
		String downright5 = prop.getProperty("downright5");
		
		String title = prop.getProperty("title");
		
		
		List<String> list = new ArrayList<String>();
		  list.add(header);
		  list.add(left1);
		  list.add(left2);
		  list.add(left3);
		  list.add(left4);
		  list.add(left5);
		  list.add(left6);
		  list.add(left7);
		  list.add(left8);
		  list.add(left9);
		  list.add(downleft1);
		  list.add(downleft2);
		  list.add(downleft3);
		  list.add(downleft4);
		  list.add(downleft5);
		  list.add(downright1);
		  list.add(downright2);
		  list.add(downright3);
		  list.add(downright4);
		  list.add(downright5);		  
		  list.add(title);
		  list.add(encoded);
		 
		  
		  PDFPassbook passbook = new PDFPassbook(list,loanCreation);
		  byte[] pdfr =  passbook.exportPassbook(res);
		  String lat = "70";
			String geoLong = "70";
		JSONObject jsonObject2 = new JSONObject();
			JSONObject pasbookPdf = new JSONObject();
			pasbookPdf.put("Lat", lat);
			pasbookPdf.put("image", "pasbookPdf.pdf");
			pasbookPdf.put("Long", geoLong);
			pasbookPdf.put("Address", "");
			pasbookPdf.put("timestamp", "");
			jsonObject2.put("pasbookPdf", pasbookPdf);

			String message =aocpvImageService.savePdf(pdfr,jsonObject2,applicationNoNOLong);
		  String base64 = Base64.getEncoder().encodeToString(pdfr);
	      String result = new String(base64); 
	    //  System.out.println("Conversion "+result);
	      org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
	      response.put("Data", result);	
	    //  response.put("Data", list);
	       logger.debug("final response"+response.size());
		 return new ResponseEntity<Object>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/fetchAllBranchUAT", method = RequestMethod.POST , produces = "application/json")
    public ResponseEntity<Object> fetchAllBranchUAT() throws IOException {
    	   	
		logger.debug("your rest api is working");
    	JSONObject jsonObject= new JSONObject();   	
    	System.out.println("1");
    	jsonObject = cRMService.fetchAllBranch();
    	System.out.println(jsonObject);
    	JSONObject jsonModified = new JSONObject(); 
    	jsonModified = jsonObject;
    	System.out.println("JSON ARRAY "+jsonModified);
    	 org.json.JSONArray jsonArray = jsonModified.getJSONObject("Data").getJSONArray("ReferenceData");
    //	org.json.JSONArray jArray = jsonModified.getJSONObject("Data").getJSONArray("ReferenceData");
    	System.out.println(jsonArray+"JSON ARRAY 2");
    	  org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
    	  response.put("Data",jsonArray);
		return new ResponseEntity<>(response.toString(), HttpStatus.OK);    	
    }
	
	@RequestMapping(value="/fetchAllImages", method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<Object> fetchAllImages(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_Correlation_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_REQUEST_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-From-ID", required = true) String X_FORM_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_TO_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_TRANSACTION_ID,			 
			 @RequestHeader(name = "Content-Type", required = true) String CONTEND_TYPE,HttpServletRequest req)  throws Exception{
		
		 JSONObject jsonObject=new JSONObject(jsonRequest);
			logger.debug("Fetch All Details");
			logger.debug("Request"+jsonRequest);
			
			if(jsonRequest.isEmpty()) {
				logger.debug("Request is empty"+jsonRequest);
				throw new EmptyInputException("Input cannot be empty");
			}
		 
			String appln = jsonObject.getJSONObject("Data").getString("ApplicationNumber");
			Long applicationNo = Long.parseLong(appln);
			
			List<AocpvImages> listAocpvImage = aocpvImageService.getByApplicationNoAll(applicationNo);
			List<Image> listOfImages=new ArrayList<>();
			if(listAocpvImage.isEmpty()) {
				throw new EmptyInputException("Images Lists are Empty");
			}
			else {
				
				for(AocpvImages aocpvImages:listAocpvImage) {
					
						String geoLocation = aocpvImages.getGeoLocation();
						JSONObject jsonObjectImage=new JSONObject(geoLocation);
				 		String pimage = jsonObjectImage.getString("image");
				 		String pLat = jsonObjectImage.getString("Lat");
				 		String pLong = jsonObjectImage.getString("Long");
				 		String pAddress = jsonObjectImage.getString("Address");
				 		String ptimestamp = jsonObjectImage.getString("timestamp");
			
				 		GeoLcation geolocation =new GeoLcation(pimage,pLat,pLong,pAddress,ptimestamp);
 		
				 		String documenttype = aocpvImages.getDocumenttype();
				 		String imageName = aocpvImages.getImageName();
				 		String type = aocpvImages.getType();
				 		long size = aocpvImages.getSize();
				 		String member=aocpvImages.getMember();
				 		byte[] images2 = aocpvImages.getImages();
				 		String encoded = Base64.getEncoder().encodeToString(images2);
				 		 
				 		Image images = new Image(documenttype,imageName,type,size,encoded,member,geolocation);
				 			
					 		listOfImages.add(images);						
				}
				
		}
			
			 logger.debug("db Call start");
	            org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
	            org.json.simple.JSONObject  data= new org.json.simple.JSONObject();
	            data.put("images", listOfImages);
	            response.put("Data", data);
	 
			 return new ResponseEntity<Object>(response,HttpStatus.OK);
		}
	
	@RequestMapping(value="/getPersonalDetailsByAppln", method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<Object> getPersonalDetailsByAppln(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_Correlation_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_REQUEST_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-From-ID", required = true) String X_FORM_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_TO_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_TRANSACTION_ID,			 
			 @RequestHeader(name = "Content-Type", required = true) String CONTEND_TYPE,HttpServletRequest req)  throws Exception{
	
		 JSONObject jsonObject=new JSONObject(jsonRequest);
			logger.debug("Fetch All Details");
			logger.debug("Request"+jsonRequest);
			
			if(jsonRequest.isEmpty()) {
				logger.debug("Request is empty"+jsonRequest);
				throw new EmptyInputException("Input cannot be empty");
			}
		 
			String appln = jsonObject.getJSONObject("Data").getString("ApplicationNumber");
			logger.debug("db Call start");
			PersonalDetailResponse personalDetail=applicationDetailsService.getPersonalDetailsByAppln(appln);
//			
//            JSONObject jsonObj = new JSONObject();
//            jsonObj.put("firstName", "Kamapali");
//            jsonObj.put("middleName", "Surendra");
//            jsonObj.put("lastName", "Obahiai");
//            jsonObj.put("mobile", "9823234567");
//            jsonObj.put("age", "42");
//            jsonObj.put("gender", "female");
//            jsonObj.put("fathername", "Surendra");
//            jsonObj.put("motherName", "Sunadari");
//            jsonObj.put("spouseName", "Priya");
//            jsonObj.put("numberOfDependant", "3");
//            jsonObj.put("educationQualification", "Engineer");
//            jsonObj.put("caste", "Brhamna");
//            jsonObj.put("religion", "Hindu");
          
            org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
            response.put("Data", personalDetail);
            logger.debug("Final Response"+response.toString());
		 return new ResponseEntity<Object>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value="/savePersonalDetailsApplication", method = RequestMethod.POST)
	public ResponseEntity<Object> savePersonalDetailsData(@RequestBody String jsonRequest ,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		
		JSONObject jsonObject=new JSONObject(jsonRequest);
		logger.debug("save data start");
		logger.debug("request"+jsonRequest);
		System.out.println("JSON STRING "+jsonObject);
		
			if(jsonRequest.isEmpty()) {
				logger.debug("request is empty"+jsonRequest);
				throw new EmptyInputException("Input cannot be empty");
			}
		String title = jsonObject.getJSONObject("Data").getString("Title");
		String fname = jsonObject.getJSONObject("Data").getString("FirstName");
		String mname = jsonObject.getJSONObject("Data").getString("MiddleName");
		String lname = jsonObject.getJSONObject("Data").getString("LastName");
		String mobile = jsonObject.getJSONObject("Data").getString("MobileNO");
		String dateOfBirth = jsonObject.getJSONObject("Data").getString("DateOfBirth");
		String age = jsonObject.getJSONObject("Data").getString("Age");
		String gender = jsonObject.getJSONObject("Data").getString("Gender");
		String fatherName = jsonObject.getJSONObject("Data").getString("FatherName");
		String motherName = jsonObject.getJSONObject("Data").getString("MotherName");
		String spouseName = jsonObject.getJSONObject("Data").getString("SpouseName");
		String numberOfDependant = jsonObject.getJSONObject("Data").getString("DependantMember");
		String educationQualification = jsonObject.getJSONObject("Data").getString("EducationQualification");
		String caste = jsonObject.getJSONObject("Data").getString("Caste");
		String religion = jsonObject.getJSONObject("Data").getString("Religion");
		String appln = jsonObject.getJSONObject("Data").getString("AppplicationNumber");
		
		String message=applicationDetailsService.addpersonalDetails(appln,jsonObject.getJSONObject("Data").toString());
		
		org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
		response.put("Message", message);
		
		logger.debug("final response"+response.toString());
		return new ResponseEntity<Object>(response,HttpStatus.OK);
		
	}
	
	@RequestMapping(value="/getPermanentAdddressByAppln", method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<Object> getPermanentAdddressByAppln(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_Correlation_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_REQUEST_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-From-ID", required = true) String X_FORM_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_TO_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_TRANSACTION_ID,			 
			 @RequestHeader(name = "Content-Type", required = true) String CONTEND_TYPE,HttpServletRequest req)  throws Exception{
		
		 JSONObject jsonObject=new JSONObject(jsonRequest);
			logger.debug("Fetch All Details");
			logger.debug("Request"+jsonRequest);
			
			if(jsonRequest.isEmpty()) {
				logger.debug("Request is empty"+jsonRequest);
				throw new EmptyInputException("Input cannot be empty");
			}
		 
			String appln = jsonObject.getJSONObject("Data").getString("ApplicationNumber");
			List<Address> list =applicationDetailsService.getAddress(appln);
         
      
            org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
            response.put("Data", list);
            logger.debug("Final Response"+response.toString());
		 return new ResponseEntity<Object>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value="/saveAddressDetailsApplication", method = RequestMethod.POST)
	public ResponseEntity<Object> saveAddressDetailsApplication(@RequestBody String jsonRequest ,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		
		JSONObject jsonObject=new JSONObject(jsonRequest);
		logger.debug("save data start");
		logger.debug("request"+jsonRequest);
		System.out.println("JSON STRING "+jsonObject);
		
			if(jsonRequest.isEmpty()) {
				logger.debug("request is empty"+jsonRequest);
				throw new EmptyInputException("Input cannot be empty");
			}
			
		String appln = jsonObject.getJSONObject("Data").getString("AppplicationNumber");
		org.json.JSONArray jsonArray = jsonObject.getJSONObject("Data").getJSONArray("Address");
		String message =applicationDetailsService.saveAddress(appln,jsonArray.toString());
		org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
		response.put("Message",message );
		
		logger.debug("final response"+response.toString());
		return new ResponseEntity<Object>(response,HttpStatus.OK);
		
	}
	@RequestMapping(value = "/fetchByApplicationPDF", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> fetchByApplicationPDF(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		res.setContentType("application/pdf");
		res.setHeader("Content-Disposition", "attachment; filename=AO.pdf");
		JSONObject jsonObject = new JSONObject(jsonRequest);
		logger.debug("request" + jsonRequest);
		if (jsonRequest.isEmpty()) {
			logger.debug("request is empty" + jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}

		String application = jsonObject.getJSONObject("Data").getString("ApplicationNo");
		Long appln = Long.parseLong(application);
		// List<ApplicationDetails>
		// list=applicationDetailsService.fetchByApplicationPDF(application);
		List<AocpCustomer> list = aocpCustomerDataService.fetchByApplicationNumberPDF(appln);
		List<AocpvIncomeDetails> list2 = aocpvIncomeService.getByApplicationNo(appln);
		AocpvLoanCreation list3 = aocpvLoanCreationService.findByApplicationNo(appln);
//		 AocpvImages aocpvImages = aocpvImageService.getByApplicationNo(appln);
//		String base64ImageCustomer = "";
//		byte[] imageCustomer = null;
//		 
//					String geoLocation = aocpvImages.getGeoLocation();
//					JSONObject jsonObjectImage = new JSONObject(geoLocation);
//					String pimage = jsonObjectImage.getString("image");
//					String pLat = jsonObjectImage.getString("Lat");
//					String pLong = jsonObjectImage.getString("Long");
//					String pAddress = jsonObjectImage.getString("Address");
//					String ptimestamp = jsonObjectImage.getString("timestamp");
//					// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MM-yyyy
//					// hh:mm:ss");
//					// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-mm-yyyy");
//					// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MM-yyyy");
//
//					// LocalDate timeStamp = LocalDate.parse(ptimestamp, formatter);
//					GeoLcation geolocation = new GeoLcation(pimage, pLat, pLong, pAddress, ptimestamp);
//
//					String documenttype = aocpvImages.getDocumenttype();
//					String imageName = aocpvImages.getImageName();
//					String type = aocpvImages.getType();
//					long size = aocpvImages.getSize();
//					String member = aocpvImages.getMember();
//					byte[] images2 = aocpvImages.getImages();
//					String encoded = Base64.getEncoder().encodeToString(images2);
//
//					Image images = new Image(documenttype, imageName, type, size, encoded, member, geolocation);
//
//					base64ImageCustomer = encoded;
//					imageCustomer = images2;
			
		PDFAo exportAO = new PDFAo(list, list2, list3);

		byte[] pdfao = exportAO.exportPDFAO(res);
		String base64 = Base64.getEncoder().encodeToString(pdfao);
		String result = new String(base64);
		System.out.println("Conversion " + result);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Data1", result);
		response.put("Data", list);
		logger.debug("final response" + response.size());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}
	
	
	
}
