package com.suryoday.loantrackingphase2.dto;

public class ProductMasterDTO {

	private Long id;
    private String productName;
    
    public ProductMasterDTO(Long id, String productName) {
        this.id = id;
        this.productName = productName;
    }
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
    
}
