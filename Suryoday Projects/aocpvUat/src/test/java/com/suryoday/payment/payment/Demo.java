package com.suryoday.payment.payment;

import java.util.Arrays;

public class Demo {

	public static void main(String[] args) {
		String s = "silent_listen";
		String[] split = s.split("_");
		String word1 = split[0];
		String word2 = split[1];

		Arrays.sort(word1.toCharArray());
		Arrays.sort(word2.toCharArray());
		System.out.println(word1);
		if (word1.equals(word2)) {
			System.out.println("String contain same character");
		} else {
			System.out.println("String contain different character");
		}
	}
}
