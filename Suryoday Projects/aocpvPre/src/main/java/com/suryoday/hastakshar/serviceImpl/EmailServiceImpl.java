package com.suryoday.hastakshar.serviceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.suryoday.hastakshar.enums.EmailTemplate;
import com.suryoday.hastakshar.pojo.HastReqStatus;
import com.suryoday.hastakshar.repository.ReqStatusRepo;
import com.suryoday.hastakshar.service.EmailService;
import com.suryoday.hastakshar.utils.Constants;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private ReqStatusRepo reqStatusRepo;

	private static Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

	@Value("${BASEURL}")
	public String BASEURL;

	@Value("${api_key}")
	public String api_key;

	@Override
	public void sendEmailToManagerOrOpsTeamAfter48hours() {
		// 1 get list
		String currentDateHour = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH"));
		System.out.println("currentDateHour :: " + currentDateHour);
		logger.debug("currentDateHour :: " + currentDateHour);
		// TESTING
//		currentDateHour = "2024-09-26-16";
		List<HastReqStatus> list = reqStatusRepo.getListForSendEmail(currentDateHour);
		System.out.println("LIST SIZE ::" + list.size());
		logger.debug("LIST SIZE ::" + list.size());

		JSONObject response = new JSONObject();
		List<HastReqStatus> newList = new ArrayList<>();
		// 2 send email base on status
		for (int i = 0; i < list.size(); i++) {
			HastReqStatus hastReqStatus = list.get(i);
			if (hastReqStatus.getStatus().equals("APPROVED")) {
				// send email on payment.operations@suryodaybank.com
				response = sendEmail("dattu.kharatmol@suryodaybank.com", "Limit Enhancement Approval",
						EmailTemplate.HASTAKSHAR_REMINDER.getTemplateName(), "16AD312BB51E89A570D9A8903436A16A",
						"Suryoday Small Finance Bank Limited", "alerts@suryodaybank.com", "T",
						hastReqStatus.getAccountNo(), hastReqStatus.getTransactionDescription());

			} else if (!hastReqStatus.getStatus().equals("REJECTED")) {
				// send email on reporting manager of activity creator
				response = sendEmail("dattu.kharatmol@suryodaybank.com", "Limit Enhancement Approval",
						EmailTemplate.HASTAKSHAR_REMINDER.getTemplateName(), "16AD312BB51E89A570D9A8903436A16A",
						"Suryoday Small Finance Bank Limited", "alerts@suryodaybank.com", "T",
						hastReqStatus.getAccountNo(), hastReqStatus.getTransactionDescription());
			}
			hastReqStatus.setEmailSent(true);
			newList.add(hastReqStatus);
		}
		reqStatusRepo.saveAll(newList);
	}

	@Override
	public void sendLimitEnhancementEmailToApprover(String approver1, String accountNo, String transactionDesc,
			String message) {
		String userEmail = reqStatusRepo.getUserEmailByUserId(approver1);

		JSONObject response = sendEmail(userEmail, "Limit Enhancement Approval",
				EmailTemplate.HASTAKSHAR_STATUS.getTemplateName(), "16AD312BB51E89A570D9A8903436A16A",
				"Suryoday Small Finance Bank Limited", "alerts@suryodaybank.com", "T", accountNo, transactionDesc);
		System.out.println(message + response + userEmail + approver1);
		logger.debug(message + response + userEmail + approver1);
	}

	@Override
	public void sendEmailToOpsTeam(String accountNo, String transactionDesc, String transactionType) {
		String email = "payment.operations@suryodaybank.com";
		JSONObject response = sendEmail(email, transactionType, EmailTemplate.HASTAKSHAR_REMINDER.getTemplateName(),
				"16AD312BB51E89A570D9A8903436A16A", "Suryoday Small Finance Bank Limited", "alerts@suryodaybank.com",
				"T", accountNo, transactionDesc);
		System.out.println(transactionType + response);
		logger.debug(transactionType + "::" + response + "::" + email);
	}

	@Override
	public JSONObject sendEmail(String toMail, String subject, String templateID, String apiKey, String fromName,
			String fromMail, String type, String var1, String var2) {
		JSONObject request = new JSONObject();
		JSONObject requestData = new JSONObject();

		// Prepare the JSON body
		requestData.put("ToMail", toMail);
		requestData.put("Subject", subject);
		requestData.put("TemplateID", templateID);
		requestData.put("ApiKey", apiKey);
		requestData.put("FromName", fromName);
		requestData.put("FromMail", fromMail);
		requestData.put("Type", type);
		requestData.put("Var1", var1);
		requestData.put("Var2", var2);
		request.put("Data", requestData);

		JSONObject jsonResponse = new JSONObject();
		try {
			// Disable SSL verification
			SSLContext sslContext = SSLContextBuilder.create().loadTrustMaterial((chain, authType) -> true).build();

			CloseableHttpClient httpClient = HttpClientBuilder.create().setSSLContext(sslContext)
					.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();

			// Create the POST request
			HttpPost post = new HttpPost(Constants.UAT.EMAIL);

			// Set headers
			post.setHeader("Content-Type", "application/json");
			post.setHeader("Postman-Token", "68ff8d21-e74d-4b39-bdbe-6ea636ab2715");
			post.setHeader("X-From-ID", "SXT");
			post.setHeader("X-Request-ID", "SXT");
			post.setHeader("X-To-ID", "CB");
			post.setHeader("X-Transaction-ID", "EabeDcEE-db3c-BddD-CbD7-4bAA992c75d4");
			post.setHeader("X-User-ID", "S2080");
			post.setHeader("cache-control", "no-cache");

			// Add JSON body to the request
			StringEntity entity = new StringEntity(request.toString());
			post.setEntity(entity);

			// Execute the request and get the response
			HttpResponse response = httpClient.execute(post);
			String result = EntityUtils.toString(response.getEntity());

			// Parse and format the response body as JSON
			jsonResponse = new JSONObject(result);
			logger.debug("SEND EMAIL RESPONSE :: " + jsonResponse + "::" + LocalDateTime.now().toString());
		} catch (Exception e) {
			logger.debug("Something went wrong in the email template API request ::" + jsonResponse);
			throw new NoSuchElementException("Something went wrong in the email template API request!");
		}
		return jsonResponse;
	}

}