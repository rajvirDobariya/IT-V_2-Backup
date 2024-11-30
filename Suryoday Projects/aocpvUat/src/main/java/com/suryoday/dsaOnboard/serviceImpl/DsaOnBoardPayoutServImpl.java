package com.suryoday.dsaOnboard.serviceImpl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suryoday.dsaOnboard.pojo.PayoutSchemeMaster;
import com.suryoday.dsaOnboard.repository.DsaOnBoardPayoutRepo;
import com.suryoday.dsaOnboard.service.DsaOnBoardPayoutService;
import com.suryoday.roaocpv.others.ROAOCPVGenerateProperty;

@Component
public class DsaOnBoardPayoutServImpl implements DsaOnBoardPayoutService {
	@Autowired
	DsaOnBoardPayoutRepo payoutrepo;

	@Override
	public PayoutSchemeMaster fetchByProductAndAgency(String product, String agencyType) {
		Optional<PayoutSchemeMaster> optional = payoutrepo.fetchByProductAndAgency(product, agencyType);
		if (optional.isPresent()) {
			return optional.get();
		} else {
			throw new NoSuchElementException("No record found");
		}
	}

	@Override
	public String fetchBySchemeCode(String schemeCode) throws IOException {
		ROAOCPVGenerateProperty x = ROAOCPVGenerateProperty.getInstance();
		x.getappprop();
		String file = x.temp + "Payout/";
		System.out.println(file);
		String content = "";
		BufferedReader in = new BufferedReader(new FileReader(file + schemeCode + ".html"));
		String str;
		while ((str = in.readLine()) != null) {
			content += str;
		}
		in.close();
		return content;

	}

}
