package com.suryoday.connector.pojo;

public class PageGroupDto {
    
	private Long moduleGroupId;
	
	private String pageName;

	private String page;

	private String channel;

	public Long getModuleGroupId() {
		return moduleGroupId;
	}

	public void setModuleGroupId(Long moduleGroupId) {
		this.moduleGroupId = moduleGroupId;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	@Override
	public String toString() {
		return "PageGroupDto [moduleGroupId=" + moduleGroupId + ", pageName=" + pageName + ", page=" + page
				+ ", channel=" + channel + "]";
	}

}
