package com.suryoday.uam.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import com.suryoday.uam.pojo.HRUsersData;
import com.suryoday.uam.pojo.BranchMaster;
import com.suryoday.uam.pojo.PermissionDto;
import com.suryoday.uam.pojo.User;
import com.suryoday.uam.pojo.UserRole;

public interface UserService {

	public User addUser(String jsonRequest) throws Exception;

	public User updateUser(String id, String jsonRequest) throws JsonParseException, JsonMappingException, IOException;

	public int deleteUser(String id);

	public List<User> fetchAll();

	public User fetchById(String id);

	public UserRole insertData(String jsonRequest);

	public UserRole updateData(String jsonRequest);

	public UserRole getById(Long id);

	public boolean deleteUserRole(Long id);

	public List<UserRole> getAll();

	public UserRole getUserRoleByUserName(String userName);

	public String getSessionId(String id, HttpServletRequest request);

	public boolean validateSessionId(String id, HttpServletRequest request);

	public List<String> fetchAllState();

	public List<String> fetchAllCity(String state);

	public List<String> fetchAllArea(String state, String city);

	public List<Long> getBranchId(String state1);

	public List<Long> getBranchId(String state1, String city);

	public List<Long> getBranchId(String state1, String city, String area);

	public boolean checkDevice(String userId, String deviceId, String serverToken);

	public List<User> findByFilter(String request) throws JsonParseException, JsonMappingException, IOException;

	public List<User> findByFilterAll(String request) throws JsonParseException, JsonMappingException, IOException;

	public void approveUser(String userId, String approvedBy, String isActive);

	public String activeuser();

	public String logout(String X_Session_ID);

	public BranchMaster fetchBranchInfo(String branchId);

	public HashSet<String> getlistofBranch(String state);

	public List<String> fetchAllRoles();

	public String getProductGroup(String userId);

	public HRUsersData fetchUserFromHr(String userId);

	public void saveUser(User newUser);

	public List<PermissionDto> getPermissions(String userRole, String moduleName, String channel, String userId);

	public void save(User fetchById);

	public String validateUser(String x_User_ID);

}
