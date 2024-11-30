package com.suryoday.connector.service;

import java.util.List;

import com.suryoday.connector.pojo.ModelGroup;
import com.suryoday.connector.pojo.ModelGroupDto;

public interface ModelGroupService {

	public ModelGroup addModelGroup(ModelGroupDto modelGroupDto);

	public ModelGroup updateModelGroup(Long id, ModelGroupDto modelGroupDto);

	public ModelGroup getByModelGroupId(Long id);

	public List<ModelGroup> getAllModelGroup();

	public boolean deleteModelGroupById(Long id);

}
