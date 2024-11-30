package com.suryoday.hastakshar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.suryoday.hastakshar.pojo.HastProxyUser;

public interface ProxyUserRepo extends JpaRepository<HastProxyUser, Integer> {

	@Query(value = "SELECT a from HastProxyUser a where a.proxyUser =:proxyUser")
	List<HastProxyUser> fetchApproverbyProxy(String proxyUser);
}
