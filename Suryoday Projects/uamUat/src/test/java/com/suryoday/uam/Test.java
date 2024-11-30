package com.suryoday.uam;


import java.util.stream.Collectors;

public class Test {

	public static void main(String[] args){
//		counting of character in String		
		String str="akash";
		 str.chars()
		.mapToObj(c -> (char) c)
		.collect(Collectors.groupingBy(c -> c, Collectors.counting()))
		.forEach((character, count) -> System.out.println(character + ": " + count));
		
	}
}
