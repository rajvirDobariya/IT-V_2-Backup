//package com.digitisation.branchreports.controller;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.digitisation.branchreports.service.BranchUserMakerService;
//
//@RestController
//public class SchedulerController {
//	Logger LOG = LoggerFactory.getLogger(SchedulerController.class);
//
//	@Autowired
//	private BranchUserMakerService branchuserservice;
//
////	/**
////	 * SCHEDULER
////	 **/
////
//	// Update Status Pending Scheduler
//	@Scheduled(cron = "0 0 0 * * MON-SAT")
//	public void runUpdateStatusNotsubmittedToPendingTask() {
//		LOG.debug("runUpdateStatusNotsubmittedToPendingTask started!");
//		branchuserservice.updateStatusNotsubmittedToPending();
//		LOG.debug("runUpdateStatusNotsubmittedToPendingTask executed successfully!");
//	}
//
////	// Quarterly Scheduler
////	@Scheduled(cron = "0 0 1 1 1/3 ?")
////	public void runQuarterlyTask() {
////		LOG.debug("runQuarterlyTask started!");
////		branchuserservice.addQuarterlyReports(null);
////		LOG.debug("runQuarterlyTask executed successfully!");
////	}
////
////	// Monthly Scheduler
////	@Scheduled(cron = "0 0 2 1 * *")
////	public void runMonthlyTask() {
////		LOG.debug("runQuarterlyTask started!");
////		branchuserservice.addMonthlyReports(null);
////		LOG.debug("addMonthlyReports executed successfully!");
////	}
////
////	// Weekly Scheduler
////	@Scheduled(cron = "0 0 3 ? * MON")
////	public void runWeeklyTask() {
////		LOG.debug("runWeeklyTask started!");
////		branchuserservice.addWeeklyReports(null);
////		LOG.debug("runWeeklyTask executed successfully!");
////	}
////
//	// Daily Scheduler
//	@Scheduled(cron = "0 0 4 * * MON-SAT")
//	public void runDailyTask() {
//		LOG.debug("runDailyTask started!");
//		branchuserservice.addDailyReports(null, null);
//		LOG.debug("runDailyTask executed successfully!");
//	}
//
//}
