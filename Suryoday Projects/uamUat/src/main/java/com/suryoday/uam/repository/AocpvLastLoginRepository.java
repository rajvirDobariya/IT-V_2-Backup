package com.suryoday.uam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.suryoday.uam.pojo.AocpvLastLogin;

@Repository
public interface AocpvLastLoginRepository extends JpaRepository<AocpvLastLogin, Integer>{

}
