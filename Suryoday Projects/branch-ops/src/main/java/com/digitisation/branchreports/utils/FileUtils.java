package com.digitisation.branchreports.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.digitisation.branchreports.exception.DigitizationException;
import com.digitisation.branchreports.model.Filetable;

@Component
public class FileUtils {

	private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);

	@Value("${filePath}")
	private String filePathInstance;

	private static String filePath;

	@PostConstruct
	public void init() {
		FileUtils.filePath = this.filePathInstance;
	}

	public static String saveDocumentToFile(MultipartFile document) {
		String uniqueFileName = "";
		try {
			uniqueFileName = UUID.randomUUID().toString() + Constants.documentExtention;
			String fullPath = filePath + File.separator + uniqueFileName;
			File file = new File(fullPath);
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(document.getBytes());
			fos.flush();
			LOG.debug(uniqueFileName);
		} catch (Exception e) {
			LOG.debug("Exception occurs in saveDocumentToFile ::" + e.getMessage());
		}
		return uniqueFileName;
	}

	public static String convertBase64ToFile(Filetable document) {
		String uniqueFileName = "";
		try {
			uniqueFileName = UUID.randomUUID().toString() + Constants.documentExtention;
			String fullPath = filePath + File.separator + uniqueFileName;
			File file = new File(fullPath);
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(Base64.getDecoder().decode(document.getBase64()));
			fos.flush();
			LOG.debug(uniqueFileName);
		} catch (Exception e) {
			LOG.debug("Exception occurs in convertBase64ToFile ::" + e.getMessage());
		}
		return uniqueFileName;
	}

	public static byte[] convertFileToBytes(String documentPath) {
		byte[] fileBytes = null;
		try {
			String fullPath = filePath + File.separator + documentPath;
			// Read file bytes
			File file = new File(fullPath);
			fileBytes = Files.readAllBytes(file.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileBytes;
	}

	public static String convertFileToBase64(String documentPath) {
		String base64Encoded = "";
		try {
			String fullPath = filePath + File.separator + documentPath;
			// Read file bytes
			File file = new File(fullPath);
			byte[] fileBytes = Files.readAllBytes(file.toPath());

			// Encode file bytes to Base64
			base64Encoded = Base64.getEncoder().encodeToString(fileBytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return base64Encoded;
	}

	public static void bytesToExcelFile(byte[] excelBytes, String filePath) {
		// Define the output file path
		String outputFilePath = filePath + "Branches.xlsx";
		try {
			// Write bytes to file
			FileOutputStream fileOutputStream = new FileOutputStream(outputFilePath);
			fileOutputStream.write(excelBytes);
			System.out.println("Excel file saved at: " + outputFilePath);
		} catch (Exception e) {
			throw new DigitizationException("Exception occur in generate file of bytes.");
		}
	}
}
