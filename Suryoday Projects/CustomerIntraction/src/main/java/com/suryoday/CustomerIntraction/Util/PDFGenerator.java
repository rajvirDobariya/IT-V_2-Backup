package com.suryoday.CustomerIntraction.Util;

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

import com.itextpdf.io.source.ByteArrayOutputStream;
import com.lowagie.text.DocumentException;

public class PDFGenerator {

	public static String generatePDF(JSONObject result) {

		try {
			String templatePath = "/MeetingHTMLTemplate.html";
			String template = loadTemplate(templatePath);
			JSONObject data = result.optJSONObject("Data");
			if (data == null) {
				throw new IllegalArgumentException("Data is missing from the JSON result.");
			}

			JSONObject customerIntraction = data.optJSONObject("customerIntraction");
			if (customerIntraction == null) {
				throw new IllegalArgumentException("customerIntraction data is missing.");
			}

			// Prepare the dynamic content
			Map<String, String> values = new HashMap<>();
			String[] parts = ((String) customerIntraction.opt("meetingNumber")).split("/");
			if (parts.length == 4) {
				String prefix = parts[0]; // "BLCSCM"
				String number = parts[1]; // "1101"
				String month = parts[2]; // "January"
				String year = parts[3];
				String meeting = number + "/" + month + "/" + year;
				values.put("meeting", meeting);
			}
			values.put("meetingNumber", customerIntraction.optString("meetingNumber"));
			values.put("meetingDate", customerIntraction.optString("meetingDate"));
			values.put("meetingStartDate", customerIntraction.optString("meetingStartDate"));
			values.put("meetingEndTime", customerIntraction.optString("meetingEndTime"));
			values.put("makerId", customerIntraction.optString("createdBy"));
			values.put("makerDate", customerIntraction.optString("createdDate"));
			values.put("checkerId", customerIntraction.optString("updatedBy"));
			values.put("checkerDate", customerIntraction.optString("updatedDate"));

			// Create tables as HTML
			JSONArray members = data.optJSONArray("memberList");
			JSONArray summary = data.optJSONArray("momSummary");
			JSONArray image = data.optJSONArray("image");
			String updatedByHo = "";
			String updatedByHoDate = "";

			if (summary != null) {
				for (int i = 0; i < summary.length(); i++) {
					// Get each JSONObject in the JSONArray
					JSONObject obj = summary.optJSONObject(i);
					if (obj != null) {
						// Extract the specific string value
						updatedByHo = obj.optString("updatedBy");
						updatedByHoDate = obj.optString("updatedDate");
					}
				}
			}

			customerIntraction.put("updatedByHo", updatedByHo);
			customerIntraction.put("updatedByHoDate", updatedByHoDate);
			values.put("customerIntractionTable", createLogTable(customerIntraction));
			values.put("memberTable", createMembersTable(members));
			values.put("summaryTable", createSummaryTable(summary));
			values.put("logTable", createLogTable(customerIntraction));
			values.put("images", createImage(image));

			// Replace placeholders in the template
			String htmlContent = replacePlaceholders(template, values);
			String htmlToXhtml = htmlToXhtml(htmlContent);

			// Render the document to PDF
			try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
				ITextRenderer renderer = new ITextRenderer();
				renderer.setDocumentFromString(htmlToXhtml);
				renderer.layout();
				renderer.createPDF(baos);
				byte[] pdfBytes = baos.toByteArray();

				// Save the PDF file
				try (FileOutputStream fos = new FileOutputStream("output.pdf")) {
					fos.write(pdfBytes);
				}

				return Base64.getEncoder().encodeToString(pdfBytes);
			}

		} catch (IOException | DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String htmlToXhtml(String html) {
		Document document = Jsoup.parse(html);
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

	private static String createLogTable(JSONObject customerIntracion) {
		StringBuilder sb = new StringBuilder();
		sb.append("<table>").append(
				"<tr><th>Maker ID</th><th>Maker Date & Time</th><th>Checker ID</th><th>Checker Date & Time</th><th>HO Verifier ID</th><th>HO Verifier Date & Time</th></tr>");

		sb.append("<tr>").append("<td>").append(customerIntracion.optString("makerId")).append("</td>").append("<td>")
				.append(customerIntracion.optString("makerDate")).append("</td>").append("<td>")
				.append(customerIntracion.optString("checkerId")).append("</td>").append("<td>")
				.append(customerIntracion.optString("checkerDate")).append("</td>").append("<td>")
				.append(customerIntracion.optString("updatedByHo")).append("</td>").append("<td>")
				.append(customerIntracion.optString("updatedByHoDate")).append("</td>").append("</tr>");
		sb.append("</table>");
		return sb.toString();
	}

	private static String createMembersTable(JSONArray members) {
		StringBuilder sb = new StringBuilder();
		sb.append("<table>").append("<tr><th>Member Role</th><th>Member Name</th><th>Member ID</th></tr>");
		for (int i = 0; i < members.length(); i++) {
			JSONObject memberData = members.getJSONObject(i);
			sb.append("<tr>").append("<td>").append(memberData.optString("role")).append("</td>").append("<td>")
					.append(memberData.optString("memberName")).append("</td>").append("<td>")
					.append(memberData.optLong("memberId")).append("</td>").append("</tr>");
		}
		sb.append("</table>");
		return sb.toString();
	}

	private static String createImage(JSONArray image) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < image.length(); i++) {
			JSONObject imageData = image.getJSONObject(i);
			String base64Image = imageData.optString("base64Image");
			String type = imageData.optString("documentType");
			sb.append("<img src='data:image/").append(type).append(";base64,").append(base64Image)
					.append("' style='width:300px; height:auto;' />").append("<br><br>");
		}
		return sb.toString();
	}

	private static String createSummaryTable(JSONArray summary) {
		StringBuilder sb = new StringBuilder();
		sb.append("<table>").append("<tr>").append("<th>Sl. No.</th>").append("<th>Topic of Discussion</th>")
				.append("<th>Branch Actionable (if any)</th>").append("<th>Feedback of customer</th>")
				.append("<th>ETA</th>").append("</tr>");

		for (int i = 0; i < summary.length(); i++) {
			JSONObject summaryItem = summary.getJSONObject(i);
			sb.append("<tr>").append("<td>").append(i + 1).append("</td>").append("<td>")
					.append(summaryItem.getString("topicOfDiscussion")).append("</td>").append("<td>")
					.append(summaryItem.getString("branchActionable")).append("</td>").append("<td>")
					.append(summaryItem.getString("feedback")).append("</td>").append("<td>")
					.append(summaryItem.getString("ETA")).append("</td>").append("</tr>");
		}

		sb.append("</table>");
		return sb.toString();
	}

}