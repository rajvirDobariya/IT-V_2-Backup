package com.suryoday.Counterfeit.Repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.suryoday.Counterfeit.Pojo.Denomination;

@Repository
public interface DenominationRepository extends JpaRepository<Denomination, Long> {

	@Transactional
	public int deleteAllByCounterfeitId(Long counterfeitId);

	@Query("SELECT d.id, d.createdDate, d.detectDate, d.denominationNote, d.tendererAccountNumber, d.tendererCustomerName, d.serialNumber, d.securityFeatureBreached,"
			+ " c.id, c.dailyMonthly, c.branchCode, c.branchName"
			+ " FROM Counterfeit c INNER JOIN Denomination d ON c.id = d.counterfeitId"
			+ " WHERE c.id =:counterfeitId ORDER BY d.id")
	public List<Object[]> findByCounterfeitId(Long counterfeitId);

	@Query("SELECT d.denominationNote, COUNT(d.denominationNote), SUM(d.denominationNote)"
			+ " FROM Counterfeit c INNER JOIN Denomination d ON c.id = d.counterfeitId" + " WHERE c.id =:counterfeitId"
			+ " GROUP BY d.denominationNote ORDER BY d.denominationNote DESC")
	public List<Object[]> findSummaryByCounterfeitId(Long counterfeitId);

}
