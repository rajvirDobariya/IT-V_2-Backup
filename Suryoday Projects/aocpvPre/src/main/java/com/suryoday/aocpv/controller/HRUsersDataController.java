package com.suryoday.aocpv.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.aocpv.pojo.HRUsersData;
import com.suryoday.aocpv.service.HrUSersDataService;
@Component
@RestController
@RequestMapping("/aocpv")


public class HRUsersDataController {
	Logger logger = LoggerFactory.getLogger(HRUsersDataController.class);
	@Autowired
	HrUSersDataService service;
//	@Scheduled(cron="0 55 23 * * *")
	@RequestMapping(value = "/saveHrData", method = RequestMethod.POST)
	public ResponseEntity<Object> saveHrUsersData(@RequestBody String bm) throws FileNotFoundException {
		 // path of file present on the system
//		LocalDate now = LocalDate.now();
//		String valueOfDay = String.valueOf(now.getDayOfMonth());
//		String valueOfMonth = String.valueOf(now.getMonthValue());
		JSONObject jsonObject=new JSONObject(bm);
		String valueOfDay = jsonObject.getJSONObject("Data").getString("Day");
		String valueOfMonth = jsonObject.getJSONObject("Data").getString("Month");
		String valueOfYear = jsonObject.getJSONObject("Data").getString("Year");
//		if(valueOfDay.length()==1)
//		{
//			valueOfDay="0"+valueOfDay;
//		}
//		else if(valueOfMonth.length()==1)
//		{
//			valueOfMonth="0"+valueOfMonth;
//			
//		}
        String path = "/opt/HRMastersData/ActiveDataDump_"+valueOfDay+""+valueOfMonth+""+valueOfYear+".txt";
//		String path = "D:\\HrUsersData\\ActiveDataDump_"+valueOfDay+""+valueOfMonth+""+valueOfYear+".txt";
        System.out.println(path);
//        String path3 = "D:\\HrUsersData\\new.txt"; 
        InputStream is = new FileInputStream(path);
//        InputStream is2 = new FileInputStream(path2);
        // Try block to check for exceptions
        try (Scanner sc = new Scanner(
                 is, StandardCharsets.UTF_8.name())) {
 
  
         
//        	PrintWriter os=new PrintWriter(path3);
        	if(sc.hasNextLine())
        	{
        		sc.nextLine();
        	}
 
            while (sc.hasNextLine()) {
            	String nextLine = sc.nextLine();
            	String[] split = nextLine.split("\\|");  	
            	String SPlit = Arrays.toString(split);
	        	List<String> list=Arrays.asList(split);
	        	Optional<HRUsersData> fetchUserFromHr = service.fetchUserFromHr(list.get(0));
	        	if(fetchUserFromHr.isPresent())
	        	{
	        		HRUsersData hrUsersData = fetchUserFromHr.get();
	        		hrUsersData.setUserName(list.get(1));
	        		hrUsersData.setUserEmail(list.get(2));
	        		hrUsersData.setGender(list.get(5));
	        		hrUsersData.setMobileNumber(list.get(6));
	        		hrUsersData.setUserRole(list.get(15));
	        		hrUsersData.setDesignation(list.get(17));
	        		hrUsersData.setArea(list.get(22));
	        		hrUsersData.setLocation(list.get(23));
	        		hrUsersData.setCity(list.get(28));
	        		hrUsersData.setState(list.get(27));
	        		hrUsersData.setBranchId(list.get(35));
	        		hrUsersData.setReportingManagerId(list.get(29));
	        		hrUsersData.setReportingManagerName(list.get(30));
	        		hrUsersData.setSkipLevelManagerId(list.get(32));
	        		hrUsersData.setSkipLevelManagerName(list.get(33));
	        		hrUsersData.setActiveStatus(list.get(37));
	        		hrUsersData.setDateOfResignation(list.get(38));
	        		hrUsersData.setLastWorkingDate(list.get(39));
	        		hrUsersData.setDateOfJoining(list.get(4));
	        		hrUsersData.setBusinessUnit(list.get(11));
	        		service.save(hrUsersData);
	        	}
	        	else
	        	{
	        	HRUsersData hrdata=new HRUsersData();
	        	hrdata.setUserId(list.get(0));
	        	hrdata.setUserName(list.get(1));
	        	hrdata.setUserEmail(list.get(2));
	        	hrdata.setGender(list.get(5));
	        	hrdata.setMobileNumber(list.get(6));
	        	hrdata.setUserRole(list.get(15));
	        	hrdata.setDesignation(list.get(17));
	        	hrdata.setArea(list.get(22));
	        	hrdata.setLocation(list.get(23));
	        	hrdata.setCity(list.get(28));
	        	hrdata.setState(list.get(27));
	        	hrdata.setBranchId(list.get(35));
	        	hrdata.setReportingManagerId(list.get(29));
	        	hrdata.setReportingManagerName(list.get(30));
	        	hrdata.setSkipLevelManagerId(list.get(32));
	        	hrdata.setSkipLevelManagerName(list.get(33));
	        	hrdata.setActiveStatus(list.get(37));
	        	hrdata.setDateOfResignation(list.get(38));
	        	hrdata.setLastWorkingDate(list.get(39));
	        	hrdata.setDateOfJoining(list.get(4));
	        	hrdata.setBusinessUnit(list.get(11));
	        	service.save(hrdata);
	        	}
//	        	os.println(list);
            }
//            os.close();
        }
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("status", HttpStatus.OK.toString());
		logger.debug("final response"+response);
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}
	
	@Scheduled(cron="0 55 23 * * *")
	@RequestMapping(value = "/saveHrUsersData", method = RequestMethod.POST)
	public ResponseEntity<Object> saveHrUsersData() throws FileNotFoundException {
		 // path of file present on the system
		LocalDate now = LocalDate.now();
		String valueOfDay = String.valueOf(now.getDayOfMonth());
		String valueOfMonth = String.valueOf(now.getMonthValue());
		if(valueOfDay.length()==1)
		{
			valueOfDay="0"+valueOfDay;
		}
		else if(valueOfMonth.length()==1)
		{
			valueOfMonth="0"+valueOfMonth;
			
		}
        String path = "/opt/HRMastersData/ActiveDataDump_"+valueOfDay+""+valueOfMonth+""+now.getYear()+".txt";
//		String path = "D:\\HrUsersData\\ActiveDataDump_"+valueOfDay+""+valueOfMonth+""+valueOfYear+".txt";
        System.out.println(path);
//        String path3 = "D:\\HrUsersData\\new.txt"; 
        InputStream is = new FileInputStream(path);
//        InputStream is2 = new FileInputStream(path2);
        // Try block to check for exceptions
        try (Scanner sc = new Scanner(
                 is, StandardCharsets.UTF_8.name())) {
 
  
         
//        	PrintWriter os=new PrintWriter(path3);
        	if(sc.hasNextLine())
        	{
        		sc.nextLine();
        	}
 
            while (sc.hasNextLine()) {
            	String nextLine = sc.nextLine();
            	String[] split = nextLine.split("\\|");  	
            	String SPlit = Arrays.toString(split);
	        	List<String> list=Arrays.asList(split);
	        	Optional<HRUsersData> fetchUserFromHr = service.fetchUserFromHr(list.get(0));
	        	if(fetchUserFromHr.isPresent())
	        	{
	        		HRUsersData hrUsersData = fetchUserFromHr.get();
	        		hrUsersData.setUserName(list.get(1));
	        		hrUsersData.setUserEmail(list.get(2));
	        		hrUsersData.setGender(list.get(5));
	        		hrUsersData.setMobileNumber(list.get(6));
	        		hrUsersData.setUserRole(list.get(15));
	        		hrUsersData.setDesignation(list.get(17));
	        		hrUsersData.setArea(list.get(22));
	        		hrUsersData.setLocation(list.get(23));
	        		hrUsersData.setCity(list.get(28));
	        		hrUsersData.setState(list.get(27));
	        		hrUsersData.setBranchId(list.get(35));
	        		hrUsersData.setReportingManagerId(list.get(29));
	        		hrUsersData.setReportingManagerName(list.get(30));
	        		hrUsersData.setSkipLevelManagerId(list.get(32));
	        		hrUsersData.setSkipLevelManagerName(list.get(33));
	        		service.save(hrUsersData);
	        	}
	        	else
	        	{
	        	HRUsersData hrdata=new HRUsersData();
	        	hrdata.setUserId(list.get(0));
	        	hrdata.setUserName(list.get(1));
	        	hrdata.setUserEmail(list.get(2));
	        	hrdata.setGender(list.get(5));
	        	hrdata.setMobileNumber(list.get(6));
	        	hrdata.setUserRole(list.get(15));
	        	hrdata.setDesignation(list.get(17));
	        	hrdata.setArea(list.get(22));
	        	hrdata.setLocation(list.get(23));
	        	hrdata.setCity(list.get(28));
	        	hrdata.setState(list.get(27));
	        	hrdata.setBranchId(list.get(35));
	        	hrdata.setReportingManagerId(list.get(29));
	        	hrdata.setReportingManagerName(list.get(30));
	        	hrdata.setSkipLevelManagerId(list.get(32));
	        	hrdata.setSkipLevelManagerName(list.get(33));
	        	service.save(hrdata);
	        	}
//	        	os.println(list);
            }
//            os.close();
        }
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("status", HttpStatus.OK.toString());
		logger.debug("final response"+response);
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

}
