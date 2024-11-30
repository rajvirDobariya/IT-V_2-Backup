package com.suryoday.twowheeler.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.aocpv.exceptionhandling.EmptyInputException;
import com.suryoday.twowheeler.pojo.AssetMaster;
import com.suryoday.twowheeler.pojo.DealerByPincode;
import com.suryoday.twowheeler.pojo.DealerMaster;
import com.suryoday.twowheeler.pojo.NtbSchemeMaster;
import com.suryoday.twowheeler.pojo.SchemeMaster;
import com.suryoday.twowheeler.pojo.TWModelMaster;
import com.suryoday.twowheeler.pojo.TwowheelerBankCode;
import com.suryoday.twowheeler.pojo.TwowheelerDetailesTable;
import com.suryoday.twowheeler.repository.PincodeMasterRepository;
import com.suryoday.twowheeler.service.TwoWheelerMastersService;
import com.suryoday.twowheeler.service.TwowheelerBankCodeService;
import com.suryoday.twowheeler.service.TwowheelerDetailsService;

@Component
@RestController
@RequestMapping(value = "/twowheeler")
public class TwoWheelerMastersController {

	private static Logger logger = LoggerFactory.getLogger(TwoWheelerMastersController.class);
	@Autowired
	TwoWheelerMastersService twowheelermastersservice;

	@Autowired
	TwowheelerDetailsService twowheelerDetailsService;

	@Autowired
	private TwowheelerBankCodeService twowheelerBankCodeService;

	@Autowired
	private PincodeMasterRepository pincodeMasterRepository;

	@RequestMapping(value = "/fetchModelMaster", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> fetchModelMaster(
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		System.out.println("fetchModelMaster start");

		List<TWModelMaster> list = twowheelermastersservice.fetchModelMaster();
		System.out.println("db Call end" + list);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Data", list);
		System.out.println(list);
		System.out.println("final response" + response.toString());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/fetchAllSchemes", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> fetchAllSchemes(
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		List<SchemeMaster> list = twowheelermastersservice.fetchAllSchemes();
		JSONArray array = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			SchemeMaster schemeMaster = list.get(i);
			String schemeCode = schemeMaster.getSchemeCode();
			String schemeDescription = schemeMaster.getSchemeDescription();
			JSONObject resp = new JSONObject();
			resp.put("SchemeCode", schemeCode);
			resp.put("SchemeDesc", schemeDescription);
			array.put(resp);

		}
		System.out.println(array);
		System.out.println("db Call end" + list);
//		System.out.println("Resp"+data);
		JSONObject response = new JSONObject();
		response.put("Data", array);
		return new ResponseEntity<Object>(response.toString(), HttpStatus.OK);
	}

	@RequestMapping(value = "/fetchBySchemeName", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> fetchByScheme(@RequestBody String jsonRequest,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		logger.debug("request" + jsonRequest);
		if (jsonRequest.isEmpty()) {
			logger.debug("request is empty" + jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}

		String schemeName = jsonObject.getJSONObject("Data").getString("SchemeName");

		SchemeMaster master = twowheelermastersservice.fetchBySchemeCode(schemeName);
		System.out.println("db Call end" + master);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Data", master);
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

//	@RequestMapping(value="/fetchByDealerCode", method = RequestMethod.POST,produces = "application/json")
//	public ResponseEntity<Object> fetchByDealerCode(@RequestBody String jsonRequest ,
//			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
//			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
//		
//		JSONObject jsonObject=new JSONObject(jsonRequest);
//		logger.debug("request"+jsonRequest);
//		if(jsonRequest.isEmpty()) {
//			logger.debug("request is empty"+jsonRequest);
//			throw new EmptyInputException("Input cannot be empty");
//		}
//		
//		String dealerCode = jsonObject.getJSONObject("Data").getString("DealerCode");
//		List<DealerMaster> list=twowheelermastersservice.fetchByDealerCode(dealerCode);
//		System.out.println("db Call end" + list);
//		org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
//		response.put("Data", list.get(0));
//		return new ResponseEntity<Object>(response,HttpStatus.OK);
//	}

	@RequestMapping(value = "/fetchAllModels", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> fetchAllModels(@RequestBody String jsonRequest,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		logger.debug("request" + jsonRequest);
		if (jsonRequest.isEmpty()) {
			logger.debug("request is empty" + jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}

		String manufacturerName = jsonObject.getJSONObject("Data").getString("MaufactureName");
		String vehicleType = jsonObject.getJSONObject("Data").getString("VehicleType");
		List<TWModelMaster> list = twowheelermastersservice.fetchByVehicle(manufacturerName, vehicleType);
		JSONArray array = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			TWModelMaster twModelMaster = list.get(i);
			String modelId = twModelMaster.getModelId();
			String modelName = twModelMaster.getModelName();
			String variantName = twModelMaster.getVariantName();
			String variantId = twModelMaster.getVariantId();
			String vehicleName = "" + modelName + " " + variantName + "";
			JSONObject resp = new JSONObject();
			resp.put("ModelId", modelId);
			resp.put("ModelName", modelName);
			resp.put("VehicleName", vehicleName);
			resp.put("VariantId", variantId);
			array.put(resp);
		}
		System.out.println("db Call end" + list);
		JSONObject response = new JSONObject();
		response.put("Data", array);
		return new ResponseEntity<Object>(response.toString(), HttpStatus.OK);
	}

	@RequestMapping(value = "/fetchByVariant", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> fetchByVariant(@RequestBody String jsonRequest,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		logger.debug("request" + jsonRequest);
		if (jsonRequest.isEmpty()) {
			logger.debug("request is empty" + jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}

		String VarinantName = jsonObject.getJSONObject("Data").getString("VarinantName");
		AssetMaster master = twowheelermastersservice.fetchByVariant(VarinantName);
		// JSONArray array=new JSONArray(list);
		// System.out.println("db Call end" + list);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Data", master);
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/fetchAllDealers", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> fetchAllDealers(@RequestBody String jsonRequest,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		logger.debug("request" + jsonRequest);
		if (jsonRequest.isEmpty()) {
			logger.debug("request is empty" + jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}

		String manufacturerName = jsonObject.getJSONObject("Data").getString("MaufactureName");
		List<DealerMaster> list = twowheelermastersservice.fetchAllDealers(manufacturerName);
		JSONArray array = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			DealerMaster dealerMaster = list.get(i);
			String dealerCode = dealerMaster.getDealerCode();
			String dealerName = dealerMaster.getDealerName();
			String branchDescription = dealerMaster.getBranchDescription();
			JSONObject resp = new JSONObject();
			resp.put("DealerCode", dealerCode);
			resp.put("DealerName", dealerName);
			resp.put("BranchDescription", branchDescription);
			array.put(resp);
		}
		System.out.println("db Call end" + list);
		JSONObject response = new JSONObject();
		response.put("Data", array);
		return new ResponseEntity<Object>(response.toString(), HttpStatus.OK);
	}

	@RequestMapping(value = "/fetchManufacture", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> fetchManufacture(
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID) {

		List<String> list = twowheelermastersservice.fetchManufacture();
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Data", list);
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/fetchScheme", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> fetchSchemeDesc(
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID) {

		List<String> list = twowheelermastersservice.fetchSchemeDesc();
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Data", list);
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/fetchModel", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> fetchModel(@RequestBody String jsonRequest,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID) {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		String manufacturerName = jsonObject.getJSONObject("Data").getString("MaufactureName");

		List<String> list = twowheelermastersservice.fetchModel(manufacturerName);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Data", list);
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/fetchDealerBypincode", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> fetchDealerBypincode(@RequestBody String jsonRequest,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		JSONObject jsonObject = new JSONObject(jsonRequest);
		String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
		String manufacturer = jsonObject.getJSONObject("Data").getString("manufacturer");

//		String address=twowheelerDetailsService.fetchAddress(applicationNo);
//		String pincode="";
//		if(address != null) {
//			org.json.JSONArray addressArray =new org.json.JSONArray(address);
//			for(int n=0;n<addressArray.length();n++) {
//				JSONObject addressInJson = addressArray.getJSONObject(n);
//				if(addressInJson.getString("addressType").equalsIgnoreCase("CURRENT ADDRESS")) {
//					 pincode = addressInJson.getString("pincode");
//				}
//			}
//		}
		TwowheelerDetailesTable twowheelerDetailesTable = twowheelerDetailsService.getByApplication(applicationNo);
		List<DealerByPincode> listResponse = new ArrayList<>();
		List<DealerByPincode> list = twowheelermastersservice.fetchAllDealer(twowheelerDetailesTable.getSalesBranchId(),
				manufacturer);
		for (DealerByPincode dealerByPincode : list) {
			String branchIds = dealerByPincode.getBranchIdArray();
			if (branchIds != null) {
				org.json.JSONArray branchIdArray = new org.json.JSONArray(branchIds);
				for (int n = 0; n < branchIdArray.length(); n++) {
					String branchId = branchIdArray.getString(n);
					if (branchId.equals(twowheelerDetailesTable.getSalesBranchId())) {
						listResponse.add(dealerByPincode);
					}
				}
			}
		}

		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Data", listResponse);
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/fetchByDealerCode", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> fetchByDealerCode(@RequestBody String jsonRequest,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		JSONObject jsonObject = new JSONObject(jsonRequest);
		String DealerCode = jsonObject.getJSONObject("Data").getString("DealerCode");
		List<DealerMaster> list = twowheelermastersservice.fetchByDealerCode(DealerCode);

		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Data", list);
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/fetchVarient", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> fetchVarient(@RequestBody String jsonRequest,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID) {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		String modelName = jsonObject.getJSONObject("Data").getString("modelName");

		List<String> list = twowheelermastersservice.fetchVarient(modelName);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Data", list);
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/getBankCode", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> getBankCode(
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID) {

		List<TwowheelerBankCode> list = twowheelerBankCodeService.getAll();
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Data", list);
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/fetchSchemeNtb", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> fetchSchemeNtb(@RequestBody String jsonRequest,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID) {
		JSONObject jsonObject = new JSONObject(jsonRequest);
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		TwowheelerDetailesTable twowheelerDetails = twowheelerDetailsService.getByApplication(applicationNo);
		String breCustomerCategory = twowheelerDetails.getBreCustomerCategory();
		String model = twowheelerDetails.getModel();

		List<String> list = twowheelermastersservice.getSchemeNtb(model, breCustomerCategory);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Data", list);
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/fetchBySchemeNameNtb", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> fetchBySchemeNameNtb(@RequestBody String jsonRequest,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		logger.debug("request" + jsonRequest);
		if (jsonRequest.isEmpty()) {
			logger.debug("request is empty" + jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}

		String schemeName = jsonObject.getJSONObject("Data").getString("SchemeName");

		NtbSchemeMaster master = twowheelermastersservice.fetchBySchemeCodeNtb(schemeName);
		System.out.println("db Call end" + master);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Data", master);
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

}
