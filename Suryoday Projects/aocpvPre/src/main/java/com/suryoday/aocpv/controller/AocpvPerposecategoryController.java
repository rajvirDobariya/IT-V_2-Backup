package com.suryoday.aocpv.controller;

import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.aocpv.pojo.AocpvPerposecategory;
import com.suryoday.aocpv.service.AocpvPerposecategoryService;

@RestController
@RequestMapping("/aocpv")
public class AocpvPerposecategoryController {
	
	@Autowired
	AocpvPerposecategoryService aocpvPerposecategoryService;
	
	private static Logger logger = LoggerFactory.getLogger(AocpvPerposecategoryController.class);
	
	 @RequestMapping(value="/fetchByCategoryId", method = RequestMethod.POST,produces = "application/json")
		public ResponseEntity<List<AocpvPerposecategory>> fecthByCategoryId(@RequestBody String jsonRequest){
		 JSONObject jsonObject=new JSONObject(jsonRequest);
		 String categoryId = jsonObject.getString("categoryId");
		 List<AocpvPerposecategory> listOfCategory = aocpvPerposecategoryService.fetchByCategoryId(categoryId);
		 if(listOfCategory.isEmpty()) {
			 return new ResponseEntity("InValid Category ID",HttpStatus.BAD_REQUEST);
		 }
		 logger.debug("Final Response" + listOfCategory);
		 return new ResponseEntity<>(listOfCategory, HttpStatus.OK);
		 
	 }

}
