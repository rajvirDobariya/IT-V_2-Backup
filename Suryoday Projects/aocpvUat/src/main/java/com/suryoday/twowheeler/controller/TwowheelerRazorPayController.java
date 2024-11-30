package com.suryoday.twowheeler.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.suryoday.twowheeler.pojo.TwowheelerPaymentDetails;
import com.suryoday.twowheeler.service.TwowheelerRazorPayService;

@Component
@RequestMapping(value = "/twowheeler")
public class TwowheelerRazorPayController {
	@Autowired
	TwowheelerRazorPayService twowheelerRazorPayService;
	private static Logger logger = LoggerFactory.getLogger(TwowheelerRazorPayController.class);

	@RequestMapping(value = "/sendPaymentLink", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> disbursement(@RequestBody String bm,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "Content-Type", required = true) String ContentType, HttpServletRequest req)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("X-Request-ID", X_Request_ID);

		JSONObject jsonObject = new JSONObject(bm);
		String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
		JSONObject sendPaymentLink = twowheelerRazorPayService.sendPaymentLink(applicationNo, Header);

		HttpStatus h = HttpStatus.BAD_GATEWAY;
		if (sendPaymentLink != null) {
//			String Data2 = sendPaymentLink.getString("data");
			logger.debug("data2");
			String Data2 = "{\r\n" + "    \"Data\": {\r\n" + "        \"Id\": \"inv_KriOkipAtOP5Jd\",\r\n"
					+ "        \"Entity\": \"invoice\",\r\n" + "        \"Receipt\": \"T1910\",\r\n"
					+ "        \"Customer_Details\": {\r\n" + "            \"Name\": \"Rishabh\",\r\n"
					+ "            \"Contact\": \"7977842840\",\r\n" + "            \"Customer_Name\": \"Rishabh\",\r\n"
					+ "            \"Customer_Contact\": \"7977842840\"\r\n" + "        },\r\n"
					+ "        \"Order_Id\": \"order_KriOl4wfJVPSBz\",\r\n" + "        \"Status\": \"issued\",\r\n"
					+ "        \"Expire_By\": \"1686729313\",\r\n" + "        \"Issued_At\": \"1671004513\",\r\n"
					+ "        \"SMS_Status\": \"sent\",\r\n" + "        \"Email_Status\": \"sent\",\r\n"
					+ "        \"Date\": \"1671004513\",\r\n" + "        \"Partial_Payment\": false,\r\n"
					+ "        \"Gross_Amount\": \"10.2\",\r\n" + "        \"Tax_Amount\": \"0\",\r\n"
					+ "        \"Taxable_Amount\": \"0\",\r\n" + "        \"Amount\": \"10.2\",\r\n"
					+ "        \"Amount_Paid\": \"0\",\r\n" + "        \"Amount_Due\": \"10.2\",\r\n"
					+ "        \"Currency\": \"INR\",\r\n" + "        \"Currency_Symbol\": \"₹\",\r\n"
					+ "        \"Description\": \"Dear Customer, Suryoday Bank requests payment towards loan repayment. Call Smile Centre on 18002667711 for queries\",\r\n"
					+ "        \"Short_URL\": \"https://rzp.io/i/vJ57qMw\",\r\n" + "        \"View_Less\": true,\r\n"
					+ "        \"Type\": \"link\",\r\n" + "        \"Group_Taxes_Discounts\": false,\r\n"
					+ "        \"Created_At\": \"1671004513\",\r\n" + "        \"Transaction_Code\": \"00\"\r\n"
					+ "    }\r\n" + "}";
			logger.debug(Data2);
			JSONObject Data1 = new JSONObject(Data2);
			if (Data1.has("Data")) {
				String paymentId = Data1.getJSONObject("Data").getString("Id");
				String paymentStatus = Data1.getJSONObject("Data").getString("Status");
				TwowheelerPaymentDetails paymentDetails = new TwowheelerPaymentDetails();
				paymentDetails.setApplicationId(applicationNo);
				paymentDetails.setPaymentId(paymentId);
				paymentDetails.setPaymentStatus(paymentStatus);
				paymentDetails.setPaymentResponse(Data1.toString());
				paymentDetails.setPaymentRequest(Data1.toString());
				twowheelerRazorPayService.save(paymentDetails);
				h = HttpStatus.OK;

				return new ResponseEntity<Object>(Data1.toString(), h);
			} else if (Data1.has("Errors")) {
				h = HttpStatus.BAD_REQUEST;

			}

			return new ResponseEntity<Object>(Data1.toString(), h);

		} else {

			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}

	@RequestMapping(value = "/fetchPaymentLink", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> fetchPaymentLink(@RequestBody String bm,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_Correlation_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID, HttpServletRequest req)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("X-Request-ID", X_Request_ID);
		Header.put("X-Correlation-ID", X_Correlation_ID);
		Header.put("X-User-ID", X_User_ID);
		JSONObject jsonObject = new JSONObject(bm);
		String orderId = jsonObject.getJSONObject("Data").getString("OrderId");
		JSONObject fetchPaymentLink = twowheelerRazorPayService.fetchPaymentLink(orderId, Header);

		HttpStatus h = HttpStatus.BAD_GATEWAY;
		if (fetchPaymentLink != null) {
			String Data2 = fetchPaymentLink.getString("data");
			logger.debug("data2");
			logger.debug(Data2);
			JSONObject Data1 = new JSONObject(Data2);
			if (Data1.has("Data")) {
				h = HttpStatus.OK;

				return new ResponseEntity<Object>(Data1.toString(), h);
			} else if (Data1.has("Error")) {
				h = HttpStatus.BAD_REQUEST;
			}
			return new ResponseEntity<Object>(Data1.toString(), h);
		} else {
			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}
}
