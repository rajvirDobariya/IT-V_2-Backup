package com.suryoday.CustomerIntraction.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.suryoday.CustomerIntraction.Pojo.CustomerIntractionDetails;

@Repository
public interface CustomerIntractionRepository extends JpaRepository<CustomerIntractionDetails, String> {

	@Query(value = "select SESSION_ID from SPRING_SESSION where SESSION_ID =:sessionid", nativeQuery = true)
	public String getAllSessionIds(String sessionid);

	@Query("SELECT DISTINCT c.meetingNumber, c.createdBy, c.status, c.createdDate, c.meetingDate, c.roRemark, c.currentMonth "
			+ "FROM CustomerIntractionDetails c "
			+ "WHERE c.status IN ('Pending at Checker', 'Pending at Maker', 'Submitted') AND"
			+ "      c.meetingDate >= :startDate AND c.meetingDate <= :endDate "
			+ "      OR (:meetingNumber IS NULL OR c.meetingNumber = :meetingNumber) " + "ORDER BY c.meetingDate DESC")
	public List<Object[]> getByROMeetingNumber(@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate, @Param("meetingNumber") String meetingNumber);

	@Query("SELECT DISTINCT c.meetingNumber, c.createdBy, c.status, c.createdDate, c.meetingDate, c.bmRemark, c.currentMonth "
			+ "FROM CustomerIntractionDetails c " + "WHERE c.status IN ('Pending at Checker', 'Submitted') AND"
			+ "      c.meetingDate >= :startDate AND c.meetingDate <= :endDate "
			+ "      OR (:meetingNumber IS NULL OR c.meetingNumber = :meetingNumber) " + "ORDER BY c.meetingDate DESC")
	public List<Object[]> getByBMMeetingNumber(@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate, @Param("meetingNumber") String meetingNumber);

	@Query("SELECT c FROM CustomerIntractionDetails c WHERE c.meetingNumber = :meetingNumber")
	public CustomerIntractionDetails findByMeetingNumber(String meetingNumber);

	@Query(value = "FROM CustomerIntractionDetails c WHERE c.meetingDate >= :startDate AND c.meetingDate < :endDate ORDER BY c.meetingDate DESC")
	List<CustomerIntractionDetails> findByMonthYear(@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate);

	@Query(value = "select user_name from tbl_hr_user_data where id =:empId", nativeQuery = true)
	public String getEmployeeData(String empId);

	@Query("SELECT DISTINCT c.meetingNumber, c.createdBy, c.status, c.createdDate, c.meetingDate, c.creditOpsRemark, c.currentMonth "
			+ "FROM CustomerIntractionDetails c " + "WHERE "
			+ "      c.meetingDate >= :startDate AND c.meetingDate <= :endDate "
			+ "      OR (:meetingNumber IS NULL OR c.meetingNumber = :meetingNumber) " + "ORDER BY c.meetingDate DESC")
	public List<Object[]> getByCrMeetingNumber(LocalDate startDate, LocalDate endDate, String meetingNumber);

}
