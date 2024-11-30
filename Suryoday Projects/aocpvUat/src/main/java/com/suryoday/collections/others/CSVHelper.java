package com.suryoday.collections.others;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import com.suryoday.collections.pojo.CSVModel;

public class CSVHelper {

	public static String TYPE = "text/csv";
	static String[] HEADERs = { "AGREEMENTID", "CUSTOMERID", "CUSTOMERNAME", "ProductGroup", "Product", "Sub product",
			"Country", "Zone", "Region", "State", "City", "District", "Branch", "BRANCH_CODE", "CurrentDPD",
			"Current_INT", "Current_POS", "Disbursal_Date", "DisbursedAmount", "EMIAmount", "BOM_POS", "EMI_OD_Amount",
			"INTEREST_OD", "INTEREST_Paid", "LOAN_STATUS", "PENAL_PENDING", "PRINCIPAL_OD", "TOTAL_ARREARS",
			"TOTAL_FORECLOSURE", "TOTAL_OUTSTANDING", "OTHER_CHARGES", "ForeclosePrepayCharge", "TENURE_MONTH",
			"Tenure_Days", "MAILINGADDRESSLINE", "MAILINGLANDMARK", "MAILINGMOBILE", "MAILINGPHONE1", "MAILINGPHONE2",
			"MAILINGZIPCODE", "MAILING_ADD_LAT", "MAILING_ADD_LONG", "NONMAILINGADDRESSLINE", "NONMAILINGCITY",
			"NONMAILINGLANDMARK", "NONMAILINGMOBILE", "NONMAILINGPHONE1", "NONMAILINGPHONE2", "NONMAILINGSTATE",
			"NONMAILINGZIPCODE", "NONMAILING_ADD_LAT", "NONMAILING_ADD_LONG", "CHASISNUM", "ENGINENUM", "Level_Desc",
			"MAKE", "MANUFACTURERDESC", "MODELID", "UNIT_DESC", "CO_APPLICANT_1_ID", "CO_APPLICANT_1_NAME",
			"CO_APPLICANT_1_PHONE", "CO_APPLICANT_2_ID", "CO_APPLICANT_2_NAME", "CO_APPLICANT_2_PHONE",
			"CO_APPLICANT_3_ID", "CO_APPLICANT_3_NAME", "CO_APPLICANT_3_PHONE", "CO_APPLICANT_4_ID",
			"CO_APPLICANT_4_NAME", "CO_APPLICANT_4_PHONE", "CO_APPLICANT_5_ID", "CO_APPLICANT_5_NAME",
			"CO_APPLICANT_5_PHONE", "GUARANTOR_1_ID", "GUARANTOR_1_NAME", "GUARANTOR_1_PHONE", "GUARANTOR_2_ID",
			"GUARANTOR_2_NAME", "GUARANTOR_2_PHONE", "GUARANTOR_3_ID", "GUARANTOR_3_NAME", "GUARANTOR_3_PHONE",
			"GUARANTOR_4_ID", "GUARANTOR_4_NAME", "GUARANTOR_4_PHONE", "REF1_ADDRS", "REF1_CONTACT", "REF2_ADDRS",
			"REF2_CONTACT", "REFERENCE1_NAME", "REFERENCE2_NAME", "CenterName", "ACC_OPEN_DATE", "NEXT_DUE_DATE",
			"LAST_PAY_DATE", "BUSINESS_NAME", "BUSINESSADDRESSLINE", "BUSINESSADDRESSCITY", "BUSINESSADDRESSSTATE",
			"BUSINESSADDRESSLANDMARK", "BUSINESSADDRESSZIPCODE", "BUSINESS_ADD_LAT", "BUSINESS_ADD_LONG",
			"BUSINESSMOBILE", "BUSINESSPHONE1", "Collector_ID", "Status" };

	public static boolean hasCSVFormat(MultipartFile file) {
		if (!TYPE.equals(file.getContentType())) {
			return false;
		}
		return true;
	}

	@SuppressWarnings("deprecation")
	public static List<CSVModel> csvToTutorials(InputStream is) {
		try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				CSVParser csvParser = new CSVParser(fileReader,
						CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

			List<CSVModel> tutorials = new ArrayList<CSVModel>();

			Iterable<CSVRecord> csvRecords = csvParser.getRecords();

			for (CSVRecord csvRecord : csvRecords) {
				CSVModel tutorial = new CSVModel(Long.parseLong(csvRecord.get("id")), csvRecord.get("AGREEMENTID"),
						csvRecord.get("CUSTOMERID"), csvRecord.get("CUSTOMERNAME"), csvRecord.get("ProductGroup"),
						csvRecord.get("Product"), csvRecord.get("Sub product"), csvRecord.get("Country"),
						csvRecord.get("Zone"), csvRecord.get("Region"), csvRecord.get("State"), csvRecord.get("City"),
						csvRecord.get("District"), csvRecord.get("Branch"), csvRecord.get("BRANCH_CODE"),
						csvRecord.get("CurrentDPD"), csvRecord.get("Current_INT"), csvRecord.get("Current_POS"),
						csvRecord.get("Disbursal_Date"), csvRecord.get("DisbursedAmount"), csvRecord.get("EMIAmount"),
						csvRecord.get("BOM_POS"), csvRecord.get("EMI_OD_Amount"), csvRecord.get("INTEREST_OD"),
						csvRecord.get("INTEREST_Paid"), csvRecord.get("LOAN_STATUS"), csvRecord.get("PENAL_PENDING"),
						csvRecord.get("PRINCIPAL_OD"), csvRecord.get("TOTAL_ARREARS"),
						csvRecord.get("TOTAL_FORECLOSURE"), csvRecord.get("TOTAL_OUTSTANDING"),
						csvRecord.get("OTHER_CHARGES"), csvRecord.get("ForeclosePrepayCharge"),
						csvRecord.get("TENURE_MONTH"), csvRecord.get("Tenure_Days"),
						csvRecord.get("MAILINGADDRESSLINE"), csvRecord.get("MAILINGLANDMARK"),
						csvRecord.get("MAILINGMOBILE"), csvRecord.get("MAILINGPHONE1"), csvRecord.get("MAILINGPHONE2"),
						csvRecord.get("MAILINGZIPCODE"), csvRecord.get("MAILING_ADD_LAT"),
						csvRecord.get("MAILING_ADD_LONG"), csvRecord.get("NONMAILINGADDRESSLINE"),
						csvRecord.get("NONMAILINGCITY"), csvRecord.get("NONMAILINGLANDMARK"),
						csvRecord.get("NONMAILINGMOBILE"), csvRecord.get("NONMAILINGPHONE1"),
						csvRecord.get("NONMAILINGPHONE2"), csvRecord.get("NONMAILINGSTATE"),
						csvRecord.get("NONMAILINGZIPCODE"), csvRecord.get("NONMAILING_ADD_LAT"),
						csvRecord.get("NONMAILING_ADD_LONG"), csvRecord.get("CHASISNUM"), csvRecord.get("ENGINENUM"),
						csvRecord.get("Level_Desc"), csvRecord.get("MAKE"), csvRecord.get("MANUFACTURERDESC"),
						csvRecord.get("MODELID"), csvRecord.get("UNIT_DESC"), csvRecord.get("YEAR"),
						csvRecord.get("CO_APPLICANT_1_ID"), csvRecord.get("CO_APPLICANT_1_NAME"),
						csvRecord.get("CO_APPLICANT_1_PHONE"), csvRecord.get("CO_APPLICANT_2_ID"),
						csvRecord.get("CO_APPLICANT_2_NAME"), csvRecord.get("CO_APPLICANT_2_PHONE"),
						csvRecord.get("CO_APPLICANT_3_ID"), csvRecord.get("CO_APPLICANT_3_NAME"),
						csvRecord.get("CO_APPLICANT_3_PHONE"), csvRecord.get("CO_APPLICANT_4_ID"),
						csvRecord.get("CO_APPLICANT_4_NAME"), csvRecord.get("CO_APPLICANT_4_PHONE"),
						csvRecord.get("CO_APPLICANT_5_ID"), csvRecord.get("CO_APPLICANT_5_NAME"),
						csvRecord.get("CO_APPLICANT_5_PHONE"), csvRecord.get("GUARANTOR_1_ID"),
						csvRecord.get("GUARANTOR_1_NAME"), csvRecord.get("GUARANTOR_1_PHONE"),
						csvRecord.get("GUARANTOR_2_ID"), csvRecord.get("GUARANTOR_2_NAME"),
						csvRecord.get("GUARANTOR_2_PHONE"), csvRecord.get("GUARANTOR_3_ID"),
						csvRecord.get("GUARANTOR_3_NAME"), csvRecord.get("GUARANTOR_3_PHONE"),
						csvRecord.get("GUARANTOR_4_ID"), csvRecord.get("GUARANTOR_4_NAME"),
						csvRecord.get("GUARANTOR_4_PHONE"), csvRecord.get("REF1_ADDRS"), csvRecord.get("REF1_CONTACT"),
						csvRecord.get("REF2_ADDRS"), csvRecord.get("REF2_CONTACT"), csvRecord.get("REFERENCE1_NAME"),
						csvRecord.get("REFERENCE2_NAME"), csvRecord.get("CenterName"), csvRecord.get("ACC_OPEN_DATE"),
						csvRecord.get("NEXT_DUE_DATE"), csvRecord.get("LAST_PAY_DATE"), csvRecord.get("BUSINESS_NAME"),
						csvRecord.get("BUSINESSADDRESSLINE"), csvRecord.get("BUSINESSADDRESSCITY"),
						csvRecord.get("BUSINESSADDRESSSTATE"), csvRecord.get("BUSINESSADDRESSLANDMARK"),
						csvRecord.get("BUSINESSADDRESSZIPCODE"), csvRecord.get("BUSINESS_ADD_LAT"),
						csvRecord.get("BUSINESS_ADD_LONG"), csvRecord.get("BUSINESSMOBILE"),
						csvRecord.get("BUSINESSPHONE1"), csvRecord.get("Collector_ID"), csvRecord.get("Status"),
						LocalDate.parse(csvRecord.get("currentDate")), LocalDate.parse(csvRecord.get("updatedDate")));

				tutorials.add(tutorial);
			}

			return tutorials;
		} catch (IOException e) {
			throw new RuntimeException("Fail to parse CSV file: " + e.getMessage());
		}
	}

}
