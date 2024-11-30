package com.suryoday.CustomerIntraction.Service.Impl;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.json.JSONObject;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.suryoday.CustomerIntraction.Exception.CustomerIntractionException;
import com.suryoday.CustomerIntraction.Util.Constants;


@Service
public class DocumentFile{

	private static final Logger LOG = LoggerFactory.getLogger(DocumentFile.class.getName());

//	private final PolicyFactory sanitizer = Sanitizers.FORMATTING.and(Sanitizers.LINKS);
	private final PolicyFactory sanitizer = Sanitizers.FORMATTING.and(Sanitizers.LINKS).and(Sanitizers.BLOCKS)
			.and(Sanitizers.STYLES).and(Sanitizers.IMAGES);

	

	public void validateDocument(JSONObject documentJson) {
		LOG.debug("Starting document creation process.");

		String name = documentJson.optString("name");
		String type = documentJson.optString("type");
		String base64 = documentJson.optString("base64");
		// Sanitize inputs
		String sanitizeName = sanitizer.sanitize(documentJson.optString("name"));
		String sanitizeType = sanitizer.sanitize(documentJson.optString("type"));
		String sanitizeBase64 = sanitizer.sanitize(documentJson.optString("base64"));
		LOG.debug("Sanitized inputs: name='{}', type='{}'", sanitizeName, sanitizeType);

		// Validate the fields using sanitized inputs
		validateInputs(sanitizeName, sanitizeType, sanitizeBase64);

		// Decode Base64 content
		byte[] fileContent;
		try {
			fileContent = Base64.getDecoder().decode(base64);
			LOG.debug("Base64 content successfully decoded.");
		} catch (IllegalArgumentException e) {
			LOG.debug("Failed to decode Base64 content.", e);
			throw new IllegalArgumentException("Invalid Base64 content provided.");
		}

		// Validate content based on MIME type
		switch (sanitizeType.toLowerCase()) {
		//PDF
		case "application/pdf":
			validatePDF(fileContent);
			break;
		//WORD
		case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
		case "application/msword":
			validateWord(fileContent);
			break;
		//WORD
		case "application/vnd.oasis.opendocument.text":
			validateOdtFile(fileContent);
			break;
		//TEXT
		case "text/plain":
			validateTextFile(fileContent);
			break;
		case "image/jpeg":
		case "image/png":
			validateImage(fileContent);
			break;
		default:
			String errorMessage = String.format("Unsupported MIME type: %s", sanitizeType);
			LOG.debug(errorMessage);
			throw new IllegalArgumentException(errorMessage);
		}

		// Validate file name against forbidden keywords
		validateFileName(sanitizeName, sanitizeType);
	}

	public void validatePDF(byte[] fileContent) {
		String decodedString = new String(fileContent, StandardCharsets.UTF_8);

		if (decodedString == null || decodedString.isEmpty()) {
			return; // Nothing to validate
		}

		for (Pattern pattern : Constants.getScriptPatterns()) {
			Matcher matcher = pattern.matcher(decodedString);
			if (matcher.find()) {
				throw new CustomerIntractionException("JavaScript or HTML detected in the PDF content.");
			}
		}
	}

	private void validateInputs(String name, String type, String base64) {
		LOG.debug("Validating input fields.");
		if (name == null || name.isEmpty()) {
			LOG.debug("File name is invalid or missing.");
			throw new CustomerIntractionException("File name is invalid or missing.");
		}
		if (type == null || type.isEmpty()) {
			LOG.debug("MIME type is invalid or missing.");
			throw new CustomerIntractionException("MIME type is invalid or missing.");
		}
		if (base64 == null || base64.isEmpty()) {
			LOG.debug("Base64 content is invalid or missing.");
			throw new CustomerIntractionException("Base64 content is invalid or missing.");
		}
		LOG.debug("Input fields validated successfully.");
	}

	private void validateFileName(String sanitizeName, String sanitizeType) {
		LOG.debug("Validating file name against forbidden keywords.");

		List<String> forbiddenKeywords = Constants.getForbiddenNameKeywords().get(sanitizeType.toLowerCase());
		if (forbiddenKeywords != null) {
			for (String keyword : forbiddenKeywords) {
				if (sanitizeName.toLowerCase().contains(keyword)) {
					String errorMessage = String.format(
							"File name validation failed: '%s' cannot contain '%s' for MIME type '%s'", sanitizeName,
							keyword, sanitizeType);
					LOG.debug(errorMessage);
					throw new CustomerIntractionException(errorMessage);
				}
			}
		}
		LOG.debug("File name validated successfully.");
	}

	public void validateWord(byte[] fileContent) {
		LOG.debug("Validating Word document content.");
		boolean isValid = false;

		// First, attempt to validate as a DOCX file
		try (ByteArrayInputStream bais = new ByteArrayInputStream(fileContent)) {
			XWPFDocument docx = new XWPFDocument(bais);
			for (XWPFParagraph paragraph : docx.getParagraphs()) {
				paragraph.getText();
			}
			LOG.debug("DOCX content validated successfully.");
			isValid = true; // Mark as valid if DOCX validation passes
		} catch (Exception e) {
			LOG.debug("DOCX validation failed, trying as DOC format.", e);
			throw new CustomerIntractionException("The provided Word File content is invalid.");
		}

		// If DOCX validation failed, try validating as a DOC
		if (!isValid) {
			try (ByteArrayInputStream bais = new ByteArrayInputStream(fileContent)) {
				HWPFDocument doc = new HWPFDocument(bais);
				WordExtractor extractor = new WordExtractor(doc);
				extractor.getText();
				LOG.debug("DOC content validated successfully.");
				isValid = true; // Mark as valid if DOC validation passes
			} catch (Exception e) {
				LOG.debug("DOC content validated successfully.");
				throw new CustomerIntractionException("The provided Word File content is invalid.");
			}
		}

		// If neither DOCX nor DOC validation succeeded, throw an exception
		if (!isValid) {
			throw new CustomerIntractionException("The provided Word File content is invalid.");
		}
	}

	public void validateTextFile(byte[] fileContent) {
		LOG.debug("Validating text file content.");
		try (ByteArrayInputStream bais = new ByteArrayInputStream(fileContent)) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(bais, StandardCharsets.UTF_8));
			StringBuilder contentBuilder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				contentBuilder.append(line);
				contentBuilder.append(System.lineSeparator());
			}
			String content = contentBuilder.toString();
			if (content.isEmpty()) {
				LOG.debug("The provided text file content is empty.");
				throw new CustomerIntractionException("The provided text file content is empty.");
			}
			LOG.debug("Text file content validated successfully.");
		} catch (IOException e) {
			LOG.debug("The provided text file content is invalid.", e);
			throw new CustomerIntractionException("The provided text file content is invalid.");
		}
	}

	public void validateOdtFile(byte[] fileContent) {
		LOG.debug("Validating ODT file content.");

		try (ByteArrayInputStream bais = new ByteArrayInputStream(fileContent);
				ZipInputStream zis = new ZipInputStream(bais)) {

			ZipEntry entry;
			boolean hasContentXml = false;

			// Check if the file is a ZIP file and contains content.xml
			while ((entry = zis.getNextEntry()) != null) {
				if ("content.xml".equals(entry.getName())) {
					hasContentXml = true;
					// Optionally: Validate the XML structure of content.xml if needed
					break;
				}
			}

			if (hasContentXml) {
				LOG.debug("ODT file validated successfully.");
			} else {
				LOG.debug("The provided file does not contain content.xml.");
				throw new CustomerIntractionException("The provided file is not a valid ODT file.");
			}
		} catch (Exception e) {
			LOG.debug("The provided file is invalid or not an ODT file.", e);
			throw new CustomerIntractionException("The provided file is invalid or not an ODT file.");
		}
	}

	public void validateExcelSpreadsheet(byte[] fileContent) {
		LOG.debug("Validating Excel spreadsheet content.");
		try (InputStream inputStream = new ByteArrayInputStream(fileContent)) {
			Workbook workbook = WorkbookFactory.create(inputStream);
			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
				Sheet sheet = workbook.getSheetAt(i);
				for (Row row : sheet) {
					for (Cell cell : row) {
						String cellValue = cell.toString();
					}
				}
			}
			LOG.debug("Excel spreadsheet content validated successfully.");
		} catch (Exception e) {
			LOG.debug("The provided Excel file content is invalid.");
			throw new CustomerIntractionException("The provided Excel file content is invalid.");
		}
	}

	public void validateOdsFile(byte[] fileContent) {
		LOG.debug("Validating ODS file content.");

		try (ByteArrayInputStream bais = new ByteArrayInputStream(fileContent);
				ZipInputStream zis = new ZipInputStream(bais)) {

			ZipEntry entry;
			boolean hasContentXml = false;
			boolean hasMetaInf = false;

			while ((entry = zis.getNextEntry()) != null) {
				if ("content.xml".equals(entry.getName())) {
					hasContentXml = true;
				}
				if ("META-INF/manifest.xml".equals(entry.getName())) {
					hasMetaInf = true;
				}
			}

			if (hasContentXml && hasMetaInf) {
				// Additional content validation
				validateContentXml(fileContent);
				LOG.debug("ODS file content validated successfully.");
			} else {
				LOG.debug("The provided file does not contain required ODS components.");
				throw new CustomerIntractionException("The provided file is not a valid ODS file.");
			}
		} catch (Exception e) {
			LOG.debug("The provided file is invalid or not an ODS file.", e);
			throw new CustomerIntractionException("The provided file is invalid or not an ODS file.");
		}
	}

	private void validateContentXml(byte[] fileContent) throws Exception {
		try (ByteArrayInputStream bais = new ByteArrayInputStream(fileContent);
				ZipInputStream zis = new ZipInputStream(bais)) {
			ZipEntry entry;
			while ((entry = zis.getNextEntry()) != null) {
				if ("content.xml".equals(entry.getName())) {
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder = factory.newDocumentBuilder();
					Document doc = builder.parse(new InputSource(zis));
					// Perform additional validation on the XML if needed
					return; // Exit method if validation is successful
				}
			}
		}
		throw new CustomerIntractionException("content.xml not found or is invalid.");
	}

	public void validateImage(byte[] fileContent) {
		LOG.debug("Validating image content.");
		try (ByteArrayInputStream bais = new ByteArrayInputStream(fileContent)) {
			BufferedImage image = ImageIO.read(bais);
			if (image == null) {
				LOG.debug("The provided image content is invalid.");
				throw new CustomerIntractionException("The provided image content is invalid.");
			}
			LOG.debug("Image content validated successfully.");
		} catch (IOException e) {
			LOG.debug("Failed to read the image content.", e);
			throw new CustomerIntractionException("Failed to read the image content.");
		}
	}

}
