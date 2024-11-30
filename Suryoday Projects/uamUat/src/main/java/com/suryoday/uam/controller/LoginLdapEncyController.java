package com.suryoday.uam.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;

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

import com.suryoday.uam.exceptionhandling.EmptyInputException;
import com.suryoday.uam.exceptionhandling.NoSuchElementException;
import com.suryoday.uam.others.AppzillonAESUtils;
import com.suryoday.uam.others.GenerateProperty;
import com.suryoday.uam.pojo.AocpvLastLogin;
import com.suryoday.uam.pojo.BranchInfo;
import com.suryoday.uam.pojo.MainResponseDto;
import com.suryoday.uam.pojo.PageGroup;
import com.suryoday.uam.pojo.PermissionDto;
import com.suryoday.uam.pojo.User;
import com.suryoday.uam.pojo.UserRole;
import com.suryoday.uam.repository.PageGroupRepository;
import com.suryoday.uam.repository.UserRepository;
import com.suryoday.uam.service.AocpService;
import com.suryoday.uam.service.AocpvLastLoginService;
import com.suryoday.uam.service.LoginLdapService;
import com.suryoday.uam.service.UserService;

@RestController
@CrossOrigin("*")
@RequestMapping("/sarathi")
public class LoginLdapEncyController {

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
	
	@RequestMapping(value = "/netbanking/validate/userency", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> login(@RequestBody String bm, HttpServletRequest request,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "channel", required = true) String CHANNEL,
			@RequestHeader(name = "versionNo", required = true) String versionNo,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(bm);
		String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");

		String key = X_Transaction_ID;

		//System.out.println("encryptedString"+encryptString);
		String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
		
		//System.out.println("decryptContainerString"+decryptContainerString);
		String data = "";

		//System.out.println("welcome" + bm);
		JSONObject Data = new JSONObject();
		String acc_type = "";
		if (X_Request_ID.equals("AOCPV")) {

			
			
			org.json.JSONObject jk = new org.json.JSONObject(decryptContainerString);

			org.json.JSONObject jm = jk.getJSONObject("Data");

			String userId = jm.getString("UserID");
			String encryptedpassword = jm.getString("Password");
			String decryptpassword = AppzillonAESUtils.decryptContainerString(encryptedpassword, key);
			org.json.JSONObject ldapContext = getLdapContext(userId, decryptpassword);
			 AocpvLastLogin aocpvLastLogin = new AocpvLastLogin();
				aocpvLastLogin.setUserId(userId);
				LocalDateTime now = LocalDateTime.now();
				aocpvLastLogin.setLoginTime(now);
				aocpvLastLogin.setVersionNo(versionNo);
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
				if (jm.has("longitude")) {
					String longitude = jm.getString("longitude");
					aocpvLastLogin.setLongitude(longitude);
				}
				if (jm.has("latitude")) {
					String latitude = jm.getString("latitude");
					aocpvLastLogin.setLatitude(latitude);
				}
				if(decryptpassword.isEmpty() || decryptpassword == null) {
					throw new EmptyInputException("please  enter password");
				}
			String serviceStatus = ldapContext.getJSONObject("data").getString("TransactionCode").toString();
			System.out.println(serviceStatus); // 00
			// db call 30639 Welcome@123 /
			System.out.println("start");
			if (serviceStatus.equals("00")) {

				User fetchById = service.fetchById(userId);
     
				System.out.println("userdetails"+fetchById);
				if (fetchById == null) {
					// return new ResponseEntity("Please Enter Vaild
					// Details",HttpStatus.BAD_REQUEST);
					throw new NoSuchElementException("You are Not authorized to View");
				}
				MainResponseDto mainResponseDto = new MainResponseDto();
				mainResponseDto.setUserId(fetchById.getUserId());
				mainResponseDto.setAdmin(fetchById.isAdmin());
				mainResponseDto.setBranchId(fetchById.getBranchId());
				mainResponseDto.setEmpId(fetchById.getEmpId());
				if(fetchById.getIsActive() ==0) {
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
				mainResponseDto.setLastAccessTime(fetchById.getLastAccessTime());
				List<BranchInfo> list=new ArrayList<>();
				if(fetchById.getBranchIdArray() != null) {
					
					org.json.JSONArray branchIdArray =new org.json.JSONArray(fetchById.getBranchIdArray());
					for(int n=0;n<branchIdArray.length();n++) {
						String branchId="";
						String branchName ="";
						org.json.JSONObject jsonObject = branchIdArray.getJSONObject(n);
						try {
							 branchId = jsonObject.getString("branchId");
							 branchName = jsonObject.getString("branchName");
							}
							catch (Exception e) {
								branchId =Long.toString(jsonObject.getLong("branchId"));
								branchName = jsonObject.getString("branchName");
							}
						BranchInfo branchInfo=new BranchInfo(branchId, branchName);
						list.add(branchInfo);
					}
					
				}
				mainResponseDto.setListofBranches(list);
				UserRole userRole = fetchById.getUserRole();
				String userName = userRole.getUserName();
				mainResponseDto.setUserRoleName(userName);
				List<PermissionDto> dtos = new ArrayList<>();
				String pageGroupId = fetchById.getUserRole().getPageGroupId();
				System.out.println("pageGroupId"+pageGroupId);
				JSONArray array = new JSONArray(pageGroupId);
				
				// get Pages for Other Roles
//				String otherRole = fetchById.getOtherRole();
//				if(otherRole!= null) {
//					dtos = aocpvservice.getPagesForOtherRole(otherRole, CHANNEL, userAccess, dtos);					
//				}				

				for (int i = 0; i < array.length(); i++) {

					String object = (String) array.get(i);
					Long sid = Long.parseLong(object);
					System.out.println(sid);
					if (sid != null) {
						Optional<PageGroup> findById2 = pageGroupRepository.findById(sid);

						if (findById2.isPresent()) {

							PageGroup pageGroup = findById2.get();
							System.out.println("chaneel"+CHANNEL);
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
							//service.checkSession(fetchById.getUserId());
				String sessionId = service.getSessionId(fetchById.getUserId(), request);
				mainResponseDto.setSERVER_TOKEN(sessionId);
				  DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MMM-dd HH:mm");
				String todayDate = dtf.format(now);
			
			
				aocpvLastLogin.setSesionId(sessionId);
				aocpvLastLogin.setLastRequestTime(fetchById.getLastAccessTime());
				String connectionstatus=	ldapContext.getJSONObject("data").getString("connectionstatus").toString();
			    aocpvLastLogin.setTransResponse(connectionstatus);
			    aocpvLastLogin.setTransStatus("S");
				
				aocpvLastLoginService.save(aocpvLastLogin);
				fetchById.setLastAccessTime(todayDate);
				userRepository.save(fetchById);
				mainResponseDto.setPermissions(dtos);
				
				
				org.json.JSONObject j= new org.json.JSONObject(mainResponseDto);
				
				data = j.toString();
				System.out.println("end encry"+data);
				String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
				System.out.println("end encry1"+encryptString2);
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
				String userStatus=	ldapContext.getJSONObject("data").getString("UserStatus").toString();
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
		String responseToString = Data.toString();
		
		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(responseToString, key);
		org.json.JSONObject data2 = new org.json.JSONObject();
		data2.put("value", encryptString2);
		org.json.JSONObject data3 = new org.json.JSONObject();
		data3.put("Data", data2);

		return new ResponseEntity<Object>(data3, HttpStatus.BAD_REQUEST);

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

}
