package com.suryoday.connector.serviceImpl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.aocpv.pojo.HRUsersData;
import com.suryoday.aocpv.repository.HRUserDataRepo;
import com.suryoday.connector.pojo.BranchMaster;
import com.suryoday.connector.pojo.NotificationDevice;
import com.suryoday.connector.pojo.PageGroup;
import com.suryoday.connector.pojo.PermissionDto;
import com.suryoday.connector.pojo.User;
import com.suryoday.connector.pojo.UserDto;
import com.suryoday.connector.pojo.UserRole;
import com.suryoday.connector.rd.util.EncrtionAngulurTest;
import com.suryoday.connector.repository.BranchInfoRepository;
import com.suryoday.connector.repository.NotificationDeviceRepository;
import com.suryoday.connector.repository.PageGroupRepository;
import com.suryoday.connector.repository.UserRepository;
import com.suryoday.connector.repository.UserRoleRepository;
import com.suryoday.connector.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserRoleRepository userRoleRepository;

	@Autowired
	private NotificationDeviceRepository notificationDeviceRepository;

	@Autowired
	private BranchInfoRepository branchInfo;

	@Autowired
	private HRUserDataRepo hruserRepository;

	@Autowired
	PageGroupRepository pageGroupRepository;

	public User addUser(String jsonRequest) throws Exception {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		String userId = jsonObject.getJSONObject("Data").getString("userId");
		String userName = jsonObject.getJSONObject("Data").getString("userName");
		long branchId1 = 0;
		if (jsonObject.getJSONObject("Data").has("branchId")) {
			String branchId = jsonObject.getJSONObject("Data").getString("branchId");
			branchId1 = Long.parseLong(branchId);
		}
		String isAdmin = jsonObject.getJSONObject("Data").getString("isAdmin");
		boolean isAdmin1 = Boolean.parseBoolean(isAdmin);
		String userRoleName = jsonObject.getJSONObject("Data").getString("userRoleName");
		// Long userRoleId1 = Long.parseLong(userRoleId);
		String isActive = "0";
		Long isActive1 = Long.parseLong(isActive);
		String empId = jsonObject.getJSONObject("Data").getString("empId");
		Long empId1 = Long.parseLong(empId);
		String mobileNo = jsonObject.getJSONObject("Data").getString("mobileNo");
		String branchName = "";
		if (jsonObject.getJSONObject("Data").has("branchName")) {
			branchName = jsonObject.getJSONObject("Data").getString("branchName");
		}
		String designation = jsonObject.getJSONObject("Data").getString("designation");
		String city = jsonObject.getJSONObject("Data").getString("city");
		String state = jsonObject.getJSONObject("Data").getString("state");
		String emailId = jsonObject.getJSONObject("Data").getString("emailId");
		String area = jsonObject.getJSONObject("Data").getString("area");
		String createdBy = jsonObject.getJSONObject("Data").getString("createdBy");
		String allowCreditAccess = jsonObject.getJSONObject("Data").getString("allowCreditAccess");
		String userAccess = jsonObject.getJSONObject("Data").getJSONArray("userAccess").toString();
		String branchIdArray = jsonObject.getJSONObject("Data").getJSONArray("branchIdArray").toString();
		Long findByEmployeeId = userRepository.findByEmployeeId(empId1);
		if (findByEmployeeId != null) {
			throw new Exception("Employee ID is already exists");
		} else {
			User user = new User();
			user.setUserId(userId);
			user.setUserName(userName);
			user.setIsActive(isActive1);
			user.setAdmin(isAdmin1);
			user.setAllowCreditAccess(allowCreditAccess);
			if (jsonObject.getJSONObject("Data").has("department")) {
				String department = jsonObject.getJSONObject("Data").getString("department");
				user.setDepartment(department);
			}
			if (jsonObject.getJSONObject("Data").has("otherRole")) {
				String otherRole = jsonObject.getJSONObject("Data").getString("otherRole");
				user.setOtherRole(otherRole);
			}
			user.setBranchId(branchId1);
			user.setEmailId(emailId);
			user.setEmpId(empId1);
			user.setMobileNo(mobileNo);
			user.setBranchName(branchName);
			user.setDesignation(designation);
			user.setCity(city);
			user.setState(state);
			user.setArea(area);
			user.setStatus("Pending");
			user.setCreatedDate(LocalDateTime.now().toString());
			user.setUpdatedDate(LocalDateTime.now().toString());
			user.setCreatedBy(createdBy);
			user.setUserAccess(userAccess);
			user.setUpdatedBy(createdBy);
			user.setBranchIdArray(branchIdArray);
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");
			LocalDateTime now = LocalDateTime.now();
			String todayDate = dtf.format(now);
			user.setCreatedDate(todayDate);
			user.setUpdatedDate(todayDate);
			Optional<UserRole> findById2 = userRoleRepository.findByUserName(userRoleName);
			if (findById2.isPresent()) {
				UserRole userRole = findById2.get();
				user.setUserRole(userRole);
			}
			User save = userRepository.save(user);
			return save;
		}

	}

	public User updateUser(String id, String jsonRequest) throws JsonParseException, JsonMappingException, IOException {
		Optional<User> findById = userRepository.findById(id);
		JSONObject jsonObject = new JSONObject(jsonRequest);
		User user = null;
		if (findById.isPresent()) {
			user = findById.get();
			UserDto userDto = new ObjectMapper().readValue(jsonRequest, UserDto.class);
			if (userDto.getBranchId() != null) {
				user.setBranchId(userDto.getBranchId());
			} else {
				user.getBranchId();
			}
			if (userDto.getDepartment() != null) {
				user.setDepartment(userDto.getDepartment());
			} else {
				user.getDepartment();
			}
			if (userDto.getAllowCreditAccess() != null) {
				user.setAllowCreditAccess(userDto.getAllowCreditAccess());
			} else {
				user.getAllowCreditAccess();
			}
			if (userDto.getEmpId() != null) {
				user.setEmpId(userDto.getEmpId());
			} else {
				user.getEmpId();
			}
			if (userDto.getEmailId() != null) {
				user.setEmailId(userDto.getEmailId());
			} else {
				user.getEmailId();
			}
			if (userDto.getIsActive() != null) {
				user.setIsActive(userDto.getIsActive());
			} else {
				user.getIsActive();
			}
			if (jsonObject.has("userAccess")) {
				user.setUserAccess(jsonObject.getJSONArray("userAccess").toString());
			}

			if (userDto.getMobileNo() != null) {
				user.setMobileNo(userDto.getMobileNo());
			} else {
				user.getMobileNo();
			}
			if (userDto.getUserName() != null) {
				user.setUserName(userDto.getUserName());
			} else {
				user.getUserName();
			}
			if (userDto.getBranchName() != null) {
				user.setBranchName(userDto.getBranchName());
			} else {
				user.getBranchName();
			}
			if (userDto.getDesignation() != null) {
				user.setDesignation(userDto.getDesignation());
			} else {
				user.getDesignation();
			}
			if (userDto.getBranchIdArray() != null) {
				user.setBranchIdArray(userDto.getBranchIdArray());
			} else {
				user.getBranchIdArray();
			}
			// logger.debug(userDto.getUserRoleId());
			if (userDto.getUserRoleName() != null) {
				Optional<UserRole> id2 = userRoleRepository.findByUserName(userDto.getUserRoleName());
				logger.debug("Id" + id2.get());
				UserRole userRole = id2.get();
				user.setUserRole(userRole);
			} else {
				user.getUserRole();
			}
			if (userDto.isAdmin()) {
				user.setAdmin(userDto.isAdmin());
			} else {
				user.setAdmin(userDto.isAdmin());
			}
			if (userDto.getCity() != null) {
				user.setCity(userDto.getCity());
			} else {
				user.getCity();
			}
			if (userDto.getState() != null) {
				user.setState(userDto.getState());
			} else {
				user.getState();
			}
			if (userDto.getArea() != null) {
				user.setArea(userDto.getArea());
			} else {
				user.getArea();
			}
			if (userDto.getOtherRole() != null) {
				user.setOtherRole(userDto.getOtherRole().toString());
			} else {
				user.getOtherRole();
			}
			if (userDto != null) {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");
				LocalDateTime now = LocalDateTime.now();
				String todayDate = dtf.format(now);
				user.setUpdatedDate(todayDate);
				user.setUpdatedBy(userDto.getUpdatedBy());
			}
		}
		User save = userRepository.save(user);
		return save;
	}

	public int deleteUser(String id) {
		Optional<User> findById = userRepository.findById(id);
		if (findById.isPresent()) {
			userRepository.deleteById(id);
			return 1;
		}
		return 0;
	}

	public List<User> fetchAll() {
		List<User> findAll = userRepository.findAll();
		return findAll;
	}

	public User fetchById(String id) {
		Optional<User> findById = userRepository.findById(id);
		User user = null;
		if (findById.isPresent()) {
			user = findById.get();
		}
		return user;
	}

	public UserRole insertData(String jsonRequest) {

		UserRole userRole = new UserRole();
		JSONObject jsonObject = new JSONObject(jsonRequest);
		String userName = jsonObject.getJSONObject("Data").getString("userName");
		String permission = jsonObject.getJSONObject("Data").getJSONArray("permission").toString();
//		String id1 = jsonObject.getJSONObject("Data").getString("id");
//		Long id = Long.parseLong(id1);
//		userRole.setId(id);
		userRole.setPageGroupId(permission);
		userRole.setUserName(userName);
		UserRole save = userRoleRepository.save(userRole);
		return save;
	}

	public UserRole updateData(String jsonRequest) {
		UserRole userRole = null;
		JSONObject jsonObject = new JSONObject(jsonRequest);
		String Id = jsonObject.getJSONObject("Data").getString("id");
		long id = Long.parseLong(Id);
		Optional<UserRole> findById = userRoleRepository.findById(id);
		if (findById.isPresent()) {
			userRole = findById.get();

			String userName = jsonObject.getJSONObject("Data").getString("userName");

			String permission = jsonObject.getJSONObject("Data").getJSONArray("permission").toString();

			if (!userName.equals(null)) {
				userRole.setUserName(userName);
			} else {
				userRole.getUserName();
			}
			if (!permission.equals(null)) {
				userRole.setPageGroupId(permission);
			} else {
				userRole.getPageGroupId();
			}
		}
		UserRole save = userRoleRepository.save(userRole);
		return save;
	}

	public UserRole getById(Long id) {
		Optional<UserRole> findById = userRoleRepository.findById(id);
		UserRole userRole = null;
		if (findById.isPresent()) {
			userRole = findById.get();
		}
		return userRole;
	}

	public boolean deleteUserRole(Long id) {
		Optional<UserRole> findById = userRoleRepository.findById(id);
		if (findById.isPresent()) {
			userRoleRepository.deleteById(id);
			return true;
		}
		return false;
	}

	public List<UserRole> getAll() {
		List<UserRole> findAll = userRoleRepository.findAll();
		return findAll;
	}

	public UserRole getUserRoleByUserName(String userName) {
		Optional<UserRole> findByUserName = userRoleRepository.findByUserName(userName);
		UserRole userRole = null;
		if (findByUserName.isPresent()) {
			userRole = findByUserName.get();
		}

		return userRole;
	}

	public String getSessionId(String id, HttpServletRequest request) {
		Object attribute = request.getSession().getAttribute(id);
		String sessionId = "";
		System.out.println(attribute);
		if (attribute != null) {
			List<String> list = userRepository.fetchUser(id);
			if (list.size() != 0) {

				for (String primaryID : list) {
					int deleteSession = userRepository.deleteSession(primaryID);
				}
				request.getSession().invalidate();
				request.getSession().setAttribute(id, id);
				sessionId = request.getSession().getId();
				return sessionId;

			} else {
				request.getSession().invalidate();
				request.getSession().setAttribute(id, id);
				sessionId = request.getSession().getId();
				return sessionId;
			}
		} else {
			request.getSession().invalidate();
			request.getSession().setAttribute(id, id);
			sessionId = request.getSession().getId();
			return sessionId;
		}
	}

	public boolean validateSessionId(String sessionIdReq, HttpServletRequest request) {

		try {
			String allSessionIds = userRepository.getAllSessionIds(sessionIdReq);
			logger.debug(allSessionIds);

			if (!allSessionIds.isEmpty()) {

				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.getMessage();

			return false;
		}

	}

	@Override
	public List<String> fetchAllState() {
		List<String> list = userRepository.fetchAllState();
		if (list.isEmpty()) {
			throw new NoSuchElementException("list is empty");
		}
		return list;
	}

	@Override
	public List<String> fetchAllCity(String state) {
		List<String> list = userRepository.fetchAllCity(state);
		if (list.isEmpty()) {
			throw new NoSuchElementException("list is empty");
		}
		return list;
	}

	@Override
	public List<String> fetchAllArea(String state, String city) {
		List<String> list = userRepository.fetchAllarea(state, city);
		if (list.isEmpty()) {
			throw new NoSuchElementException("list is empty");
		}
		return list;
	}

	@Override
	public List<Long> getBranchId(String state1) {
		List<Long> list = userRepository.fetchallbranchId(state1);
		if (list.isEmpty()) {
			throw new NoSuchElementException("list is empty");
		}
		return list;
	}

	@Override
	public List<Long> getBranchId(String state1, String city) {
		List<Long> list = userRepository.fetchBranchId1(state1, city);
		if (list.isEmpty()) {
			throw new NoSuchElementException("list is empty");
		}
		return list;
	}

	@Override
	public List<Long> getBranchId(String state1, String city, String area) {
		List<Long> list = userRepository.fetchBranchId2(state1, city, area);
		if (list.isEmpty()) {
			throw new NoSuchElementException("list is empty");
		}
		return list;
	}

	@Override
	public boolean checkDevice(String userId, String deviceId, String serverToken) {
		String findByDeviceId = notificationDeviceRepository.findByDeviceId(deviceId);
		if (findByDeviceId == null) {
			NotificationDevice findByUserId = notificationDeviceRepository.findByUserId(userId);
			if (findByUserId == null) {
				NotificationDevice notificationDevice = new NotificationDevice();
				notificationDevice.setDeviceId(deviceId);
				notificationDevice.setUserId(userId);
				notificationDevice.setServerToken(serverToken);
				notificationDeviceRepository.save(notificationDevice);
				return true;
			} else {
				findByUserId.setDeviceId(deviceId);
				findByUserId.setServerToken(serverToken);

				notificationDeviceRepository.save(findByUserId);
				return true;
			}
		}
		return false;
	}

	@Override
	public List<User> findByFilter(String jsonRequest) throws JsonParseException, JsonMappingException, IOException {

		UserDto userDto = new ObjectMapper().readValue(jsonRequest, UserDto.class);
		Long branchId = userDto.getBranchId();
		Long empId = userDto.getEmpId();
		if (branchId == null || empId == null) {
			List<User> findByFilter = userRepository.getByBranchIdOrEmpId(branchId, empId);
			if (!findByFilter.isEmpty()) {
				return findByFilter;
			} else {
				throw new NoSuchElementException("list is empty");
			}
		} else if (branchId != null && empId != null) {
			List<User> byBranchIdAndEmpId = userRepository.getByBranchIdAndEmpId(branchId, empId);
			if (!byBranchIdAndEmpId.isEmpty()) {
				return byBranchIdAndEmpId;
			} else {
				throw new NoSuchElementException("list is empty");
			}
		}
		return null;
	}

	@Override
	public List<User> findByFilterAll(String jsonRequest) throws JsonParseException, JsonMappingException, IOException {
		UserDto userDto = new ObjectMapper().readValue(jsonRequest, UserDto.class);
		Long status = userDto.getIsActive();
		String state = userDto.getState();
		String area = userDto.getArea();
		String city = userDto.getCity();
		List<User> filterAll = new ArrayList<>();
		if (status == 2) {
			if (state.equalsIgnoreCase("All")) {
				filterAll = userRepository.findAllState();
				if (!filterAll.isEmpty()) {
					return filterAll;
				} else {
					throw new NoSuchElementException("list is empty");
				}
			} else {
				if (city.equalsIgnoreCase("All")) {
					filterAll = userRepository.getByState(state);
					if (!filterAll.isEmpty()) {
						return filterAll;
					} else {
						throw new NoSuchElementException("list is empty");
					}
				} else {
					if (area.equalsIgnoreCase("All")) {
						filterAll = userRepository.getByStateAndCity(state, city);
						if (!filterAll.isEmpty()) {
							return filterAll;
						} else {
							throw new NoSuchElementException("list is empty");
						}

					} else {
						filterAll = userRepository.getByStateCityArea(state, city, area);
						if (!filterAll.isEmpty()) {
							return filterAll;
						} else {
							throw new NoSuchElementException("list is empty");
						}
					}
				}
			}

		} else {
			if (state.equalsIgnoreCase("All")) {
				filterAll = userRepository.getByStatus(status);
				if (!filterAll.isEmpty()) {
					return filterAll;
				} else {
					throw new NoSuchElementException("list is empty");
				}

			} else {
				if (city.equalsIgnoreCase("All")) {
					filterAll = userRepository.getByStatusAndState(status, state);
					if (!filterAll.isEmpty()) {
						return filterAll;
					} else {
						throw new NoSuchElementException("list is empty");
					}

				} else {
					if (area.equalsIgnoreCase("All")) {
						filterAll = userRepository.getStatusAndStateAndCity(status, state, city);
						if (!filterAll.isEmpty()) {
							return filterAll;
						} else {
							throw new NoSuchElementException("list is empty");
						}

					} else {
						filterAll = userRepository.getFilterByAll(status, state, city, area);
						if (!filterAll.isEmpty()) {
							return filterAll;
						} else {
							throw new NoSuchElementException("list is empty");
						}

					}
				}
			}
		}

	}

	@Override
	public void approveUser(String userId, String approvedBy, String isActive) {
		LocalDateTime localDateTime = LocalDateTime.now();

		long isActive1 = Long.parseLong(isActive);
		userRepository.approvedUser(userId, approvedBy, isActive1, localDateTime);

	}

	@Override
	public String activeuser() {
		String allSessionIds = userRepository.activeusers();
		logger.debug(allSessionIds);

		return allSessionIds;
	}

	@Override
	public String logout(String X_Session_ID) {
		userRepository.logoutSession(X_Session_ID);
		return "logout sucessfully";
	}

	@Override
	public BranchMaster fetchBranchInfo(String branchId) {
		Optional<BranchMaster> optional = branchInfo.fetchByBranchId(branchId);
		if (optional.isPresent()) {
			return optional.get();
		}
		throw new NoSuchElementException("BranchId Not Present");
	}

	@Override
	public HashSet<String> getlistofBranch(String state) {
		HashSet<String> listofbranch = new HashSet<>();
		List<String> list = userRepository.getlistofBranch(state);
		if (list.size() == 0) {
			throw new NoSuchElementException("BranchId Not Present for this state");
		}
		for (String branchIdArray : list) {
			if (branchIdArray != null) {
				org.json.JSONArray branchIdArray1 = new org.json.JSONArray(branchIdArray);
				for (int n = 0; n < branchIdArray1.length(); n++) {
					String branchId = "";

					org.json.JSONObject jsonObject1 = branchIdArray1.getJSONObject(n);
					try {
						branchId = jsonObject1.getString("branchId");
					} catch (Exception e) {
						branchId = Long.toString(jsonObject1.getLong("branchId"));
					}
					listofbranch.add(branchId);
				}
			}
		}
		return listofbranch;
	}

	@Override
	public List<String> fetchAllRoles() {
		List<String> list = userRepository.fetchAllRoles();
		if (list.size() == 0) {
			throw new NoSuchElementException("List is Empty");
		}
		return list;
	}

	@Override
	public String getProductGroup(String userId) {
		String productGroup = userRepository.getProductGroup(userId);
		return productGroup;
	}

	@Override
	public HRUsersData fetchUserFromHr(String userId) {
		Optional<HRUsersData> optional = hruserRepository.fetchUserFromHr(userId);
		if (optional.isPresent()) {
			return optional.get();
		}
		throw new NoSuchElementException("No record found in hr data");
	}

	@Override
	public void saveUser(User newUser) {
		String branchId = String.valueOf(newUser.getBranchId());
		String userId = newUser.getUserId();
		LocalDateTime now = LocalDateTime.now();
		String productGroup = "Liabilities";
		userRepository.save(newUser);
		userRepository.saveProductUser(branchId, userId, productGroup, now);
	}

	@Override
	public List<PermissionDto> getPermissions(String userRole, String moduleName, String CHANNEL) {
		Optional<UserRole> optional = userRoleRepository.findByUserName(userRole);
		if (optional.isPresent()) {
			List<PermissionDto> dtos = new ArrayList<>();
			UserRole userRoles = optional.get();
			String pageGroupId = userRoles.getPageGroupId();
			JSONArray array = new JSONArray(pageGroupId);
			for (int i = 0; i < array.length(); i++) {

				String object = (String) array.get(i);
				Long sid = Long.parseLong(object);

				if (sid != null) {
					Optional<PageGroup> findById2 = pageGroupRepository.findById(sid);

					if (findById2.isPresent()) {

						PageGroup pageGroup = findById2.get();
						if (pageGroup.getChannel().equals(CHANNEL)
								&& pageGroup.getModuleGroup().getModelGroupName().equalsIgnoreCase(moduleName)) {
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
			return dtos;
		}
		throw new NoSuchElementException("No record found");
	}

	@Override
	public void save(User fetchById) {
		userRepository.save(fetchById);
	}

	@Override
	public String validateUser(String x_User_ID) {
		Optional<String> optional = userRepository.validateUser(x_User_ID);
		if (optional.isPresent()) {
			String sessionId = optional.get();
			String substring = sessionId.substring(0, Math.min(sessionId.length(), 16));
			String encodeKey = EncrtionAngulurTest.encodeKey(substring);
			return encodeKey;
		}
		return null;
	}

	@Override
	public String getSessionIdForWeb(String id, HttpServletRequest request, String browser) {
		Object attribute = request.getSession().getAttribute(id);
		String sessionId = "";

		if (attribute == null && browser.equals("different")) {
			List<String> list = userRepository.fetchUser(id);
			if (list.size() != 0) {

				for (String primaryID : list) {
					int deleteSession = userRepository.deleteSession(primaryID);
				}
				request.getSession().invalidate();
				request.getSession().setAttribute(id, id);
				sessionId = request.getSession().getId();
				return sessionId;

			} else {
				request.getSession().invalidate();
				request.getSession().setAttribute(id, id);
				sessionId = request.getSession().getId();
				return sessionId;
			}
		} else if (attribute != null && browser.equals("same")) {
			List<String> list = userRepository.fetchUser(id);
			String sessionPrimaryId = list.get(0);
			sessionId = userRepository.getSameSessionIdByUser(sessionPrimaryId);
			return sessionId;
		} else {
			request.getSession().invalidate();
			request.getSession().setAttribute(id, id);
			sessionId = request.getSession().getId();
			return sessionId;
		}
	}

	private void list(int i) {
		// TODO Auto-generated method stub

	}

}
