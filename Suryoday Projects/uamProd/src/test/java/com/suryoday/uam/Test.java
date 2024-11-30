package com.suryoday.uam;

import java.util.ArrayList;
import java.util.List;

import com.suryoday.uam.pojo.UserAccessResponse;



public class Test {

	
	public static void main(String[] args) {
		List<UserAccessResponse> filterMap=new ArrayList<>();
		filterMap.add(new UserAccessResponse("INSURANCE", "clinical_notes"));
		long count = filterMap.stream().filter(e->e.getModuleName().equals("INSURANCE")).count();
		if(count<1) {
			System.out.println(count);
		}
	}
}
