package com.suryoday.aocpv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.suryoday.aocpv.pojo.AocpvLastLogin;

@Repository
public interface AocpvLastLoginRepository extends JpaRepository<AocpvLastLogin, Integer> {

}
