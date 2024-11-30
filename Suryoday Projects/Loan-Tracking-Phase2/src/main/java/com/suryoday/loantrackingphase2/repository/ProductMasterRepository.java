package com.suryoday.loantrackingphase2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.suryoday.loantrackingphase2.dto.ProductMasterDTO;
import com.suryoday.loantrackingphase2.model.ProductMaster;

@Repository
public interface ProductMasterRepository extends JpaRepository<ProductMaster, Long> {

	@Query("SELECT new com.suryoday.loantrackingphase2.dto.ProductMasterDTO(p.id, p.productName) FROM ProductMaster p")
	public List<ProductMasterDTO> findAllProducts();

	@Query(value = "select SESSION_ID from SPRING_SESSION where SESSION_ID =:sessionid", nativeQuery = true)
	public String getAllSessionIds(String sessionid);

	@Query(value = "SELECT user_name FROM tbl_hr_user_data WHERE id =:empId", nativeQuery = true)
	public String getEmployeeNameById(Integer empId);

	@Query(value = "select SESSION_ID from SPRING_SESSION where SESSION_ID =:sessionId", nativeQuery = true)
	public String checkSessingId(String sessionId);

}
