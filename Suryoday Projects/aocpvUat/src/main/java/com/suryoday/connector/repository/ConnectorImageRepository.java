package com.suryoday.connector.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import com.suryoday.connector.pojo.ConnectorImages;

public interface ConnectorImageRepository {

	@Query(value = "SELECT top 1 a.id from connector_images a order by a.id desc", nativeQuery = true)
	Optional<Integer> fetchLastId();

	@Query(value = "SELECT a from ConnectorImages a where a.applicationNo =:appln and a.documenttype =:documenttype")
	Optional<ConnectorImages> getByApplicationNoAndName(Long appln, String documenttype);

	void save(ConnectorImages connectorImages);

}
