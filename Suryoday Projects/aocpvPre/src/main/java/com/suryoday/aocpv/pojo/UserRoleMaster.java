package com.suryoday.aocpv.pojo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Entity
public class UserRoleMaster implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String brcd;
	private String uid;
	private String ucls;
	private String ufn;
	private String acn;
	private String lang;
	private String pexxpr_date;
	private String lsgn_date;
	private String zlgtim;
	private String tsdr;
	private String tscr;
	private String csovr;
	private String chshrt;
	private String altculs;
	private String rodr;
	private String rocr;
	private String batrej;
	private String tpm;
	private String currenv;
	private String eaddr;
	private String cuid;
	private String cdat_date;
	private String ctim;
	private String status;
	private String sreason;
	private String mobile;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getBrcd() {
		return brcd;
	}
	public void setBrcd(String brcd) {
		this.brcd = brcd;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getUcls() {
		return ucls;
	}
	public void setUcls(String ucls) {
		this.ucls = ucls;
	}
	public String getUfn() {
		return ufn;
	}
	public void setUfn(String ufn) {
		this.ufn = ufn;
	}
	public String getAcn() {
		return acn;
	}
	public void setAcn(String acn) {
		this.acn = acn;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public String getPexxpr_date() {
		return pexxpr_date;
	}
	public void setPexxpr_date(String pexxpr_date) {
		this.pexxpr_date = pexxpr_date;
	}
	public String getLsgn_date() {
		return lsgn_date;
	}
	public void setLsgn_date(String lsgn_date) {
		this.lsgn_date = lsgn_date;
	}
	public String getZlgtim() {
		return zlgtim;
	}
	public void setZlgtim(String zlgtim) {
		this.zlgtim = zlgtim;
	}
	public String getTsdr() {
		return tsdr;
	}
	public void setTsdr(String tsdr) {
		this.tsdr = tsdr;
	}
	public String getTscr() {
		return tscr;
	}
	public void setTscr(String tscr) {
		this.tscr = tscr;
	}
	public String getCsovr() {
		return csovr;
	}
	public void setCsovr(String csovr) {
		this.csovr = csovr;
	}
	public String getChshrt() {
		return chshrt;
	}
	public void setChshrt(String chshrt) {
		this.chshrt = chshrt;
	}
	public String getAltculs() {
		return altculs;
	}
	public void setAltculs(String altculs) {
		this.altculs = altculs;
	}
	public String getRodr() {
		return rodr;
	}
	public void setRodr(String rodr) {
		this.rodr = rodr;
	}
	public String getRocr() {
		return rocr;
	}
	public void setRocr(String rocr) {
		this.rocr = rocr;
	}
	public String getBatrej() {
		return batrej;
	}
	public void setBatrej(String batrej) {
		this.batrej = batrej;
	}
	public String getTpm() {
		return tpm;
	}
	public void setTpm(String tpm) {
		this.tpm = tpm;
	}
	public String getCurrenv() {
		return currenv;
	}
	public void setCurrenv(String currenv) {
		this.currenv = currenv;
	}
	public String getEaddr() {
		return eaddr;
	}
	public void setEaddr(String eaddr) {
		this.eaddr = eaddr;
	}
	public String getCuid() {
		return cuid;
	}
	public void setCuid(String cuid) {
		this.cuid = cuid;
	}
	public String getCdat_date() {
		return cdat_date;
	}
	public void setCdat_date(String cdat_date) {
		this.cdat_date = cdat_date;
	}
	public String getCtim() {
		return ctim;
	}
	public void setCtim(String ctim) {
		this.ctim = ctim;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSreason() {
		return sreason;
	}
	public void setSreason(String sreason) {
		this.sreason = sreason;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
}
