package com.suryoday.dsaOnboard.service;

import java.util.List;
import java.util.Set;

import com.suryoday.dsaOnboard.pojo.DsaOnboardCPV;

public interface DsaOnboardCPVService {

	Set<String> fetchState();

	Set<String> fetchCity(String state);

	List<DsaOnboardCPV> getCPVList(String city);

}
