package com.suryoday.payment.payment;

public class Emi {
	public static void main(String[] args) {
		double annualIncome = 5000;
		double monthlyIncome = annualIncome / 12;

		System.out.println(Math.round(monthlyIncome));
	}
}
