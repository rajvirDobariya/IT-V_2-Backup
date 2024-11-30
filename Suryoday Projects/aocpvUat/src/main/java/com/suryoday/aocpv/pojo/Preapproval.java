package com.suryoday.aocpv.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Preapproval_tbl")
public class Preapproval {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long CustomerID;
	String MEMBER_NAME;
	String REFERENCE_NUMBER;
	String DATE_OF_BIRTH;
	String ADDRESS;
	String STATE;
	String POSTAL;
	String LANDPHONE_NUMBER;

	public Preapproval() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getCustomerID() {
		return CustomerID;
	}

	public void setCustomerID(Long customerID) {
		CustomerID = customerID;
	}

	public String getMEMBER_NAME() {
		return MEMBER_NAME;
	}

	public void setMEMBER_NAME(String mEMBER_NAME) {
		MEMBER_NAME = mEMBER_NAME;
	}

	public String getREFERENCE_NUMBER() {
		return REFERENCE_NUMBER;
	}

	public void setREFERENCE_NUMBER(String rEFERENCE_NUMBER) {
		REFERENCE_NUMBER = rEFERENCE_NUMBER;
	}

	public String getDATE_OF_BIRTH() {
		return DATE_OF_BIRTH;
	}

	public void setDATE_OF_BIRTH(String dATE_OF_BIRTH) {
		DATE_OF_BIRTH = dATE_OF_BIRTH;
	}

	public String getADDRESS() {
		return ADDRESS;
	}

	public void setADDRESS(String aDDRESS) {
		ADDRESS = aDDRESS;
	}

	public String getSTATE() {
		return STATE;
	}

	public void setSTATE(String sTATE) {
		STATE = sTATE;
	}

	public String getPOSTAL() {
		return POSTAL;
	}

	public void setPOSTAL(String pOSTAL) {
		POSTAL = pOSTAL;
	}

	public String getLANDPHONE_NUMBER() {
		return LANDPHONE_NUMBER;
	}

	public void setLANDPHONE_NUMBER(String lANDPHONE_NUMBER) {
		LANDPHONE_NUMBER = lANDPHONE_NUMBER;
	}

	@Override
	public String toString() {
		return "Preapproval [CustomerID=" + CustomerID + ", MEMBER_NAME=" + MEMBER_NAME + ", REFERENCE_NUMBER="
				+ REFERENCE_NUMBER + ", DATE_OF_BIRTH=" + DATE_OF_BIRTH + ", ADDRESS=" + ADDRESS + ", STATE=" + STATE
				+ ", POSTAL=" + POSTAL + ", LANDPHONE_NUMBER=" + LANDPHONE_NUMBER + "]";
	}

}
