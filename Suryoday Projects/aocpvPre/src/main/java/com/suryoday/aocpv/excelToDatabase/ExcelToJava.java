package com.suryoday.aocpv.excelToDatabase;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.suryoday.aocpv.pojo.LoanDetails;



public class ExcelToJava {
	
	private static MissingCellPolicy xRow;

	public static boolean checkExcelformat(MultipartFile file) {
		
		String contentType=file.getContentType(); 
		if(contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
			return true;
		}
		else
		return false;	
	}
	
	
	public static List<LoanDetails> convertExcelToList(InputStream is){
		List<LoanDetails> list =new ArrayList<>();
		
	
		try {
			//DataFormatter formatter = new DataFormatter();
			XSSFWorkbook workbook =new XSSFWorkbook(is);
			XSSFSheet sheet =workbook.getSheet("Sheet1");
			int rowNumber=0;
			
			Iterator<Row> iterator=sheet.iterator();
			while(iterator.hasNext()) {
				Row row=iterator.next();
				//it will skip the first row
				if(rowNumber==0) {
					rowNumber++;
					continue;
				}
				int lastColumn = Math.max(row.getLastCellNum(), 90);
				//System.out.println(row.getLastCellNum());
				LoanDetails loanDetails=new LoanDetails();
				
				loanDetails.setStatus("Initiated");
				LocalDate date=LocalDate.now();
				loanDetails.setCreateDate(date);
				loanDetails.setUpdatedate(date);
				loanDetails.setIsActive("1");
				
				for(int cn=0;cn<lastColumn;cn++) {
					//if cell is empty then it will return null
					Cell cell=row.getCell(cn,xRow.RETURN_BLANK_AS_NULL );
					//skip the empty cell
					if(cell==null) {
						continue;
					}
					//int cid=cell.getColumnIndex();
					// cell=row.getCell(cn);
					switch (cn) {
					case 0:loanDetails.setCustomerID((long) cell.getNumericCellValue());
						
					break;
					case 1:loanDetails.setMemberName(cell.getStringCellValue());
					break;
					case 2:loanDetails.setMobilePhone((long) cell.getNumericCellValue());
					break;
					case 3:loanDetails.setReferenceNo(cell.getStringCellValue());
					break;
					case 4:loanDetails.setAccountNo(cell.getStringCellValue());
						
					break;
					case 5:loanDetails.setELIGIBLE_LOAN_VALUE_2YEARS((double) cell.getNumericCellValue());
					break;
					case 6:loanDetails.setAmount((double) cell.getNumericCellValue());
					break;
					case 7:loanDetails.setDisbursedAmt((double) cell.getNumericCellValue());
					break;
					case 8:loanDetails.setSumOutstandingBalanceOwn(cell.getNumericCellValue());
						
					break;
					case 9:loanDetails.setSSFB_DPD_BUCKETS(cell.getStringCellValue());
					break;
					case 10:loanDetails.setMFI_VINTAGE_SSFB((int) cell.getNumericCellValue());
					break;
					case 11:loanDetails.setSumInstallmentAmountopen(cell.getNumericCellValue());
						
					break;
					case 12:loanDetails.setMFI_VINTAGE((int) cell.getNumericCellValue());
					break;
					case 13:loanDetails.setRETAIL_BUREAU_VINTAGE((int) cell.getNumericCellValue());
					break;
					case 14:loanDetails.setRETAIL_IMPUTED_EMI_WO_CCOD_CURRENT(cell.getNumericCellValue());
					break;
					case 15:loanDetails.setMAX_CURRENT_EMI(cell.getNumericCellValue());
					break;
					case 16:loanDetails.setNUM_SECURED_ACCTS((int) cell.getNumericCellValue());
					break;
					case 17:loanDetails.setTOTAL_NUM_MFI_ACCTS((int) cell.getNumericCellValue());
					break;
					case 18:loanDetails.setNUM_UNSECURED_ACCTS((int) cell.getNumericCellValue());
					break;
					case 19:loanDetails.setNUM_HL_LAP_ACCTS((int) cell.getNumericCellValue());
					break;
					case 20:loanDetails.setNUM_BL_ACCTS((int) cell.getNumericCellValue());	
					break;
					case 21:loanDetails.setNUM_PL_ACCTS((int) cell.getNumericCellValue());
					break;
					case 22:loanDetails.setNUM_SECURED_LIVE_ACCTS((int) cell.getNumericCellValue());
						
					break;
					case 23:loanDetails.setNUM_UNSECURED_LIVE_ACCTS((int) cell.getNumericCellValue());
					break;
					case 24:loanDetails.setNUM_HL_LAP_LIVE((int) cell.getNumericCellValue());
					break;
					case 25:loanDetails.setNUM_PL_LIVE((int) cell.getNumericCellValue());
					break;
					case 26:loanDetails.setNUM_BL_LIVE((int) cell.getNumericCellValue());
					break;
					case 27:loanDetails.setNumOpenAccount((int) cell.getNumericCellValue());
					break;
					case 28:loanDetails.setNUM_CLOSED_ACCTS((int) cell.getNumericCellValue());
					break;
					case 29:loanDetails.setNUM_SECURED_CLOSED_ACCTS((int) cell.getNumericCellValue());
					break;
					case 30:loanDetails.setNUM_UNSECURED_CLOSED_ACCTS((int) cell.getNumericCellValue());
					break;
					case 31:loanDetails.setNUM_HL_LAP_CLOSED((int) cell.getNumericCellValue());			
					break;
					case 32:loanDetails.setNUM_BL_CLOSED((int) cell.getNumericCellValue());
					break;
					case 33:loanDetails.setNUM_PL_CLOSED((int) cell.getNumericCellValue());
					break;
					case 34:loanDetails.setLATEST_ACCOUNTSTATUS_MFI(cell.getStringCellValue());
					break;
					case 35:loanDetails.setLATEST_ACCOUNTSTATUS_SECURED(cell.getStringCellValue());
					break;
					case 36:loanDetails.setLATEST_ACCOUNTSTATUS_UNSECURED(cell.getStringCellValue());
					break;
					case 37:loanDetails.setLATEST_ACCOUNTSTATUS_HL_LAP(cell.getStringCellValue());
					break;
					case 38:loanDetails.setLATEST_ACCOUNTSTATUS_BL(cell.getStringCellValue());
					break;
					case 39:loanDetails.setLATEST_ACCOUNTSTATUS_PL(cell.getStringCellValue());
					break;
					case 40:loanDetails.setTOTAL_MFI_DISBURSEMENT((double) cell.getNumericCellValue());
					break;
					case 41:loanDetails.setTOTAL_DISB_SECURED((double) cell.getNumericCellValue());
					break;
					case 42:loanDetails.setTOTAL_DISB_UNSECURED((double) cell.getNumericCellValue());
						
					break;
					case 43:loanDetails.setTOTAL_DISB_HL_LAP(cell.getNumericCellValue());
						
					break;
					case 44:loanDetails.setTOTAL_DISB_BL(cell.getNumericCellValue());
					break;
					case 45:loanDetails.setTOTAL_DISB_PL(cell.getNumericCellValue());
						
					break;
					case 46:loanDetails.setSECURED_POS(cell.getNumericCellValue());
					break;
					case 47:loanDetails.setUNSECURED_POS(cell.getNumericCellValue());
					break;
					case 48:loanDetails.setHL_POS(cell.getNumericCellValue());
					break;
					case 49:loanDetails.setLAP_POS(cell.getNumericCellValue());
					break;
					case 50:loanDetails.setBL_POS(cell.getNumericCellValue());
					break;
					case 51:loanDetails.setPL_POS(cell.getNumericCellValue());
					break;
					case 52:loanDetails.setSumOutstandingBalance(cell.getNumericCellValue());
					break;
					case 53:loanDetails.setMFI_BUREAU_VINTAGE((int) cell.getNumericCellValue());
					break;
					case 54:loanDetails.setBUREAU_VINTAGE_SECURED((int) cell.getNumericCellValue());
					break;
					case 55:loanDetails.setBUREAU_VINTAGE_UNSECURED((int) cell.getNumericCellValue());
					break;
					case 56:loanDetails.setBUREAU_VINTAGE_HL_LAP((int) cell.getNumericCellValue());
						
					break;
					case 57:loanDetails.setBUREAU_VINTAGE_BL((int) cell.getNumericCellValue());
						
					break;
					case 58:loanDetails.setBUREAU_VINTAGE_PL((int) cell.getNumericCellValue());
						
					break;
					case 59:loanDetails.setMAX_MFI_EMI(cell.getNumericCellValue());
						
					break;
					case 60:loanDetails.setMAX_EMI_SECURED(cell.getNumericCellValue());
						
					break;
					case 61:loanDetails.setMAX_EMI_UNSECURED(cell.getNumericCellValue());
					break;
					case 62:loanDetails.setMAX_EMI_HL_LAP(cell.getNumericCellValue());
					break;
					case 63:loanDetails.setMAX_EMI_BL(cell.getNumericCellValue());
					break;
					case 64:loanDetails.setMAX_EMI_PL(cell.getNumericCellValue());
					break;
					case 65:loanDetails.setMAX_LOAN_TENURE_SECURED((int) cell.getNumericCellValue());
					break;
					case 66:loanDetails.setMAX_LOAN_TENURE_UNSECURED((int) cell.getNumericCellValue());
					break;
					case 67:loanDetails.setMAX_LOAN_TENURE_HL_LAP((int) cell.getNumericCellValue());
					break;
					case 68:loanDetails.setMAX_LOAN_TENURE_HL_LAP((int) cell.getNumericCellValue());
					break;
					case 69:loanDetails.setMAX_LOAN_TENURE_BL((int) cell.getNumericCellValue());
					break;
					case 70:loanDetails.setLatestCloseDate(cell.getDateCellValue());
					break;
					case 71:loanDetails.setLATEST_CLOSEDATE_HL_LAP(cell.getDateCellValue());
					break;
					case 72:loanDetails.setLATEST_CLOSEDATE_SECURED(cell.getDateCellValue());
					break;
					case 73:loanDetails.setLATEST_CLOSEDATE_UNSECURED(cell.getDateCellValue());
					break;
					case 74:loanDetails.setLATEST_CLOSEDATE_PL(cell.getDateCellValue());
					break;
					case 75:loanDetails.setLATEST_CLOSEDATE_BL(cell.getDateCellValue());
					break;
					case 76:loanDetails.setMAX_LOAN_AMOUNT_MFI(cell.getNumericCellValue());
					break;
					case 77:loanDetails.setMAX_LOAN_AMOUNT_SECURED(cell.getNumericCellValue());
					break;
					case 78:loanDetails.setMAX_LOAN_AMOUNT_UNSECURED(cell.getNumericCellValue());
					break;
					case 79:loanDetails.setMAX_LOAN_AMOUNT_HL_LAP(cell.getNumericCellValue());
					break;
					case 80:loanDetails.setMAX_LOAN_AMOUNT_BL(cell.getNumericCellValue());
					break;
					case 81:loanDetails.setMAX_LOAN_AMOUNT_PL(cell.getNumericCellValue());
					break;
					case 82:loanDetails.setBranchCode((int) cell.getNumericCellValue());
					break;
					case 83:loanDetails.setBranchId((long) cell.getNumericCellValue());
					break;
					case 84:loanDetails.setCurrent_CycleNo((int) cell.getNumericCellValue());
					break;
					case 85:loanDetails.setBranch_Name(cell.getStringCellValue());
					break;
					case 86:loanDetails.setState(cell.getStringCellValue());
					break;
					case 87:loanDetails.setCITY(cell.getStringCellValue());
					break;
					case 88:loanDetails.setDISTRICT_NAME(cell.getStringCellValue());
					break;
					case 89:loanDetails.setCentreName(cell.getStringCellValue());
					break;
					default:
						break;
						
						}
				
				
				}
		
			//	System.out.println(loanDetails);
				if(loanDetails.getReferenceNo()!= null) {
					list.add(loanDetails);
				}
					
		
		}
		}
		catch (Exception e) {
			e.fillInStackTrace();
		}
		
		
		return list;
	}
		
		
		
}
