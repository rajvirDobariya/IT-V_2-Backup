package com.suryoday.dsaOnboard.serviceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Random;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.suryoday.aocpv.serviceImp.NoSuchElementException;
import com.suryoday.dsaOnboard.pojo.MortgageDetails;
import com.suryoday.dsaOnboard.repository.MortgageDetailsRepo;
import com.suryoday.dsaOnboard.service.MortgageDetailsService;

@Service
public class MortgageDetailsServImpl implements MortgageDetailsService {
	@Autowired
	MortgageDetailsRepo mortgageDetailsRepo;
	@Value("${mortgaegfile}")
	private String mortgaegfilePath;

	@Override
	public void saveMortgageDetails(JSONObject jsonObject, MultipartFile file) throws IOException {
		MortgageDetails mortgageDetails = new MortgageDetails();
		mortgageDetails.setContactNo(jsonObject.getString("ContactNo"));
		mortgageDetails.setDateOfActivity(jsonObject.getString("DateOfActivity"));
		mortgageDetails.setEmpId(jsonObject.getString("EmpId"));
		mortgageDetails.setEmpName(jsonObject.getString("EmpName"));
		mortgageDetails.setGeoLatLong(jsonObject.getString("GeoLatLong"));
//		mortgageDetails.setPurposeToVisit(jsonObject.getString("PurposeTovisit"));
		mortgageDetails.setRelationshipType(jsonObject.getString("RelationshipType"));
		mortgageDetails.setRemarks(jsonObject.getString("Remarks"));
		mortgageDetails.setVisitName(jsonObject.getString("VisitName"));
		mortgageDetails.setPurposeToVisit(jsonObject.getString("PurposeToVisit"));
		mortgageDetails.setToVisit(jsonObject.getString("ToVisit"));
		mortgageDetails.setImage(file.getBytes());
		mortgageDetails.setApplicationId(generateRandom(10));
		System.out.println(mortgageDetails);
		mortgageDetailsRepo.save(mortgageDetails);
	}

	public long generateRandom(int length) {
		Random random = new Random();
		char[] digits = new char[length];
		digits[0] = (char) (random.nextInt(9) + '1');
		for (int i = 1; i < length; i++) {
			digits[i] = (char) (random.nextInt(10) + '0');
		}
		return Long.parseLong(new String(digits));
	}

	@Override
	public List<MortgageDetails> fetchByEmpId(JSONObject jsonObject) {
		String empId = jsonObject.getString("EmpId");
		List<MortgageDetails> list = mortgageDetailsRepo.fetchByEmpId(empId);
		if (list.isEmpty()) {
			throw new NoSuchElementException("List is empty");
		}
		return list;

	}

	@Override
	public List<MortgageDetails> fetchByDate(JSONObject jsonObject) {
		String startDate = jsonObject.getString("StartDate");
		String endDate = jsonObject.getString("EndDate");
		List<MortgageDetails> list = mortgageDetailsRepo.fetchByDate(LocalDateTime.parse(startDate),
				LocalDateTime.parse(endDate));
		if (list.isEmpty()) {
			throw new NoSuchElementException("List is empty");
		}
		return list;
	}

	@Override
	public JSONObject writeExcel(JSONArray json) {
		String base64 = null;
		JSONObject returnjson = new JSONObject();
		try {
			Workbook wb = new XSSFWorkbook();
			Sheet sheet = wb.createSheet("new sheet");
			Row row = sheet.createRow(0);

			Row row1 = null;
			CellStyle headerStyle = wb.createCellStyle();

			Font headerFont = wb.createFont();
			headerFont.setBold(true);
			headerFont.setFontHeightInPoints((short) 12);
			// headerFont.setColor(IndexedColors.BLACK.index);
			headerStyle.setFont(headerFont);
			headerStyle.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());
			// headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			// Create a cell and put a value in it.

			String[] coloumnHeading = { "EmpId", "EmpName", "Purpose", "ToVisit", "VisitName", "DateOfActivity",
					"ContactNo", "RelationshipType", "Remarks", "Location" };
			for (int i = 0; i < coloumnHeading.length; i++) {
				Cell cell = row.createCell(i);
				cell.setCellValue(coloumnHeading[i]);
				cell.setCellStyle(headerStyle);

			}

			for (int i = 0; i < json.length(); i++) {
				JSONObject obj = json.getJSONObject(i);
				System.out.println(obj.toString());
				String empId = obj.getString("empId");
				String empName = obj.getString("empName");
				String purposeToVisit = obj.getString("purposeToVisit");
				String toVisit = obj.getString("toVisit");
				String visitName = obj.getString("visitName");
				String dateOfActivity = obj.getString("dateOfActivity");
				String contactNo = obj.getString("contactNo");
				String relationshipType = obj.getString("relationshipType");
				String remarks = obj.getString("remarks");
				String geoLatLong = obj.getString("geoLatLong");
				JSONObject latLong = new JSONObject(geoLatLong);
				String url = "https://www.google.com/maps?q=" + latLong.get("latitude") + "," + latLong.get("longitude")
						+ "";
				row1 = sheet.createRow((short) i + 1);

				row1.createCell(0).setCellValue(empId);
				row1.createCell(1).setCellValue(empName);
				row1.createCell(2).setCellValue(purposeToVisit);
				row1.createCell(3).setCellValue(toVisit);
				row1.createCell(4).setCellValue(visitName);
				row1.createCell(5).setCellValue(dateOfActivity);
				row1.createCell(6).setCellValue(contactNo);
				row1.createCell(7).setCellValue(relationshipType);
				row1.createCell(8).setCellValue(remarks);
				Cell createCell = row1.createCell(9);
				CreationHelper createHelper = wb.getCreationHelper();
				Hyperlink link = createHelper.createHyperlink(HyperlinkType.URL);
				link.setAddress(url);
				createCell.setCellValue(url);
				createCell.setHyperlink(link);
//				row1.createCell(8).setCellValue(url);

			}

			FileOutputStream fileOut = new FileOutputStream(mortgaegfilePath);

			wb.write(fileOut);
			fileOut.close();

			base64 = exceltobase64(mortgaegfilePath);
			returnjson.put("Base64", base64);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
