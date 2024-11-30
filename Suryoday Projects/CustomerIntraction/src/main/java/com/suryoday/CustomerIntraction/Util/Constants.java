package com.suryoday.CustomerIntraction.Util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class Constants {

	public static Map<String, String> getReportStatusMap() {

		Map<String, String> reportStatusMap = new HashMap<>();

		reportStatusMap.put("Today's Reports", "Not Submitted");
		reportStatusMap.put("Pending Reports", "Pending");
		reportStatusMap.put("Submitted Reports", "Pending at Checker");
		reportStatusMap.put("Rejected Reports", "Pending at Maker");
		reportStatusMap.put("Approved Reports", "Submitted");

		return reportStatusMap;
	}

	// Static method to access forbidden name keywords map
	public static Map<String, List<String>> getForbiddenNameKeywords() {

		Map<String, List<String>> forbiddenNameKeywords = new HashMap<>();

		forbiddenNameKeywords.put("text/plain", Arrays.asList("pdf", "sheet", "document", "jpeg", "jpg", "docx", "doc",
				"xlsx", "xls", "png", "spreadsheet"));
		forbiddenNameKeywords.put("application/pdf", Arrays.asList("text", "sheet", "document", "jpeg", "jpg", "docx",
				"doc", "xlsx", "xls", "png", "spreadsheet"));
		forbiddenNameKeywords.put("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
				Arrays.asList("text", "pdf", "document", "jpeg", "jpg", "docx", "doc", "png", "spreadsheet"));
		forbiddenNameKeywords.put("application/vnd.openxmlformats-officedocument.wordprocessingml.document",
				Arrays.asList("text", "pdf", "sheet", "jpeg", "jpg", "xlsx", "xls", "png", "spreadsheet"));
		forbiddenNameKeywords.put("image/jpeg",
				Arrays.asList("text", "pdf", "sheet", "document", "docx", "doc", "xlsx", "xls", "png", "spreadsheet"));
		forbiddenNameKeywords.put("image/png", Arrays.asList("text", "pdf", "sheet", "document", "jpeg", "jpg", "docx",
				"doc", "xlsx", "xls", "spreadsheet"));
		forbiddenNameKeywords.put("application/vnd.oasis.opendocument.spreadsheet",
				Arrays.asList("text", "pdf", "document", "jpeg", "jpg", "docx", "doc", "xlsx", "xls", "png"));
		forbiddenNameKeywords.put("application/vnd.oasis.opendocument.text", Arrays.asList("pdf", "sheet", "document",
				"jpeg", "jpg", "docx", "doc", "xlsx", "xls", "png", "spreadsheet"));
		return forbiddenNameKeywords;
	}

	// Static method to access forbidden name keywords map
	public static Pattern[] getScriptPatterns() {
		Pattern[] SCRIPT_PATTERNS = new Pattern[] {
				// JavaScript Patterns
				Pattern.compile(
						"\\b(eval|alert|document\\.write|document\\.location|location\\.href|window\\.open|window\\.eval|window\\.execScript)\\b",
						Pattern.CASE_INSENSITIVE),
				Pattern.compile("(<script.*?>.*?</script>)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
				Pattern.compile("\\b(on[a-z]+\\s*=\\s*['\"].*?javascript:.*?['\"])", Pattern.CASE_INSENSITIVE),
				Pattern.compile("javascript:\\s*['\"].*?['\"]", Pattern.CASE_INSENSITIVE),
				Pattern.compile("<iframe[^>]*src=['\"].*?javascript:.*?['\"].*?>", Pattern.CASE_INSENSITIVE),
				Pattern.compile("<img[^>]*src=['\"].*?javascript:.*?['\"].*?>", Pattern.CASE_INSENSITIVE),
				Pattern.compile("document\\.cookie", Pattern.CASE_INSENSITIVE),
				Pattern.compile("window\\.name", Pattern.CASE_INSENSITIVE),
				Pattern.compile("eval\\s*\\(", Pattern.CASE_INSENSITIVE),
				Pattern.compile("function\\s+[a-zA-Z_$][0-9a-zA-Z_$]*\\s*\\(", Pattern.CASE_INSENSITIVE),

				// HTML Patterns
				Pattern.compile("<\\s*script\\b[^>]*>(.*?)<\\s*/\\s*script\\s*>",
						Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
				Pattern.compile("<\\s*iframe\\b[^>]*>(.*?)<\\s*/\\s*iframe\\s*>",
						Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
				Pattern.compile("<\\s*embed\\b[^>]*>(.*?)<\\s*/\\s*embed\\s*>",
						Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
				Pattern.compile("<\\s*object\\b[^>]*>(.*?)<\\s*/\\s*object\\s*>",
						Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
				Pattern.compile("<\\s*img\\b[^>]*src\\s*=\\s*['\"].*?javascript:.*?['\"].*?>",
						Pattern.CASE_INSENSITIVE),
				Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE) };

		return SCRIPT_PATTERNS;
	}

}
