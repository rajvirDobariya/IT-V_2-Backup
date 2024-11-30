package com.suryoday.uam.service;

import java.util.List;

import com.suryoday.uam.pojo.ModelGroup;
import com.suryoday.uam.pojo.ModelGroupDto;

public interface ModelGroupService {
	
   public ModelGroup addModelGroup(ModelGroupDto modelGroupDto);
	
	public ModelGroup updateModelGroup(Long id, ModelGroupDto modelGroupDto);
	
	public ModelGroup getByModelGroupId(Long id);
	
	public List<ModelGroup> getAllModelGroup();
	
	public boolean deleteModelGroupById(Long id);

}
