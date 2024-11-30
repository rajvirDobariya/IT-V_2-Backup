package com.suryoday.hastakshar.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.hastakshar.service.EmailService;

@RestController
@RequestMapping("/hastakshar/email")
public class EmailController {

	private static Logger logger = LoggerFactory.getLogger(EmailController.class);

	@Autowired
	private EmailService emailService;

	@PostMapping("/test")
	public String testing() {
		return "Testing";
	}

	@PostMapping("/sendEmailToManagerOrOpsTeamAfter48hours")
	public void sendEmailToManagerOrOpsTeamAfter48hours() {
		emailService.sendEmailToManagerOrOpsTeamAfter48hours();
		logger.debug("sendEmailToManagerOrOpsTeamAfter48hours...");
	}
}
