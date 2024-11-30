package com.suryoday.uam.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suryoday.uam.exceptionhandling.EmptyInputException;
import com.suryoday.uam.exceptionhandling.ErrorResponse;
import com.suryoday.uam.exceptionhandling.NoSuchElementException;
import com.suryoday.uam.others.GenerateProperty;
import com.suryoday.uam.pojo.AocpvLastLogin;
import com.suryoday.uam.pojo.BranchInfo;
import com.suryoday.uam.pojo.HRUsersData;
import com.suryoday.uam.pojo.MainResponseDto;
import com.suryoday.uam.pojo.OtherRole;
import com.suryoday.uam.pojo.PageGroup;
import com.suryoday.uam.pojo.PermissionDto;
import com.suryoday.uam.pojo.User;
import com.suryoday.uam.pojo.UserAccessResponse;
import com.suryoday.uam.pojo.UserRole;
import com.suryoday.uam.repository.PageGroupRepository;
import com.suryoday.uam.repository.UserRepository;
import com.suryoday.uam.repository.UserRoleRepository;
import com.suryoday.uam.service.AocpService;
import com.suryoday.uam.service.AocpvLastLoginService;
import com.suryoday.uam.service.LoginLdapService;
import com.suryoday.uam.service.UserService;
import com.suryoday.uam.utils.Constants;

@RestController
@CrossOrigin("*")
@RequestMapping("/sarathi")
public class LoginLdapController extends OncePerRequestFilter {

	@Autowired
	private AocpService aocpvservice;

	@Autowired
	LoginLdapService ldapService;

	@Autowired
	UserService service;

	@Autowired
	PageGroupRepository pageGroupRepository;

	@Autowired
	private AocpvLastLoginService aocpvLastLoginService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	UserRoleRepository userRoleRepository;

	@RequestMapping(value = "/netbanking/validate/user", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> login(@RequestBody String bm, HttpServletRequest request,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Frame-Options", required = true) String X_Frame_Options,
			@RequestHeader(name = "X-XSS-Protection", required = true) String X_XSS_Protection,
			@RequestHeader(name = "Strict-Transport-Security", required = true) String Strict_Transport_Security,
			@RequestHeader(name = "Content-Security-Policy", required = true) String Content_Security_Policy,
			@RequestHeader(name = "X-Content-Type-Options", required = true) String X_Content_Type_Options,
			@RequestHeader(name = "channel", required = true) String CHANNEL,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {

		System.out.println("welcome" + bm);
		JSONObject Data = new JSONObject();
		String acc_type = "";
		if (X_Request_ID.equals("AOCPV")) {
			org.json.JSONObject jk = new org.json.JSONObject(bm);
			String userId = "";
			String password = "";
			org.json.JSONObject jm = jk.getJSONObject("Data");
			if (jm.has("UserID")) {
				userId = jm.getString("UserID");
			} else {
				throw new EmptyInputException("The parameter UserID is required.");
			}
			if (jm.has("Password")) {
				password = jm.getString("Password");
			} else {
				throw new EmptyInputException("The parameter Password is required.");
			}
			if (jm.length() > 2) {
				throw new EmptyInputException("The other parameter is not required.");
			}
			if (userId.isEmpty() && password.isEmpty()) {
				throw new EmptyInputException("Please enter username and password.");
			} else if (password.isEmpty() || password == null) {
				throw new EmptyInputException("Please enter password");
			} else if (userId.isEmpty() || userId == null) {
				throw new EmptyInputException("Please enter username");
			}
			if (userId.matches("\\d+") != true || userId.length() != 5) {
				throw new EmptyInputException("Please enter valid username");
			}
			org.json.JSONObject ldapContext = getLdapContext(userId, password);
			AocpvLastLogin aocpvLastLogin = new AocpvLastLogin();
			aocpvLastLogin.setUserId(userId);
			aocpvLastLogin.setSesionId("BDHDHFDDFH6483748");
			LocalDateTime now = LocalDateTime.now();

			aocpvLastLogin.setLoginTime(now);
			if (jm.has("AppId")) {
				String AppId = jm.getString("AppId");
				aocpvLastLogin.setAppId(AppId);
			}
			if (jm.has("sourceIP")) {
				String sourceIP = jm.getString("sourceIP");
				aocpvLastLogin.setSourceIp(sourceIP);
			}
			if (jm.has("DeviceID")) {
				String DeviceId = jm.getString("DeviceID");
				aocpvLastLogin.setDeviceId(DeviceId);
			}
			if (jm.has("VersionNo")) {
				String VersionNo = jm.getString("VersionNo");
				aocpvLastLogin.setVersionNo(VersionNo);
			}
			if (jm.has("longitude")) {
				String longitude = jm.getString("longitude");
				aocpvLastLogin.setLongitude(longitude);
			}
			if (jm.has("latitude")) {
				String latitude = jm.getString("latitude");
				aocpvLastLogin.setLatitude(latitude);
			}

			String serviceStatus = ldapContext.getJSONObject("data").getString("TransactionCode").toString();
			System.out.println(serviceStatus); // 00
			// db call 30639 Welcome@123 /
			System.out.println("start");
			if (serviceStatus.equals("00")) {

				User fetchById = service.fetchById(userId);

				System.out.println("end");
				if (fetchById == null) {
					// return new ResponseEntity("Please Enter Vaild
					// Details",HttpStatus.BAD_REQUEST);
					HRUsersData user = service.fetchUserFromHr(userId);
					System.out.println(user);
					User newUser = new User();
					newUser.setUserId(userId);
					newUser.setEmpId(Long.parseLong(userId));
					newUser.setUserName(user.getUserName());
					newUser.setUserAccess("[LEAD-MANAGEMENT]");
					newUser.setDesignation(user.getDesignation());
					newUser.setEmailId(user.getUserEmail());
					newUser.setMobileNo(user.getMobileNumber());
					newUser.setArea(user.getArea());
					newUser.setCity(user.getCity());
					newUser.setState(user.getState());
					newUser.setIsActive(Long.parseLong("1"));
					newUser.setBranchId(Long.parseLong(user.getBranchId()));
					newUser.setBranchName(user.getLocation());
					Optional<UserRole> findById2 = userRoleRepository.findByUserName("AO");
					if (findById2.isPresent()) {
						UserRole userRole = findById2.get();
						newUser.setUserRole(userRole);
					}
					service.saveUser(newUser);
					fetchById = newUser;
//						throw new NoSuchElementException("You are Not authorized to View");
				}
				MainResponseDto mainResponseDto = new MainResponseDto();
				mainResponseDto.setUserId(fetchById.getUserId());
				mainResponseDto.setAdmin(fetchById.isAdmin());
				mainResponseDto.setBranchId(fetchById.getBranchId());
				mainResponseDto.setEmpId(fetchById.getEmpId());
				if (fetchById.getIsActive() == 0) {
					throw new NoSuchElementException("User is not Active");
				}
				mainResponseDto.setIsActive(fetchById.getIsActive());
				mainResponseDto.setMobileNo(fetchById.getMobileNo());
				mainResponseDto.setUserName(fetchById.getUserName());
				mainResponseDto.setBranchName(fetchById.getBranchName());
				mainResponseDto.setDesignation(fetchById.getDesignation());
				mainResponseDto.setCity(fetchById.getCity());
				mainResponseDto.setState(fetchById.getState());
				mainResponseDto.setArea(fetchById.getArea());
				String sessionId = service.getSessionId(fetchById.getUserId(), request);
				mainResponseDto.setSERVER_TOKEN(sessionId);
				mainResponseDto.setCreatedBy(fetchById.getCreatedBy());
				mainResponseDto.setCreatedDate(fetchById.getCreatedDate());
				mainResponseDto.setUpdatedDate(fetchById.getUpdatedDate());
				mainResponseDto.setLastAccessTime(fetchById.getLastAccessTime());
				String userAccess = fetchById.getUserAccess();
				if (fetchById.getOtherRole() != null) {
					List<OtherRole> otherRole = new ObjectMapper().readValue(fetchById.getOtherRole(),
							new TypeReference<List<OtherRole>>() {
							});
					mainResponseDto.setOtherRole(otherRole);
				}
				if (userAccess != null) {
					org.json.JSONArray userAccessInJson = new org.json.JSONArray(userAccess);
					List<String> listUserAccess = new ArrayList<>();
					for (int n = 0; n < userAccessInJson.length(); n++) {
						String asset = userAccessInJson.getString(n);
						if (asset.equalsIgnoreCase("LEAD-MANAGEMENT")) {
							String productGroup = service.getProductGroup(userId);
							mainResponseDto.setProductGroup(productGroup);
						}
						listUserAccess.add(asset);
					}
					mainResponseDto.setUserAccess(listUserAccess);
				}
				List<BranchInfo> list = new ArrayList<>();
				if (fetchById.getBranchIdArray() != null) {
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
				}
				mainResponseDto.setListofBranches(list);
				UserRole userRole = fetchById.getUserRole();
				String userName = userRole.getUserName();
				mainResponseDto.setUserRoleName(userName);
				List<PermissionDto> dtos = new ArrayList<>();
				String pageGroupId = fetchById.getUserRole().getPageGroupId();
				JSONArray array = new JSONArray(pageGroupId);

				// get Pages for Other Roles
				String otherRole = fetchById.getOtherRole();
				if (otherRole != null) {
					dtos = aocpvservice.getPagesForOtherRole(otherRole, CHANNEL, userAccess, dtos, null);
				}

				for (int i = 0; i < array.length(); i++) {

					String object = (String) array.get(i);
					Long sid = Long.parseLong(object);

					if (sid != null) {
						Optional<PageGroup> findById2 = pageGroupRepository.findById(sid);

						if (findById2.isPresent()) {

							PageGroup pageGroup = findById2.get();
							if (pageGroup.getChannel().equals(CHANNEL)) {
								PermissionDto dto = new PermissionDto();
								dto.setIndex(pageGroup.getId());
								dto.setName(pageGroup.getModuleGroup().getModelGroupName());
								dto.setPage(pageGroup.getPageName());
								dto.setTitle(pageGroup.getPageName());
								dto.setPage(pageGroup.getPage());
								dto.setChannel(pageGroup.getChannel());
								if (userAccess != null) {
									org.json.JSONArray userAccessInJson = new org.json.JSONArray(userAccess);
									int count = 0;
									for (int n = 0; n < userAccessInJson.length(); n++) {
										String asset = userAccessInJson.getString(n);
										if (asset.equalsIgnoreCase(dto.getName())) {
											dtos.add(dto);
										} else if (dto.getName().equals("COMMON")) {
											if (count == 0) {
												dtos.add(dto);
											}
											count++;
										}
									}
								}

							}
						}
					}
				}

				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MMM-dd HH:mm");
				String todayDate = dtf.format(now);

				aocpvLastLogin.setLastRequestTime(fetchById.getLastAccessTime());
				String connectionstatus = ldapContext.getJSONObject("data").getString("connectionstatus").toString();
				aocpvLastLogin.setTransResponse(connectionstatus);
				aocpvLastLogin.setTransStatus("S");
				aocpvLastLoginService.save(aocpvLastLogin);
				fetchById.setLastAccessTime(todayDate);
				userRepository.save(fetchById);
				mainResponseDto.setPermissions(dtos);
				return new ResponseEntity(mainResponseDto, HttpStatus.OK);

				// UserRoleMaster userRoleMaster=ldapService.getByuserId(userId);
				// JSONObject data1= new JSONObject();

				// data1.put("UID1", jm.getString("UserID").toString());
				// data1.put("BRCD",userRoleMaster.getBrcd());
				// data1.put("name",userRoleMaster.getUfn());
				// data1.put("userrole",userRoleMaster.getUcls());
				// data1.put("mobile",userRoleMaster.getMobile() );

				// Data.put("Data", data1);
				// return new ResponseEntity<Object>(Data, HttpStatus.OK);
			}

			else {
				JSONObject data1 = new JSONObject();
				String userStatus = ldapContext.getJSONObject("data").getString("UserStatus").toString();
				aocpvLastLogin.setTransResponse(userStatus);
				aocpvLastLogin.setTransStatus("F");
				aocpvLastLoginService.save(aocpvLastLogin);
				data1.put("code", HttpStatus.BAD_REQUEST);
				data1.put("message", "INVALID OR WRONG PASSWORD");
				Data.put("Error", ldapContext);
				String errormsg = ldapContext.getJSONObject("data").getString("UserStatus").toString();
				// return new ResponseEntity<Object>(Data.toString(), HttpStatus.BAD_REQUEST);
				throw new NoSuchElementException(errormsg);
			}

		}
//		List<Benificier>  l = new ArrayList<Benificier>();
//		List<BenificiaryInfo>  BenificiaryInfo1 = new ArrayList<BenificiaryInfo>();
//		
//		
//		
//	//	l= benificierService.FindAllCustomer(customerid,accounttype);
//		l= benificierService.viewcustmerbytype(customerid,acc_type);
//		if(l.size() == 0)
//		{
//			JSONObject Response = new JSONObject();
//			JSONObject Error = new JSONObject();
//			Error.put("Code", "100");
//			Error.put("Description", "No Record Found");
//			Response.put("Error", Error);
//			return new ResponseEntity<Object>(Response, HttpStatus.OK);
//		}
//		else
//		{
//			
//			for(int i = 0; i<l.size(); i++){
//				Benificier b=l.get(i);
//				
//				BenificiaryInfo b1= new BenificiaryInfo();
//				
//				
////				if(b.getType().equals("2"))
////				{
////					
////				}
////				else if(b.getType().equals("3"))
////				{
////					
////				}
//					
//					b1.setBeneficiaryId(b.getAccount());
//					b1.setNickName(b.getNickName());
//					b1.setBeneficiaryName(b.getName());
//					b1.setSequence(Integer.parseInt(b.getSequence()));
//					b1.setBeneficiaryEmailId(b.getEmailId());
//					b1.setStatus(b.getStatus());
//					b1.setBeneficiaryMobileNumber(b.getMobileNumber());
//					b1.setBeneficiaryMaxLimit(b.getMaxLimit());
//					b1.setBeneficiaryBank(b.getBankName());
//					b1.setBeneficiaryBankCity(b.getBankCity());
//					b1.setBeneficiaryBranch(b.getBankBranch());
//					b1.setBeneficiaryBankIfsc(b.getBankIfsc());
//				
//				
//				
//				
//				BenificiaryInfo1.add(b1);
//			    
//			}
//			
//			
//			
//			
//		
//			
//			JSONObject BenificiaryInfo= new JSONObject();
//			BenificiaryInfo.put("BenificiaryInfo", BenificiaryInfo1);
//			BenificiaryInfo.put("TransactionCode", "00");
//			
		JSONObject data1 = new JSONObject();

		data1.put("code", HttpStatus.BAD_REQUEST);
		data1.put("message", "wrong headers");
		Data.put("Error", data1);

		return new ResponseEntity<Object>(Data, HttpStatus.BAD_REQUEST);

	}

	public org.json.JSONObject getLdapContext(String username, String password) {
		JSONObject ResponsMess = new JSONObject();
		org.json.JSONObject ldapresponse = new org.json.JSONObject();

		GenerateProperty x = GenerateProperty.getInstance();
		x.getappprop();
		DirContext ctx = null;
		String connectionstatus = "";
		String StatusCode = "";
		String UserStatus = "";
		String find = "52e";

		String url = x.LDAPURLDC;
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		env.put("java.naming.security.authentication", "simple");
		env.put("java.naming.security.principal", username + "@" + "suryodaybank.local");
		env.put("java.naming.security.credentials", password);
		env.put("java.naming.provider.url", url);
		try {
			ctx = new InitialDirContext(env);
			connectionstatus = "Connected";
			System.out.println(ctx);
			ResponsMess.put("connectionstatus", "Connected");
			ResponsMess.put("UserStatus", UserStatus);
			ResponsMess.put("TransactionCode", "00");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Catch" + e.getMessage());
			String exceptindata = e.getMessage().toString();

			int i = exceptindata.indexOf(find);
			if (i > 0) {
				System.out.println(exceptindata.substring(i, i + find.length()));
				StatusCode = exceptindata.substring(i, i + find.length());
			}

			if (StatusCode.equals(find)) {
				// wromg pwd
				UserStatus = "Please enter valid password";

			} else {
				// locked pwd
				UserStatus = "User Is Locked Kindly Try After 30 Minutes Or Raise A ticket In Service Desk";

			}

			connectionstatus = "Not Connected";

			ResponsMess.put("connectionstatus", "Not Connected");
			ResponsMess.put("UserStatus", UserStatus);
			ResponsMess.put("TransactionCode", "1");

		} finally {
//			
			try {
				ctx.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				// System.out.println("finally" +e.getMessage());

			}
		}

		ldapresponse.put("data", ResponsMess);

		return ldapresponse;
		// return connectionstatus;
	}

	@RequestMapping(value = "/getPermissions", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchByApplicationNo(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "channel", required = true) String CHANNEL,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		org.json.JSONObject jsonObject = new org.json.JSONObject(jsonRequest);
		String userRole = jsonObject.getJSONObject("Data").getString("userRole");
		String moduleName = jsonObject.getJSONObject("Data").getString("moduleName");

		List<PermissionDto> list = service.getPermissions(userRole, moduleName, CHANNEL, X_User_ID);

		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("status", HttpStatus.OK.toString());
		response.put("Data", list);
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/getUserDetails", method = RequestMethod.POST)
	public ResponseEntity<Object> getUserDetails(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "channel", required = true) String CHANNEL,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {

		org.json.JSONObject jsonObject = new org.json.JSONObject(jsonRequest);
		String userId = jsonObject.getJSONObject("Data").getString("userId");
		User fetchById = service.fetchById(userId);
		if (fetchById == null) {
			throw new NoSuchElementException("No record found");
		}
		if (fetchById.getUserAccess() != null) {
			List<UserAccessResponse> map = Constants.getAllAccessModulesMap();

//					List<String> list = Arrays.asList(fetchById.getUserAccess());
//					for (UserAccessResponse keyMap:map){
//			            if (!list.contains(keyMap.getModuleName())) {
//			                map.remove(keyMap);
//			            }
//					
//					}
//					fetchById.setUserAccess(map.toString());
			ObjectMapper objectMapper = new ObjectMapper();
			List<String> module = objectMapper.readValue(fetchById.getUserAccess(), new TypeReference<List<String>>() {
			});
			List<UserAccessResponse> collect = map.stream().filter(entity -> module.contains(entity.getModuleName()))
					.collect(Collectors.toList());
			fetchById.setUserAccess(collect.toString());
		}
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("status", HttpStatus.OK.toString());
		response.put("Data", fetchById);
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if (request.getMethod().equals("OPTIONS")) {
			// response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			// throw new NoSuchElementException("You are Not authorized");
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "unatharised Access");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);

			ErrorResponse errorResponse = new ErrorResponse();
			errorResponse.setCode(401);
			errorResponse.setMessage("Unauthorized Access");

			byte[] responseToSend = restResponseBytes(errorResponse);
			((HttpServletResponse) response).setHeader("Content-Type", "application/json");
			((HttpServletResponse) response).setStatus(401);
			response.getOutputStream().write(responseToSend);
			return;
		} else {
			// Validate and set Access-Control-Allow-Origin header
			String origin = request.getHeader("Origin");
			response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, origin);
			response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST");

			response.setHeader("Strict-Transport-Security", "false");
			response.setHeader("X-Frame-Options", "");
			response.setHeader("X-Content-Type-Options", "nosniff");
			response.setHeader("Content-Security-Policy", "src");
			response.setHeader("X-XSS-Protection", "0");

			filterChain.doFilter(request, response);
		}

	}

	private byte[] restResponseBytes(ErrorResponse eErrorResponse) throws IOException, JsonProcessingException {
		String serialized = new ObjectMapper().writeValueAsString(eErrorResponse);
		return serialized.getBytes();
	}

	@PostMapping("/testing/userPermissionEncy")
	public List<UserRole> testingUserPermissionEncy() throws Exception {
		return service.testingUserPermissionEncy();
	}

}
