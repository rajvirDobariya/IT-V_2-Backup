package com.suryoday.uam.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.suryoday.uam.pojo.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

	Optional<User> findByUserId(String id);

	@Query(value = "select SESSION_ID from SPRING_SESSION where SESSION_ID =:sessionid", nativeQuery = true)
	String getAllSessionIds(@Param("sessionid") String sessionid);

	@Query(value = "select SESSION_ID,PRIMARY_ID from SPRING_SESSION where SESSION_ID =:sessionid", nativeQuery = true)
	List<String> getAllSessionIdsWeb(@Param("sessionid") String sessionid);
	
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM SPRING_SESSION  where PRIMARY_ID=:primaryID", nativeQuery = true)
	public int deleteSession(@Param("primaryID") String primaryID);

	//@Query(value = "select SESSION_PRIMARY_ID from SPRING_SESSION_ATTRIBUTES where ATTRIBUTE_NAME =:userID1", nativeQuery = true)
	//Optional<String> fetchUser(@Param("userID1") String userID1);

	@Query(value = "select SESSION_PRIMARY_ID from SPRING_SESSION_ATTRIBUTES where ATTRIBUTE_NAME =:userID1", nativeQuery = true)
	List<String> fetchUser(@Param("userID1") String userID1);
	
	@Query(value = "SELECT distinct a.state from User a ")
	List<String> fetchAllState();

	@Query(value = "SELECT distinct a.city from User a where a.state =:state ")
	List<String> fetchAllCity(String state);

	@Query(value = "SELECT distinct a.area from User a where a.state =:state and a.city =:city")
	List<String> fetchAllarea(String state, String city);

	@Query(value = "SELECT distinct a.branchId from User a where a.state =:state1")
	List<Long> fetchallbranchId(String state1);

	@Query(value = "SELECT distinct a.branchId from User a where a.state =:state1 and a.city =:city")
	List<Long> fetchBranchId1(String state1, String city);

	@Query(value = "SELECT distinct a.branchId from User a where a.state =:state1 and a.city =:city and a.area =:area")
	List<Long> fetchBranchId2(String state1, String city, String area);

	@Query(value = "SELECT TOP(10) * FROM tbl_user", nativeQuery = true)
	public List<User> findAll();

	@Query(value = "select emp_id from tbl_user where emp_id =:employeeId", nativeQuery = true)
	public Long findByEmployeeId(@Param("employeeId") Long employeeId);

	@Query(value = "select * from tbl_user where  branch_id=:branchId or emp_id=:employeeId ", nativeQuery = true)
	public List<User> getByBranchIdOrEmpId(@Param("branchId") Long branchId, @Param("employeeId") Long employeeId);

	@Query(value = "select * from tbl_user where  branch_id=:branchId and emp_id=:employeeId ", nativeQuery = true)
	public List<User> getByBranchIdAndEmpId(@Param("branchId") Long branchId, @Param("employeeId") Long employeeId);

	@Query(value = "select * from tbl_user where  is_active=:status and state=:state  ", nativeQuery = true)
	public List<User> getByStatusAndState(@Param("status") Long status, @Param("state") String state);

	@Query(value = "select * from tbl_user where  is_active=:status ", nativeQuery = true)
	public List<User> getByStatus(@Param("status") Long status);

	@Query(value = "select * from tbl_user where  is_active=:status and state=:state and city=:city ", nativeQuery = true)
	public List<User> getStatusAndStateAndCity(@Param("status") Long status, @Param("state") String state,
			@Param("city") String city);

	@Query(value = "select * from tbl_user where  is_active=:status and state=:state and city=:city and area =:area", nativeQuery = true)
	public List<User> getFilterByAll(@Param("status") Long status, @Param("state") String state,
			@Param("city") String city, @Param("area") String area);

	@Query(value = "select * from tbl_user where state=:state", nativeQuery = true)
	public List<User> getByState(@Param("state") String state);

	@Query(value = "select * from tbl_user where state=:state and city=:city", nativeQuery = true)
	public List<User> getByStateAndCity(@Param("state") String state, @Param("city") String city);

	@Query(value = "select * from tbl_user where state=:state and city=:city and area =:area", nativeQuery = true)
	public List<User> getByStateCityArea(@Param("state") String state, @Param("city") String city,
			@Param("area") String area);
	
	@Query(value = "SELECT * FROM tbl_user", nativeQuery = true)
	public List<User> findAllState();
   
	@Modifying
	@Transactional
	@Query(value="update User set approvedBy =:approvedBy,approvedDate=:localDateTime,isActive=:isActive1 , status='Approved' where userId=:userId")
	void approvedUser(String userId, String approvedBy, long isActive1, LocalDateTime localDateTime);
	
	@Query(value = "select COUNT(SESSION_ID) AS activesessionid from spring_session;", nativeQuery = true)
	String activeusers();

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM SPRING_SESSION  where [SESSION_ID]=:X_Session_ID", nativeQuery = true)
	void logoutSession(String X_Session_ID);

	@Query(value = "SELECT a.branchIdArray from User a where a.state =:state1")
	List<String> getlistofBranch(String state1);
	
	@Query(value = "SELECT * FROM tbl_user where [user_permission_id]=:userpermissionid ", nativeQuery = true)
	List<User> fetchByCredit(String userpermissionid);

	@Query(value = "SELECT [user_name] FROM tbl_user where emp_id=:userId ", nativeQuery = true)
	String getUserName(String userId);

	@Query(value = "SELECT [model_group_name] FROM [tbl_model_group] ", nativeQuery = true)
	List<String> fetchAllRoles();

	@Query(value = "select [productGroup] from Lead.dbo.[Product_UserGroup] where [userId] =:userId", nativeQuery = true)
	String getProductGroup(String userId);

	@Transactional
	@Modifying
	@Query(value = "INSERT Lead.dbo.[Product_UserGroup] ([branchId],[userId],[productGroup],[createdDate]) VALUES (:branchId,:userId,:productGroup,:now)",nativeQuery = true)
	void saveProductUser(String branchId, String userId, String productGroup, LocalDateTime now);

	@Query(value = "select b.SESSION_ID  from [SPRING_SESSION_ATTRIBUTES]  as a inner join [SPRING_SESSION] as b on a.SESSION_PRIMARY_ID=b.PRIMARY_IDwhere a.ATTRIBUTE_NAME=:userId", nativeQuery = true)
	Optional<String> validateUser(String userId);
}
