package com.suryoday.mhl.serviceImpl;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.mhl.pojo.CollateralDetails;
import com.suryoday.mhl.repository.CollateralDetailsRepository;
import com.suryoday.mhl.service.CollateralDetailsService;

@Service
public class CollateralDetailsServiceImpl implements CollateralDetailsService {

	@Autowired
	CollateralDetailsRepository collateralDetailsRepository;

	@Override
	public String saveCollateralDetails(JSONObject jsonObject) {

		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		String identificationOfProperty = jsonObject.getJSONObject("Data").getString("identificationOfProperty");
		String propertyType = jsonObject.getJSONObject("Data").getString("propertyType");
		String AgeOfBuilding = jsonObject.getJSONObject("Data").getString("AgeOfBuilding");
		String propertyClassification = jsonObject.getJSONObject("Data").getString("propertyClassification");
		String ValueOfProperty = jsonObject.getJSONObject("Data").getString("ValueOfProperty");
		String AreaOfProperty = jsonObject.getJSONObject("Data").getString("AreaOfProperty");
		String DetailsOfSite = jsonObject.getJSONObject("Data").getString("DetailsOfSite");
		String typeOfLoan = jsonObject.getJSONObject("Data").getString("typeOfLoan");
		String nameOfPropertyHolders = jsonObject.getJSONObject("Data").getString("nameOfPropertyHolders");
		String yearOfPurchase = jsonObject.getJSONObject("Data").getString("yearOfPurchase");
		String purchasedValue = jsonObject.getJSONObject("Data").getString("purchasedValue");
		String currentMarketValue = jsonObject.getJSONObject("Data").getString("currentMarketValue");
		String builtUpArea = jsonObject.getJSONObject("Data").getString("builtUpArea");
		String builtUpEstimate = jsonObject.getJSONObject("Data").getString("builtUpEstimate");
		String loanDetails = jsonObject.getJSONObject("Data").getString("loanDetails");

		CollateralDetails collateralDetails = new CollateralDetails();
		collateralDetails.setApplicationNo(applicationNo);
		collateralDetails.setIdentificationOfProperty(identificationOfProperty);
		collateralDetails.setPropertyType(propertyType);
		collateralDetails.setAgeOfBuilding(AgeOfBuilding);
		collateralDetails.setPropertyClassification(propertyClassification);
		collateralDetails.setValueOfProperty(ValueOfProperty);
		collateralDetails.setAreaOfProperty(AreaOfProperty);
		collateralDetails.setDetailsOfSite(DetailsOfSite);
		collateralDetails.setTypeOfLoan(typeOfLoan);
		collateralDetails.setNameOfPropertyHolders(nameOfPropertyHolders);
		collateralDetails.setYearOfPurchase(yearOfPurchase);
		collateralDetails.setPurchasedValue(purchasedValue);
		collateralDetails.setCurrentMarketValue(currentMarketValue);
		collateralDetails.setBuiltUpArea(builtUpArea);
		collateralDetails.setBuiltUpEstimate(builtUpEstimate);
		collateralDetails.setLoanDetails(loanDetails);

		collateralDetailsRepository.save(collateralDetails);

		return "Collateraldetails Save Successfully";
	}

}
