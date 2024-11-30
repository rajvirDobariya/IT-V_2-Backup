package com.suryoday.aocpv.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.suryoday.aocpv.pojo.Charges;

@Component
public interface ChargesService {

	List<Charges> fetchByProductCode(String loanAmount, String tenure);


	String saveProductCode(long applicationNo, String productCode);


	List<Charges> fetchByProductCode(String productCode);


	List<Charges> fetchAll();

}
