package com.suryoday.connector.pojo;

public class UserRoleDto {
	
	private Long id;
	
    private String moduleName;
    
    private String pageName;
	
	private String pageLink;
	
	private String channel;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getPageLink() {
		return pageLink;
	}

	public void setPageLink(String pageLink) {
		this.pageLink = pageLink;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	@Override
	public String toString() {
		return "UserRoleDto [id=" + id + ", moduleName=" + moduleName + ", pageName=" + pageName + ", pageLink="
				+ pageLink + ", channel=" + channel + "]";
	}

}
