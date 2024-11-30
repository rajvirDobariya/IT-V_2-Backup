package com.suryoday.customerOnboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.suryoday.customerOnboard.entity.CustomerOnboardImage;
@Repository
public interface CustomerOnboardImageRepository extends JpaRepository<CustomerOnboardImage, Long> {

}
