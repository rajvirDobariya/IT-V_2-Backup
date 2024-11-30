package com.suryoday.mhl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.suryoday.mhl.pojo.CollateralDetails;

@Repository
public interface CollateralDetailsRepository extends JpaRepository<CollateralDetails, String>{

}
