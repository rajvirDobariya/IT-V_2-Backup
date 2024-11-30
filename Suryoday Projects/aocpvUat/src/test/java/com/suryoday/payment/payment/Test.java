package com.suryoday.payment.payment;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Test {
	public static void main(String[] args) {

//		List<String> list = Arrays.asList("ebBill","pan","akash","sss","akash");
//			List<String> collect = list.stream().filter(t->t.startsWith("e"))
//			.collect(Collectors.toList());
//			list.stream().filter(t->t.startsWith("a")).forEach(t-> System.out.println(t));
//			list.stream().map(t->t.concat("cat")).forEach(t->System.out.println(t));
//			List<String> distinct = list.stream().distinct().collect(Collectors.toList());
//			list.stream().distinct().forEach(t->System.out.println(t));
//			long count = list.stream().filter(t->t.startsWith("a")).count();
//			list.stream().sorted().forEach(t->System.out.println(t));
//			list.stream().sorted(Comparator.reverseOrder()).forEach(t->System.out.println(t));
//==========================================================================================================				
//			List<String> documentType = Arrays.asList("ebBill","pan");
		List<String> list = Arrays.asList("ebBill", "akash", "sss", "akash");
//			Set<DocCheckListResponse> documentResponse=new HashSet<>();
//			list.stream()
//            .collect(Collectors.groupingBy(e -> e, Collectors.counting()))
//            .forEach((element, count) ->documentResponse
//            	.add(new DocCheckListResponse(element,Long.toString(count), "YES")));
//			
//			documentType.stream().filter(element -> !list.contains(element))
//			.forEach(e->documentResponse.add(new DocCheckListResponse(e,"0", "NO")));
//			System.out.println(documentResponse);
//			
		Map<String, Long> collect = list.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
		System.out.println("collect" + collect);
//			String str="akash";
//			 str.chars()
//			.mapToObj(c -> (char) c)
//			.collect(Collectors.groupingBy(c -> c, Collectors.counting()))
//			.forEach((character, count) -> System.out.println(character + ": " + count));
//==========================================================================================================		
//		LocalDate today = LocalDate.now();
//		int dayOfMonth = today.getDayOfMonth();
//		LocalDate plusMonths =null;
//		if(dayOfMonth>=15){
//			plusMonths =today.plusMonths(2).withDayOfMonth(6);
//		}else {
//			 plusMonths = today.plusMonths(1).withDayOfMonth(6);
//		}
//==========================================================================================================		

//		String s="JEFSQVRISUAyMDAzMjAyNA==";
//		 SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
//		String originalInput = "$ARATHI@"+sdf.format(new Date());
//		String encodedString = Base64.getEncoder().encodeToString(originalInput.getBytes());

//==========================================================================================================		

//								-----Occurrences of Each Character-----		
//		String s="akash";
//		Map<Character, Integer> map=new HashMap<>();
//		for(int i=0;i<s.length();i++) {
//			if(map.containsKey(s.charAt(i))) {
//				Integer count = map.get(s.charAt(i));
//				count++;
//				map.put(s.charAt(i), count);
//			}
//			else {
//				map.put(s.charAt(i), 1);
//			}
//		}
//		map.forEach((character, count) -> System.out.println(character + ": " + count));
//		
//			s.chars().mapToObj(c->(char)c)
//			.collect(Collectors.groupingBy(c->c,Collectors.counting()))
//			.forEach((character, count) -> System.out.println(character + ": " + count));

//==========================================================================================================		

//						-----Reverse String-----
//		String s="akash",reverse="";
//		for(int i=0;i<s.length();i++)
//		{
//			char charAt = s.charAt(i);
//			reverse=charAt+reverse;
//		}
//		System.out.println(reverse);
//		String reversed = new StringBuilder(s).reverse().toString();
//		System.out.println(reversed);

//==========================================================================================================		

//		String s="my name is akash";
//		Arrays.stream(s.split(" ")).collect(Collectors.groupingBy(c->c,Collectors.counting()))
//		.forEach((character, count) -> System.out.println(character + ": " + count));

//        String reversedLastWord = Arrays.stream(s.split(" ")) // Split the string into words
//                .map(word -> {
//                    if (word.equals(s.split(" ")[s.split(" ").length - 1])) { // If it's the last word
//                        return new StringBuilder(word).reverse().toString(); // Reverse the last word
//                    } else {
//                        return word; // Keep other words as is
//                    }
//                })
//                .collect(Collectors.joining(" "));
//        String reversed = Arrays.stream(s.split(" ")) // Split the string into words
//                .map(word-> new StringBuilder(word).reverse().toString()).collect(Collectors.joining(" "));
//        System.out.println(reversed);

//==========================================================================================================		

//		 List<Integer> array1 = Arrays.asList(1, 2, 3, 4,5);
//	        List<Integer> array2 = Arrays.asList(5, 6, 7, 8,7);
//	        Map<Integer, Integer> map=new HashMap<>();
//	        for(int i=0;i<array1.size();i++) {
//	        	if(i%2==0 && i<array2.size()-1) {
//	        		map.put(array1.get(i), array2.get(i+1));	
//	        	}
//	        }
//   		 Form a map by taking even index from array1 and odd index from array2
//	        Map<Integer, Integer> resultMap = IntStream.range(0, Math.min(array1.size(), array2.size()))
//	                .filter(i -> i % 2 == 0)
//	                .boxed()
//	                .collect(Collectors.toMap(
//	                        i -> array1.get(i),
//	                        i -> array2.get(i+1)
//	                ));
//
//	        // Print the resulting map
//		String panCardNo="9730880904";
//		
//		String substring = "XXXXXX"+panCardNo.substring(6);
//		System.out.println(substring);

	}
}
