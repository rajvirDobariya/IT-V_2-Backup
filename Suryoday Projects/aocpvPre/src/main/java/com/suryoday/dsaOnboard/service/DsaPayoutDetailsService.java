package com.suryoday.dsaOnboard.service;

import com.suryoday.dsaOnboard.pojo.DsaOnboardPayoutDetails;

public interface DsaPayoutDetailsService {

	DsaOnboardPayoutDetails getByApplicationNo(String applicationNo);

	String saveData(DsaOnboardPayoutDetails dsaOnboardPayoutDetails);

}
