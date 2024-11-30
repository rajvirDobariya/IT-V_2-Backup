package com.suryoday.loantrackingphase2.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.loantrackingphase2.exception.LoanException;
import com.suryoday.loantrackingphase2.model.LoanProduct;
import com.suryoday.loantrackingphase2.repository.LoanProductRepository;
import com.suryoday.loantrackingphase2.service.LoanPrductService;
import com.suryoday.loantrackingphase2.utils.EncryptDecryptHelper;

@Service
public class LoanProductServiceImpl implements LoanPrductService {

	Logger LOG = LoggerFactory.getLogger(LoanProductServiceImpl.class);

	@Autowired
	private LoanProductRepository loanProductRepository;

	@Autowired
	private EncryptDecryptHelper encryptDecryptHelper;

	@Override
	public JSONObject getAll(String channel, String X_Session_ID, String X_User_ID, Boolean isEncrypt) {
		JSONObject response = new JSONObject();
		JSONObject responseData = new JSONObject();

		// 1 VALIDATE & DECRYPT REQUEST
		if (isEncrypt) {
			encryptDecryptHelper.validateHeadersAndSessionId(channel, X_Session_ID);
		}

		// RESPONSE
		responseData.put("loanProducts", loanProductRepository.findAll());
		response.put("Data", responseData);
		return response;

	}

	@Override
	public List<LoanProduct> addLoanProducts() {
		List<LoanProduct> products = new ArrayList<>();

		// List of product names from the provided list
		String[] productNames = { "7008_MICRO HL", "MFLAP_MICRO MORT IF LAP", "MHLPF_MHL PRIME", "MHLPP_MHL PRIME",
				"MLAPC_MICRO MORT LAP CONST", "MLCPF_MICRO MORT LAP CONST PRIME", "MLCPP_MICRO MORT LAP CONST PRIME",
				"MLPPF_MICRO MORT LAP PRIME", "MLAPP_MICRO MORT LAP PRIME", "MMLAP_MICRO MORT LAP",
				"CVNEW_COMMERCIAL VEHICLE", "NCARL_NEW CAR LOAN", "UCARL_USED CAR LOAN", "CENEW_CONST EQUIPMENT",
				"8005_FIG", "FIGNI_FIG", "7004_HOME LOAN", "7007_SBLAP", "7007_SBLAP- PRE APPROVED STP",
				"7007_SBLAP- PRE APPROVED NON STP", "SMBL_SECURED-MBL", "SMBL_SECURED-MBL-KISSHT",
				"80014_UNSECURED BL" };

		// Initialize the product code
		int productCode = 1;

		// Iterate over product names and create LoanProduct instances
		for (String name : productNames) {
			LoanProduct product = new LoanProduct();
			product.setId(productCode++); // Increment product code for each product
			product.setProductCode(String.valueOf(productCode++)); // Increment product code for each product
			product.setProductName(name);
			product.setCreatedAt(LocalDateTime.now());

			// Add to list
			products.add(product);
		}
		return loanProductRepository.saveAll(products);
	}

}
