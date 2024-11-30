package com.suryoday.uam.serviceImp;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.uam.pojo.ModelGroup;
import com.suryoday.uam.pojo.ModelGroupDto;
import com.suryoday.uam.repository.ModelGroupRepository;
import com.suryoday.uam.service.ModelGroupService;

@Service
public class ModelGroupServiceImpl implements ModelGroupService {
	

	@Autowired
	ModelGroupRepository modelGroupRepository;
	
	public ModelGroup addModelGroup(ModelGroupDto modelGroupDto) {
		ModelGroup save = null;
		List<ModelGroup> findByModelGroupName = modelGroupRepository.findByModelGroupName(modelGroupDto.getModelGroupName());
		if(findByModelGroupName.isEmpty()) {
			ModelGroup modelGroup = new ModelGroup();
			modelGroup.setIsActive(modelGroupDto.getIsActive());
			modelGroup.setModelGroupName(modelGroupDto.getModelGroupName());
		    save = modelGroupRepository.save(modelGroup);
		}
		return save;
	}

	
	public ModelGroup updateModelGroup(Long id, ModelGroupDto modelGroupDto) {
		Optional<ModelGroup> findById = modelGroupRepository.findById(id);
		ModelGroup save = null;
		if(findById.isPresent()) {
			ModelGroup modelGroup = findById.get();
			if(modelGroupDto.getModelGroupName() ==null) {
				modelGroup.getModelGroupName();
			}else {
				
				modelGroup.setModelGroupName(modelGroupDto.getModelGroupName());
			}
			if(modelGroupDto.getIsActive() != null) {
				modelGroup.setIsActive(modelGroupDto.getIsActive());
			}else {
				modelGroup.getIsActive();
			}
			 save = modelGroupRepository.save(modelGroup);
		}
		return save;
	}

	
	public ModelGroup getByModelGroupId(Long id) {
		Optional<ModelGroup> findById = modelGroupRepository.findById(id);
		ModelGroup modelGroup  = null;
		if(findById.isPresent()) {
			modelGroup = findById.get();
		}
		
		return modelGroup;
	}

	
	public List<ModelGroup> getAllModelGroup() {
		List<ModelGroup> findAll = modelGroupRepository.findAll();
		return findAll;
	}

	
	public boolean deleteModelGroupById(Long id) {
		Optional<ModelGroup> findById = modelGroupRepository.findById(id);
		if(findById.isPresent()) {
			modelGroupRepository.deleteById(id);
			return true;
		}
		return false;
	}

}
