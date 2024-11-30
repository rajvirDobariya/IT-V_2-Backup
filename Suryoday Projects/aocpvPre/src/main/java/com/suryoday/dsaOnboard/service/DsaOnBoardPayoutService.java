package com.suryoday.dsaOnboard.service;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.suryoday.dsaOnboard.pojo.PayoutSchemeMaster;

@Component
public interface DsaOnBoardPayoutService {

	PayoutSchemeMaster fetchByProductAndAgency(String product, String agencyType);

	String fetchBySchemeCode(String schemeCode) throws IOException;

}
