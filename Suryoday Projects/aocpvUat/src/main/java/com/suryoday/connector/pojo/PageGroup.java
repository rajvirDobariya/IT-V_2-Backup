package com.suryoday.connector.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_pagegroup")
public class PageGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "module_name")
	private ModelGroup moduleGroup;

	@Column(name = "page_name")
	private String pageName;

	@Column(name = "page_link")
	private String page;

	@Column(name = "channel")
	private String channel;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ModelGroup getModuleGroup() {
		return moduleGroup;
	}

	public void setModuleGroup(ModelGroup moduleGroup) {
		this.moduleGroup = moduleGroup;
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
		return "PageGroup [id=" + id + ", moduleGroup=" + moduleGroup + ", pageName=" + pageName + ", page=" + page
				+ ", channel=" + channel + "]";
	}

}
