package com.suryoday.customerOnboard.serviceImpl;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.customerOnboard.entity.CustomerOnboardDetails;
import com.suryoday.customerOnboard.exceptionHandler.ResourceNotFoundException;
import com.suryoday.customerOnboard.repository.CustomerOnboardDetailsRepository;
import com.suryoday.customerOnboard.service.CustomerOnboardDetailsService;

@Service
public class CustomerOnboardDetailsServiceImpl implements CustomerOnboardDetailsService{

	@Autowired
	private CustomerOnboardDetailsRepository onboardDetailsRepository;
	
	@Override
	public String createLead(String mobileNo, String userId) {
		
		String applicationNo = LocalDateTime.now().toString().replace("-", "").replace("T", "").replace(":", "").substring(2,14);
		
		CustomerOnboardDetails customerDetails =new CustomerOnboardDetails();
		customerDetails.setApplicationNo(Long.parseLong(applicationNo));
		customerDetails.setMobileNo(mobileNo);
		customerDetails.setMobileNoVerify("YES");
		customerDetails.setCreatedTime(LocalDateTime.now());
		customerDetails.setUpdatedTime(LocalDateTime.now());
		customerDetails.setStatus("PROGRESS");
		customerDetails.setCreatedBy(userId);
		onboardDetailsRepository.save(customerDetails);
		return applicationNo;
	}

	@Override
	public void saveResponse(String proof, String proofIdNo, String applicationNo, String response) {
		
		Optional<CustomerOnboardDetails> optional = onboardDetailsRepository.getByAapplicationNo(Long.parseLong(applicationNo));
		CustomerOnboardDetails customerDetails=new CustomerOnboardDetails();
		if (optional.isPresent()) {
			customerDetails=optional.get();
		}
		else {
			customerDetails.setApplicationNo(Long.parseLong(applicationNo));
		}
			if (proof.equals("panCard")) {
				customerDetails.setPancardNo(proofIdNo);
				customerDetails.setPancardNoVerify("YES");
				customerDetails.setPancardResponse(response);
				JSONObject json=new JSONObject(response);
				customerDetails.setNameOnPanCard(json.getString("NameOnCard"));
				customerDetails.setDobOnPanCard(json.getString("dobOnCard"));
			} else if (proof.equals("ekyc")) {
				customerDetails.setEkycDoneBy(proofIdNo);
				customerDetails.setEkycVerify("YES");
				customerDetails.setEkycResponse(response);
			}
			onboardDetailsRepository.save(customerDetails);
	}

	@Override
	public boolean validateSessionId(String x_Session_ID, HttpServletRequest request) {
		try {
			String allSessionIds = onboardDetailsRepository.getAllSessionIds(x_Session_ID);
			
			if (!allSessionIds.isEmpty()) {


				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.getMessage();


			return false;
		}
	}

	@Override
	public String getSessionId(String id, HttpServletRequest request) {
		Object attribute = request.getSession().getAttribute(id);
		String sessionId = "";
		if (attribute != null) {
			  List<String> list = onboardDetailsRepository.fetchUser(id);
			if (list.size() != 0) {
				
				for(String primaryID:list) {
				int deleteSession = onboardDetailsRepository.deleteSession(primaryID);	
				}
					request.getSession().invalidate();
					request.getSession().setAttribute(id, id);
					sessionId = request.getSession().getId();
					return sessionId;
				
			}
			else {
				request.getSession().invalidate();
				request.getSession().setAttribute(id, id);
				sessionId = request.getSession().getId();
				return sessionId;
			}
		} else {
			request.getSession().invalidate();
			request.getSession().setAttribute(id, id);
			sessionId = request.getSession().getId();
			return sessionId;
		}
		
	}

	@Override
	public CustomerOnboardDetails getByAapplicationNo(String applicationNo) {
		
		return onboardDetailsRepository.getByAapplicationNo(Long.parseLong(applicationNo))
				                       .orElseThrow(()-> new ResourceNotFoundException());
	}

	@Override
	public void save(CustomerOnboardDetails customeDetails) {
		onboardDetailsRepository.save(customeDetails);
		
	}

}
