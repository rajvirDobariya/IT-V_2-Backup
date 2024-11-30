package com.suryoday.hastakshar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.suryoday.hastakshar.pojo.HastMaster;

@Repository
public interface MasterRepo extends JpaRepository<HastMaster, String> {

	@Query(value = "SELECT a.department FROM HastMaster a GROUP BY a.department")
	List<String> fetchDepartment();

	@Query(value = "SELECT a.transactionTypes from HastMaster a where a.department =:department AND a.State =:State")
	List<String> fetchTxnTypewithState(String department, String State);

	@Query(value = "SELECT a.transactionTypes from HastMaster a where a.department =:department")
	List<String> fetchTxnTypewithOutState(String department);

	@Query(value = "SELECT a from HastMaster a where a.transactionTypes =:transactionTypes AND a.State =:State")
	List<HastMaster> fetchRowDetails(String transactionTypes, String State);

	@Query(value = "SELECT a.keyword from HastMaster a where a.transactionTypes =:transactionType")
	List<String> fetchKeyword(String transactionType);

	@Query(value = "SELECT a from HastMaster a where a.transactionTypes =:transactionTypes AND a.transactionDescription =:transactionDescription")
	HastMaster fetchTxnTypeTxnDescp(String transactionTypes, String transactionDescription);
	
	public HastMaster findByTransactionTypes(String transactionType);
}
