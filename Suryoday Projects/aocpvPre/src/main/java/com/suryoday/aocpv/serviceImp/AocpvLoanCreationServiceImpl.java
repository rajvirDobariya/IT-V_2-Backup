package com.suryoday.aocpv.serviceImp;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.aocpv.exceptionhandling.TimeOutException;
import com.suryoday.aocpv.pojo.AocpCustomer;
import com.suryoday.aocpv.pojo.AocpvLoanCreation;
import com.suryoday.aocpv.repository.AocpvLoanCreationRepo;
import com.suryoday.aocpv.service.AocpvIncomeService;
import com.suryoday.aocpv.service.AocpvLoanCreationService;
import com.suryoday.aocpv.service.GetSavingDetailsService;

@Service
public class AocpvLoanCreationServiceImpl implements AocpvLoanCreationService{

	
	private static Logger logger = LoggerFactory.getLogger(AocpvLoanCreationServiceImpl.class);
	@Autowired
	AocpvLoanCreationRepo aocpvLoanCreationRepo;

	@Autowired
	GetSavingDetailsService getSavingDetailsService;
	
	@Autowired
	AocpvIncomeService aocpvIncomeService;
	
	@Override
	public void save(AocpCustomer aocpCustomer, String x_User_ID, String finalSanctionAmount) {
		
		JSONObject Header= new JSONObject();
		// Header.put("X-Correlation-ID","" );
		 Header.put("X-From-ID","TAB" );
		 Header.put("X-To-ID","Others" );
		 Header.put("X-Transaction-ID","EabeDcEE-db3c-BddD-CbD7-4bAA992c75d4" );
		 Header.put("X-User-ID",x_User_ID );
		 Header.put("X-Request-ID","TAB" );
		 
		 String AccountId="";
		 String AcctName="";
		 long customerId = aocpCustomer.getCustomerId();
		 String customerIdInString = Long.toString(customerId);
        if(!customerIdInString.equals("0")) {
	
		JSONObject   data= new JSONObject();
		 data.put("MobileNo", "");
		 data.put("AadhaarNo", "");
		 data.put("AadhaarReferenceNo", "");
		 data.put("PanNo", "");
		 data.put("CustomerNo",customerIdInString);
		 data.put("BranchCode", "");
		 data.put("ProductGroup", "CASA");
		 
		 
		 JSONObject  request= new JSONObject();
		 request.put("Data", data);
		 
		 
		 JSONObject getdetails= getSavingDetailsService.getDetails(request, Header);
		 
		System.out.println(getdetails);
		logger.debug("response"+getdetails.toString() );
		 HttpStatus  h=HttpStatus.BAD_GATEWAY;
		 
		 if(getdetails!=null) {
			 String Data2= getdetails.getString("data");
			 System.out.println("data2");
			 JSONObject Data1= new JSONObject(Data2);
			
			 System.out.println(Data1);
			 
			 if(Data1.has("Data"))
			 {
				   h= HttpStatus.OK;
				   
			 }
			 else if(Data1.has("Error"))
			 {
				 String string = Data1.getJSONObject("Error").getString("Description");
				 
				 h= HttpStatus.BAD_REQUEST;
				 
				 throw new NoSuchElementException(string);
			 }
			 logger.debug("response"+Data1);
			 //JSONArray jsonArray = Data1.getJSONArray("AccountDetails");
			 if(Data1.getJSONObject("Data").has("AccountDetails")) {
				 JSONArray jsonArray = Data1.getJSONObject("Data").getJSONArray("AccountDetails");
				 for(int n=0;n<jsonArray.length();n++) {
				 JSONObject jsonObject2 = jsonArray.getJSONObject(n);
				 JSONObject jsonObject = jsonObject2.getJSONObject("Product");
				 	String group = jsonObject.getString("Group");
				 	if(group.equals("SAV")) {
				 		AccountId = jsonObject2.getString("AccountId");
				 		AcctName = jsonObject.getString("AcctName");	
				 	
				 	}
				 } 
			 }
			 
		
			
			  
		 }
		 else
		 {
			 logger.debug("timeout");
			throw new TimeOutException("time out ");
		 }
}
		
		String adharNo= aocpvIncomeService.getByApplicationNoAndMember(aocpCustomer.getApplicationNo());
		 
		AocpvLoanCreation loanCreation=new AocpvLoanCreation();
		loanCreation.setCustomerId(aocpCustomer.getCustomerId());
		loanCreation.setApplicationNo(aocpCustomer.getApplicationNo());
		loanCreation.setCustomerName(aocpCustomer.getName());
		loanCreation.setStatus("INITIATED");
		loanCreation.setAadharNo(adharNo);
		loanCreation.setBranchId(aocpCustomer.getBranchid());
		loanCreation.setMobileNo(aocpCustomer.getMobileNo());
		loanCreation.setTargetAccount(AccountId);
		loanCreation.setAcctName(AcctName);
		loanCreation.setIsVerify("NO");
		loanCreation.setSendAgreement("NO");
		loanCreation.setUpload_aggreement_letter("NO");
		loanCreation.setUpload_sancation_letter("NO");
		 LocalDate now = LocalDate.now();
		loanCreation.setCreatedDate(now);
		
		if(finalSanctionAmount == null || finalSanctionAmount.isEmpty() ) {
			
		}
		else {
			org.json.JSONArray finalSanctionInJson =new org.json.JSONArray(finalSanctionAmount);
			String sanctionAmount=null;
			String sanctionTENURE=null;
			String sanctionROI=null;
			String sancationEMI=null;
  			for(int n=0;n<finalSanctionInJson.length();n++) {
  				JSONObject jsonObject2 = finalSanctionInJson.getJSONObject(n);
					 sanctionAmount = jsonObject2.getString("sanctionAmount");
					 sanctionTENURE = jsonObject2.getString("sanctionTENURE");
					 sanctionROI = jsonObject2.getString("sanctionROI");
					 sancationEMI = jsonObject2.getString("sancationEMI");		
  			}
			loanCreation.setSanctionedLoanAmount(sanctionAmount);
			loanCreation.setRateOfInterest(sanctionROI);
			loanCreation.setTenure(sanctionTENURE);
			loanCreation.setSancationEMI(sancationEMI);
		}
		aocpvLoanCreationRepo.save(loanCreation);
	}

	@Override
	public AocpvLoanCreation findByApplicationNo(long applicationNo) {
		Optional<AocpvLoanCreation> optional =aocpvLoanCreationRepo.getByApplicationNo(applicationNo);
		if(optional.isPresent()) {
			return optional.get();
		}
		throw new NoSuchElementException("No Record Found In LoanCreation");
	}

	@Override
	public List<AocpvLoanCreation> fetchByBranchId(String branchId , String status) {
		List<AocpvLoanCreation> list =aocpvLoanCreationRepo.fetchByBranchId(branchId,status);
		
		if(list.isEmpty()) {
			throw new NoSuchElementException("No Record Found In LoanCreation");
		}
		return list;
	}

	@Override
	public String update(AocpvLoanCreation aocpvLoanCreation) {
		
		aocpvLoanCreationRepo.save(aocpvLoanCreation);
		return "update Successfully";
	}

	@Override
	public AocpvLoanCreation getByApplicationNo(long applicationNoInLong, String status) {
		Optional<AocpvLoanCreation> optional=aocpvLoanCreationRepo.getByApplicationNoAndStatus(applicationNoInLong,status);
		if(optional.isPresent()) {
			return optional.get();
		}
		throw new NoSuchElementException("No Record Found In LoanCreation");
	}

	@Override
	public List<AocpvLoanCreation> getLoanPassbookPDFDetails(String customer) {
		long customerId=Long.parseLong(customer);
		 List<AocpvLoanCreation> list =aocpvLoanCreationRepo.fetchByCustomer(customerId);        
	        if(list.isEmpty()) {
	            throw new NoSuchElementException("No Record Found In LoanCreation");
	        }
	        return list;
	}

	@Override
	public void saveData(AocpvLoanCreation findByApplicationNo) {
		aocpvLoanCreationRepo.save(findByApplicationNo);
		
	}
	
	
	
}
