package com.suryoday.twowheeler.serviceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suryoday.roaocpv.others.ROAOCPVGenerateProperty;
import com.suryoday.twowheeler.pojo.AddressDetails;
import com.suryoday.twowheeler.pojo.BankDetailsResponse;
import com.suryoday.twowheeler.pojo.TwoWheelerFamilyMember;
import com.suryoday.twowheeler.pojo.TwowheelerDetailesTable;
import com.suryoday.twowheeler.pojo.TwowheelerImage;
import com.suryoday.twowheeler.service.CamReportService;
import com.suryoday.twowheeler.service.TwowheelerDetailsService;
import com.suryoday.twowheeler.service.TwowheelerFamilyMemberService;
import com.suryoday.twowheeler.service.TwowheelerImageService;

@Service
public class CamReportServiceImpl implements CamReportService {

	@Autowired
	TwowheelerDetailsService twowheelerDetailsService;

	@Autowired
	TwowheelerFamilyMemberService familyMemberService;

	@Autowired
	TwowheelerImageService imageService;

	@Override
	public JSONObject getcamReport(String applicationNo) {

		ROAOCPVGenerateProperty x = ROAOCPVGenerateProperty.getInstance();
		x.getappprop();
		String excelFilePath = x.onePager + "CAM_REPORT.xlsx";

		TwowheelerDetailesTable twowheelerDetails = twowheelerDetailsService.getByApplication(applicationNo);
		TwoWheelerFamilyMember familyMember = familyMemberService.getByApplicationNoAndMember(applicationNo,
				"APPLICANT");

		String base64 = null;
		JSONObject returnjson = new JSONObject();
		try {
			FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
			Workbook workbook = WorkbookFactory.create(inputStream);

			Sheet sheet = workbook.getSheetAt(0);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String now = LocalDateTime.now().format(formatter);
			String createdTime = twowheelerDetails.getCreatedTimestamp().format(formatter);
			Cell cell2Update1 = sheet.getRow(6).getCell(2);
			cell2Update1.setCellValue(createdTime);

			Cell cell2Update2 = sheet.getRow(7).getCell(2);
			cell2Update2.setCellValue(twowheelerDetails.getAppNoWithProductCode());

			Cell cell2Update3 = sheet.getRow(8).getCell(2);
			cell2Update3.setCellValue(familyMember.getNameOnAAdhar() != null ? familyMember.getNameOnAAdhar()
					: twowheelerDetails.getName());

			Cell cell2Update4 = sheet.getRow(9).getCell(2);
			cell2Update4.setCellValue(
					twowheelerDetails.getRmBranchName() != null ? twowheelerDetails.getRmBranchName() : "NA");

			Cell cell2Update5 = sheet.getRow(10).getCell(2);
			cell2Update5.setCellValue(
					twowheelerDetails.getRmStateName() != null ? twowheelerDetails.getRmStateName() : "NA");

			Cell cell2Update6 = sheet.getRow(11).getCell(2);
			cell2Update6.setCellValue(twowheelerDetails.getRmName() != null ? twowheelerDetails.getRmName() : "NA");

			Cell cell2Update7 = sheet.getRow(12).getCell(2);
			cell2Update7
					.setCellValue(twowheelerDetails.getRmMobileNo() != null ? twowheelerDetails.getRmMobileNo() : "NA");

			// Customer Details -Individual Customer -Member Details - Applicant
			TwowheelerImage twowheelerImage = imageService.getByApplicationNoAndDocument(applicationNo, "customerPhoto")
					.get(0);
			int pictureIndex = workbook.addPicture(twowheelerImage.getImages(), Workbook.PICTURE_TYPE_JPEG);
			Drawing drawing = sheet.createDrawingPatriarch();
			XSSFClientAnchor anchor = new XSSFClientAnchor();
			anchor.setCol1(1); // Column B
			anchor.setRow1(17); // Row 3
			anchor.setCol2(2); // Column C
			anchor.setRow2(31); // Row 4
			Picture pict = drawing.createPicture(anchor, pictureIndex);
			Cell cell2Update8 = sheet.getRow(18).getCell(3);
			cell2Update8.setCellValue(familyMember.getNameOnAAdhar() != null ? familyMember.getNameOnAAdhar()
					: twowheelerDetails.getName());

			Cell cell2Update9 = sheet.getRow(19).getCell(3);
			cell2Update9.setCellValue(familyMember.getMobile() != null ? familyMember.getMobile() : "");

			Cell cell2Update10 = sheet.getRow(20).getCell(3);
			cell2Update10.setCellValue(familyMember.getAge() != null ? familyMember.getAge()
					: "" + "-" + familyMember.getDob() != null ? familyMember.getDob() : "");

			Cell cell2Update11 = sheet.getRow(21).getCell(3);
			cell2Update11.setCellValue(familyMember.getFatherName() != null ? familyMember.getFatherName() : "");

			Cell cell2Update12 = sheet.getRow(22).getCell(3);
			cell2Update12.setCellValue("SELF");

			Cell cell2Update13 = sheet.getRow(23).getCell(3);
			cell2Update13.setCellValue(familyMember.getGender() != null ? familyMember.getGender() : "");

			Cell cell2Update14 = sheet.getRow(24).getCell(3);
			cell2Update14.setCellValue(familyMember.getOccupation() != null ? familyMember.getOccupation() : "");

			Cell cell2Update15 = sheet.getRow(25).getCell(3);
			cell2Update15
					.setCellValue(familyMember.getEmploymentType() != null ? familyMember.getEmploymentType() : "");

			Cell cell2Update16 = sheet.getRow(26).getCell(3);
			cell2Update16.setCellValue(familyMember.getOccupation() != null ? familyMember.getOccupation() : "");

			Cell cell2Update17 = sheet.getRow(27).getCell(3);
			cell2Update17.setCellValue(familyMember.getWorkStability() != null ? familyMember.getWorkStability() : "");

			Cell cell2Update18 = sheet.getRow(28).getCell(3);
			cell2Update18.setCellValue(familyMember.getAnnualIncome() != null ? familyMember.getAnnualIncome() : "");

			Cell cell2Update19 = sheet.getRow(29).getCell(3);
			cell2Update19.setCellValue(familyMember.getCompanyName() != null ? familyMember.getCompanyName() : "");

			Cell cell2Update20 = sheet.getRow(30).getCell(3);
			cell2Update20.setCellValue("");

			// KYC Details

			Cell cell2Update21 = sheet.getRow(33).getCell(2);
			Cell cell2Update22 = sheet.getRow(33).getCell(3);
			Cell cell2Update23 = sheet.getRow(33).getCell(4);

			cell2Update21.setCellValue(familyMember.getMobile() != null ? familyMember.getMobile() : "");
			cell2Update22.setCellValue("");
			cell2Update23.setCellValue(familyMember.getMobileVerify() != null ? familyMember.getMobileVerify() : "NO");

			Cell cell2Update24 = sheet.getRow(34).getCell(2);
			Cell cell2Update25 = sheet.getRow(34).getCell(3);
			Cell cell2Update26 = sheet.getRow(34).getCell(4);
			cell2Update24.setCellValue("PAN");
			cell2Update25.setCellValue(familyMember.getPanCard() != null ? familyMember.getPanCard() : "");
			cell2Update26
					.setCellValue(familyMember.getPancardNoVerify() != null ? familyMember.getPancardNoVerify() : "NO");

			Cell cell2Update27 = sheet.getRow(35).getCell(2);
			Cell cell2Update28 = sheet.getRow(35).getCell(3);
			Cell cell2Update29 = sheet.getRow(35).getCell(4);
			cell2Update27.setCellValue("AADHAAR");
			cell2Update28.setCellValue(familyMember.getAadharCard() != null ? familyMember.getAadharCard() : "");
			cell2Update29.setCellValue(familyMember.getEkycVerify() != null ? familyMember.getEkycVerify() : "NO");

			Cell cell2Update30 = sheet.getRow(36).getCell(2);
			Cell cell2Update31 = sheet.getRow(36).getCell(3);
			Cell cell2Update32 = sheet.getRow(36).getCell(4);
			cell2Update30.setCellValue("AADHAAR");
			cell2Update31.setCellValue(familyMember.getAadharCard() != null ? familyMember.getAadharCard() : "");
			cell2Update32.setCellValue(familyMember.getEkycVerify() != null ? familyMember.getEkycVerify() : "NO");

			Cell cell2Update33 = sheet.getRow(37).getCell(2);
			Cell cell2Update34 = sheet.getRow(37).getCell(3);
			Cell cell2Update35 = sheet.getRow(37).getCell(4);
			cell2Update33.setCellValue("");
			cell2Update34.setCellValue("");
			cell2Update35.setCellValue("NO");

			Cell cell2Update36 = sheet.getRow(38).getCell(2);
			Cell cell2Update37 = sheet.getRow(38).getCell(3);
			Cell cell2Update38 = sheet.getRow(38).getCell(4);
			cell2Update36
					.setCellValue(twowheelerDetails.getUtilityBill() != null ? twowheelerDetails.getUtilityBill() : "");
			cell2Update37.setCellValue(
					twowheelerDetails.getUtilityBillNo() != null ? twowheelerDetails.getUtilityBillNo() : "");
			cell2Update38.setCellValue(
					twowheelerDetails.getUtilityBillIsVerify() != null ? twowheelerDetails.getUtilityBillIsVerify()
							: "");

			// CFR Check

			Cell cell2Update39 = sheet.getRow(40).getCell(2);
			cell2Update39.setCellValue("NO MATCH");

			// Bureau details

			Cell cell2Update40 = sheet.getRow(42).getCell(2);
			cell2Update40.setCellValue(
					twowheelerDetails.getBureauaScore() != null ? twowheelerDetails.getBureauaScore() : "NA");

			// AML

			Cell cell2Update41 = sheet.getRow(44).getCell(1);
			cell2Update41.setCellValue("AML STATUS:" + " " + "NO MATCH");

			Cell cell2Update42 = sheet.getRow(44).getCell(2);
			cell2Update42.setCellValue("AMLMATCH:" + " " + " NOT_APPLICABLE");

			Cell cell2Update43 = sheet.getRow(44).getCell(3);
			cell2Update43.setCellValue("AMLSCORE:" + " " + "NO MATCH");

			// Hunter

			Cell cell2Update44 = sheet.getRow(44).getCell(2);
			cell2Update44.setCellValue("Hunter STATUS:" + " " + "No Match");

			// Address Details

			String addressArray = familyMember.getAddressArray();
			Cell cell2Update45 = sheet.getRow(49).getCell(2);
			Cell cell2Update46 = sheet.getRow(49).getCell(3);
			Cell cell2Update47 = sheet.getRow(49).getCell(4);

			Cell cell2Update48 = sheet.getRow(50).getCell(2);
			Cell cell2Update49 = sheet.getRow(50).getCell(3);
			Cell cell2Update50 = sheet.getRow(50).getCell(4);

			Cell cell2Update51 = sheet.getRow(51).getCell(2);
			Cell cell2Update52 = sheet.getRow(51).getCell(3);
			Cell cell2Update53 = sheet.getRow(51).getCell(4);

			Cell cell2Update54 = sheet.getRow(52).getCell(2);
			Cell cell2Update55 = sheet.getRow(52).getCell(3);
			Cell cell2Update56 = sheet.getRow(52).getCell(4);

			Cell cell2Update57 = sheet.getRow(53).getCell(2);
			Cell cell2Update58 = sheet.getRow(53).getCell(3);
			Cell cell2Update59 = sheet.getRow(53).getCell(4);

			Cell cell2Update60 = sheet.getRow(54).getCell(2);
			Cell cell2Update61 = sheet.getRow(54).getCell(3);
			Cell cell2Update62 = sheet.getRow(54).getCell(4);

			Cell cell2Update63 = sheet.getRow(55).getCell(2);
			Cell cell2Update64 = sheet.getRow(55).getCell(3);
			Cell cell2Update65 = sheet.getRow(55).getCell(4);

			Cell cell2Update66 = sheet.getRow(56).getCell(2);
			Cell cell2Update67 = sheet.getRow(56).getCell(3);
			Cell cell2Update68 = sheet.getRow(56).getCell(4);

			Cell cell2Update69 = sheet.getRow(57).getCell(2);
			Cell cell2Update70 = sheet.getRow(57).getCell(3);
			Cell cell2Update71 = sheet.getRow(57).getCell(4);

			Cell cell2Update72 = sheet.getRow(58).getCell(2);
			Cell cell2Update73 = sheet.getRow(58).getCell(3);
			Cell cell2Update74 = sheet.getRow(58).getCell(4);
			if (familyMember.getAddressArray() != null) {
				ObjectMapper objectMapper = new ObjectMapper();
				try {
					List<AddressDetails> addressList = objectMapper.readValue(addressArray,
							new TypeReference<List<AddressDetails>>() {
							});
					for (AddressDetails addressDetails : addressList) {
						if (addressDetails.getAddressType().equalsIgnoreCase("CURRENT ADDRESS")) {
							cell2Update45.setCellValue(addressDetails.getAddress_Line1());
							cell2Update48.setCellValue(addressDetails.getAddress_Line2());
							cell2Update51.setCellValue(addressDetails.getLandmark());
							cell2Update54.setCellValue(addressDetails.getCity());
							cell2Update57.setCellValue(addressDetails.getDistrict());
							cell2Update60.setCellValue(addressDetails.getState());
							cell2Update63.setCellValue(addressDetails.getPincode());
							cell2Update66.setCellValue(addressDetails.getHouseOwnership());
							cell2Update69.setCellValue(familyMember.getNegativeLocality());
							cell2Update72.setCellValue(familyMember.getLocalityName());

						} else if (addressDetails.getAddressType().equalsIgnoreCase("Permanent ADDRESS")) {
							cell2Update46.setCellValue(addressDetails.getAddress_Line1());
							cell2Update49.setCellValue(addressDetails.getAddress_Line2());
							cell2Update52.setCellValue(addressDetails.getLandmark());
							cell2Update55.setCellValue(addressDetails.getCity());
							cell2Update58.setCellValue(addressDetails.getDistrict());
							cell2Update61.setCellValue(addressDetails.getState());
							cell2Update64.setCellValue(addressDetails.getPincode());
							cell2Update67.setCellValue(addressDetails.getHouseOwnership());
							cell2Update70.setCellValue("No");
							cell2Update73.setCellValue("");
						} else if (addressDetails.getAddressType().equalsIgnoreCase("Business ADDRESS")) {
							cell2Update47.setCellValue(addressDetails.getAddress_Line1());
							cell2Update50.setCellValue(addressDetails.getAddress_Line2());
							cell2Update53.setCellValue(addressDetails.getLandmark());
							cell2Update56.setCellValue(addressDetails.getCity());
							cell2Update59.setCellValue(addressDetails.getDistrict());
							cell2Update62.setCellValue(addressDetails.getState());
							cell2Update65.setCellValue(addressDetails.getPincode());
							cell2Update68.setCellValue(addressDetails.getHouseOwnership());
							cell2Update71.setCellValue("No");
							cell2Update74.setCellValue("");
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			TwoWheelerFamilyMember familyMemberCoApp = familyMemberService.fetchByApplicationNoAndMember(applicationNo,
					"CO_APPLICANT");
			// coApplicant details
			Cell cell2Update75 = sheet.getRow(61).getCell(3);
			cell2Update75.setCellValue(
					familyMemberCoApp.getNameOnAAdhar() != null ? familyMemberCoApp.getNameOnAAdhar() : "");

			Cell cell2Update76 = sheet.getRow(62).getCell(3);
			cell2Update76.setCellValue(familyMemberCoApp.getMobile() != null ? familyMemberCoApp.getMobile() : "");

			Cell cell2Update77 = sheet.getRow(63).getCell(3);

			cell2Update77.setCellValue(familyMemberCoApp.getAge() != null ? familyMemberCoApp.getAge()
					: "" + " " + familyMemberCoApp.getDob() != null ? familyMemberCoApp.getDob() : "");

			Cell cell2Update78 = sheet.getRow(64).getCell(3);

			cell2Update78
					.setCellValue(familyMemberCoApp.getFatherName() != null ? familyMemberCoApp.getFatherName() : "");

			Cell cell2Update79 = sheet.getRow(65).getCell(3);
			cell2Update79.setCellValue(
					familyMemberCoApp.getRelationWithApplicant() != null ? familyMemberCoApp.getRelationWithApplicant()
							: "");

			Cell cell2Update80 = sheet.getRow(66).getCell(3);
			cell2Update80.setCellValue(familyMemberCoApp.getGender() != null ? familyMemberCoApp.getGender() : "");

			Cell cell2Update81 = sheet.getRow(67).getCell(3);
			String NatureofBusiness1 = cell2Update81.getStringCellValue();
			cell2Update81
					.setCellValue(familyMemberCoApp.getOccupation() != null ? familyMemberCoApp.getOccupation() : "");

			Cell cell2Update82 = sheet.getRow(68).getCell(3);
			cell2Update82.setCellValue(
					familyMemberCoApp.getEmploymentType() != null ? familyMemberCoApp.getEmploymentType() : "");

			Cell cell2Update83 = sheet.getRow(69).getCell(3);
			cell2Update83
					.setCellValue(familyMemberCoApp.getOccupation() != null ? familyMemberCoApp.getOccupation() : "");

			Cell cell2Update84 = sheet.getRow(70).getCell(3);
			cell2Update84.setCellValue(
					familyMemberCoApp.getWorkStability() != null ? familyMemberCoApp.getWorkStability() : "");

			Cell cell2Update85 = sheet.getRow(71).getCell(3);
			String GrossSalary1 = cell2Update85.getStringCellValue();
			cell2Update85.setCellValue(
					familyMemberCoApp.getAnnualIncome() != null ? familyMemberCoApp.getAnnualIncome() : "");

			Cell cell2Update86 = sheet.getRow(72).getCell(3);
			String CATCompany1 = cell2Update86.getStringCellValue();
			cell2Update86
					.setCellValue(familyMemberCoApp.getCompanyName() != null ? familyMemberCoApp.getCompanyName() : "");

			Cell cell2Update87 = sheet.getRow(73).getCell(3);
			String ApplicantLAT1 = cell2Update86.getStringCellValue();
			cell2Update87.setCellValue("");

			// KYC Details

			Cell cell2Update88 = sheet.getRow(76).getCell(2);
			Cell cell2Update89 = sheet.getRow(76).getCell(3);
			Cell cell2Update90 = sheet.getRow(76).getCell(4);
			cell2Update88.setCellValue(familyMemberCoApp.getMobile() != null ? familyMemberCoApp.getMobile() : "");
			cell2Update89.setCellValue("");
			cell2Update90.setCellValue(
					familyMemberCoApp.getMobileVerify() != null ? familyMemberCoApp.getMobileVerify() : "");

			Cell cell2Update91 = sheet.getRow(77).getCell(2);
			Cell cell2Update92 = sheet.getRow(77).getCell(3);
			Cell cell2Update93 = sheet.getRow(77).getCell(4);
			cell2Update91.setCellValue(familyMemberCoApp.getPanCard() != null ? "PAN" : "");
			cell2Update92.setCellValue(familyMemberCoApp.getPanCard() != null ? familyMemberCoApp.getPanCard() : "");
			cell2Update93.setCellValue(
					familyMemberCoApp.getPancardNoVerify() != null ? familyMemberCoApp.getPancardNoVerify() : "");

			Cell cell2Update94 = sheet.getRow(78).getCell(2);
			Cell cell2Update95 = sheet.getRow(78).getCell(3);
			Cell cell2Update96 = sheet.getRow(78).getCell(4);
			cell2Update94.setCellValue(familyMemberCoApp.getAadharCard() != null ? "AADHAAR" : "");
			cell2Update95
					.setCellValue(familyMemberCoApp.getAadharCard() != null ? familyMemberCoApp.getAadharCard() : "");
			cell2Update96.setCellValue(
					familyMemberCoApp.getAadharNoVerify() != null ? familyMemberCoApp.getAadharNoVerify() : "");

			Cell cell2Update97 = sheet.getRow(79).getCell(2);
			Cell cell2Update98 = sheet.getRow(79).getCell(3);
			Cell cell2Update99 = sheet.getRow(79).getCell(4);
			cell2Update97.setCellValue(familyMemberCoApp.getAadharCard() != null ? "AADHAAR" : "");
			cell2Update98
					.setCellValue(familyMemberCoApp.getAadharCard() != null ? familyMemberCoApp.getAadharCard() : "");
			cell2Update99.setCellValue(
					familyMemberCoApp.getAadharNoVerify() != null ? familyMemberCoApp.getAadharNoVerify() : "");

			Cell cell2Update00 = sheet.getRow(80).getCell(2);
			Cell cell2Update01 = sheet.getRow(80).getCell(3);
			Cell cell2Update02 = sheet.getRow(80).getCell(4);
			cell2Update00.setCellValue("");
			cell2Update01.setCellValue("");
			cell2Update02.setCellValue("");

			Cell cell2Update03 = sheet.getRow(81).getCell(2);
			Cell cell2Update04 = sheet.getRow(81).getCell(3);
			Cell cell2Update05 = sheet.getRow(81).getCell(4);

			cell2Update03.setCellValue("");
			cell2Update04.setCellValue("");
			cell2Update05.setCellValue("");

			// CFR Check

			Cell cell2Update06 = sheet.getRow(83).getCell(2);
			cell2Update06.setCellValue("NO MATCH");

			// Bureau details

			Cell cell2Update07 = sheet.getRow(85).getCell(2);
			cell2Update07.setCellValue("");

			// AML

			Cell cell2Update08 = sheet.getRow(87).getCell(1);
			cell2Update08.setCellValue("AML STATUS:" + " " + "NO MATCH");

			Cell cell2Update09 = sheet.getRow(87).getCell(2);
			String AMLMATCH1 = cell2Update09.getStringCellValue();
			cell2Update09.setCellValue("AMLMATCH:" + " " + " NOT_APPLICABLE");

			Cell cell2Update010 = sheet.getRow(87).getCell(3);
			String AMLSCORE1 = cell2Update010.getStringCellValue();
			cell2Update010.setCellValue("AMLSCORE:" + " " + "NO MATCH");

			// Hunter

			Cell cell2Update011 = sheet.getRow(89).getCell(2);
			String HunterStatus1 = cell2Update011.getStringCellValue();
			cell2Update011.setCellValue("No Match");

			// Address Details

			Cell cell2Update012 = sheet.getRow(92).getCell(2);
			Cell cell2Update013 = sheet.getRow(92).getCell(3);
			Cell cell2Update014 = sheet.getRow(92).getCell(4);

			Cell cell2Update015 = sheet.getRow(93).getCell(2);
			Cell cell2Update016 = sheet.getRow(93).getCell(3);
			Cell cell2Update017 = sheet.getRow(93).getCell(4);

			Cell cell2Update018 = sheet.getRow(94).getCell(2);
			Cell cell2Update019 = sheet.getRow(94).getCell(3);
			Cell cell2Update020 = sheet.getRow(94).getCell(4);

			Cell cell2Update021 = sheet.getRow(95).getCell(2);
			Cell cell2Update022 = sheet.getRow(95).getCell(3);
			Cell cell2Update023 = sheet.getRow(95).getCell(4);

			Cell cell2Update024 = sheet.getRow(96).getCell(2);
			Cell cell2Update025 = sheet.getRow(96).getCell(3);
			Cell cell2Update026 = sheet.getRow(96).getCell(4);

			Cell cell2Update027 = sheet.getRow(97).getCell(2);
			Cell cell2Update028 = sheet.getRow(97).getCell(3);
			Cell cell2Update029 = sheet.getRow(97).getCell(4);

			Cell cell2Update030 = sheet.getRow(98).getCell(2);
			Cell cell2Update031 = sheet.getRow(98).getCell(3);
			Cell cell2Update032 = sheet.getRow(98).getCell(4);

			Cell cell2Update033 = sheet.getRow(99).getCell(2);
			Cell cell2Update034 = sheet.getRow(99).getCell(3);
			Cell cell2Update035 = sheet.getRow(99).getCell(4);

			Cell cell2Update036 = sheet.getRow(100).getCell(2);
			Cell cell2Update037 = sheet.getRow(100).getCell(3);
			Cell cell2Update038 = sheet.getRow(100).getCell(4);

			Cell cell2Update039 = sheet.getRow(101).getCell(2);
			Cell cell2Update040 = sheet.getRow(101).getCell(3);
			Cell cell2Update041 = sheet.getRow(101).getCell(4);

			String addressArrayCoApplicant = familyMemberCoApp.getAddressArray();
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				List<AddressDetails> addressList = objectMapper.readValue(addressArrayCoApplicant,
						new TypeReference<List<AddressDetails>>() {
						});
				for (AddressDetails addressDetails : addressList) {
					if (addressDetails.getAddressType().equalsIgnoreCase("CURRENT ADDRESS")) {
						cell2Update012.setCellValue(addressDetails.getAddress_Line1());
						cell2Update015.setCellValue(addressDetails.getAddress_Line2());
						cell2Update018.setCellValue(addressDetails.getLandmark());
						cell2Update021.setCellValue(addressDetails.getCity());
						cell2Update024.setCellValue(addressDetails.getDistrict());
						cell2Update027.setCellValue(addressDetails.getState());
						cell2Update030.setCellValue(addressDetails.getPincode());
						cell2Update033.setCellValue(addressDetails.getHouseOwnership());
						cell2Update036.setCellValue(familyMember.getNegativeLocality());
						cell2Update039.setCellValue(familyMember.getLocalityName());

					} else if (addressDetails.getAddressType().equalsIgnoreCase("Permanent ADDRESS")) {
						cell2Update013.setCellValue(addressDetails.getAddress_Line1());
						cell2Update016.setCellValue(addressDetails.getAddress_Line2());
						cell2Update019.setCellValue(addressDetails.getLandmark());
						cell2Update022.setCellValue(addressDetails.getCity());
						cell2Update025.setCellValue(addressDetails.getDistrict());
						cell2Update028.setCellValue(addressDetails.getState());
						cell2Update031.setCellValue(addressDetails.getPincode());
						cell2Update034.setCellValue(addressDetails.getHouseOwnership());
						cell2Update037.setCellValue("No");
						cell2Update040.setCellValue("");
					} else if (addressDetails.getAddressType().equalsIgnoreCase("Business ADDRESS")) {
						cell2Update014.setCellValue(addressDetails.getAddress_Line1());
						cell2Update017.setCellValue(addressDetails.getAddress_Line2());
						cell2Update020.setCellValue(addressDetails.getLandmark());
						cell2Update023.setCellValue(addressDetails.getCity());
						cell2Update026.setCellValue(addressDetails.getDistrict());
						cell2Update029.setCellValue(addressDetails.getState());
						cell2Update032.setCellValue(addressDetails.getPincode());
						cell2Update035.setCellValue(addressDetails.getHouseOwnership());
						cell2Update038.setCellValue("No");
						cell2Update041.setCellValue("");
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			// Asset details:-
			Cell cell2Update042 = sheet.getRow(166).getCell(2);
			cell2Update042.setCellValue(twowheelerDetails.getManufacture());

			Cell cell2Update043 = sheet.getRow(167).getCell(2);
			cell2Update043.setCellValue(twowheelerDetails.getModel());

			Cell cell2Update044 = sheet.getRow(168).getCell(2);
			cell2Update044.setCellValue(twowheelerDetails.getDealerName());

			Cell cell2Update045 = sheet.getRow(169).getCell(2);
			cell2Update045.setCellValue(twowheelerDetails.getExShowroomPrice());

			Cell cell2Update046 = sheet.getRow(170).getCell(2);
			cell2Update046.setCellValue(twowheelerDetails.getRoadTax());

			Cell cell2Update047 = sheet.getRow(171).getCell(2);
			cell2Update047.setCellValue(twowheelerDetails.getInsuranceEmi());

			Cell cell2Update048 = sheet.getRow(172).getCell(2);
			String processingFee = "";
			if (twowheelerDetails.getLoanCharges() != null) {
				org.json.JSONArray loanCarges = new org.json.JSONArray(twowheelerDetails.getLoanCharges());
				for (int n = 0; n < loanCarges.length(); n++) {
					JSONObject jsonObject = loanCarges.getJSONObject(n);
					if (jsonObject.getString("chargeName").equalsIgnoreCase("Processing Fee")) {
						processingFee = jsonObject.getString("totalAmount");
					}
				}
			}

			cell2Update048.setCellValue(processingFee);

			Cell cell2Update049 = sheet.getRow(173).getCell(2);
			cell2Update049.setCellValue(twowheelerDetails.getAccessories());

			Cell cell2Update050 = sheet.getRow(174).getCell(2);
			cell2Update050.setCellValue(twowheelerDetails.getAddonsCharges());

			Cell cell2Update051 = sheet.getRow(175).getCell(2);
			cell2Update051.setCellValue(twowheelerDetails.getTotalOnRoadPrice());

			// Loan Details:-
			Cell cell2Update052 = sheet.getRow(179).getCell(2);
			cell2Update052.setCellValue("Executive");

			Cell cell2Update053 = sheet.getRow(180).getCell(2);
			cell2Update053.setCellValue("2 Wheeleer");

			Cell cell2Update054 = sheet.getRow(181).getCell(2);
			cell2Update054.setCellValue(twowheelerDetails.getScheme());

			Cell cell2Update055 = sheet.getRow(182).getCell(2);
			cell2Update055.setCellValue(twowheelerDetails.getMarginMoney());

			Cell cell2Update056 = sheet.getRow(183).getCell(2);
			cell2Update056.setCellValue(twowheelerDetails.getAmount());

			Cell cell2Update057 = sheet.getRow(184).getCell(2);
			cell2Update057.setCellValue(twowheelerDetails.getRateOfInterest());

			Cell cell2Update058 = sheet.getRow(185).getCell(2);
			cell2Update058.setCellValue(twowheelerDetails.getTenure());

			// Reference details:-
			Cell cell2Update079 = sheet.getRow(189).getCell(2);
			Cell cell2Update080 = sheet.getRow(189).getCell(3);
			Cell cell2Update059 = sheet.getRow(190).getCell(2);
			Cell cell2Update060 = sheet.getRow(190).getCell(3);
			Cell cell2Update061 = sheet.getRow(191).getCell(2);
			Cell cell2Update062 = sheet.getRow(191).getCell(3);
			Cell cell2Update063 = sheet.getRow(192).getCell(2);
			Cell cell2Update064 = sheet.getRow(192).getCell(3);
			Cell cell2Update065 = sheet.getRow(193).getCell(2);
			Cell cell2Update066 = sheet.getRow(193).getCell(3);
			Cell cell2Update067 = sheet.getRow(194).getCell(2);
			Cell cell2Update068 = sheet.getRow(194).getCell(3);
			Cell cell2Update069 = sheet.getRow(195).getCell(2);
			Cell cell2Update070 = sheet.getRow(195).getCell(3);
			Cell cell2Update071 = sheet.getRow(196).getCell(2);
			Cell cell2Update072 = sheet.getRow(196).getCell(3);
			Cell cell2Update075 = sheet.getRow(197).getCell(2);
			Cell cell2Update076 = sheet.getRow(197).getCell(3);
			Cell cell2Update077 = sheet.getRow(198).getCell(2);
			Cell cell2Update078 = sheet.getRow(198).getCell(3);
			if (twowheelerDetails.getReferenceInfo() != null) {
				org.json.JSONArray referenceInfo = new org.json.JSONArray(twowheelerDetails.getReferenceInfo());
				JSONObject jsonObject1 = referenceInfo.getJSONObject(0);
				cell2Update079.setCellValue(jsonObject1.getString("referenceType"));
				cell2Update059.setCellValue(jsonObject1.getString("referenceName"));
				cell2Update061.setCellValue(jsonObject1.getString("addressLine1"));
				cell2Update063.setCellValue(jsonObject1.getString("addressLine2"));
				cell2Update065.setCellValue(jsonObject1.getString("pineCode"));
				cell2Update067.setCellValue(jsonObject1.getString("city"));
				cell2Update069.setCellValue(jsonObject1.getString("state"));
				cell2Update071.setCellValue(jsonObject1.getString("district"));
				cell2Update075.setCellValue(jsonObject1.getString("mobileNumber"));
				cell2Update077.setCellValue("");
				if (referenceInfo.length() > 1) {
					JSONObject jsonObject2 = referenceInfo.getJSONObject(1);
					cell2Update080.setCellValue(jsonObject1.getString("referenceType"));
					cell2Update060.setCellValue(jsonObject2.getString("referenceName"));
					cell2Update062.setCellValue(jsonObject2.getString("addressLine1"));
					cell2Update064.setCellValue(jsonObject1.getString("addressLine2"));
					cell2Update066.setCellValue(jsonObject1.getString("pineCode"));
					cell2Update068.setCellValue(jsonObject1.getString("city"));
					cell2Update070.setCellValue(jsonObject1.getString("state"));
					cell2Update072.setCellValue(jsonObject1.getString("district"));
					cell2Update076.setCellValue(jsonObject1.getString("mobileNumber"));
					cell2Update078.setCellValue("");
				}
			}

			// Sheet4:-

			Sheet BRE = workbook.getSheetAt(3);

			Cell Update1 = BRE.getRow(6).getCell(2);
			Cell Update2 = BRE.getRow(6).getCell(3);
			Cell Update3 = BRE.getRow(6).getCell(4);
			Update1.setCellValue(twowheelerDetails.getRequiredAmount());
			Update2.setCellValue("");
			Update3.setCellValue(twowheelerDetails.getAmount());

			Cell Update4 = BRE.getRow(7).getCell(2);
			Cell Update5 = BRE.getRow(7).getCell(3);
			Cell Update6 = BRE.getRow(7).getCell(4);
			Update4.setCellValue(twowheelerDetails.getRateOfInterest());
			Update5.setCellValue(twowheelerDetails.getRateOfInterest());
			Update6.setCellValue(twowheelerDetails.getRateOfInterest());

			Cell Update7 = BRE.getRow(8).getCell(2);
			Cell Update8 = BRE.getRow(8).getCell(3);
			Cell Update9 = BRE.getRow(8).getCell(4);

			Update7.setCellValue(twowheelerDetails.getTenure());
			Update8.setCellValue(twowheelerDetails.getTenure());
			Update9.setCellValue(twowheelerDetails.getTenure());

			Cell Update10 = BRE.getRow(9).getCell(2);
			Cell Update11 = BRE.getRow(9).getCell(3);
			Cell Update12 = BRE.getRow(9).getCell(4);

			Update10.setCellValue("");
			Update11.setCellValue("");
			Update12.setCellValue(twowheelerDetails.getEmi());

			Cell Update13 = BRE.getRow(10).getCell(2);
			Cell Update14 = BRE.getRow(10).getCell(3);
			Cell Update15 = BRE.getRow(10).getCell(4);
			String LTVApplied = Update13.getStringCellValue();
			String LTVEligible = Update14.getStringCellValue();
			String LTVApproved = Update15.getStringCellValue();
			Update13.setCellValue(twowheelerDetails.getAppliedLtv());
			Update14.setCellValue(twowheelerDetails.getEligibleLtv());
			Update15.setCellValue("73.74");

			// Sheet4:-Deviation Details
			if (twowheelerDetails.getBreResponse() != null) {
				JSONObject breResponseJson = new JSONObject(twowheelerDetails.getBreResponse());
				int row = 15;
				for (String keys : breResponseJson.keySet()) {

					if (!breResponseJson.getJSONObject(keys).get("deviation_group").equals(null)) {
						String deviation = breResponseJson.getJSONObject(keys).get("deviation_group").toString();
						String description = breResponseJson.getJSONObject(keys).get("description").toString();
						Cell Update16 = BRE.getRow(row).getCell(1);
						Update16.setCellValue(description);

						Cell Update17 = BRE.getRow(row).getCell(3);
						Update17.setCellValue(deviation);
						row++;
					}
				}
			}

//Policy Hardstop Deviations(sheet4)

			Cell Update46 = BRE.getRow(33).getCell(2);
			Update46.setCellValue("");
			Cell Update47 = BRE.getRow(33).getCell(3);
			Update47.setCellValue("");
			Cell Update48 = BRE.getRow(34).getCell(2);
			Update48.setCellValue("");
			Cell Update49 = BRE.getRow(34).getCell(3);
			Update49.setCellValue("");
			Cell Update50 = BRE.getRow(35).getCell(2);
			Update50.setCellValue("");
			Cell Update51 = BRE.getRow(35).getCell(3);
			Update51.setCellValue("");
			Cell Update52 = BRE.getRow(36).getCell(2);
			Update52.setCellValue("");
			Cell Update53 = BRE.getRow(36).getCell(3);
			Update53.setCellValue("");
			Cell Update54 = BRE.getRow(37).getCell(2);
			Update54.setCellValue("");
			Cell Update55 = BRE.getRow(37).getCell(3);
			Update55.setCellValue("");
			Cell Update56 = BRE.getRow(38).getCell(2);
			Update56.setCellValue("");
			Cell Update57 = BRE.getRow(38).getCell(3);
			Update57.setCellValue("");
			Cell Update58 = BRE.getRow(39).getCell(2);
			Update58.setCellValue("");
			Cell Update59 = BRE.getRow(39).getCell(3);
			Update59.setCellValue("");
			Cell Update60 = BRE.getRow(40).getCell(2);
			Update60.setCellValue("");
			Cell Update61 = BRE.getRow(40).getCell(3);
			Update61.setCellValue("");
			Cell Update62 = BRE.getRow(41).getCell(2);
			Update62.setCellValue("");
			Cell Update63 = BRE.getRow(41).getCell(3);
			Update63.setCellValue("");
			Cell Update64 = BRE.getRow(42).getCell(2);
			Update64.setCellValue("");
			Cell Update65 = BRE.getRow(42).getCell(3);
			Update65.setCellValue("");
			Cell Update66 = BRE.getRow(43).getCell(2);
			Update66.setCellValue("");
			Cell Update67 = BRE.getRow(43).getCell(3);
			Update67.setCellValue("");
			Cell Update68 = BRE.getRow(44).getCell(2);
			Update68.setCellValue("");
			Cell Update69 = BRE.getRow(44).getCell(3);
			Update69.setCellValue("");
			Cell Update70 = BRE.getRow(45).getCell(2);
			Update70.setCellValue("");
			Cell Update71 = BRE.getRow(45).getCell(3);
			Update71.setCellValue("");
			Cell Update72 = BRE.getRow(46).getCell(2);
			Update72.setCellValue("");
			Cell Update73 = BRE.getRow(46).getCell(3);
			Update73.setCellValue("");
			Cell Update74 = BRE.getRow(47).getCell(2);
			Update74.setCellValue("");
			Cell Update75 = BRE.getRow(47).getCell(3);
			Update75.setCellValue("");

//Manual Deviations(sheet4)

			Cell Update76 = BRE.getRow(51).getCell(2);
			Update76.setCellValue("");
			Cell Update77 = BRE.getRow(51).getCell(3);
			Update77.setCellValue("");
			Cell Update78 = BRE.getRow(52).getCell(2);
			Update78.setCellValue("");
			Cell Update79 = BRE.getRow(52).getCell(3);
			Update79.setCellValue("");
			Cell Update80 = BRE.getRow(53).getCell(2);
			Update80.setCellValue("");
			Cell Update81 = BRE.getRow(53).getCell(3);
			Update81.setCellValue("");
			Cell Update82 = BRE.getRow(54).getCell(2);
			Update82.setCellValue("");
			Cell Update83 = BRE.getRow(54).getCell(3);
			Update83.setCellValue("");
			Cell Update84 = BRE.getRow(55).getCell(2);
			Update84.setCellValue("");
			Cell Update85 = BRE.getRow(55).getCell(3);
			Update85.setCellValue("");
			Cell Update86 = BRE.getRow(56).getCell(2);
			Update86.setCellValue("");
			Cell Update87 = BRE.getRow(56).getCell(3);
			Update87.setCellValue("");
			Cell Update88 = BRE.getRow(57).getCell(2);
			Update88.setCellValue("");
			Cell Update89 = BRE.getRow(57).getCell(3);
			Update89.setCellValue("");
			Cell Update90 = BRE.getRow(58).getCell(2);
			Update90.setCellValue("");
			Cell Update91 = BRE.getRow(58).getCell(3);
			Update91.setCellValue("");
			Cell Update92 = BRE.getRow(59).getCell(2);
			Update92.setCellValue("");
			Cell Update93 = BRE.getRow(59).getCell(3);
			Update93.setCellValue("");
			Cell Update94 = BRE.getRow(60).getCell(2);
			Update94.setCellValue("");
			Cell Update95 = BRE.getRow(60).getCell(3);
			Update95.setCellValue("");
			Cell Update96 = BRE.getRow(61).getCell(2);
			Update96.setCellValue("");
			Cell Update97 = BRE.getRow(61).getCell(3);
			Update97.setCellValue("");
			Cell Update98 = BRE.getRow(62).getCell(2);
			Update98.setCellValue("");
			Cell Update99 = BRE.getRow(62).getCell(3);
			Update99.setCellValue("");
			Cell Update00 = BRE.getRow(63).getCell(2);
			Update00.setCellValue("");
			Cell Update01 = BRE.getRow(63).getCell(3);
			Update01.setCellValue("");
			Cell Update02 = BRE.getRow(64).getCell(2);
			Update02.setCellValue("");
			Cell Update03 = BRE.getRow(64).getCell(3);
			Update03.setCellValue("");
			Cell Update04 = BRE.getRow(65).getCell(2);
			Update04.setCellValue("");
			Cell Update05 = BRE.getRow(65).getCell(3);
			Update05.setCellValue("");

//Sanction Conditions(sheet4)

			Cell Update06 = BRE.getRow(74).getCell(1);
			Update06.setCellValue("");
			Cell Update07 = BRE.getRow(75).getCell(1);
			Update07.setCellValue("");
			Cell Update08 = BRE.getRow(76).getCell(1);
			Update08.setCellValue("");
			Cell Update09 = BRE.getRow(77).getCell(1);
			Update09.setCellValue("");
			Cell Update010 = BRE.getRow(78).getCell(1);
			Update010.setCellValue("");
			Cell Update011 = BRE.getRow(79).getCell(1);
			Update011.setCellValue("");
			Cell Update012 = BRE.getRow(80).getCell(1);
			Update012.setCellValue("");
			Cell Update013 = BRE.getRow(81).getCell(1);
			Update013.setCellValue("");
			Cell Update014 = BRE.getRow(82).getCell(1);
			Update014.setCellValue("");
			Cell Update015 = BRE.getRow(83).getCell(1);
			Update015.setCellValue("");
			Cell Update016 = BRE.getRow(84).getCell(1);
			Update016.setCellValue("");
			Cell Update017 = BRE.getRow(85).getCell(1);
			Update017.setCellValue("");
			Cell Update018 = BRE.getRow(86).getCell(1);
			Update018.setCellValue("");
			Cell Update019 = BRE.getRow(87).getCell(1);
			Update019.setCellValue("");
			Cell Update020 = BRE.getRow(88).getCell(1);
			Update020.setCellValue("");

			// SHEET2:-
			Sheet bankDetails = workbook.getSheetAt(1);

			if (twowheelerDetails.getBankDetails() != null) {

				try {
					ObjectMapper objectMapper = new ObjectMapper();
					List<BankDetailsResponse> reference = objectMapper.readValue(twowheelerDetails.getBankDetails(),
							new TypeReference<List<BankDetailsResponse>>() {
							});

					if (reference.size() >= 1) {
						BankDetailsResponse bankDetailsResponse = reference.get(0);
						Cell updateValue1 = bankDetails.getRow(6).getCell(2);
						updateValue1.setCellValue(bankDetailsResponse.getAccountName());
						Cell updateValue2 = bankDetails.getRow(7).getCell(2);
						updateValue2.setCellValue(bankDetailsResponse.getMember());
						Cell updateValue3 = bankDetails.getRow(8).getCell(2);
						updateValue3.setCellValue(bankDetailsResponse.getAccountType());
						Cell updateValue4 = bankDetails.getRow(9).getCell(2);
						updateValue4.setCellValue(bankDetailsResponse.getAccountNumber());
						Cell updateValue5 = bankDetails.getRow(10).getCell(2);
						updateValue5.setCellValue(bankDetailsResponse.getAccountIfsc());
						Cell updateValue6 = bankDetails.getRow(11).getCell(2);
						updateValue6.setCellValue(bankDetailsResponse.getAccountBankName());
						Cell updateValue7 = bankDetails.getRow(12).getCell(2);
						updateValue7.setCellValue(bankDetailsResponse.getAccountBranchId());
						Cell updateValue8 = bankDetails.getRow(13).getCell(2);
						updateValue8.setCellValue(bankDetailsResponse.getRepaymentType());
					}
					if (reference.size() >= 2) {
						BankDetailsResponse bankDetailsResponse = reference.get(1);
						Cell updateValue1 = bankDetails.getRow(16).getCell(2);
						updateValue1.setCellValue(bankDetailsResponse.getAccountName());
						Cell updateValue2 = bankDetails.getRow(17).getCell(2);
						updateValue2.setCellValue(bankDetailsResponse.getMember());
						Cell updateValue3 = bankDetails.getRow(18).getCell(2);
						updateValue3.setCellValue(bankDetailsResponse.getAccountType());
						Cell updateValue4 = bankDetails.getRow(19).getCell(2);
						updateValue4.setCellValue(bankDetailsResponse.getAccountNumber());
						Cell updateValue5 = bankDetails.getRow(20).getCell(2);
						updateValue5.setCellValue(bankDetailsResponse.getAccountIfsc());
						Cell updateValue6 = bankDetails.getRow(21).getCell(2);
						updateValue6.setCellValue(bankDetailsResponse.getAccountBankName());
						Cell updateValue7 = bankDetails.getRow(22).getCell(2);
						updateValue7.setCellValue(bankDetailsResponse.getAccountBranchId());
						Cell updateValue8 = bankDetails.getRow(23).getCell(2);
						updateValue8.setCellValue(bankDetailsResponse.getRepaymentType());
					}

					if (reference.size() >= 3) {
						BankDetailsResponse bankDetailsResponse = reference.get(2);
						Cell updateValue1 = bankDetails.getRow(26).getCell(2);
						updateValue1.setCellValue(bankDetailsResponse.getAccountName());
						Cell updateValue2 = bankDetails.getRow(27).getCell(2);
						updateValue2.setCellValue(bankDetailsResponse.getMember());
						Cell updateValue3 = bankDetails.getRow(28).getCell(2);
						updateValue3.setCellValue(bankDetailsResponse.getAccountType());
						Cell updateValue4 = bankDetails.getRow(29).getCell(2);
						updateValue4.setCellValue(bankDetailsResponse.getAccountNumber());
						Cell updateValue5 = bankDetails.getRow(30).getCell(2);
						updateValue5.setCellValue(bankDetailsResponse.getAccountIfsc());
						Cell updateValue6 = bankDetails.getRow(31).getCell(2);
						updateValue6.setCellValue(bankDetailsResponse.getAccountBankName());
						Cell updateValue7 = bankDetails.getRow(32).getCell(2);
						updateValue7.setCellValue(bankDetailsResponse.getAccountBranchId());
						Cell updateValue8 = bankDetails.getRow(33).getCell(2);
						updateValue8.setCellValue(bankDetailsResponse.getRepaymentType());
					}

					if (reference.size() >= 4) {
						BankDetailsResponse bankDetailsResponse = reference.get(3);
						Cell updateValue1 = bankDetails.getRow(36).getCell(2);
						updateValue1.setCellValue(bankDetailsResponse.getAccountName());
						Cell updateValue2 = bankDetails.getRow(37).getCell(2);
						updateValue2.setCellValue(bankDetailsResponse.getMember());
						Cell updateValue3 = bankDetails.getRow(38).getCell(2);
						updateValue3.setCellValue(bankDetailsResponse.getAccountType());
						Cell updateValue4 = bankDetails.getRow(39).getCell(2);
						updateValue4.setCellValue(bankDetailsResponse.getAccountNumber());
						Cell updateValue5 = bankDetails.getRow(40).getCell(2);
						updateValue5.setCellValue(bankDetailsResponse.getAccountIfsc());
						Cell updateValue6 = bankDetails.getRow(41).getCell(2);
						updateValue6.setCellValue(bankDetailsResponse.getAccountBankName());
						Cell updateValue7 = bankDetails.getRow(42).getCell(2);
						updateValue7.setCellValue(bankDetailsResponse.getAccountBranchId());
						Cell updateValue8 = bankDetails.getRow(43).getCell(2);
						updateValue8.setCellValue(bankDetailsResponse.getRepaymentType());
					}

					if (reference.size() >= 5) {
						BankDetailsResponse bankDetailsResponse = reference.get(4);
						Cell updateValue1 = bankDetails.getRow(46).getCell(2);
						updateValue1.setCellValue(bankDetailsResponse.getAccountName());
						Cell updateValue2 = bankDetails.getRow(47).getCell(2);
						updateValue2.setCellValue(bankDetailsResponse.getMember());
						Cell updateValue3 = bankDetails.getRow(48).getCell(2);
						updateValue3.setCellValue(bankDetailsResponse.getAccountType());
						Cell updateValue4 = bankDetails.getRow(49).getCell(2);
						updateValue4.setCellValue(bankDetailsResponse.getAccountNumber());
						Cell updateValue5 = bankDetails.getRow(50).getCell(2);
						updateValue5.setCellValue(bankDetailsResponse.getAccountIfsc());
						Cell updateValue6 = bankDetails.getRow(51).getCell(2);
						updateValue6.setCellValue(bankDetailsResponse.getAccountBankName());
						Cell updateValue7 = bankDetails.getRow(52).getCell(2);
						updateValue7.setCellValue(bankDetailsResponse.getAccountBranchId());
						Cell updateValue8 = bankDetails.getRow(53).getCell(2);
						updateValue8.setCellValue(bankDetailsResponse.getRepaymentType());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

//SHEET3:-
			Sheet obligations = workbook.getSheetAt(2);

			Cell updateValue41 = obligations.getRow(9).getCell(2);
			updateValue41.setCellValue("NOT DISCLOSED");
			Cell updateValue42 = obligations.getRow(9).getCell(3);
			updateValue42.setCellValue("NOT DISCLOSED");
			Cell updateValue43 = obligations.getRow(9).getCell(4);
			updateValue43.setCellValue("NOT DISCLOSED");

			Cell updateValue44 = obligations.getRow(10).getCell(2);
			updateValue44.setCellValue("60000.0");
			Cell updateValue45 = obligations.getRow(10).getCell(3);
			updateValue45.setCellValue("124000.0");
			Cell updateValue46 = obligations.getRow(10).getCell(4);
			updateValue46.setCellValue("35000.0");

			Cell updateValue47 = obligations.getRow(11).getCell(2);
			updateValue47.setCellValue("60327.0");
			Cell updateValue48 = obligations.getRow(11).getCell(3);
			updateValue48.setCellValue("31769.0");
			Cell updateValue49 = obligations.getRow(11).getCell(4);
			updateValue49.setCellValue("0.0");

			Cell updateValue50 = obligations.getRow(12).getCell(2);
			updateValue50.setCellValue("0.0");
			Cell updateValue51 = obligations.getRow(12).getCell(3);
			updateValue51.setCellValue("0.0");
			Cell updateValue52 = obligations.getRow(12).getCell(4);
			updateValue52.setCellValue("0.0");

			Cell updateValue53 = obligations.getRow(13).getCell(2);
			updateValue53.setCellValue("1");
			Cell updateValue54 = obligations.getRow(13).getCell(3);
			updateValue54.setCellValue("1");
			Cell updateValue55 = obligations.getRow(13).getCell(4);
			updateValue55.setCellValue("3");

			Cell updateValue56 = obligations.getRow(14).getCell(2);
			updateValue56.setCellValue("0");
			Cell updateValue57 = obligations.getRow(14).getCell(3);
			updateValue57.setCellValue("0");
			Cell updateValue58 = obligations.getRow(14).getCell(4);
			updateValue58.setCellValue("0");

			Cell updateValue59 = obligations.getRow(15).getCell(2);
			updateValue59.setCellValue("07/07/2023");
			Cell updateValue60 = obligations.getRow(15).getCell(3);
			updateValue60.setCellValue("25/06/2023");
			Cell updateValue61 = obligations.getRow(15).getCell(4);
			updateValue61.setCellValue("08/06/2023");

			Cell updateValue62 = obligations.getRow(16).getCell(2);
			updateValue62.setCellValue("60000.0");
			Cell updateValue63 = obligations.getRow(16).getCell(3);
			updateValue63.setCellValue("0.0");
			Cell updateValue64 = obligations.getRow(16).getCell(4);
			updateValue64.setCellValue("0.0");

			// Status of obligations:-

			Cell updateValue65 = obligations.getRow(18).getCell(2);
			updateValue65.setCellValue("NO");
			Cell updateValue66 = obligations.getRow(18).getCell(3);
			updateValue66.setCellValue("NO");
			Cell updateValue67 = obligations.getRow(18).getCell(4);
			updateValue67.setCellValue("NO");

			Cell updateValue68 = obligations.getRow(19).getCell(2);
			updateValue68.setCellValue("YES");
			Cell updateValue69 = obligations.getRow(19).getCell(3);
			updateValue69.setCellValue("YES");
			Cell updateValue70 = obligations.getRow(19).getCell(4);
			updateValue70.setCellValue("YES");

			Cell updateValue71 = obligations.getRow(20).getCell(2);
			updateValue71.setCellValue("Live");
			Cell updateValue72 = obligations.getRow(20).getCell(3);
			updateValue72.setCellValue("Live");
			Cell updateValue73 = obligations.getRow(20).getCell(4);
			updateValue73.setCellValue("Live");

			Cell updateValue74 = obligations.getRow(21).getCell(2);
			updateValue74.setCellValue("Bureau");
			Cell updateValue75 = obligations.getRow(21).getCell(3);
			updateValue75.setCellValue("Bureau");
			Cell updateValue76 = obligations.getRow(21).getCell(4);
			updateValue76.setCellValue("Bureau");

			Cell updateValue77 = obligations.getRow(22).getCell(2);
			updateValue77.setCellValue("31/07/2023");
			Cell updateValue78 = obligations.getRow(22).getCell(3);
			updateValue78.setCellValue("13/08/2023");
			Cell updateValue79 = obligations.getRow(22).getCell(4);
			updateValue79.setCellValue("20/08/2023");

			Cell updateValue80 = obligations.getRow(23).getCell(2);
			updateValue80.setCellValue("");
			Cell updateValue81 = obligations.getRow(23).getCell(3);
			updateValue81.setCellValue("");
			Cell updateValue82 = obligations.getRow(23).getCell(4);
			updateValue82.setCellValue("Very Good");

			inputStream.close();

			FileOutputStream outputStream = new FileOutputStream(x.onePager + applicationNo + "CAM_REPORT.xlsx");
			workbook.write(outputStream);
			workbook.close();
			outputStream.close();

			base64 = exceltobase64(x.onePager + applicationNo + "CAM_REPORT.xlsx");
			returnjson.put("Base64", base64);

		} catch (IOException | EncryptedDocumentException ex) {
			ex.printStackTrace();
		}
		return returnjson;
	}

	public String exceltobase64(String filepath) {
		String base64 = null;
		try {
			File file = new File(filepath);
			FileInputStream fis = new FileInputStream(file);
			byte[] bytes = new byte[(int) file.length()];
			fis.read(bytes);
			base64 = Base64.getEncoder().encodeToString(bytes);

		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}

		return base64;
	}
}
