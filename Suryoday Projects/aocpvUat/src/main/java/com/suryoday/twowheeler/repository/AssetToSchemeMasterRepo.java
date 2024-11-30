package com.suryoday.twowheeler.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.suryoday.twowheeler.pojo.AssetToSchemeMaster;

public interface AssetToSchemeMasterRepo extends JpaRepository<AssetToSchemeMaster, String> {

	@Query(value = "Select a.schemeDescription from AssetToSchemeMaster a where a.model =:model and a.creditStatusDescription =:breCustomerCategory")
	Optional<List<String>> getByModelAndCreditStatusDescription(String model, String breCustomerCategory);

}
