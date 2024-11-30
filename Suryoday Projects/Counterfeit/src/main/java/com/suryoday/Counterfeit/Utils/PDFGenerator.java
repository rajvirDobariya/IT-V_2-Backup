package com.suryoday.Counterfeit.Utils;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.lowagie.text.DocumentException;

public class PDFGenerator {

	/**
	 * This is PDF generator from HTMP Template with dynamic values.
	 **/

	public static String generatePDF(JSONObject result) {
		try {
			// Load the HTML template
			String templatePath = "/CounterfeitHTMLTepmlate.html";

			String template = loadTemplate(templatePath);
			// Extract data from the result
			JSONObject data = result.optJSONObject("Data");
			if (data == null) {
				throw new IllegalArgumentException("Data is missing from the JSON result.");
			}

			JSONObject counterfeit = data.optJSONObject("counterfeit");
			if (counterfeit == null) {
				throw new IllegalArgumentException("Counterfeit data is missing.");
			}

			String dailyMonthly = counterfeit.optString("dailyMonthly");
			dailyMonthly = "Daily".equals(dailyMonthly) ? "date " + counterfeit.optString("detectDate")
					: "month " + counterfeit.optString("month");

			// Prepare the dynamic content
			Map<String, String> values = new HashMap();
			values.put("dailyMonthly", dailyMonthly);
			values.put("receivedStatus",
					(data.optJSONArray("denominations") != null && data.optJSONArray("denominations").length() > 0)
							? "received"
							: "not received");

			// Create tables as HTML
			JSONArray denominations = data.optJSONArray("denominations");
			JSONArray summary = data.optJSONArray("summary");
			values.put("activityId", String.valueOf(counterfeit.optLong("id")));
			values.put("counterfeitTable", createCounterfeitTable(counterfeit));
			values.put("threeChecks", createThreeChecks(counterfeit));
			values.put("denominationsTable", createDenominationsTable(denominations));
			values.put("summaryTable",
					createSummaryTable(summary, data.optLong("grandCount"), data.optLong("grandTotal")));

			// Replace place_holders in the template
			String htmlContent = replacePlaceholders(template, values);
			return generatePDFV1(htmlContent);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String createThreeChecks(JSONObject counterfeit) {
		boolean theNotesHaveBeenImpounded = counterfeit.optBoolean("theNotesHaveBeenImpounded", false);
		boolean theRegisterUpdatedWithDetails = counterfeit.optBoolean("theRegisterUpdatedWithDetails", false);
		boolean anAcknowledgmentReceiptPrepared = counterfeit.optBoolean("anAcknowledgmentReceiptPrepared", false);

		return "<h2>Three Checks Confirmation</h2>" + "<table>" + "<tr><th>Check</th><th>Status</th></tr>"
				+ "<tr><td>Notes Have Been Impounded</td><td>" + (theNotesHaveBeenImpounded ? "Yes" : "No")
				+ "</td></tr>" + "<tr><td>Register Updated With Details</td><td>"
				+ (theRegisterUpdatedWithDetails ? "Yes" : "No") + "</td></tr>"
				+ "<tr><td>Acknowledgment Receipt Prepared</td><td>" + (anAcknowledgmentReceiptPrepared ? "Yes" : "No")
				+ "</td></tr>" + "</table>";
	}

	public static String generatePDFV2(String htmlContent) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			HtmlConverter.convertToPdf(htmlContent, baos);
			byte[] pdfBytes = baos.toByteArray();
			try (FileOutputStream fos = new FileOutputStream("output.pdf")) {
				fos.write(pdfBytes);
			}
			return Base64.getEncoder().encodeToString(pdfBytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String generatePDFV1(String htmlContent) {
		String htmlToXhtml = htmlToXhtml(htmlContent);
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			ITextRenderer renderer = new ITextRenderer();
			renderer.setDocumentFromString(htmlToXhtml);
			renderer.layout();
			renderer.createPDF(baos);
			byte[] pdfBytes = baos.toByteArray();
			try (FileOutputStream fos = new FileOutputStream("output.pdf")) {
				fos.write(pdfBytes);
			}
			return Base64.getEncoder().encodeToString(pdfBytes);
		} catch (IOException | DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String htmlToXhtml(String html) {
		org.jsoup.nodes.Document document = Jsoup.parse(html);
		document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
		return document.html();
	}

	private static String loadTemplate(String templatePath) throws IOException {
		try (java.io.InputStream is = PDFGenerator.class.getResourceAsStream(templatePath);
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
			StringBuilder content = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				content.append(line).append("\n");
			}
			return content.toString();
		}
	}

	private static String replacePlaceholders(String template, Map<String, String> values) {
		String result = template;
		for (Map.Entry<String, String> entry : values.entrySet()) {
			result = result.replace("${" + entry.getKey() + "}", entry.getValue());
		}
		return result;
	}

	private static String createCounterfeitTable(JSONObject counterfeit) {

		return "<h2>Details of Counterfeit Banknotes</h2>" + "<table>"
				+ "<tr><th>Maker Id</th><th>Maker Date And Time</th><th>Checker Id</th><th>Verifier Date And Time</th><th>Branch Code</th><th>Branch Name</th></tr>"
				+ "<tr>" + "<td>" + counterfeit.optLong("roId") + "</td>" + "<td>"
				+ counterfeit.optString("createdDate") + "</td>" + "<td>" + counterfeit.optLong("bmId") + "</td>"
				+ "<td>" + counterfeit.optString("updateDate") + "</td>" + "<td>" + counterfeit.optString("branchCode")
				+ "</td>" + "<td>" + counterfeit.optString("branchName") + "</td>" + "</tr>" + "</table>";
	}

	private static String createDenominationsTable(JSONArray denominations) {
		StringBuilder sb = new StringBuilder();
		if (denominations.length() > 0) {
			sb.append("<h2>Denominations</h2>");
			sb.append("<table><tr>" + "<th style='width: 15%;'>Date of Detection</th>"
					+ "<th style='width: 15%;'>Name of Branch</th>"
					+ "<th style='width: 15%;'>Details of Tenderer Account Number</th>"
					+ "<th style='width: 20%;'>Details of Tenderer Customer Name</th>"
					+ "<th style='width: 10%;'>Denomination</th>" + "<th style='width: 10%;'>Serial Number</th>"
					+ "<th style='width: 15%;'>Security Feature Breached</th>" + "</tr>");

			for (int i = 0; i < denominations.length(); i++) {
				JSONObject denomination = denominations.getJSONObject(i);
				sb.append("<tr>").append("<td>").append(denomination.getString("detectDate")).append("</td>")
						.append("<td>").append(denomination.getString("branchName")).append("</td>").append("<td>")
						.append(denomination.getString("tendererAccountNumber")).append("</td>")
						.append("<td style='word-wrap: break-word;'>")
						.append(denomination.getString("tendererCustomerName")).append("</td>").append("<td>")
						.append(denomination.getInt("denominationNote")).append("</td>")
						.append("<td style='word-wrap: break-word;'>").append(denomination.getString("serialNumber"))
						.append("</td>").append("<td style='word-wrap: break-word;'>")
						.append(denomination.getString("securityFeatureBreached")).append("</td>").append("</tr>");
			}
			sb.append("</table>");
		} else {
			sb.append("").toString();
		}
		return sb.toString();
	}

	private static String createSummaryTable(JSONArray summary, long grandCount, long grandTotal) {
		StringBuilder sb = new StringBuilder();
		if (summary.length() > 0) {
			sb.append("<h2>Summary</h2>");
			sb.append("<table><tr><th>Denomination Note(INR)</th><th>Count</th><th>Total(INR)</th></tr>");
			for (int i = 0; i < summary.length(); i++) {
				JSONObject summaryItem = summary.getJSONObject(i);
				sb.append("<tr>").append("<td>&#8377; ").append(summaryItem.getInt("denominationNote")).append("</td>")
						.append("<td>").append(summaryItem.getInt("count")).append("</td>").append("<td>&#8377; ")
						.append(summaryItem.getLong("total")).append("</td>") // Use HTML entity for ₹
						.append("</tr>");
			}
			sb.append("<tr><td><b>Grand Total</b></td><td><b>").append(grandCount).append("</b></td><td><b>&#8377; ")
					.append(grandTotal).append("</b></td></tr>");
			sb.append("</table>");
		} else {
			sb.append("").toString();
		}

		return sb.toString();
	}

}
