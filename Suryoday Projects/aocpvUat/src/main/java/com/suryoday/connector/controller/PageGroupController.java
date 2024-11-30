package com.suryoday.connector.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.connector.pojo.PageGroup;
import com.suryoday.connector.pojo.PageGroupDto;
import com.suryoday.connector.service.PageGroupService;

@RestController
@RequestMapping(value = "/connector")
public class PageGroupController {

	@Autowired
	PageGroupService pageGroupService;

	private static Logger logger = LoggerFactory.getLogger(PageGroupController.class);

	@RequestMapping(value = "/pageGroup", method = RequestMethod.POST)
	public ResponseEntity<PageGroup> addPageGroup(@RequestBody PageGroupDto pageGroupDto) {
		PageGroup addPageGroup = pageGroupService.addPageGroup(pageGroupDto);
		logger.debug("Post Request" + pageGroupDto);
		// logger.debug("Response came from DB"+addPageGroup);
		if (addPageGroup == null) {
			return new ResponseEntity("Please Enter Vaild Details", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(addPageGroup, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/pageGroup/{id}", method = RequestMethod.PUT)
	public ResponseEntity<PageGroup> updatePageGroup(@PathVariable("id") Long id,
			@RequestBody PageGroupDto pageGroupDto) {
		PageGroup updatePageGroup = pageGroupService.updatePageGroup(id, pageGroupDto);
		logger.debug("Put Request By Id" + pageGroupDto + id);
		// logger.debug("Response came from DB"+updatePageGroup);
		if (updatePageGroup == null) {
			return new ResponseEntity("Please Enter Vaild Details", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(updatePageGroup, HttpStatus.OK);
	}

	@RequestMapping(value = "/pageGroup/{id}", method = RequestMethod.GET)
	public ResponseEntity<PageGroup> getPageGroupById(@PathVariable("id") Long id) {
		PageGroup getById = pageGroupService.getByPageGroupId(id);
		logger.debug("Get Request By id" + id);
		// logger.debug("Response came from DB"+getById);
		if (getById == null) {
			return new ResponseEntity("Please Enter Vaild Details", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(getById, HttpStatus.OK);
	}

	@RequestMapping(value = "/pageGroups", method = RequestMethod.GET)
	public ResponseEntity<List<PageGroup>> getAllPageGroup() {
		List<PageGroup> pages = pageGroupService.getAllPages();
		logger.debug("Get All Request");
		// logger.debug("Response came from DB"+pages);
		if (pages == null) {
			return new ResponseEntity("Please Enter Vaild Details", HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(pages, HttpStatus.OK);

	}

	@RequestMapping(value = "/pageGroup/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<PageGroup> deletePageGroupById(@PathVariable("id") Long id) {
		boolean deletePageGroup = pageGroupService.deletePageGroupById(id);
		logger.debug("Delete Request By Id" + id);
		// logger.debug("Response came from DB"+deletePageGroup);
		if (deletePageGroup == false) {
			return new ResponseEntity("Please Enter Vaild Details", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity(deletePageGroup, HttpStatus.OK);

	}

}
