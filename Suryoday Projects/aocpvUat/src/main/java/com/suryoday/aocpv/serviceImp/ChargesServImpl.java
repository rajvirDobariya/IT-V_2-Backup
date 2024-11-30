package com.suryoday.aocpv.serviceImp;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suryoday.aocpv.pojo.AocpCustomer;
import com.suryoday.aocpv.pojo.Charges;
import com.suryoday.aocpv.repository.AocpCustomerDataRepo;
import com.suryoday.aocpv.repository.ChargesRepository;
import com.suryoday.aocpv.service.ChargesService;

@Component
public class ChargesServImpl implements ChargesService {
	@Autowired
	ChargesRepository chargesrepo;

	@Autowired
	AocpCustomerDataRepo aocpcustomerrepo;

	@Override
	public List<Charges> fetchByProductCode(String loanAmount, String tenure) {
		List<Charges> list = chargesrepo.fetchByProductCode(loanAmount, tenure);
		if (list.isEmpty()) {
			throw new NoSuchElementException("List is empty1");
		}
		return list;
	}

	@Override
	public String saveProductCode(long applicationNo, String productCode) {
		Optional<AocpCustomer> optional = aocpcustomerrepo.getByApp(applicationNo);
		if (optional.isPresent()) {
			// aocpcustomerrepo.updateProductCode(applicationNo,productCode);
			return "Data saved successfully";
		}
		return "No data found";
	}

	@Override
	public List<Charges> fetchByProductCode(String productCode) {
		List<Charges> list = chargesrepo.fetchByProductCode(productCode);
		if (list.isEmpty()) {
			throw new NoSuchElementException("List is empty1");
		}
		return list;
	}

}
