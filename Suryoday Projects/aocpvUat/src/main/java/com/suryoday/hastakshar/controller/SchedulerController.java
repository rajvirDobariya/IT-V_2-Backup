package com.suryoday.hastakshar.controller;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.hastakshar.enums.EmailTemplate;
import com.suryoday.hastakshar.service.EmailService;

@RestController
@RequestMapping("/hastakshar/scheduler")
public class SchedulerController {

	private static Logger logger = LoggerFactory.getLogger(SchedulerController.class);

	@Autowired
	private EmailService emailService;

	// WRONG ENTRY TAT 48 HOUR EMAIL
	@Scheduled(cron = "0 0 * * * ?")
	public void sendEmailToManagerOrOpsTeamAfter48hours() {
		emailService.sendEmailToManagerOrOpsTeamAfter48hours();
		logger.debug("Email Sent...");
	}

	// Testing Send Email API
	@PostMapping("/testing/send/email")
	public String testingSendEmailAPI() {
		JSONObject response = emailService.sendEmail("it.developer@suryodaybank.com", "subject",
				EmailTemplate.HASTAKSHAR_REMINDER.getTemplateName(), "16AD312BB51E89A570D9A8903436A16A",
				"Suryoday Small Finance Bank Limited", "alerts@suryodaybank.com", "T", "", "");
		System.out.println(response);
		return response.toString();
	}
}
