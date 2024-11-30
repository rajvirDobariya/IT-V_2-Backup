package com.suryoday.hastakshar.utils;

import java.util.ArrayList;
import java.util.List;

public class Constants {
	public static class TransctionType {
		public static String LIMIT_ENHANCEMENT_25L_TO_2CR = "Limit Enhancement up to 2 crore";
		public static String LIMIT_ENHANCEMENT_10L_TO_25L = "Limit Enhancement up to 25 lakh";
		public static String LINK_MOBLIE_NUMBER = "Link Mobile Number";
		public static String FD_OD = "FD OD";

	}

	public static String WRONG_ENTRY = "Wrong Entry";
	public static String NA = "NA";
	public static String APPROVED = "APPROVED";

	public static class UAT {
		// UAT
		public static String EMAIL = "https://intramashery.suryodaybank.co.in/template/email?api_key=kyqak5muymxcrjhc5q57vz9v";
		public static String API_KEY = "kyqak5muymxcrjhc5q57vz9v";
		public static String MB = "https://intramashery.suryodaybank.co.in/ssfbuat/ibm/mobileNumber/updateLimit";
		public static String ENHANCE = "https://intramashery.suryodaybank.co.in/ssfbuat/limit/enhance";
		public static String LINK_TO_CIF = "https://intramashery.suryodaybank.co.in/customers/230000742/case/v2?api_key=kyqak5muymxcrjhc5q57vz9v";
		public static String FD_OD_PREFIX = "https://intramashery.suryodaybank.co.in/ssfbuat/accounts/TDRD/";
		public static String FD_OD_SUFFIX = "/v2?api_key=kyqak5muymxcrjhc5q57vz9v";
		// PROD
//		public static String EMAIL = "https://intramashery.suryodaybank.com/ssfb/template/email?api_key=twkmgdbequkp827u8zdqe5bm";
//		public static String API_KEY = "twkmgdbequkp827u8zdqe5bm";
//		public static String MB = "https://intramashery.suryodaybank.com/ssfb/ibm/mobileNumber/updateLimit";
//		public static String ENHANCE = "https://intramashery.suryodaybank.com/ssfb/limit/enhance";
//		public static String LINK_TO_CIF = "https://intramashery.suryodaybank.com/customers/230000742/case/v2?api_key=twkmgdbequkp827u8zdqe5bm";
//		public static String FD_OD_PREFIX = "https://intramashery.suryodaybank.com/ssfbuat/accounts/TDRD/";
//		public static String FD_OD_SUFFIX = "/v2?api_key=twkmgdbequkp827u8zdqe5bm";

	}

	public static class FD_OD {

		public static class Roles {
			public static final String CBM = "CBM";
			public static final String CBO = "CBO";
			public static final String CFO = "CFO";
			public static final String CEO = "CEO";
		}

		public static class TransactionType {
			public static final String FD_OD_LESS_THAN_50_L = "FD OD Less than 50 L";
			public static final String FD_OD_50_L_TO_1_CR = "FD OD 50 L to 1 Cr";
			public static final String FD_OD_ABOVE_1_CR = "FD OD Above 1 Cr";
		}

		public static List<String> TransactionTypeList = new ArrayList<>();
		static {
			TransactionTypeList.add("FD OD Less than 50 L");
			TransactionTypeList.add("FD OD 50 L to 1 Cr");
			TransactionTypeList.add("FD OD Above 1 Cr");
		}

		public static class TransactionDescription {
			public static final String NINETY_PERCENTAGE = "90%";
			public static final String IF_MORE_THAN_90_TO_95_PERCENTAGE = "If more than 90 to 95%";
			public static final String ABOVE_95_PERCENTAGE = "Above 95%";
		}

		public static class TransactionDescriptionV2 {
			public static final String TWO_PERCENTAGE_OR_MORE = "2% or more";
			public static final String LESS_THAN_2_PERCENTAGE = "Less than 2%";
			public static final String LESS_THAN_1_PERCENTAGE = "Less than 1%";
		}

	}

}
