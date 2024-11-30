package com.suryoday.twowheeler.serviceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.poi.EmptyFileException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.aocpv.pojo.LoanDetail;
import com.suryoday.aocpv.pojo.PreApprovedListTwoWheeler;
import com.suryoday.twowheeler.repository.PreApprovalListRepository;
import com.suryoday.twowheeler.service.PreApprovalListService;

@Service
public class PreApprovalListServiceImpl implements PreApprovalListService {

	@Autowired
	private PreApprovalListRepository approvalListRepository;

	@Override
	public List<LoanDetail> findByDate(LocalDate startdate, LocalDate enddate, Long branchId) {

		return approvalListRepository.getByDate(startdate, enddate, branchId)
				.orElseThrow(() -> new NoSuchElementException("NO Record found"));
	}

	@Override
	public PreApprovedListTwoWheeler getByReferenceNo(String customerId) {
		long custid = Long.parseLong(customerId);
		return approvalListRepository.getByCustomerID(custid)
				.orElseThrow(() -> new NoSuchElementException("customer not present"));
	}

	@Override
	public PreApprovedListTwoWheeler saveSingleData(PreApprovedListTwoWheeler loanDetails) {
		return approvalListRepository.save(loanDetails);

	}

	@Override
	public List<LoanDetail> getByIdAndstatus(long branchId, String status, String productCode) {
		if (branchId == 0 || status == null) {
			throw new EmptyFileException();
		}

		return approvalListRepository.getByIdAndStatus(branchId, status, productCode)
				.orElseThrow(() -> new NoSuchElementException("list is empty"));
	}

}
