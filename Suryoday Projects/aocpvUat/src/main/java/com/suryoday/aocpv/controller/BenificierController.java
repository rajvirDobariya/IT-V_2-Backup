package com.suryoday.aocpv.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.aocpv.exceptionhandling.ResourceNotFoundException;
import com.suryoday.aocpv.pojo.BenificiaryInfo;
import com.suryoday.aocpv.pojo.Benificier;
import com.suryoday.aocpv.service.BenificierService;

@RestController

public class BenificierController {

	private static final Logger logger = LoggerFactory.getLogger(BenificierController.class);

	@Autowired
	BenificierService benificierService;

	@PostMapping("/getalldata")
	public List<Benificier> getalldata() throws ResourceNotFoundException {

		System.out.println("welcome");

		return benificierService.fetchAll();
	}

	@RequestMapping(value = "/customer/{customerid}/beneficiary/view/{accounttype}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Object> getalldataByCif(@PathVariable(name = "customerid") String customerid,
			@PathVariable(name = "accounttype") String accounttype,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {

		System.out.println("welcome");
		System.out.println("welcome".getBytes());
		String acc_type = "";
		if (accounttype.equals("rtgs")) {
			acc_type = "3";
		} else if (accounttype.equals("intra")) {
			acc_type = "2";
		} else {
			return new ResponseEntity<Object>("Invalid Account Type", HttpStatus.BAD_REQUEST);

		}

		List<Benificier> l = new ArrayList<Benificier>();
		List<BenificiaryInfo> BenificiaryInfo1 = new ArrayList<BenificiaryInfo>();

		// l= benificierService.FindAllCustomer(customerid,accounttype);
		l = benificierService.viewcustmerbytype(customerid, acc_type);
		if (l.size() == 0) {
			JSONObject Response = new JSONObject();
			JSONObject Error = new JSONObject();
			Error.put("Code", "100");
			Error.put("Description", "No Record Found");
			Response.put("Error", Error);
			return new ResponseEntity<Object>(Response, HttpStatus.OK);
		} else {

			for (int i = 0; i < l.size(); i++) {
				Benificier b = l.get(i);

				BenificiaryInfo b1 = new BenificiaryInfo();

//				if(b.getType().equals("2"))
//				{
//					
//				}
//				else if(b.getType().equals("3"))
//				{
//					
//				}

				b1.setBeneficiaryId(b.getAccount());
				b1.setNickName(b.getNickName());
				b1.setBeneficiaryName(b.getName());
				b1.setSequence(Integer.parseInt(b.getSequence()));
				b1.setBeneficiaryEmailId(b.getEmailId());
				b1.setStatus(b.getStatus());
				b1.setBeneficiaryMobileNumber(b.getMobileNumber());
				b1.setBeneficiaryMaxLimit(b.getMaxLimit());
				b1.setBeneficiaryBank(b.getBankName());
				b1.setBeneficiaryBankCity(b.getBankCity());
				b1.setBeneficiaryBranch(b.getBankBranch());
				b1.setBeneficiaryBankIfsc(b.getBankIfsc());

				BenificiaryInfo1.add(b1);

			}

			JSONObject BenificiaryInfo = new JSONObject();
			BenificiaryInfo.put("BenificiaryInfo", BenificiaryInfo1);
			BenificiaryInfo.put("TransactionCode", "00");

			JSONObject Data = new JSONObject();
			Data.put("Data", BenificiaryInfo);

			return new ResponseEntity<Object>(Data, HttpStatus.OK);

		}

	}

	@RequestMapping(value = "/customer/{customerid}/beneficiary/add", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<Object> insertdata(@RequestBody String bm,
			@PathVariable(name = "customerid") String customerid,
			@RequestHeader(value = "User-Agent", required = true) String userAgent,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {

		if (X_From_ID.equals("IBR") || X_From_ID.equals("MB")) {
			// logger.info("User agent is: {}", userAgent);
			System.out.println("User agent is: {}" + userAgent);

			System.out.println("insert" + bm.toString());
			System.out.println("api key " + customerid);

			org.json.JSONObject s = benificierService.saveBenificier(bm.toString(), customerid);

			return new ResponseEntity<Object>(s.toString(), HttpStatus.OK);

		} else {
			return new ResponseEntity<Object>("Invalid X_From_ID", HttpStatus.BAD_REQUEST);
		}

		// return s.toString();
	}

	@RequestMapping(value = "/customer/{customerid}/beneficiary/modify", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<Object> modifydata(@RequestBody String bm,
			@PathVariable(name = "customerid") String customerid,
			@RequestHeader(value = "User-Agent", required = true) String userAgent,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {

		if (X_From_ID.equals("IBR") || X_From_ID.equals("MB")) {
			// logger.info("User agent is: {}", userAgent);
			System.out.println("User agent is: {}" + userAgent);

			System.out.println("modify" + bm.toString());
			System.out.println("api key " + customerid);

			org.json.JSONObject s = benificierService.modifyBenificier(bm.toString(), customerid);

			return new ResponseEntity<Object>(s.toString(), HttpStatus.OK);

		} else {
			return new ResponseEntity<Object>("Invalid X_From_ID", HttpStatus.BAD_REQUEST);
		}

		// return s.toString();
	}

	@RequestMapping(value = "/delete/beneficiary", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> deletebeneficiary(@RequestBody String bm,
			@RequestHeader(value = "User-Agent", required = true) String userAgent,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {

		if (X_From_ID.equals("IBR") || X_From_ID.equals("MB")) {
			// logger.info("User agent is: {}", userAgent);

			System.out.println("delete" + bm.toString());

			org.json.JSONObject s = benificierService.deleteBenificier(bm.toString());

			return new ResponseEntity<Object>(s.toString(), HttpStatus.OK);

		} else {
			return new ResponseEntity<Object>("Invalid X_From_ID", HttpStatus.BAD_REQUEST);
		}
	}

	// @Scheduled(cron = "10 15 7 * * *")

	@Scheduled(cron = "0 0/30 * * * ?") // every 30 min
	public void updatbenifierstatus() {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		System.out.println("Time" + dateTimeFormatter.format(LocalDateTime.now()));

		benificierService.modifyBenificierStatus();

	}

}
