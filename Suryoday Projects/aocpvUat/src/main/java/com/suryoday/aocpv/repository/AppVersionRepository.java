package com.suryoday.aocpv.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.suryoday.aocpv.pojo.AppVersion;

@Repository
public interface AppVersionRepository extends JpaRepository<AppVersion, String> {

	@Query(value = "SELECT a from AppVersion a ")
	Optional<AppVersion> findLatestVersion();

}
