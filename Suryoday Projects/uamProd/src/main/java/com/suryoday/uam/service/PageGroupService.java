package com.suryoday.uam.service;

import java.util.List;

import com.suryoday.uam.pojo.PageGroup;
import com.suryoday.uam.pojo.PageGroupDto;

public interface PageGroupService {
	
	public PageGroup addPageGroup(PageGroupDto pageGroupDto);
	
	public PageGroup updatePageGroup(Long id, PageGroupDto pageGroupDto);
	
	public PageGroup getByPageGroupId(Long id);
	
	public List<PageGroup> getAllPages();
	
	public boolean deletePageGroupById(Long id);
	
}
