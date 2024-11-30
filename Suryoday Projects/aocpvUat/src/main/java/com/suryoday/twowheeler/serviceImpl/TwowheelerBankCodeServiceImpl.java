package com.suryoday.twowheeler.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.twowheeler.pojo.TwowheelerBankCode;
import com.suryoday.twowheeler.repository.TwowheelerBankCodeRepository;
import com.suryoday.twowheeler.service.TwowheelerBankCodeService;

@Service
public class TwowheelerBankCodeServiceImpl implements TwowheelerBankCodeService {

	@Autowired
	TwowheelerBankCodeRepository twowheelerBankCodeRepository;

	@Override
	public List<TwowheelerBankCode> getAll() {
		List<TwowheelerBankCode> list = twowheelerBankCodeRepository.findAll();
		return list;
	}

}
