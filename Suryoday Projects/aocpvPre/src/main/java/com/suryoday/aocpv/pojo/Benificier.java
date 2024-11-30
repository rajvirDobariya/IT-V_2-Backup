package com.suryoday.aocpv.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "Tbl_Benificier")
public class Benificier {
	
	
	public Benificier()
	{
		
	}
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Pid;
	
	@Column(name = "name")
    private String Name;
	
	
	@Column(name = "Sequence")
    private String Sequence;
	
	@Column(name = "Address1")
    private String Address1;
	
	
	@Column(name = "State")
    private String State;
	
	@Column(name = "City")
    private String City;
	
    
	@Column(name = "Zip")
    private String Zip;
	
	@Column(name = "EmailId")
    private String EmailId;
  
	
	@Column(name = "MobileNumber")
    private String MobileNumber;
   
	

	
	@Column(name = "Status")
    private String Status ="A";
	
	
	@Column(name = "NickName")
    private String NickName;
	
	@Column(name = "Account")
    private String Account;
	
	
    private String CustomerId;
	
	@Column(name = "AccountType")
    private String AccountType;
	
	@Column(name = "Type")
    private String Type;
	
	@Column(name = "BankIfsc")
    private String BankIfsc;
	
	@Column(name = "BankCity")
    private String BankCity;
	
	@Column(name = "BankBranch")
    private String BankBranch;
	
	@Column(name = "BankName")
    private String BankName;
	
	
	@Column(name = "MaxLimit")
    private String MaxLimit;
	
	@Temporal(TemporalType.TIMESTAMP)
    private Date CreatedDate;

	
	@Temporal(TemporalType.TIMESTAMP)
    private Date lastupdateDate;
	

	
	public Date getCreatedDate() {
		return CreatedDate;
	}


	public void setCreatedDate(Date createdDate) {
		CreatedDate = createdDate;
	}


	public Date getLastupdateDate() {
		return lastupdateDate;
	}


	public void setLastupdateDate(Date lastupdateDate) {
		this.lastupdateDate = lastupdateDate;
	}


	public String getCustomerId() {
		return CustomerId;
	}


	public void setCustomerId(String customerId) {
		
		if(customerId == null){
			customerId = "";
	    }
		CustomerId = customerId;
	}


	public Long getPid() {
		return Pid;
	}


	public void setPid(Long pid) {
			Pid = pid;
	}


	public String getName() {
		return Name;
	}


	public void setName(String name) {
		if(name == null){
			name = "";
	    }
		Name = name;
		
	}


	

	public String getNickName() {
		return NickName;
	}


	public void setNickName(String nickName) {
		
		if(nickName == null){
			nickName = "";
	    }
		NickName = nickName;
	}


	public String getAccount() {
		return Account;
	}


	public void setAccount(String account) {
		Account = account;
	}


	public String getAccountType() {
		return AccountType;
	}


	public void setAccountType(String accountType) {
		
		if(accountType == null){
			accountType = "";
	    }
		
		AccountType = accountType;
	}


	public String getBankIfsc() {
		return BankIfsc;
	}


	public void setBankIfsc(String bankIfsc) {
	
		
		BankIfsc =  bankIfsc;
	}


	public String getBankCity() {
		return BankCity;
	}


	public void setBankCity(String bankCity) {
		BankCity =  bankCity;
	}


	public String getBankBranch() {
		return BankBranch;
	}


	public void setBankBranch(String bankBranch) {
		BankBranch =  bankBranch;
	}


	public String getMaxLimit() {
		return MaxLimit;
	}


	public void setMaxLimit(String maxLimit) {
		MaxLimit =  maxLimit;
	}

	 

	public String getBankName() {
		return BankName;
	}


	public void setBankName(String bankName) {
		BankName = bankName;
	}

	

	public String getSequence() {
		return Sequence;
	}


	public void setSequence(String sequence) {
		Sequence = sequence;
	}


	public String getAddress1() {
		
		if(Address1 == null)
		{
			Address1 = "";
		}
		return Address1;
	}


	public void setAddress1(String address1) {
		
		Address1 =  address1;
	}


	public String getState() {
		if(State == null){
			State = "";
	    }
		return State;
	}


	public void setState(String state) {
		if(state == null){
			state = "";
	    }
		State = state;
	}


	public String getCity() {
		
		if(City == null){
			City = "";
	    }
		
		return City;
	}


	public void setCity(String city) {
		City = city;
	}


	public String getZip() {
		if(Zip == null)
		{
			Zip = "";
		}
		return Zip;
	}


	public void setZip(String zip) {
		Zip = zip;
	}


	public String getEmailId() {
		

		if(EmailId == null)
		{
			EmailId = "";
		}
		return EmailId;
	}


	public void setEmailId(String emailId) {
		EmailId = emailId;
	}


	public String getMobileNumber() {
		
		if(MobileNumber == null)
		{
			MobileNumber = "";
		}
		return MobileNumber;
		
	}


	public void setMobileNumber(String mobileNumber) {
		MobileNumber =  mobileNumber;
	}


	public String getStatus() {
		
		if(Status == null)
		{
			Status = "";
		}
		
		return Status;
	}
	
	


	public void setStatus(String status) {
		Status = status;
	}
	
	


	public String getType() {
		if(Type == null)
		{
			Type = "";
		}
		return Type;
	}


	public void setType(String type) {
		Type = type;
	}


	@Override
	public String toString() {
		return "Benificier [Pid=" + Pid + ", Name=" + Name + ", Sequence=" + Sequence + ", Address1=" + Address1
				+ ", State=" + State + ", City=" + City + ", Zip=" + Zip + ", EmailId=" + EmailId + ", MobileNumber="
				+ MobileNumber + ", Status=" + Status + ", NickName=" + NickName + ", Account=" + Account
				+ ", CustomerId=" + CustomerId + ", AccountType=" + AccountType + ", Type=" + Type + ", BankIfsc="
				+ BankIfsc + ", BankCity=" + BankCity + ", BankBranch=" + BankBranch + ", BankName=" + BankName
				+ ", MaxLimit=" + MaxLimit + ", getCustomerId()=" + getCustomerId() + ", getPid()=" + getPid()
				+ ", getName()=" + getName() + ", getNickName()=" + getNickName() + ", getAccount()=" + getAccount()
				+ ", getAccountType()=" + getAccountType() + ", getBankIfsc()=" + getBankIfsc() + ", getBankCity()="
				+ getBankCity() + ", getBankBranch()=" + getBankBranch() + ", getMaxLimit()=" + getMaxLimit()
				+ ", getBankName()=" + getBankName() + ", getSequence()=" + getSequence() + ", getAddress1()="
				+ getAddress1() + ", getState()=" + getState() + ", getCity()=" + getCity() + ", getZip()=" + getZip()
				+ ", getEmailId()=" + getEmailId() + ", getMobileNumber()=" + getMobileNumber() + ", getStatus()="
				+ getStatus() + ", getType()=" + getType() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}


	

	

	

	
	
    
    
    
    
    
    
    

}
