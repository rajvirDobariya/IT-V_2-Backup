package com.suryoday.twowheeler.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.suryoday.twowheeler.pojo.TWModelMaster;
@Component
@Repository
public interface TwoWheelerMastersRepo extends JpaRepository<TWModelMaster, String>{

	@Query(value="Select a from TWModelMaster a")
	List<TWModelMaster> fetchModelMaster();

	@Query(value="Select a from TWModelMaster a where a.manufacturerName=:manufacturerName and a.vehicleCategory=:vehicleType")
	List<TWModelMaster> fetchByVehicel(String manufacturerName, String vehicleType);

	@Query(value="Select a from TWModelMaster a where a.variantName=:variantId")
	Optional<TWModelMaster> fetchByVariant(String variantId);

	@Query(value="Select distinct a.manufacturerName from TWModelMaster a ")
	List<String> fetchManufacture();

	@Query(value="Select distinct a.modelName from TWModelMaster a where a.manufacturerName =:manufacturerName")
	List<String> fetchModel(String manufacturerName);

	@Query(value="Select distinct a.variantName from TWModelMaster a where a.modelName =:modelName")
	List<String> fetchVarient(String modelName);


}
