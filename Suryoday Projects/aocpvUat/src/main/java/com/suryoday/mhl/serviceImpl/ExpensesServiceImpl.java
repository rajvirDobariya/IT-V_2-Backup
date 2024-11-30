package com.suryoday.mhl.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.mhl.pojo.Expenses;
import com.suryoday.mhl.repository.ExpensesRepository;
import com.suryoday.mhl.service.ExpensesService;

@Service
public class ExpensesServiceImpl implements ExpensesService {

	@Autowired
	ExpensesRepository expensesRepository;

	@Override

	public void saveExpensesDetails(Expenses exp) {

		Optional<Expenses> applicationNo = expensesRepository.checkApplicationNo(exp.getApplicationNo());
		System.out.println("Value Of Optional " + applicationNo);

		if (applicationNo.isPresent()) {
			Expenses expenses = applicationNo.get();
			exp.setId(expenses.getId());
		}
		expensesRepository.save(exp);
	}
}
