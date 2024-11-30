package com.suryoday.hastakshar.serviceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.suryoday.hastakshar.pojo.NewBranchMaster;
import com.suryoday.hastakshar.repository.NewBranchMasterRepo;
import com.suryoday.hastakshar.service.NewBranchMasterService;

@Service
public class NewBranchMasterServiceImpl implements NewBranchMasterService {

	@Autowired
	private NewBranchMasterRepo newBranchMasterRepo;

	@Override
	public void updateBranchExcel(MultipartFile branchExcel) {
		List<NewBranchMaster> branchMasterList = new ArrayList<>();

		newBranchMasterRepo.deleteAll();

		try (Workbook workbook = new XSSFWorkbook(branchExcel.getInputStream())) {
			Sheet sheet = workbook.getSheetAt(0); // Read the first sheet
			for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Start from row 1, assuming row 0 is header
				Row row = sheet.getRow(i);
				if (row == null)
					continue;

				NewBranchMaster branchMaster = new NewBranchMaster();
				branchMaster.setId(UUID.randomUUID().toString());
				
				// Set branchCode (column index 0)
				Cell branchCodeCell = row.getCell(0);
				branchMaster.setBranchCode(getCellValue(branchCodeCell));

				// Set branchName (column index 1)
				Cell branchNameCell = row.getCell(1);
				branchMaster.setBranchName(getCellValue(branchNameCell));

				// Set cbmEmpId (column index 16)
				Cell cbmEmpIdCell = row.getCell(16);
				branchMaster.setCbmEmpId(getCellValue(cbmEmpIdCell));

				branchMasterList.add(branchMaster);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		newBranchMasterRepo.saveAll(branchMasterList);
	}

	private String getCellValue(Cell cell) {
		if (cell == null)
			return null;

		switch (cell.getCellType()) {
		case STRING:
			return cell.getStringCellValue();
		case NUMERIC:
			return String.valueOf((int) cell.getNumericCellValue()); // assuming the numeric value is integer
		case BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());
		case FORMULA:
			return cell.getCellFormula();
		default:
			return null;
		}
	}
}
