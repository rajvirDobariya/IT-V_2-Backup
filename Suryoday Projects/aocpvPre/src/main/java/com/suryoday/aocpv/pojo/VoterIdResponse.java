package com.suryoday.aocpv.pojo;

public class VoterIdResponse {
private String name;
private String epic_no;
private String ac_name;
private String state;
private String district;
private String house_no;
private String ps_name;
private String rln_type;
private String rln_name;
private String pc_name;
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getEpic_no() {
	return epic_no;
}
public void setEpic_no(String epic_no) {
	this.epic_no = epic_no;
}
public String getAc_name() {
	return ac_name;
}
public void setAc_name(String ac_name) {
	this.ac_name = ac_name;
}
public String getState() {
	return state;
}
public void setState(String state) {
	this.state = state;
}
public String getDistrict() {
	return district;
}
public void setDistrict(String district) {
	this.district = district;
}
public String getHouse_no() {
	return house_no;
}
public void setHouse_no(String house_no) {
	this.house_no = house_no;
}
public String getPs_name() {
	return ps_name;
}
public void setPs_name(String ps_name) {
	this.ps_name = ps_name;
}
public String getRln_type() {
	return rln_type;
}
public void setRln_type(String rln_type) {
	this.rln_type = rln_type;
}
public String getRln_name() {
	return rln_name;
}
public void setRln_name(String rln_name) {
	this.rln_name = rln_name;
}
public String getPc_name() {
	return pc_name;
}
public void setPc_name(String pc_name) {
	this.pc_name = pc_name;
}
public VoterIdResponse(String name, String epic_no, String ac_name, String state, String district, String house_no,
		String ps_name, String rln_type, String rln_name, String pc_name) {
	super();
	this.name = name;
	this.epic_no = epic_no;
	this.ac_name = ac_name;
	this.state = state;
	this.district = district;
	this.house_no = house_no;
	this.ps_name = ps_name;
	this.rln_type = rln_type;
	this.rln_name = rln_name;
	this.pc_name = pc_name;
}
public VoterIdResponse() {
	super();
	// TODO Auto-generated constructor stub
}


}
