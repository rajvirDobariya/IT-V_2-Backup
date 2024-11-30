package com.suryoday.aocpv.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import com.suryoday.connector.pojo.MainResponseDto;
import com.suryoday.connector.pojo.PageGroup;
import com.suryoday.connector.pojo.PermissionDto;
import com.suryoday.connector.pojo.User;
import com.suryoday.connector.pojo.UserRole;
import com.suryoday.connector.repository.PageGroupRepository;
import com.suryoday.connector.repository.UserRepository;
import com.suryoday.connector.repository.UserRoleRepository;
import com.suryoday.connector.service.UserService;
import com.suryoday.connector.serviceImpl.GenerateProperty;

@RestController
@CrossOrigin("*")
@RequestMapping("/aocpv/v1")
public class LoginLdapController {

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

			org.json.JSONObject jm = jk.getJSONObject("Data");

			String userId = jm.getString("UserID");
			String password = jm.getString("Password");
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
			if (password.isEmpty() || password == null) {
				throw new EmptyInputException("please enter password");
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
					Optional<UserRole> findById2 = userRoleRepository.findByUserName("RO");
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
//					mainResponseDto.setSERVER_TOKEN("BDHDHFDDFH6483748");
				String sessionId = "";
				if (jm.has("browserName")) {
					String browserName = jm.getString("browserName");
					if (fetchById.getLastBrowserName() != null && fetchById.getLastBrowserName().equals(browserName)) {
						sessionId = service.getSessionIdForWeb(fetchById.getUserId(), request, "same");
					} else {
						sessionId = service.getSessionId(fetchById.getUserId(), request);
					}
					fetchById.setLastBrowserName(browserName);
				} else {
					sessionId = service.getSessionId(fetchById.getUserId(), request);
				}
				mainResponseDto.setSERVER_TOKEN(sessionId);
				mainResponseDto.setCreatedBy(fetchById.getCreatedBy());
				mainResponseDto.setCreatedDate(fetchById.getCreatedDate());
				mainResponseDto.setUpdatedDate(fetchById.getUpdatedDate());
				mainResponseDto.setLastAccessTime(fetchById.getLastAccessTime());
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
				List<String> access = dtos.stream().map(PermissionDto::getName).collect(Collectors.toList());

				List<String> collect = mainResponseDto.getUserAccess().stream().filter(e -> access.contains(e))
						.collect(Collectors.toList());

				mainResponseDto.setUserAccess(collect);
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
				UserStatus = "WRONG USERNAME OR PASSWORD";

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

		List<PermissionDto> list = service.getPermissions(userRole, moduleName, CHANNEL);

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
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		org.json.JSONObject jsonObject = new org.json.JSONObject(jsonRequest);
		String userId = jsonObject.getJSONObject("Data").getString("userId");
		User fetchById = service.fetchById(userId);
		fetchById.setUserRole(null);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("status", HttpStatus.OK.toString());
		response.put("Data", fetchById);
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}
}
