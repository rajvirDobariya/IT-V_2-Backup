package com.suryoday.aocpv.service;

import java.util.List;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.suryoday.aocpv.pojo.Preapproval;

@Service
public interface AocpService {

//	insert Api
//	ALready exist NickName  Api
//	ALready exist Account  Api
//	delete api pid 
//	fetch by  accountid    confirmation
//	update api 

	// public List<Benificier> fetchAll();

	JSONObject checkappversion(String paramString);

	List<Preapproval> preapprovallist();

//	JSONObject saveBenificier(String paramString,String customerid);
//	
//	JSONObject modifyBenificier(String paramString,String customerid);
//	
//	
//	JSONObject deleteBenificier(String paramString);
//	
//	
//	public List<Benificier>  FindAllCustomer(String customerid);
//	
//	public List<Benificier>  viewcustmerbytype(String customerid,String accounttype);
//
//	public void modifyBenificierStatus();

}
