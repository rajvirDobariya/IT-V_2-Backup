package com.suryoday.connector.pojo;

public class BranchInfo {

private String branchId;
private String branchName;
public String getBranchId() {
	return branchId;
}
public void setBranchId(String branchId) {
	this.branchId = branchId;
}
public String getBranchName() {
	return branchName;
}
public void setBranchName(String branchName) {
	this.branchName = branchName;
}
public BranchInfo(String branchId, String branchName) {
	super();
	this.branchId = branchId;
	this.branchName = branchName;
}
public BranchInfo() {
	super();
	// TODO Auto-generated constructor stub
}

}
