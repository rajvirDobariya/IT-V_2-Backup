package com.suryoday.aocpv.service;

import java.util.List;

import com.suryoday.aocpv.pojo.AocpvPerposecategory;

public interface AocpvPerposecategoryService {

	public List<AocpvPerposecategory> fetchByCategoryId(String categoryId);

}
