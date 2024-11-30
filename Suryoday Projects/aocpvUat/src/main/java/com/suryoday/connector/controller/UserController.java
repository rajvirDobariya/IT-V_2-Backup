package com.suryoday.connector.controller;

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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suryoday.aocpv.exceptionhandling.EmptyInputException;
import com.suryoday.connector.pojo.BranchInfo;
import com.suryoday.connector.pojo.BranchListResponse;
import com.suryoday.connector.pojo.BranchMaster;
import com.suryoday.connector.pojo.MainResponseDto;
import com.suryoday.connector.pojo.OtherRole;
import com.suryoday.connector.pojo.PageGroup;
import com.suryoday.connector.pojo.PermissionDto;
import com.suryoday.connector.pojo.PermissionResponseDto;
import com.suryoday.connector.pojo.User;
import com.suryoday.connector.pojo.UserRole;
import com.suryoday.connector.pojo.UserRoleDto;
import com.suryoday.connector.repository.PageGroupRepository;
import com.suryoday.connector.repository.UserRepository;
import com.suryoday.connector.repository.UserRoleRepository;
import com.suryoday.connector.service.BranchMappingService;
import com.suryoday.connector.service.UserService;

@RestController
@RequestMapping("/connector")
public class UserController {
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

	private static Logger logger = LoggerFactory.getLogger(UserController.class);

	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	public ResponseEntity<MainResponseDto> addUser(@RequestBody String jsonRequest) throws Exception {

		User addUser = userService.addUser(jsonRequest);
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
		mainResponseDto.setDepartment(addUser.getDepartment());
		mainResponseDto.setEmailId(addUser.getEmailId());
		mainResponseDto.setUserName(addUser.getUserName());
		mainResponseDto.setBranchName(addUser.getBranchName());
		mainResponseDto.setDesignation(addUser.getDesignation());
		mainResponseDto.setCity(addUser.getCity());
		mainResponseDto.setState(addUser.getState());
		mainResponseDto.setArea(addUser.getArea());
		mainResponseDto
				.setAllowCreditAccess(addUser.getAllowCreditAccess() != null ? addUser.getAllowCreditAccess() : "NO");
		String userAccess = addUser.getUserAccess();
		if (userAccess != null) {
			org.json.JSONArray userAccessInJson = new org.json.JSONArray(userAccess);
			List<String> listUserAccess = new ArrayList<>();
			for (int n = 0; n < userAccessInJson.length(); n++) {
				String asset = userAccessInJson.getString(n);
				listUserAccess.add(asset);
			}
			mainResponseDto.setUserAccess(listUserAccess);
		}
		mainResponseDto.setCreatedBy(addUser.getCreatedBy());
		mainResponseDto.setCreatedDate(addUser.getCreatedDate());
		mainResponseDto.setUpdatedDate(addUser.getUpdatedDate());
		mainResponseDto.setUpdatedBy(addUser.getUpdatedBy());
		mainResponseDto.setLastAccessTime(addUser.getLastAccessTime());
		mainResponseDto.setUserRoleName(addUser.getUserRole().getUserName());
		if (addUser.getOtherRole() != null) {
			List<OtherRole> otherRole = new ObjectMapper().readValue(addUser.getOtherRole(),
					new TypeReference<List<OtherRole>>() {
					});
			mainResponseDto.setOtherRole(otherRole);
		}
		if (addUser.getBranchIdArray() != null) {
			List<BranchInfo> list = new ArrayList<>();
			org.json.JSONArray branchIdArray = new org.json.JSONArray(addUser.getBranchIdArray());
			for (int n = 0; n < branchIdArray.length(); n++) {
				JSONObject jsonObject = branchIdArray.getJSONObject(n);
				String branchId = jsonObject.getString("branchId");
				String branchName = jsonObject.getString("branchName");
				BranchInfo branchInfo = new BranchInfo(branchId, branchName);
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
		return new ResponseEntity<>(mainResponseDto, HttpStatus.CREATED);

	}

	@RequestMapping(value = "/updateUser/{id}", method = RequestMethod.PUT)
	public ResponseEntity<MainResponseDto> updateUser(@PathVariable("id") String id, @RequestBody String jsonRequest)
			throws JsonParseException, JsonMappingException, IOException {
		User updateUser = userService.updateUser(id, jsonRequest);
		logger.debug("Put Request with Id" + jsonRequest + id);
		// logger.debug("Request came from DB" + updateUser);
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
		mainResponseDto.setDepartment(updateUser.getDepartment());
		mainResponseDto.setEmailId(updateUser.getEmailId());
		mainResponseDto.setDesignation(updateUser.getDesignation());
		mainResponseDto.setCity(updateUser.getCity());
		mainResponseDto.setState(updateUser.getState());
		mainResponseDto.setArea(updateUser.getArea());
		mainResponseDto.setAllowCreditAccess(
				updateUser.getAllowCreditAccess() != null ? updateUser.getAllowCreditAccess() : "NO");
		if (updateUser.getOtherRole() != null) {
			List<OtherRole> otherRole = new ObjectMapper().readValue(updateUser.getOtherRole(),
					new TypeReference<List<OtherRole>>() {
					});
			mainResponseDto.setOtherRole(otherRole);
		}
		logger.debug("stage 1");
		String userAccess = updateUser.getUserAccess();
		if (userAccess != null) {
			logger.debug("stage 2");
			org.json.JSONArray userAccessInJson = new org.json.JSONArray(userAccess);
			List<String> listUserAccess = new ArrayList<>();
			for (int n = 0; n < userAccessInJson.length(); n++) {
				String asset = userAccessInJson.getString(n);
				listUserAccess.add(asset);
			}
			mainResponseDto.setUserAccess(listUserAccess);
		}
		mainResponseDto.setCreatedBy(updateUser.getCreatedBy());
		mainResponseDto.setCreatedDate(updateUser.getCreatedDate());
		mainResponseDto.setUpdatedDate(updateUser.getUpdatedDate());
		mainResponseDto.setUpdatedBy(updateUser.getUpdatedBy());
		mainResponseDto.setUserRoleName(updateUser.getUserRole().getUserName());
		mainResponseDto.setLastAccessTime(updateUser.getLastAccessTime());
		logger.debug("stage 3");
		if (updateUser.getBranchIdArray() != null) {
			List<BranchInfo> list = new ArrayList<>();
			org.json.JSONArray branchIdArray = new org.json.JSONArray(updateUser.getBranchIdArray());
			for (int n = 0; n < branchIdArray.length(); n++) {
				String branchId = "";
				String branchName = "";
				JSONObject jsonObject = branchIdArray.getJSONObject(n);
				try {
					branchId = jsonObject.getString("branchId");
					branchName = jsonObject.getString("branchName");
				} catch (Exception e) {
					branchId = Long.toString(jsonObject.getLong("branchId"));
					branchName = jsonObject.getString("branchName");
				}
				BranchInfo branchInfo = new BranchInfo(branchId, branchName);
				list.add(branchInfo);
			}
			mainResponseDto.setListofBranches(list);
		}
		logger.debug("stage 4" + updateUser.getUserRole().getId());

		Optional<UserRole> findById2 = userRoleRepository.findById(updateUser.getUserRole().getId());
		logger.debug("stage 5" + findById2);
		if (findById2.isPresent()) {
			logger.debug("stage 6");
			UserRole userRole2 = findById2.get();
			String pageGroupId = userRole2.getPageGroupId();
			List<PermissionDto> dtos = new ArrayList<>();
			JSONArray array = new JSONArray(pageGroupId);
			for (int i = 0; i < array.length(); i++) {
				Object object = array.get(i);
				String string = object.toString();
				Long sid = Long.parseLong(string);
				if (sid != null) {
					logger.debug("stage 7" + sid);
					Optional<PageGroup> findById3 = pageGroupRepository.findById(sid);
					if (findById3.isPresent()) {
						logger.debug("stage 8" + sid);
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
			logger.debug("Response" + mainResponseDto);
		}
		return new ResponseEntity<>(mainResponseDto, HttpStatus.OK);
	}

	@RequestMapping(value = "/deleteUser", method = RequestMethod.DELETE)
	public ResponseEntity<User> deleteUser(@RequestHeader(name = "X-User-ID", required = true) String X_User_ID) {
		int deleteUser = userService.deleteUser(X_User_ID);
		logger.debug("Delete Request with Id" + X_User_ID);
		logger.debug("Response " + deleteUser);
		if (deleteUser == 0) {
			return new ResponseEntity("Please Enter Vaild Details", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity(deleteUser, HttpStatus.OK);
	}

	@RequestMapping(value = "/fetchAll", method = RequestMethod.GET)
	public ResponseEntity<Object> fetchAll(HttpServletRequest request) throws Exception {
		List<User> fetchAll = userService.fetchAll();
		logger.debug("Get All Request");
		// logger.debug("Request came from DB" + fetchAll);
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
			mainResponseDto
					.setAllowCreditAccess(user.getAllowCreditAccess() != null ? user.getAllowCreditAccess() : "NO");
			mainResponseDto.setLastAccessTime(user.getLastAccessTime());
			if (user.getOtherRole() != null) {
				List<OtherRole> otherRole = new ObjectMapper().readValue(user.getOtherRole(),
						new TypeReference<List<OtherRole>>() {
						});
				mainResponseDto.setOtherRole(otherRole);
			}
			if (user.getBranchIdArray() != null) {
				List<BranchInfo> list = new ArrayList<>();
				org.json.JSONArray branchIdArray = new org.json.JSONArray(user.getBranchIdArray());
				for (int n = 0; n < branchIdArray.length(); n++) {
					String branchId = "";
					String branchName = "";
					JSONObject jsonObject = branchIdArray.getJSONObject(n);
					try {
						branchId = jsonObject.getString("branchId");
						branchName = jsonObject.getString("branchName");
					} catch (Exception e) {
						branchId = Long.toString(jsonObject.getLong("branchId"));
						branchName = jsonObject.getString("branchName");
					}
					BranchInfo branchInfo = new BranchInfo(branchId, branchName);
					list.add(branchInfo);
				}
				mainResponseDto.setListofBranches(list);
			}

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
				// String sessionId = userService.getSessionId(user.getUserId(), request);
				mainResponseDto.setSERVER_TOKEN("BDHDHFDDFH6483748");
			}

			mainResponseDtos.add(mainResponseDto);
			logger.debug("Response" + mainResponseDtos);
		}
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Data", mainResponseDtos);
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/fetchById/{id}", method = RequestMethod.GET)
	public ResponseEntity<MainResponseDto> fetchById(@PathVariable("id") String id, HttpServletRequest request)
			throws Exception {
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
		mainResponseDto.setEmailId(fetchById.getEmailId());
		mainResponseDto.setCity(fetchById.getCity());
		mainResponseDto.setState(fetchById.getState());
		mainResponseDto.setArea(fetchById.getArea());
		mainResponseDto.setCreatedBy(fetchById.getCreatedBy());
		mainResponseDto.setCreatedDate(fetchById.getCreatedDate());
		mainResponseDto.setUpdatedDate(fetchById.getUpdatedDate());
		mainResponseDto.setUpdatedBy(fetchById.getUpdatedBy());
		mainResponseDto.setAllowCreditAccess(
				fetchById.getAllowCreditAccess() != null ? fetchById.getAllowCreditAccess() : "NO");
		if (fetchById.getOtherRole() != null) {
			List<OtherRole> otherRole = new ObjectMapper().readValue(fetchById.getOtherRole(),
					new TypeReference<List<OtherRole>>() {
					});
			mainResponseDto.setOtherRole(otherRole);
		}
		String userAccess = fetchById.getUserAccess();
		if (userAccess != null) {
			org.json.JSONArray userAccessInJson = new org.json.JSONArray(userAccess);
			List<String> listUserAccess = new ArrayList<>();
			for (int n = 0; n < userAccessInJson.length(); n++) {
				String asset = userAccessInJson.getString(n);
				listUserAccess.add(asset);
			}
			mainResponseDto.setUserAccess(listUserAccess);
		}
		if (fetchById.getBranchIdArray() != null) {
			List<BranchInfo> list = new ArrayList<>();

			org.json.JSONArray branchIdArray = new org.json.JSONArray(fetchById.getBranchIdArray());
			for (int n = 0; n < branchIdArray.length(); n++) {
				String branchId = "";
				String branchName = "";
				org.json.JSONObject jsonObject = branchIdArray.getJSONObject(n);
				try {
					branchId = jsonObject.getString("branchId");
					branchName = jsonObject.getString("branchName");
				} catch (Exception e) {
					branchId = Long.toString(jsonObject.getLong("branchId"));
					branchName = jsonObject.getString("branchName");
				}
				BranchInfo branchInfo = new BranchInfo(branchId, branchName);
				list.add(branchInfo);
			}
			mainResponseDto.setListofBranches(list);
		}
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
		// String sessionId = userService.getSessionId(id, request);
		mainResponseDto.setSERVER_TOKEN("BDHDHFDDFH6483748");
		mainResponseDto.setPermissions(dtos);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMyyyy HHmmss");
		LocalDateTime now = LocalDateTime.now();
		String todayDate = dtf.format(now);
		fetchById.setLastAccessTime(todayDate);
		userRepository.save(fetchById);
		logger.debug("Response" + mainResponseDto);
		return new ResponseEntity<>(mainResponseDto, HttpStatus.OK);
	}

	@RequestMapping(value = "/insertUserRole", method = RequestMethod.POST)
	public ResponseEntity<UserRole> insertData(@RequestBody String jsonRequest) {
		UserRole insertData = userService.insertData(jsonRequest);
		logger.debug("Post Request" + jsonRequest);
		// logger.debug("Respone came from DB" + insertData);
		if (insertData == null) {
			return new ResponseEntity("Please Enter Vaild Details", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(insertData, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/updateUserRole/{id}", method = RequestMethod.PUT)
	public ResponseEntity<UserRole> updateData(@PathVariable("id") Long id, @RequestBody String jsonRequest) {

		UserRole updateData = userService.updateData(jsonRequest);
		logger.debug("Put Request By Id" + jsonRequest + id);
		// logger.debug("Response came from DB" + updateData);
		if (updateData == null) {
			return new ResponseEntity("Please Enter Vaild Details", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(updateData, HttpStatus.OK);

	}

	@RequestMapping(value = "/fetchByUserRoleId/{id}", method = RequestMethod.GET)
	public ResponseEntity<UserRole> fetchByUserRoleId(@PathVariable("id") Long id) {
		UserRole getById = userService.getById(id);
		logger.debug("Get Request By Id" + id);
		// logger.debug("Respone came from DB" + getById);
		if (getById == null) {
			return new ResponseEntity("Please Enter Vaild Details", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(getById, HttpStatus.OK);
	}

	@RequestMapping(value = "/fetchAllUserroles", method = RequestMethod.GET)
	public ResponseEntity<List<UserRole>> fetchAllUserRoles() {
		List<UserRole> getAll = userService.getAll();
		logger.debug("Get All Request");
		// logger.debug("Respone came from DB" + getAll);
		if (getAll == null) {
			return new ResponseEntity("Please Enter Vaild Details", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(getAll, HttpStatus.OK);
	}

	@RequestMapping(value = "/deleteUserRole", method = RequestMethod.POST)
	public ResponseEntity<Object> deleteUserRole(@RequestBody String jsonRequest) {
		JSONObject jsonObject = new JSONObject(jsonRequest);
		String userId = jsonObject.getJSONObject("Data").getString("userRoleId");
		long id = Long.parseLong(userId);
		boolean deleteUserRole = userService.deleteUserRole(id);
		logger.debug("Delete Request By Id" + id);
		// logger.debug("Response came from DB" + deleteUserRole);
		if (deleteUserRole == false) {
			return new ResponseEntity("Please Enter Vaild Details", HttpStatus.BAD_REQUEST);
		}
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("message", "deleted");
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/fetchByUserName", method = RequestMethod.GET)
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

	@RequestMapping(value = "/fetchByBranchIdOrEmpId", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchByBranchIdOrEmpId(@RequestBody String jsonRequest)
			throws JsonParseException, JsonMappingException, IOException {
		List<User> findByFilter = userService.findByFilter(jsonRequest);
		return new ResponseEntity<Object>(findByFilter, HttpStatus.OK);

	}

	@RequestMapping(value = "/customFilter", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchByFilter(@RequestBody String jsonRequest)
			throws JsonParseException, JsonMappingException, IOException {
		List<User> findByFilter = userService.findByFilterAll(jsonRequest);
		return new ResponseEntity<Object>(findByFilter, HttpStatus.OK);

	}

	@RequestMapping(value = "/approveUser", method = RequestMethod.POST)
	public void approvedUser(@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestBody String jsonRequest) {
		JSONObject jsonObject = new JSONObject(jsonRequest);

		String userId = jsonObject.getJSONObject("Data").getString("userId");
		String isActive = jsonObject.getJSONObject("Data").getString("isActive");

		userService.approveUser(userId, X_User_ID, isActive);
	}

	@RequestMapping(value = "/activeuser", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<UserRole> activeuser() {
		Object numberactiveuser = userService.activeuser();
		org.json.JSONObject data2 = new org.json.JSONObject();
		data2.put("Activeusers", numberactiveuser);
		System.out.println(data2);
		return new ResponseEntity(data2.toString(), HttpStatus.OK);
	}

	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public ResponseEntity<Object> logoutMember(
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		String message = userService.logout(X_Session_ID);

		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("message", message);
		logger.debug("final response" + response.toString());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/CheckUserRoleStatus", method = RequestMethod.POST)
	public ResponseEntity<Object> CheckUserRoleStatus(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		logger.debug("CheckUserRoleStatus Start");
		JSONObject jsonObject = new JSONObject(jsonRequest);
		String page_name = jsonObject.getJSONObject("Data").getString("page_name");
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			User fetchById = userService.fetchById(X_User_ID);
			String pageGroupId = fetchById.getUserRole().getPageGroupId();
			JSONArray array = new JSONArray(pageGroupId);
			for (int i = 0; i < array.length(); i++) {
				String object = (String) array.get(i);
				Long sid = Long.parseLong(object);
				if (sid != null) {
					Optional<PageGroup> findById2 = pageGroupRepository.findById(sid);
					if (findById2.isPresent()) {
						String pageName = findById2.get().getPage();
						if (pageName.equalsIgnoreCase(page_name)) {
							org.json.simple.JSONObject response = new org.json.simple.JSONObject();
							response.put("message", "TRUE");
							logger.debug("final response" + response.toString());
							return new ResponseEntity<Object>(response, HttpStatus.OK);
						}

					}
				}
			}
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "Wrong PageName");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("Wrong PageName");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/fetchByBranchId", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchBranchId(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		logger.debug("request" + jsonRequest);
		if (jsonRequest.isEmpty()) {
			logger.debug("request is empty" + jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}
		String branchId = jsonObject.getJSONObject("Data").getString("branchId");
		BranchMaster branchMaster = userService.fetchBranchInfo(branchId);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Data", branchMaster);
		logger.debug("final response" + response.size());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/fetchTopTenBranches", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchTopTenBranches(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		logger.debug("request" + jsonRequest);
		if (jsonRequest.isEmpty()) {
			logger.debug("request is empty" + jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}
		List<BranchMaster> branchMaster = branchMappingService.fetchTopTenBranches();
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Data", branchMaster);
		logger.debug("final response" + response.size());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/addBranches", method = RequestMethod.POST)
	public ResponseEntity<Object> addBranches(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		logger.debug("request" + jsonRequest);
		if (jsonRequest.isEmpty()) {
			logger.debug("request is empty" + jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}
		String branchId = jsonObject.getJSONObject("Data").getString("branchId");
		String branchname = jsonObject.getJSONObject("Data").getString("branchName");
		String address = jsonObject.getJSONObject("Data").getString("address");
		String city = jsonObject.getJSONObject("Data").getString("city");
		String state = jsonObject.getJSONObject("Data").getString("state");
		String pincode = jsonObject.getJSONObject("Data").getString("pincode");
		LocalDateTime now = LocalDateTime.now();

		BranchMaster branchMaster = new BranchMaster();
		branchMaster.setBranchId(branchId);
		branchMaster.setBranchName(branchname);
		branchMaster.setAddress(address);
		branchMaster.setCity(city);
		branchMaster.setState(state);
		branchMaster.setPincode(pincode);
		branchMaster.setCreatedTime(now);

		String message = branchMappingService.addBranches(branchMaster);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("message", message);
		logger.debug("final response" + response.size());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/fetchAllBranches", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchAllBranches(
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		List<BranchListResponse> branchMaster = branchMappingService.fetchAllBranches();
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Data", branchMaster);
		logger.debug("final response" + response.size());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/fetchAllRoles", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchAllRoles(
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		List<String> fetchAllRoles = userService.fetchAllRoles();
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Data", fetchAllRoles);
		logger.debug("final response" + response.size());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}
}
