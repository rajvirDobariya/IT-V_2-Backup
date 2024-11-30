package com.suryoday.twowheeler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.suryoday.twowheeler.pojo.TwowheelerPaymentDetails;

@Component
public interface TwowheelerRazorPayRepo extends JpaRepository<TwowheelerPaymentDetails, String> {

}
