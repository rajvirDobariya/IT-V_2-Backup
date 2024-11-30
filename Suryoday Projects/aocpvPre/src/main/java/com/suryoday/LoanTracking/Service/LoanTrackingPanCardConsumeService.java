package com.suryoday.LoanTracking.Service;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
@Component
public interface LoanTrackingPanCardConsumeService {

	JSONObject panCardValidation(String panCardNo, JSONObject header);

}
