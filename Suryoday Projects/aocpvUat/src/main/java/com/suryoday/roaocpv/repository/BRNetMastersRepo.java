package com.suryoday.roaocpv.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.suryoday.roaocpv.pojo.BRNetMasters;

@Component
@Repository
public interface BRNetMastersRepo extends JpaRepository<BRNetMasters, String> {

	@Query(value = "Select a from BRNetMasters a where a.ID=:categotyType And a.Description=:param")
	Optional<BRNetMasters> fetchBrMaster(String categotyType, String param);

}
