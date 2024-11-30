package com.suryoday.connector.pojo;

public class PermissionDto {

	private Long index;

	private String name;

	private String checked;

	private String title;

	private String page;

	private String channel;

	public Long getIndex() {
		return index;
	}

	public void setIndex(Long index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	@Override
	public String toString() {
		return "PermissionDto [index=" + index + ", name=" + name + ", checked=" + checked + ", title=" + title
				+ ", page=" + page + ", channel=" + channel + "]";
	}

}
