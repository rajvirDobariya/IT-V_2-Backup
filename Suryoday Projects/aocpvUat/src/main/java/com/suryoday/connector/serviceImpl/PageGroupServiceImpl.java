package com.suryoday.connector.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.connector.pojo.ModelGroup;
import com.suryoday.connector.pojo.PageGroup;
import com.suryoday.connector.pojo.PageGroupDto;
import com.suryoday.connector.repository.ModelGroupRepository;
import com.suryoday.connector.repository.PageGroupRepository;
import com.suryoday.connector.service.PageGroupService;

@Service
public class PageGroupServiceImpl implements PageGroupService {

	@Autowired
	PageGroupRepository pageGroupRepository;

	@Autowired
	ModelGroupRepository modelGroupRepository;

	public PageGroup addPageGroup(PageGroupDto pageGroupDto) {

		PageGroup pageGroup = new PageGroup();
		pageGroup.setPage(pageGroupDto.getPage());
		pageGroup.setPageName(pageGroupDto.getPageName());
		pageGroup.setChannel(pageGroupDto.getChannel());
		Optional<ModelGroup> findById = modelGroupRepository.findById(pageGroupDto.getModuleGroupId());
		if (findById.isPresent()) {
			ModelGroup modelGroup = findById.get();
			pageGroup.setModuleGroup(modelGroup);
		}
		PageGroup save = pageGroupRepository.save(pageGroup);
		return save;
	}

	public PageGroup updatePageGroup(Long id, PageGroupDto pageGroupDto) {

		Optional<PageGroup> findById = pageGroupRepository.findById(id);
		PageGroup pageGroup = null;
		if (findById.isPresent()) {

			pageGroup = findById.get();
			if (!pageGroupDto.getPage().equals(null)) {
				pageGroup.setPage(pageGroupDto.getPage());
			} else {
				pageGroup.getPage();
			}
			if (!pageGroupDto.getPageName().equals(null)) {
				pageGroup.setPageName(pageGroupDto.getPageName());
			} else {
				pageGroup.getPageName();
			}
			if (!pageGroup.getChannel().equals(null)) {
				pageGroup.setChannel(pageGroupDto.getChannel());
			} else {
				pageGroup.getChannel();
			}
			if (pageGroupDto.getModuleGroupId() != null) {
				Optional<ModelGroup> findById1 = modelGroupRepository.findById(pageGroupDto.getModuleGroupId());
				if (findById1.isPresent()) {
					ModelGroup modelGroup = findById1.get();
					pageGroup.setModuleGroup(modelGroup);
				}
			} else {
				pageGroup.getModuleGroup();
			}
		}

		PageGroup save = pageGroupRepository.save(pageGroup);

		return save;
	}

	public PageGroup getByPageGroupId(Long id) {

		Optional<PageGroup> findById = pageGroupRepository.findById(id);
		PageGroup pageGroup = null;
		if (findById.isPresent()) {
			pageGroup = findById.get();
		}
		return pageGroup;
	}

	public List<PageGroup> getAllPages() {
		List<PageGroup> findAll = pageGroupRepository.findAll();
		return findAll;
	}

	public boolean deletePageGroupById(Long id) {
		Optional<PageGroup> findById = pageGroupRepository.findById(id);
		if (findById.isPresent()) {
			pageGroupRepository.deleteById(id);
			return true;
		}
		return false;
	}

}
