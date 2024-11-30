package com.suryoday.aocpv.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.aocpv.exceptionhandling.EmptyInputException;
import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.aocpv.pojo.AocpvLastLogin;
import com.suryoday.aocpv.pojo.HRUsersData;
import com.suryoday.aocpv.service.AocpService;
import com.suryoday.aocpv.service.AocpvLastLoginService;
import com.suryoday.aocpv.service.LoginLdapService;
import com.suryoday.connector.pojo.BranchInfo;
import com.suryoday.connector.pojo.MainResponseDtoWeb;
import com.suryoday.connector.pojo.PageGroup;
import com.suryoday.connector.pojo.PermissionDto;
import com.suryoday.connector.pojo.User;
import com.suryoday.connector.pojo.UserRole;
import com.suryoday.connector.rd.util.AppzillonAESUtils;
import com.suryoday.connector.rd.util.Crypt;
import com.suryoday.connector.rd.util.EncrtionAngulurTest;
import com.suryoday.connector.repository.PageGroupRepository;
import com.suryoday.connector.repository.UserRepository;
import com.suryoday.connector.repository.UserRoleRepository;
import com.suryoday.connector.service.UserService;
import com.suryoday.connector.serviceImpl.GenerateProperty;

@RestController
@CrossOrigin(origins = { "http://localhost:4200", "http://10.20.25.37:443", "https://sarathi.suryodaybank.com/",
		"http://10.20.18.66:443", "https://sarathi.suryodaybank.co.in/" })
@RequestMapping("/aocpv/v1")
public class LoginLdapEncyWebController {

	@Autowired
	AocpService aocpvservice;

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
	private UserRoleRepository userRoleRepository;

	@RequestMapping(value = "/netbanking/validate/userEncyWeb", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> login(@RequestBody String bm, HttpServletRequest request,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "channel", required = true) String CHANNEL,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(bm);
		String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");

//		GenerateProperty x = GenerateProperty.getInstance();
//		x.getappprop();
//		String key = x.ENCYKEY;
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
		String originalInput = "$ARATHI@" + sdf.format(new Date());
		String key = Base64.getEncoder().encodeToString(originalInput.getBytes());

		String decryptContainerString = "";
		// System.out.println("encryptedString"+encryptString);
		if (CHANNEL.equalsIgnoreCase("WEB")) {
			decryptContainerString = Crypt.decrypt(encryptString, key);
		} else {
			decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
		}

		// System.out.println("decryptContainerString"+decryptContainerString);
		String data = "";

		// System.out.println("welcome" + bm);
		JSONObject Data = new JSONObject();
		String acc_type = "";
		if (X_Request_ID.equals("AOCPV")) {

			// call db
			// select quey if found
			// if app version is match
			// return allow
			// else please update apk
			System.out.println("hi ");

//			
//			

			org.json.JSONObject jk = new org.json.JSONObject(decryptContainerString);

			org.json.JSONObject jm = jk.getJSONObject("Data");

			String userId = jm.getString("UserID");
			String password = jm.getString("Password");
			if (password.isEmpty() || password == null) {
				throw new EmptyInputException("please  enter password");
			}

			if (!userId.equals(X_User_ID)) {
				throw new EmptyInputException("Invalid UserId");
			}
			org.json.JSONObject ldapContext = getLdapContext(userId, password);

			String serviceStatus = ldapContext.getJSONObject("data").getString("TransactionCode").toString();
			System.out.println(serviceStatus); // 00
			// db call 30639 Welcome@123 /
			System.out.println("start");
			if (serviceStatus.equals("00")) {

				User fetchById = service.fetchById(userId);

				System.out.println("userdetails" + fetchById);
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
					Optional<UserRole> findById2 = userRoleRepository.findByUserName("RO");
					if (findById2.isPresent()) {
						UserRole userRole = findById2.get();
						newUser.setUserRole(userRole);
					}
					service.saveUser(newUser);
					fetchById = newUser;
//					throw new NoSuchElementException("You are Not authorized to View");
				}
				MainResponseDtoWeb mainResponseDto = new MainResponseDtoWeb();
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
				mainResponseDto.setCreatedBy(fetchById.getCreatedBy());
				mainResponseDto.setCreatedDate(fetchById.getCreatedDate());
				mainResponseDto.setUpdatedDate(fetchById.getUpdatedDate());
				mainResponseDto.setDepartment(fetchById.getDepartment());
				mainResponseDto.setLevel(fetchById.getLevel());
				mainResponseDto.setEmailId(fetchById.getEmailId());
				mainResponseDto.setAllowCreditAccess(fetchById.getAllowCreditAccess());
				String userAccess = fetchById.getUserAccess();
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
				// mainResponseDto.setSERVER_TOKEN("BDHDHFDDFH6483748");
				UserRole userRole = fetchById.getUserRole();
				String userName = userRole.getUserName();
				mainResponseDto.setUserRoleName(userName);
				List<PermissionDto> dtos = new ArrayList<>();
				String pageGroupId = fetchById.getUserRole().getPageGroupId();
				System.out.println("pageGroupId" + pageGroupId);
				JSONArray array = new JSONArray(pageGroupId);
				for (int i = 0; i < array.length(); i++) {

					String object = (String) array.get(i);
					Long sid = Long.parseLong(object);
					System.out.println(sid);
					if (sid != null) {
						Optional<PageGroup> findById2 = pageGroupRepository.findById(sid);

						if (findById2.isPresent()) {

							PageGroup pageGroup = findById2.get();
							System.out.println("chaneel" + CHANNEL);
							if (pageGroup.getChannel().equals(CHANNEL)) {
								PermissionDto dto = new PermissionDto();
								dto.setIndex(pageGroup.getId());
								dto.setName(pageGroup.getModuleGroup().getModelGroupName());
								dto.setPage(pageGroup.getPageName());
								dto.setTitle(pageGroup.getPageName());
								dto.setPage(pageGroup.getPage());
								dto.setChannel(pageGroup.getChannel());
								dtos.add(dto);
							}
						}
					}
				}
				List<String> access = dtos.stream().map(PermissionDto::getName).collect(Collectors.toList());

				List<String> collect = mainResponseDto.getUserAccess().stream().filter(e -> access.contains(e))
						.collect(Collectors.toList());

				mainResponseDto.setUserAccess(collect);

				String sessionId = "";
				if (jm.has("browserName")) {
					String browserName = jm.getString("browserName");
					if (fetchById.getLastBrowserName() != null && fetchById.getLastBrowserName().equals(browserName)) {
						sessionId = service.getSessionIdForWeb(fetchById.getUserId(), request, "same");
					} else {
						sessionId = service.getSessionIdForWeb(fetchById.getUserId(), request, "different");
					}
					fetchById.setLastBrowserName(browserName);
				} else {
					sessionId = service.getSessionId(fetchById.getUserId(), request);
				}

				mainResponseDto.setSERVER_TOKEN(sessionId);
				String substring = sessionId.substring(0, Math.min(sessionId.length(), 16));
				String encodeKey = EncrtionAngulurTest.encodeKey(substring);
				mainResponseDto.setServerKey(encodeKey);
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MMM-dd HH:mm");
				LocalDateTime now = LocalDateTime.now();
				String todayDate = dtf.format(now);

				AocpvLastLogin aocpvLastLogin = new AocpvLastLogin();
				aocpvLastLogin.setUserId(userId);
				aocpvLastLogin.setSesionId(sessionId);
				aocpvLastLogin.setLastRequestTime(fetchById.getLastAccessTime());
				aocpvLastLogin.setLoginTime(now);
				if (jm.has("AppId")) {
					String AppId = jm.getString("AppId");
					aocpvLastLogin.setAppId(AppId);
				}
				if (jm.has("DeviceId")) {
					String DeviceId = jm.getString("DeviceId");
					aocpvLastLogin.setDeviceId(DeviceId);
				}
				if (jm.has("sourceIP")) {
					String sourceIP = jm.getString("sourceIP");
					aocpvLastLogin.setSourceIp(sourceIP);
				}
				if (jm.has("VersionNo")) {
					String VersionNo = jm.getString("VersionNo");
					aocpvLastLogin.setVersionNo(VersionNo);
				}
				aocpvLastLoginService.save(aocpvLastLogin);
				fetchById.setLastAccessTime(todayDate);
				userRepository.save(fetchById);
				mainResponseDto.setPermissions(dtos);

				org.json.JSONObject j = new org.json.JSONObject(mainResponseDto);

				data = j.toString();
				System.out.println("end encry" + data);
				String encryptString2 = "";
				if (CHANNEL.equalsIgnoreCase("WEB")) {
					encryptString2 = Crypt.encrypt(data, key);
				} else {
					encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
				}

				org.json.JSONObject data2 = new org.json.JSONObject();
				data2.put("value", encryptString2);
				org.json.JSONObject data3 = new org.json.JSONObject();
				data3.put("Data", data2);
				return new ResponseEntity<>(data3.toString(), HttpStatus.OK);

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

				data1.put("code", HttpStatus.BAD_REQUEST);
				data1.put("message", "Invalid Or Wrong Password");
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
		String responseToString = Data.toString();

		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(responseToString, key);
		org.json.JSONObject data2 = new org.json.JSONObject();
		data2.put("value", encryptString2);
		org.json.JSONObject data3 = new org.json.JSONObject();
		data3.put("Data", data2);

		return new ResponseEntity<Object>(data3, HttpStatus.BAD_REQUEST);

	}

	public org.json.JSONObject getLdapContext(String username, String password) throws IOException {
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
		// String url = "ldap://10.20.34.11:389";
		// String url ="ldap://10.40.34.11:389";
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
				UserStatus = "Wrong UserName Or Password";

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

	@RequestMapping(value = "/getPermissionsEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchByApplicationNo(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "channel", required = true) String CHANNEL,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = service.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = Crypt.decrypt(encryptString, X_encode_ID);

			org.json.JSONObject jsonObject = new org.json.JSONObject(decryptContainerString);
			String userRole = jsonObject.getJSONObject("Data").getString("userRole");
			String moduleName = jsonObject.getJSONObject("Data").getString("moduleName");

			List<PermissionDto> list = service.getPermissions(userRole, moduleName, CHANNEL);
			JSONArray j = new JSONArray(list);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			response.put("status", HttpStatus.OK.toString());
			response.put("Data", j);
			String data = response.toString();
			String encryptString2 = Crypt.encrypt(data, X_encode_ID);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			return new ResponseEntity<Object>(data3, HttpStatus.OK);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/getUserDetailsEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> getUserDetails(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "channel", required = true) String CHANNEL,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = service.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = Crypt.decrypt(encryptString, X_encode_ID);
			org.json.JSONObject jsonObject = new org.json.JSONObject(decryptContainerString);
			String userId = jsonObject.getJSONObject("Data").getString("userId");
			User fetchById = service.fetchById(userId);
			if (fetchById == null) {
				throw new NoSuchElementException("No record found");
			}
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			response.put("status", HttpStatus.OK.toString());
			org.json.JSONObject j = new org.json.JSONObject(fetchById);
			response.put("Data", j);
			String data = response.toString();
			String encryptString2 = Crypt.encrypt(data, X_encode_ID);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			return new ResponseEntity<Object>(data3, HttpStatus.OK);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);

			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}
}
