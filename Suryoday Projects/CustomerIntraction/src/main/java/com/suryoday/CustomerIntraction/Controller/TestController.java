package com.suryoday.CustomerIntraction.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/CustomerIntraction")
public class TestController {

	Logger LOG = LoggerFactory.getLogger(TestController.class);

	@PostMapping("/Welcome")
	public String welcome() {
		LOG.debug("CustomerIntractionController :: create Meeting :: ");
		return "Welcome";
	}
	
}
