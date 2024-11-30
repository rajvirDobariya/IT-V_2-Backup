package com.suryoday.twowheeler.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.suryoday.twowheeler.pojo.DealerByPincode;
import com.suryoday.twowheeler.pojo.DealerMaster;

@Component
@Repository
public interface DealerMasterRepo extends JpaRepository<DealerMaster, String> {

//	@Query(value="Select a from DealerMaster a where a.dealerCode=:dealercode")
//	List<DealerMaster> fetchByDealerCode(String dealercode);

	@Query(value = "Select a from DealerMaster a where a.manufacturerDescription=:manufacturerName")
	List<DealerMaster> fetchAllDealers(String manufacturerName);

	@Query(value = "SELECT a from DealerMaster a WHERE a.dealerCode=:dealerCode")
	List<DealerMaster> fetchByDealerCode(String dealerCode);

	@Query(value = "SELECT new com.suryoday.twowheeler.pojo.DealerByPincode(l.dealerCode, l.dealerName, l.dealerAddress, l.branchIdArray) from DealerMaster l where l.pinCode=:pinCode")
	List<DealerByPincode> fetchDealerBypincode(String pinCode);

	@Query(value = "SELECT new com.suryoday.twowheeler.pojo.DealerByPincode(l.dealerCode, l.dealerName, l.dealerAddress, l.branchIdArray) from DealerMaster l where l.manufacturerDescription=:manufacturer")
	List<DealerByPincode> fetchAllDealer(String manufacturer);
}
