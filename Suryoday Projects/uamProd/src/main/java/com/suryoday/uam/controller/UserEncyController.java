package com.suryoday.uam.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.uam.exceptionhandling.EmptyInputException;
import com.suryoday.uam.others.AppzillonAESUtils;
import com.suryoday.uam.others.Crypt;
import com.suryoday.uam.pojo.BranchInfo;
import com.suryoday.uam.pojo.BranchListResponse;
import com.suryoday.uam.pojo.BranchMaster;
import com.suryoday.uam.pojo.MainResponseDto;
import com.suryoday.uam.pojo.PageGroup;
import com.suryoday.uam.pojo.PermissionDto;
import com.suryoday.uam.pojo.PermissionResponseDto;
import com.suryoday.uam.pojo.User;
import com.suryoday.uam.pojo.UserRole;
import com.suryoday.uam.pojo.UserRoleDto;
import com.suryoday.uam.repository.PageGroupRepository;
import com.suryoday.uam.repository.UserRepository;
import com.suryoday.uam.repository.UserRoleRepository;
import com.suryoday.uam.service.BranchMappingService;
import com.suryoday.uam.service.UserService;


@RestController
@RequestMapping("/sarathi")
public class UserEncyController {
	@Autowired
	private UserService userService;

	@Autowired
	private UserRoleRepository userRoleRepository;

	@Autowired
	private PageGroupRepository pageGroupRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BranchMappingService branchMappingService;
	
	private static Logger logger = LoggerFactory.getLogger(UserEncyController.class);

	@RequestMapping(value = "/addUserEncy", method = RequestMethod.POST)
	public ResponseEntity<?> addUser(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			HttpServletRequest request
			) throws Exception {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		    if (sessionId == true ) { 
			 String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
		String key = X_Session_ID;
		String decryptContainerString = Crypt.decrypt(encryptString,X_encode_ID);
//			JSONObject jsonObject = new JSONObject(decryptContainerString);
		User addUser = userService.addUser(decryptContainerString);
		logger.debug("Post Request" + jsonRequest);
		// logger.debug("Response came from DB" + addUser);
		if (addUser == null) {
			return new ResponseEntity("Please Enter Vaild Details", HttpStatus.BAD_REQUEST);
		}
		MainResponseDto mainResponseDto = new MainResponseDto();
		mainResponseDto.setUserId(addUser.getUserId());
		mainResponseDto.setAdmin(addUser.isAdmin());
		mainResponseDto.setBranchId(addUser.getBranchId());
		mainResponseDto.setEmpId(addUser.getEmpId());
		mainResponseDto.setIsActive(addUser.getIsActive());
		mainResponseDto.setMobileNo(addUser.getMobileNo());
		mainResponseDto.setUserName(addUser.getUserName());
		mainResponseDto.setBranchName(addUser.getBranchName());
		mainResponseDto.setDesignation(addUser.getDesignation());
		mainResponseDto.setCity(addUser.getCity());
		mainResponseDto.setState(addUser.getState());
		mainResponseDto.setArea(addUser.getArea());
		mainResponseDto.setCreatedBy(addUser.getCreatedBy());
		mainResponseDto.setCreatedDate(addUser.getCreatedDate());
		mainResponseDto.setUpdatedDate(addUser.getUpdatedDate());
		mainResponseDto.setUpdatedBy(addUser.getUpdatedBy());
		mainResponseDto.setLastAccessTime(addUser.getLastAccessTime());
		mainResponseDto.setUserRoleName(addUser.getUserRole().getUserName());
		
		if(addUser.getBranchIdArray() != null) {
			List<BranchInfo> list=new ArrayList<>();
			org.json.JSONArray branchIdArray =new org.json.JSONArray(addUser.getBranchIdArray());
			for(int n=0;n<branchIdArray.length();n++) {
				JSONObject jsonObject = branchIdArray.getJSONObject(n);
				String branchId = jsonObject.getString("branchId");
				String branchName = jsonObject.getString("branchName");
				BranchInfo branchInfo=new BranchInfo(branchId, branchName);
				list.add(branchInfo);
			}
			mainResponseDto.setListofBranches(list);
		}
		Optional<UserRole> findById2 = userRoleRepository.findById(addUser.getUserRole().getId());
		if (findById2.isPresent()) {
			UserRole userRole2 = findById2.get();
			String pageGroupId = userRole2.getPageGroupId();
			List<PermissionDto> dtos = new ArrayList<>();
			JSONArray array = new JSONArray(pageGroupId);
			for (int i = 0; i < array.length(); i++) {
				Object object = array.get(i);
				String string = object.toString();
				Long sid = Long.parseLong(string);
				if (sid != null) {
					Optional<PageGroup> findById3 = pageGroupRepository.findById(sid);
					if (findById3.isPresent()) {
						PageGroup pageGroup = findById3.get();
						PermissionDto dto = new PermissionDto();
						dto.setIndex(pageGroup.getId());
						dto.setName(pageGroup.getModuleGroup().getModelGroupName());
						dto.setTitle(pageGroup.getPageName());
						dto.setPage(pageGroup.getPage());
						dto.setChannel(pageGroup.getChannel());
						dtos.add(dto);
					}
				}

			}
			mainResponseDto.setPermissions(dtos);
			logger.debug("Response" + mainResponseDto);
		}
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		JSONObject j=new JSONObject(mainResponseDto);
		response.put("Data", j);
		String	data = response.toString();
 		String encryptString2 = Crypt.encrypt(data, X_encode_ID);
 		org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
 		data2.put("value", encryptString2);
 		org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
 		data3.put("Data", data2);
 		return new ResponseEntity<Object>(data3, HttpStatus.OK);
//		return new ResponseEntity<>(mainResponseDto, HttpStatus.CREATED);
		    } 
		    else {
		        org.json.JSONObject data2 = new org.json.JSONObject();
		        data2.put("value", "SessionId is expired or Invalid sessionId");
		        org.json.JSONObject data3 = new org.json.JSONObject();
		        data3.put("Error", data2);
		        logger.debug("SessionId is expired or Invalid sessionId");
		        return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		    } 
	}

	@RequestMapping(value = "/updateUserEncy/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Object> updateUser(@PathVariable("id") String id, @RequestBody String jsonRequest,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			HttpServletRequest request)
			throws Exception {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		    if (sessionId == true ) {
		   
			 String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
		String key = X_Session_ID;
		String decryptContainerString = Crypt.decrypt(encryptString,X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
		User updateUser = userService.updateUser(id, decryptContainerString);
		if (updateUser == null) {
			return new ResponseEntity("Please Enter Vaild Details", HttpStatus.BAD_REQUEST);
		}
		MainResponseDto mainResponseDto = new MainResponseDto();
		mainResponseDto.setUserId(updateUser.getUserId());
		mainResponseDto.setAdmin(updateUser.isAdmin());
		mainResponseDto.setBranchId(updateUser.getBranchId());
		mainResponseDto.setEmpId(updateUser.getEmpId());
		mainResponseDto.setIsActive(updateUser.getIsActive());
		mainResponseDto.setMobileNo(updateUser.getMobileNo());
		mainResponseDto.setUserName(updateUser.getUserName());
		mainResponseDto.setBranchName(updateUser.getBranchName());
		mainResponseDto.setDesignation(updateUser.getDesignation());
		mainResponseDto.setCity(updateUser.getCity());
		mainResponseDto.setState(updateUser.getState());
		mainResponseDto.setArea(updateUser.getArea());
		mainResponseDto.setCreatedBy(updateUser.getCreatedBy());
		mainResponseDto.setCreatedDate(updateUser.getCreatedDate());
		mainResponseDto.setUpdatedDate(updateUser.getUpdatedDate());
		mainResponseDto.setUpdatedBy(updateUser.getUpdatedBy());
		mainResponseDto.setUserRoleName(updateUser.getUserRole().getUserName());
		mainResponseDto.setLastAccessTime(updateUser.getLastAccessTime());
		Optional<UserRole> findById2 = userRoleRepository.findById(updateUser.getUserRole().getId());
		if (findById2.isPresent()) {
			UserRole userRole2 = findById2.get();
			String pageGroupId = userRole2.getPageGroupId();
			List<PermissionDto> dtos = new ArrayList<>();
			JSONArray array = new JSONArray(pageGroupId);
			for (int i = 0; i < array.length(); i++) {
				Object object = array.get(i);
				String string = object.toString();
				Long sid = Long.parseLong(string);
				if (sid != null) {
					Optional<PageGroup> findById3 = pageGroupRepository.findById(sid);
					if (findById3.isPresent()) {
						PageGroup pageGroup = findById3.get();
						// String channel =null;
						// if( channel =="MB") {
						// }
						// pageGroupRepository.findByChannel(channel);
						PermissionDto dto = new PermissionDto();
						dto.setIndex(pageGroup.getId());
						dto.setName(pageGroup.getModuleGroup().getModelGroupName());
						dto.setTitle(pageGroup.getPageName());
						dto.setPage(pageGroup.getPage());
						dto.setChannel(pageGroup.getChannel());
						dtos.add(dto);

					}
				}
			}
			mainResponseDto.setPermissions(dtos);
		}
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		JSONObject j=new JSONObject(mainResponseDto);
		response.put("Data", j);
		String	data = response.toString();
 		String encryptString2 = Crypt.encrypt(data, X_encode_ID);
 		org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
 		data2.put("value", encryptString2);
 		org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
 		data3.put("Data", data2);
 		return new ResponseEntity<Object>(data3, HttpStatus.OK);
		    } 
		    else {
		        org.json.JSONObject data2 = new org.json.JSONObject();
		        data2.put("value", "SessionId is expired or Invalid sessionId");
		        org.json.JSONObject data3 = new org.json.JSONObject();
		        data3.put("Error", data2);
		        logger.debug("SessionId is expired or Invalid sessionId");
		        return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		    } 	
	}

	@RequestMapping(value = "/deleteUserEncy", method = RequestMethod.DELETE)
	public ResponseEntity<User> deleteUser(@RequestHeader(name = "X-User-ID", required = true) String X_User_ID) {
		int deleteUser = userService.deleteUser(X_User_ID);
		logger.debug("Delete Request with Id" + X_User_ID);
		logger.debug("Response " + deleteUser);
		if (deleteUser == 0) {
			return new ResponseEntity("Please Enter Vaild Details", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity(deleteUser, HttpStatus.OK);
	}

	@RequestMapping(value = "/fetchAllEncy",method = RequestMethod.GET)
	public ResponseEntity<Object> fetchAll(
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
				HttpServletRequest request) throws Exception 
	{
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
	//	boolean sessionId = true;
		    if (sessionId == true ) {
		    	// userService.getSessionId(X_User_ID, request); 
		List<User> fetchAll = userService.fetchAll();
		logger.debug("Get All Request");
		if (fetchAll == null) {
			return new ResponseEntity("Please Enter Vaild Details", HttpStatus.BAD_REQUEST);
		}
		List<MainResponseDto> mainResponseDtos = new ArrayList<>();
		for (User user : fetchAll) {
			MainResponseDto mainResponseDto = new MainResponseDto();
			mainResponseDto.setUserId(user.getUserId());
			mainResponseDto.setAdmin(user.isAdmin());
			mainResponseDto.setBranchId(user.getBranchId());
			mainResponseDto.setEmpId(user.getEmpId());
			mainResponseDto.setIsActive(user.getIsActive());
			mainResponseDto.setMobileNo(user.getMobileNo());
			mainResponseDto.setUserName(user.getUserName());
			mainResponseDto.setBranchName(user.getBranchName());
			mainResponseDto.setDesignation(user.getDesignation());
			mainResponseDto.setCity(user.getCity());
			mainResponseDto.setState(user.getState());
			mainResponseDto.setArea(user.getArea());
			mainResponseDto.setCreatedBy(user.getCreatedBy());
			mainResponseDto.setCreatedDate(user.getCreatedDate());
			mainResponseDto.setUpdatedDate(user.getUpdatedDate());
			mainResponseDto.setUpdatedBy(user.getUpdatedBy());
			mainResponseDto.setUserRoleName(user.getUserRole().getUserName());
			mainResponseDto.setLastAccessTime(user.getLastAccessTime());
			Optional<UserRole> findById2 = userRoleRepository.findById(user.getUserRole().getId());
			if (findById2.isPresent()) {
				UserRole userRole2 = findById2.get();
				String pageGroupId = userRole2.getPageGroupId();
				List<PermissionDto> dtos = new ArrayList<>();
				JSONArray array = new JSONArray(pageGroupId);
				for (int i = 0; i < array.length(); i++) {
					Object object = array.get(i);
					String string = object.toString();
					Long sid = Long.parseLong(string);
					if (sid != null) {
						Optional<PageGroup> findById3 = pageGroupRepository.findById(sid);
						if (findById3.isPresent()) {
							PageGroup pageGroup = findById3.get();
							PermissionDto dto = new PermissionDto();
							dto.setIndex(pageGroup.getId());
							dto.setName(pageGroup.getModuleGroup().getModelGroupName());
							dto.setTitle(pageGroup.getPageName());
							dto.setPage(pageGroup.getPage());
							dto.setChannel(pageGroup.getChannel());
							dtos.add(dto);
						}
					}
				}
				mainResponseDto.setPermissions(dtos);
				//String sessionId1 = userService.getSessionId(user.getUserId(), request);
				mainResponseDto.setSERVER_TOKEN("BDHDHFDDFH6483748");
			}

			mainResponseDtos.add(mainResponseDto);
			logger.debug("Response" + mainResponseDtos);
		}
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		JSONArray j= new JSONArray(mainResponseDtos);
		response.put("Data", j);
		String	data = response.toString();
 		String encryptString2 = Crypt.encrypt(data, X_encode_ID);
 		org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
 		data2.put("value", encryptString2);
 		org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
 		data3.put("Data", data2);
 		return new ResponseEntity<Object>(data3, HttpStatus.OK);
		//return new ResponseEntity<>(mainResponseDtos, HttpStatus.OK);
		    } 
		    else {
		        org.json.JSONObject data2 = new org.json.JSONObject();
		        data2.put("value", "SessionId is expired or Invalid sessionId");
		        org.json.JSONObject data3 = new org.json.JSONObject();
		        data3.put("Error", data2);
		        logger.debug("SessionId is expired or Invalid sessionId");
		        return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		    } 
	}

	@RequestMapping(value = "/fetchByIdEncy/{id}", method = RequestMethod.GET)
	public ResponseEntity<MainResponseDto> fetchById(@PathVariable("id") String id, HttpServletRequest request) {
		User fetchById = userService.fetchById(id);
		logger.debug("Get Request By Id" + id);
		// logger.debug("Request came from DB" + fetchById);
		if (fetchById == null) {
			return new ResponseEntity("Please Enter Vaild Details", HttpStatus.BAD_REQUEST);
		}
		MainResponseDto mainResponseDto = new MainResponseDto();
		mainResponseDto.setUserId(fetchById.getUserId());
		mainResponseDto.setAdmin(fetchById.isAdmin());
		mainResponseDto.setBranchId(fetchById.getBranchId());
		mainResponseDto.setEmpId(fetchById.getEmpId());
		mainResponseDto.setIsActive(fetchById.getIsActive());
		mainResponseDto.setMobileNo(fetchById.getMobileNo());
		mainResponseDto.setUserName(fetchById.getUserName());
		mainResponseDto.setDesignation(fetchById.getDesignation());
		mainResponseDto.setBranchName(fetchById.getBranchName());
		mainResponseDto.setCity(fetchById.getCity());
		mainResponseDto.setState(fetchById.getState());
		mainResponseDto.setArea(fetchById.getArea());
		mainResponseDto.setCreatedBy(fetchById.getCreatedBy());
		mainResponseDto.setCreatedDate(fetchById.getCreatedDate());
		mainResponseDto.setUpdatedDate(fetchById.getUpdatedDate());
		mainResponseDto.setUpdatedBy(fetchById.getUpdatedBy());
		mainResponseDto.setLastAccessTime(fetchById.getLastAccessTime());
		UserRole userRole = fetchById.getUserRole();
		String userName = userRole.getUserName();
		mainResponseDto.setUserRoleName(userName);
		List<PermissionDto> dtos = new ArrayList<>();
		String pageGroupId = fetchById.getUserRole().getPageGroupId();
		JSONArray array = new JSONArray(pageGroupId);
		for (int i = 0; i < array.length(); i++) {
			Object object = array.get(i);
			String string = object.toString();
			Long sid = Long.parseLong(string);
			if (sid != null) {
				Optional<PageGroup> findById2 = pageGroupRepository.findById(sid);
				if (findById2.isPresent()) {
					PageGroup pageGroup = findById2.get();
					PermissionDto dto = new PermissionDto();
					dto.setIndex(pageGroup.getId());
					dto.setName(pageGroup.getModuleGroup().getModelGroupName());
					dto.setTitle(pageGroup.getPageName());
					dto.setPage(pageGroup.getPage());
					dto.setChannel(pageGroup.getChannel());
					dtos.add(dto);
				}
			}
		}
		String sessionId = userService.getSessionId(id, request);
		mainResponseDto.setSERVER_TOKEN(sessionId);
		mainResponseDto.setPermissions(dtos);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMyyyy HHmmss");
		LocalDateTime now = LocalDateTime.now();
		String todayDate = dtf.format(now);
		fetchById.setLastAccessTime(todayDate);
		userRepository.save(fetchById);
		logger.debug("Response" + mainResponseDto);
		return new ResponseEntity<>(mainResponseDto, HttpStatus.OK);
	}

	@RequestMapping(value = "/insertUserRoleEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> insertData(@RequestBody String jsonRequest) {
		UserRole insertData = userService.insertData(jsonRequest);
		logger.debug("Post Request" + jsonRequest);
		// logger.debug("Respone came from DB" + insertData);
		if (insertData == null) {
			return new ResponseEntity("Please Enter Vaild Details", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(insertData, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/updateUserRoleEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> updateData( @RequestBody String jsonRequest,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			HttpServletRequest request) throws Exception {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		  if (sessionId == true ) {
			  String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			  String decryptContainerString = Crypt.decrypt(encryptString,X_encode_ID);	
			  JSONObject jsonObject = new JSONObject(decryptContainerString);
		
		UserRole updateData = userService.updateData(decryptContainerString);
		if (updateData == null) {
			return new ResponseEntity("Please Enter Vaild Details", HttpStatus.BAD_REQUEST);
		}
		org.json.simple.JSONObject pdresponse = new org.json.simple.JSONObject();
		pdresponse.put("message", "updated");
		String	data = pdresponse.toString();
 		String encryptString2 = Crypt.encrypt(data, X_encode_ID);
 		org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
 		data2.put("value", encryptString2);
 		org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
 		data3.put("Data", data2);
 		return new ResponseEntity<Object>(data3, HttpStatus.OK);
		  }
		  else {
		        org.json.JSONObject data2 = new org.json.JSONObject();
		        data2.put("value", "SessionId is expired or Invalid sessionId");
		        org.json.JSONObject data3 = new org.json.JSONObject();
		        data3.put("Error", data2);
		        logger.debug("SessionId is expired or Invalid sessionId");
		        return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		    } 
	}

	@RequestMapping(value = "/fetchByUserRoleIdEncy/{id}", method = RequestMethod.GET)
	public ResponseEntity<UserRole> fetchByUserRoleId(@PathVariable("id") Long id) {
		UserRole getById = userService.getById(id);
		logger.debug("Get Request By Id" + id);
		// logger.debug("Respone came from DB" + getById);
		if (getById == null) {
			return new ResponseEntity("Please Enter Vaild Details", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(getById, HttpStatus.OK);
	}

	@RequestMapping(value = "/fetchAllUserrolesEncy", method = RequestMethod.GET)
	public ResponseEntity<Object> fetchAllUserRoles(
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
				HttpServletRequest request) throws Exception {
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		//	boolean sessionId = true;
			    if (sessionId == true ) {
			    	 //userService.getSessionId(X_User_ID, request); 
			logger.debug("Get All Request");
		List<UserRole> getAll = userService.getAll();
		logger.debug("Get All Request");
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		if (getAll == null) {
			response.put("message", "Please Enter Vaild Details");
			return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
		}
	
		JSONArray j=new JSONArray(getAll);
		response.put("Data", j);
		String	data = response.toString();
 		String encryptString2 = Crypt.encrypt(data, X_encode_ID);
 		org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
 		data2.put("value", encryptString2);
 		org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
 		data3.put("Data", data2);
 		return new ResponseEntity<Object>(data3, HttpStatus.OK);
		//return new ResponseEntity<>(getAll, HttpStatus.OK);
			    } 
			    else {
			        org.json.JSONObject data2 = new org.json.JSONObject();
			        data2.put("value", "SessionId is expired or Invalid sessionId");
			        org.json.JSONObject data3 = new org.json.JSONObject();
			        data3.put("Error", data2);
			        logger.debug("SessionId is expired or Invalid sessionId");
			        return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
			    } 
	}

	@RequestMapping(value = "/deleteUserRoleEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> deleteUserRole(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			HttpServletRequest request) throws Exception {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
	
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
	
		  if (sessionId == true ) {
			  String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			  String decryptContainerString = Crypt.decrypt(encryptString,X_encode_ID);	
			  JSONObject jsonObject = new JSONObject(decryptContainerString);
				String userId = jsonObject.getJSONObject("Data").getString("userRoleId");
				long id = Long.parseLong(userId);
		boolean deleteUserRole = userService.deleteUserRole(id);
		logger.debug("Delete Request By Id" + id);
		// logger.debug("Response came from DB" + deleteUserRole);
		if (deleteUserRole == false) {
			return new ResponseEntity("Please Enter Vaild Details", HttpStatus.BAD_REQUEST);
		}
		org.json.simple.JSONObject pdresponse = new org.json.simple.JSONObject();
		pdresponse.put("message", "deleted");
		String	data = pdresponse.toString();
 		String encryptString2 = Crypt.encrypt(data, X_encode_ID);
 		org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
 		data2.put("value", encryptString2);
 		org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
 		data3.put("Data", data2);
 		return new ResponseEntity<Object>(data3, HttpStatus.OK);
		  }
		  else {
		        org.json.JSONObject data2 = new org.json.JSONObject();
		        data2.put("value", "SessionId is expired or Invalid sessionId");
		        org.json.JSONObject data3 = new org.json.JSONObject();
		        data3.put("Error", data2);
		        logger.debug("SessionId is expired or Invalid sessionId");
		        return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		    } 
	}

	@RequestMapping(value = "/fetchByUserNameEncy", method = RequestMethod.GET)
	public ResponseEntity<PermissionResponseDto> fetchByUserName(@RequestParam("userName") String userName) {
		UserRole userRoleByUserName = userService.getUserRoleByUserName(userName);
		logger.debug("Get Request By username" + userName);
		// logger.debug("Response came from DB" + userRoleByUserName);
		if (userRoleByUserName == null) {
			return new ResponseEntity("Please Enter Vaild Details", HttpStatus.BAD_REQUEST);
		}
		List<UserRoleDto> dtos = new ArrayList<>();
		String pageGroupId = userRoleByUserName.getPageGroupId();
		JSONArray array = new JSONArray(pageGroupId);
		for (int i = 0; i < array.length(); i++) {
			Object object = array.get(i);
			String string = object.toString();
			Long sid = Long.parseLong(string);
			if (sid != null) {
				Optional<PageGroup> findById2 = pageGroupRepository.findById(sid);
				if (findById2.isPresent()) {
					PageGroup pageGroup = findById2.get();
					UserRoleDto dto = new UserRoleDto();
					dto.setModuleName(pageGroup.getModuleGroup().getModelGroupName());
					dto.setPageName(pageGroup.getPageName());
					dto.setPageLink(pageGroup.getPage());
					dto.setChannel(pageGroup.getChannel());
					dto.setId(pageGroup.getId());
					dtos.add(dto);
				}
			}
		}
		PermissionResponseDto permissionResponseDto = new PermissionResponseDto();
		permissionResponseDto.setId(userRoleByUserName.getId());
		permissionResponseDto.setUserName(userRoleByUserName.getUserName());
		permissionResponseDto.setPermissions(dtos);
		logger.debug("Response" + permissionResponseDto);
		return new ResponseEntity<>(permissionResponseDto, HttpStatus.OK);
	}

	@RequestMapping(value = "/fetchByBranchIdOrEmpIdEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchByBranchIdOrEmpId(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			HttpServletRequest request)throws Exception {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		  if (sessionId == true ) {
			  String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			  String decryptContainerString = Crypt.decrypt(encryptString,X_encode_ID);	
			  List<User> findByFilter = userService.findByFilter(decryptContainerString);
			  JSONArray j=new JSONArray(findByFilter);
				org.json.simple.JSONObject response = new org.json.simple.JSONObject();
				response.put("Data", j);
				String	data = response.toString();
		 		String encryptString2 = Crypt.encrypt(data, X_encode_ID);
		 		org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
		 		data2.put("value", encryptString2);
		 		org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
		 		data3.put("Data", data2);
		 		return new ResponseEntity<Object>(data3, HttpStatus.OK);
				
				  }
				  else {
				        org.json.JSONObject data2 = new org.json.JSONObject();
				        data2.put("value", "SessionId is expired or Invalid sessionId");
				        org.json.JSONObject data3 = new org.json.JSONObject();
				        data3.put("Error", data2);
				        logger.debug("SessionId is expired or Invalid sessionId");
				        return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
				    } 

	}

	@RequestMapping(value = "/customFilterEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchByFilter(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			HttpServletRequest request)
			throws Exception {
		
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		  if (sessionId == true ) {
			  String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			  String decryptContainerString = Crypt.decrypt(encryptString,X_encode_ID);	
			  
		List<User> findByFilter = userService.findByFilterAll(decryptContainerString);
		JSONArray j=new JSONArray(findByFilter);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Data", j);
		String	data = response.toString();
 		String encryptString2 = Crypt.encrypt(data, X_encode_ID);
 		org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
 		data2.put("value", encryptString2);
 		org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
 		data3.put("Data", data2);
 		return new ResponseEntity<Object>(data3, HttpStatus.OK);
		
		  }
		  else {
		        org.json.JSONObject data2 = new org.json.JSONObject();
		        data2.put("value", "SessionId is expired or Invalid sessionId");
		        org.json.JSONObject data3 = new org.json.JSONObject();
		        data3.put("Error", data2);
		        logger.debug("SessionId is expired or Invalid sessionId");
		        return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		    } 
	}

	@RequestMapping(value = "/approveUserEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> approvedUser(@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			HttpServletRequest request,
			@RequestBody String jsonRequest) throws Exception {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		    if (sessionId == true ) {
		    	// userService.getSessionId(X_User_ID, request); 
			 String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
		String key = X_Session_ID;
		String decryptContainerString = Crypt.decrypt(encryptString,X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
		String userId = jsonObject.getJSONObject("Data").getString("userId");
		String isActive = jsonObject.getJSONObject("Data").getString("isActive");
		
		userService.approveUser(userId, X_User_ID,isActive);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("message", "updated");
		String	data = response.toString();
 		String encryptString2 = Crypt.encrypt(data, X_encode_ID);
 		org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
 		data2.put("value", encryptString2);
 		org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
 		data3.put("Data", data2);
 		return new ResponseEntity<Object>(data3, HttpStatus.OK);
		    } 
		    else {
		        org.json.JSONObject data2 = new org.json.JSONObject();
		        data2.put("value", "SessionId is expired or Invalid sessionId");
		        org.json.JSONObject data3 = new org.json.JSONObject();
		        data3.put("Error", data2);
		        logger.debug("SessionId is expired or Invalid sessionId");
		        return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		    } 	
	}
	
	@RequestMapping(value = "/activeuserEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<UserRole> activeuser() {
		Object numberactiveuser = userService.activeuser();
        org.json.JSONObject data2 = new org.json.JSONObject();
        data2.put("Activeusers", numberactiveuser);
        System.out.println(data2);
		return new ResponseEntity(data2.toString(), HttpStatus.OK);
	}
	@RequestMapping(value="/logoutEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> logoutMember(
		   	@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			 @RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			 HttpServletRequest request,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception{
	
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		 if (sessionId == true ) {
	    	// userService.getSessionId(X_User_ID, request); 
					String message	=userService.logout(X_Session_ID);
					
		 	org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
				response.put("message", message);
				logger.debug("final response"+response.toString());
				String	data = response.toString();
		 		String encryptString2 = Crypt.encrypt(data, X_encode_ID);
		 		org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
		 		data2.put("value", encryptString2);
		 		org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
		 		data3.put("Data", data2);
		 		return new ResponseEntity<Object>(data3, HttpStatus.OK);
		 } 
		    else {
		        org.json.JSONObject data2 = new org.json.JSONObject();
		        data2.put("value", "SessionId is expired or Invalid sessionId");
		        org.json.JSONObject data3 = new org.json.JSONObject();
		        data3.put("Error", data2);
		        logger.debug("SessionId is expired or Invalid sessionId");
		        return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		    } 	
	}
	@RequestMapping(value="/logoutency", method = RequestMethod.POST)
	public ResponseEntity<Object> logoutMemberMob(
		   	@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			 @RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			 HttpServletRequest request,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception{
	
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		 if (sessionId == true ) {
	    	// userService.getSessionId(X_User_ID, request); 
					String message	=userService.logout(X_Session_ID);
					String key = X_Session_ID;
		 	org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
				response.put("message", message);
				logger.debug("final response"+response.toString());
				String	data = response.toString();
				String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
		 		org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
		 		data2.put("value", encryptString2);
		 		org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
		 		data3.put("Data", data2);
		 		return new ResponseEntity<Object>(data3, HttpStatus.OK);
		 } 
		    else {
		        org.json.JSONObject data2 = new org.json.JSONObject();
		        data2.put("value", "SessionId is expired or Invalid sessionId");
		        org.json.JSONObject data3 = new org.json.JSONObject();
		        data3.put("Error", data2);
		        logger.debug("SessionId is expired or Invalid sessionId");
		        return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		    } 	
	}
	
	@RequestMapping(value="/fetchByBranchIdEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchBranchId(@RequestBody String jsonRequest ,
			 @RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			 @RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			 HttpServletRequest request,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {
		
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		    if (sessionId == true ) {
		    	// userService.getSessionId(X_User_ID, request); 
			 String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
		String key = X_Session_ID;
		String decryptContainerString = Crypt.decrypt(encryptString,X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
		logger.debug("request"+jsonRequest);
		if(jsonRequest.isEmpty()) {
			logger.debug("request is empty"+jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}
		String branchId = jsonObject.getJSONObject("Data").getString("branchId");
			BranchMaster branchMaster=userService.fetchBranchInfo(branchId);
		org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
		JSONObject k=new JSONObject(branchMaster);
		response.put("Data", k);
		logger.debug("final response"+response.size());
		String	data = response.toString();
 		String encryptString2 = Crypt.encrypt(data, X_encode_ID);
 		org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
 		data2.put("value", encryptString2);
 		org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
 		data3.put("Data", data2);
 		return new ResponseEntity<Object>(data3, HttpStatus.OK);
		    } 
		    else {
		        org.json.JSONObject data2 = new org.json.JSONObject();
		        data2.put("value", "SessionId is expired or Invalid sessionId");
		        org.json.JSONObject data3 = new org.json.JSONObject();
		        data3.put("Error", data2);
		        logger.debug("SessionId is expired or Invalid sessionId");
		        return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		    } 
	}
	
	@RequestMapping(value="/fetchAllBranchesEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchAllBranches(
			 @RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			 @RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			 HttpServletRequest request,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {
		
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
	    if (sessionId == true ) {
		List<BranchListResponse> branchMaster=branchMappingService.fetchAllBranches();
		org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
	
		JSONArray k=new JSONArray(branchMaster);
		response.put("Data", k);
		logger.debug("final response"+response.size());
		String	data = response.toString();
 		String encryptString2 = Crypt.encrypt(data, X_encode_ID);
 		org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
 		data2.put("value", encryptString2);
 		org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
 		data3.put("Data", data2);
 		return new ResponseEntity<Object>(data3, HttpStatus.OK);
	    } 
	    else {
	        org.json.JSONObject data2 = new org.json.JSONObject();
	        data2.put("value", "SessionId is expired or Invalid sessionId");
	        org.json.JSONObject data3 = new org.json.JSONObject();
	        data3.put("Error", data2);
	        logger.debug("SessionId is expired or Invalid sessionId");
	        return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
	    } 
	}
	@RequestMapping(value="/fetchAllRolesEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchAllRoles(
			 @RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			 @RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			 HttpServletRequest request,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {
		
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
	    if (sessionId == true ) {
		List<String> fetchAllRoles=userService.fetchAllRoles();
		org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
		JSONArray k=new JSONArray(fetchAllRoles);
		response.put("Data", k);
		String	data = response.toString();
 		String encryptString2 = Crypt.encrypt(data, X_encode_ID);
 		org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
 		data2.put("value", encryptString2);
 		org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
 		data3.put("Data", data2);
 		return new ResponseEntity<Object>(data3, HttpStatus.OK);
	    } 
	    else {
	        org.json.JSONObject data2 = new org.json.JSONObject();
	        data2.put("value", "SessionId is expired or Invalid sessionId");
	        org.json.JSONObject data3 = new org.json.JSONObject();
	        data3.put("Error", data2);
	        logger.debug("SessionId is expired or Invalid sessionId");
	        return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
	    } 
	}	
}
