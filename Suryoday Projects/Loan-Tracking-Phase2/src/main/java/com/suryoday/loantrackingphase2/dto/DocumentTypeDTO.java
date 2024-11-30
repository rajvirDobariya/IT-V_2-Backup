package com.suryoday.loantrackingphase2.dto;

import java.util.List;

public class DocumentTypeDTO {

	private Long id;
    private String typeName;
    private List<DocumentDTO> documents;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public List<DocumentDTO> getDocuments() {
		return documents;
	}
	public void setDocuments(List<DocumentDTO> documents) {
		this.documents = documents;
	}

    
}
