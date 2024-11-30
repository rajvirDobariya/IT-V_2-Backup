package com.suryoday.goNogo.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.goNogo.pojo.GoNogoCustomerDetails;
import com.suryoday.goNogo.pojo.GoNogoDetailsResponse;
import com.suryoday.goNogo.repository.GoNogoCustDetailsRepository;
import com.suryoday.goNogo.service.GoNogoCustDetailsService;

@Service
public class GoNogoCustDetailsServiceImpl implements GoNogoCustDetailsService {

	@Autowired
	GoNogoCustDetailsRepository goNogoCustDetailsRepository;

	@Override
	public GoNogoCustomerDetails getByApplicationNo(String applicationNo) {
		Optional<GoNogoCustomerDetails> optional = goNogoCustDetailsRepository
				.getByAapplicationNo(Long.parseLong(applicationNo));
		if (optional.isPresent()) {
			return optional.get();
		}
		return new GoNogoCustomerDetails();
	}

	@Override
	public String createLead(String mobileNo) {
		String applicationNo = (LocalDate.now().toString().replace("-", "") + "0001").substring(2, 12);
		Optional<String> fetchLastApplicationNo = goNogoCustDetailsRepository.fetchLastApplicationNo();
		if (fetchLastApplicationNo.isPresent()) {

			String application_No = fetchLastApplicationNo.get();
			String dateInDB = application_No.substring(0, 6);
			String currentDate = LocalDate.now().toString().replace("-", "").substring(2, 8);
			if (currentDate.equals(dateInDB)) {
				Long applicationno = Long.parseLong(application_No);
				applicationno++;
				applicationNo = applicationno.toString();

			}
		}
		GoNogoCustomerDetails customerDetails = new GoNogoCustomerDetails();
		customerDetails.setApplicationNo(Long.parseLong(applicationNo));
		customerDetails.setMobileNo(mobileNo);
		customerDetails.setMobileVerify("YES");
		customerDetails.setCreatedDate(LocalDateTime.now());
		customerDetails.setUpdatedDate(LocalDateTime.now());
		goNogoCustDetailsRepository.save(customerDetails);

		return applicationNo;
	}

	@Override
	public void saveResponse(String proof, String proofIdNo, String applicationNo, String response) {
		Optional<GoNogoCustomerDetails> optional = goNogoCustDetailsRepository
				.getByAapplicationNo(Long.parseLong(applicationNo));
		GoNogoCustomerDetails customerDetails = new GoNogoCustomerDetails();
		if (optional.isPresent()) {
			customerDetails = optional.get();
		} else {
			customerDetails.setApplicationNo(Long.parseLong(applicationNo));
		}
		if (proof.equals("panCard")) {
			customerDetails.setPancard(proofIdNo);
			customerDetails.setPancardVerify("YES");
			customerDetails.setPancardResponse(response);
		} else if (proof.equals("ekyc")) {
			customerDetails.setEkycDoneBy(proofIdNo);
			customerDetails.setEkycVerify("YES");
			customerDetails.setEkycResponse(response);
		}
		goNogoCustDetailsRepository.save(customerDetails);
	}

	@Override
	public void save(GoNogoCustomerDetails goNogoCustomerDetails) {
		goNogoCustDetailsRepository.save(goNogoCustomerDetails);

	}

	@Override
	public GoNogoCustomerDetails getByApplicationno(String applicationNo) {
		Optional<GoNogoCustomerDetails> optional = goNogoCustDetailsRepository
				.getByAapplicationNo(Long.parseLong(applicationNo));
		if (optional.isPresent()) {
			optional.get();
		}
		throw new NoSuchElementException("No Record Found");
	}

	@Override
	public List<GoNogoDetailsResponse> fetchByDateAndbranch(LocalDateTime startdate, LocalDateTime enddate,
			String status, String branchId) {
		List<GoNogoDetailsResponse> list = goNogoCustDetailsRepository.fetchByDateAndbranch(startdate, enddate, status,
				branchId);
		if (list.size() == 0) {
			throw new NoSuchElementException("No Record Found");
		}
		return list;
	}

}
