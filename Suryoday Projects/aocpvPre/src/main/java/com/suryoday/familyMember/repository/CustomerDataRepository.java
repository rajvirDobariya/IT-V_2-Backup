package com.suryoday.familyMember.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.suryoday.familyMember.pojo.CustomerData;



@Repository
public interface CustomerDataRepository extends JpaRepository<CustomerData, String>{

	Optional<CustomerData> getByCustomerId(String customerId);

}
