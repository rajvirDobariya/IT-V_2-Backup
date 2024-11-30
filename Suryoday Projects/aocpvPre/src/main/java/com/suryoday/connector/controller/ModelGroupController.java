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

import com.suryoday.connector.pojo.ModelGroup;
import com.suryoday.connector.pojo.ModelGroupDto;
import com.suryoday.connector.service.ModelGroupService;

@RestController
@RequestMapping(value = "/connector")
public class ModelGroupController {

	@Autowired
	ModelGroupService modelGroupService;
	
	private static Logger logger = LoggerFactory.getLogger(ModelGroupController.class);

	@RequestMapping(value = "/modelGroup", method = RequestMethod.POST)
	public ResponseEntity<ModelGroup> addModelGroup(@RequestBody ModelGroupDto modelGroupDto) {
		ModelGroup addModelGroup = modelGroupService.addModelGroup(modelGroupDto);
		logger.debug("Post Request"+modelGroupDto);
	  //  logger.debug("Response came from DB"+addModelGroup);
		if (addModelGroup == null) {
			return new ResponseEntity("Please Enter Vaild Details", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(addModelGroup, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/modelGroup/{id}", method = RequestMethod.PUT)
	public ResponseEntity<ModelGroup> updateModelGroup(@PathVariable("id") Long id,
			@RequestBody ModelGroupDto modelGroupDto) {
		ModelGroup updateModelGroup = modelGroupService.updateModelGroup(id, modelGroupDto);
		logger.debug("Put Request By Id"+modelGroupDto+id);
		logger.debug("Response came from DB"+updateModelGroup);
		if (updateModelGroup == null) {
			return new ResponseEntity("Please Enter Vaild Details", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(updateModelGroup, HttpStatus.OK);
	}

	@RequestMapping(value = "/modelGroup/{id}", method = RequestMethod.GET)
	public ResponseEntity<ModelGroup> getModelGroupById(@PathVariable("id") Long id) {
		ModelGroup byModelGroupId = modelGroupService.getByModelGroupId(id);
		logger.debug("Get Request By id"+ id);
		//logger.debug("Response came from DB"+byModelGroupId);
		if (byModelGroupId == null) {
			return new ResponseEntity("Please Enter Vaild Details", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(byModelGroupId, HttpStatus.OK);
	}

	@RequestMapping(value = "/modelGroups", method = RequestMethod.GET)
	public ResponseEntity<List<ModelGroup>> getAllPageGroup() {
		List<ModelGroup> allModelGroup = modelGroupService.getAllModelGroup();
		logger.debug("Get All Request");
		//logger.debug("Response came from DB"+allModelGroup);
		if (allModelGroup == null) {
			return new ResponseEntity("Please Enter Vaild Details", HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(allModelGroup, HttpStatus.OK);

	}

	@RequestMapping(value = "/modelGroup/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<ModelGroup> deletePageGroupById(@PathVariable("id") Long id) {
		boolean deleteModelGroupById = modelGroupService.deleteModelGroupById(id);
		logger.debug("Delete Request By Id"+id);
		//logger.debug("Response came from DB"+deleteModelGroupById);
		if (deleteModelGroupById == false) {
			return new ResponseEntity("Please Enter Vaild Details", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity(deleteModelGroupById, HttpStatus.OK);

	}
}
