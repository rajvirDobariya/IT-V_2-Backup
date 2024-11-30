package com.suryoday.twowheeler.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.suryoday.twowheeler.pojo.AssetMaster;

@Component
@Repository
public interface TWAssetMasterRepository extends JpaRepository<AssetMaster, String> {

	@Query(value = "Select distinct a.variant from AssetMaster a where a.model =:modelName")
	List<String> fetchVarient(String modelName);

	@Query(value = "Select distinct a.model from AssetMaster a where a.manufacturer =:manufacturerName")
	List<String> fetchModel(String manufacturerName);

	@Query(value = "Select distinct a.manufacturer from AssetMaster a ")
	List<String> fetchManufacture();

	@Query(value = "Select a from AssetMaster a where a.variant=:variantName")
	Optional<AssetMaster> fetchByVariant(String variantName);

}
