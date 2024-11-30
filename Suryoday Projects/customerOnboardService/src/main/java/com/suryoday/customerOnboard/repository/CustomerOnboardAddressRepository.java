package com.suryoday.customerOnboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.suryoday.customerOnboard.service.CustomerOnboardAddressService;

public interface CustomerOnboardAddressRepository extends JpaRepository<CustomerOnboardAddressService, Long> {

}
