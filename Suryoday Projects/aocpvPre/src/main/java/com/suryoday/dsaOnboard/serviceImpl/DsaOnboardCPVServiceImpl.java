package com.suryoday.dsaOnboard.serviceImpl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.dsaOnboard.pojo.DsaOnboardCPV;
import com.suryoday.dsaOnboard.repository.DsaOnboardCPVRepository;
import com.suryoday.dsaOnboard.service.DsaOnboardCPVService;

@Service
public class DsaOnboardCPVServiceImpl implements DsaOnboardCPVService{

	@Autowired
	DsaOnboardCPVRepository dsaOnboardCPVRepository;
	
	@Override
	public Set<String> fetchState() {
		Set<String> list =dsaOnboardCPVRepository.fetchState();
		return list;
	}

	@Override
	public Set<String> fetchCity(String state) {
		Set<String> list =dsaOnboardCPVRepository.fetchCity(state);
		return list;
	}

	@Override
	public List<DsaOnboardCPV> getCPVList(String city) {
		List<DsaOnboardCPV> list =dsaOnboardCPVRepository.getCPVList(city);
		return list;
	}

}
