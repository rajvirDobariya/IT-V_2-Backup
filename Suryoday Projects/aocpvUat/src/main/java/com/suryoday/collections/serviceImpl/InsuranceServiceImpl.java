package com.suryoday.collections.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.collections.pojo.Insurance;
import com.suryoday.collections.repository.InsuranceRepository;
import com.suryoday.collections.service.InsuranceService;

@Service
public class InsuranceServiceImpl implements InsuranceService {

	@Autowired
	InsuranceRepository insuranceRepository;

	@Override
	public String calculateEmi(double amountIndouble, String productCode, String bankNameShort, String tenure) {
		if (tenure.isEmpty()) {
			throw new NoSuchElementException("tenure cannot be blank");
		}
		int time = Integer.parseInt(tenure);
		int updatetime = time;
		if (updatetime <= 12 && updatetime >= 6) {
			updatetime = 12;
		} else if (updatetime <= 24 && updatetime >= 13) {
			updatetime = 24;
		} else if (updatetime <= 36 && updatetime >= 25) {
			updatetime = 36;
		}
		String timeperiod = Integer.toString(updatetime);
		Optional<Insurance> optional = insuranceRepository.fetchProduct(productCode, bankNameShort, timeperiod);
		if (optional.isPresent()) {
			double maxAmount = optional.get().getMaxAmount();
			double rate = optional.get().getRate();

			rate = rate / updatetime * time;
			double emi = amountIndouble * rate;
			double finalEmi = Math.min(maxAmount, emi);
			String string = Double.toString(finalEmi);
			return string;
		} else {
			List<Insurance> list = insuranceRepository.fetchProductName(productCode);
			if (list.size() == 0) {
				throw new NoSuchElementException("Product is Not present");
			}
			List<Insurance> list2 = insuranceRepository.fetchBankName(productCode, bankNameShort);
			if (list2.size() == 0) {
				throw new NoSuchElementException("Current Bank is Not present For this Product");
			} else {
				throw new NoSuchElementException("tenure not present");
			}

		}

	}

}
