package com.suryoday.connector.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.connector.pojo.BranchListResponse;
import com.suryoday.connector.pojo.BranchMaster;
import com.suryoday.connector.repository.BranchInfoRepository;
import com.suryoday.connector.service.BranchMappingService;

@Service
public class BranchMappingServiceImpl implements BranchMappingService {

	@Autowired
	private BranchInfoRepository branchInfo;

	@Override
	public List<BranchMaster> fetchTopTenBranches() {
		List<BranchMaster> list = branchInfo.fetchTopTenBranches();
		if (list.size() == 0) {
			throw new NoSuchElementException("List is empty");
		}
		return list;
	}

	@Override
	public String addBranches(BranchMaster branchMaster) {
		Optional<BranchMaster> optional = branchInfo.fetchByBranchId(branchMaster.getBranchId());

		if (optional.isPresent()) {
			branchMaster.setSrNo(optional.get().getSrNo());
		} else {
			int srNo = branchInfo.fetchlastSrno();
			srNo++;
			branchMaster.setSrNo(srNo);
		}
		branchInfo.save(branchMaster);
		return "added";

	}

	@Override
	public List<BranchListResponse> fetchAllBranches() {
		List<BranchListResponse> list = branchInfo.fetchAllBranches();
		if (list.size() == 0) {
			throw new NoSuchElementException("List is empty");
		}
		return list;
	}

}
