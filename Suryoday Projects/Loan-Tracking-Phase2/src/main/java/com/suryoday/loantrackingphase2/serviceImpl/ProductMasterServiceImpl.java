package com.suryoday.loantrackingphase2.serviceImpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.suryoday.loantrackingphase2.dto.ProductMasterDTO;
import com.suryoday.loantrackingphase2.exception.LoanException;
import com.suryoday.loantrackingphase2.mapper.ProductMasterMaapper;
import com.suryoday.loantrackingphase2.model.Document;
import com.suryoday.loantrackingphase2.model.DocumentType;
import com.suryoday.loantrackingphase2.model.ProductMaster;
import com.suryoday.loantrackingphase2.repository.DocumentRepository;
import com.suryoday.loantrackingphase2.repository.ProductMasterRepository;
import com.suryoday.loantrackingphase2.service.ProductMasterService;
import com.suryoday.loantrackingphase2.utils.EncryptDecryptHelper;

@Service
public class ProductMasterServiceImpl implements ProductMasterService {

	private static final Logger LOG = LoggerFactory.getLogger(ProductMasterServiceImpl.class.getName());

	@Autowired
	private DocumentRepository documentRepository;
	@Autowired
	private ProductMasterRepository productMasterRepository;
	@Autowired
	private com.suryoday.loantrackingphase2.repository.DocumentTypeRepository documentTypeRepository;

	private final EncryptDecryptHelper encryptDecryptHelper;

	@Autowired
	public ProductMasterServiceImpl(EncryptDecryptHelper encryptDecryptHelper) {
		this.encryptDecryptHelper = encryptDecryptHelper;
	}
	
	@Override
	public String addExcel(MultipartFile file) {
	    // Delete existing records
	    documentRepository.deleteAll();
	    documentTypeRepository.deleteAll();
	    productMasterRepository.deleteAll();

	    long productIdCounter = 1; // Start ProductMaster ID from 1
	    long documentTypeIdCounter = 1; // Start DocumentType ID from 1
	    long documentIdCounter = 1; // Start Document ID from 1

	    long productOrderNo = 1; // Start ProductMaster orderNo from 1
	    long documentTypeOrderNo = 1; // Start DocumentType orderNo from 1
	    long documentOrderNo = 1; // Start Document orderNo from 1

	    try {
	        if (file.isEmpty()) {
	            LOG.error("Uploaded file is empty.");
	            return "File is empty.";
	        }

	        // Open the workbook from the uploaded MultipartFile
	        InputStream inputStream = file.getInputStream();
	        Workbook workbook = new XSSFWorkbook(inputStream);
	        LOG.debug("Workbook loaded successfully from uploaded file.");

	        // Iterate through each sheet
	        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
	            Sheet sheet = workbook.getSheetAt(i);
	            String productName = workbook.getSheetName(i);

	            LOG.debug("Processing sheet: " + productName);

	            // Create ProductMaster
	            ProductMaster productMaster = new ProductMaster();
	            productMaster.setId(productIdCounter++); // Manually set the ID
	            productMaster.setProductName(productName);
	            productMaster.setOrderNo(productOrderNo++); // Set ProductMaster orderNo

	            JSONArray documentTypesArray = new JSONArray(); // Array for all DocumentTypes
	            DocumentType currentDocumentType = null;
	            documentTypeOrderNo = 1; // Reset DocumentType orderNo for each new product

	            for (Row row : sheet) {
	                // Skip the header row
	                if (row.getRowNum() == 0) {
	                    LOG.debug("Skipping header row.");
	                    continue;
	                }

	                Cell cell0 = row.getCell(0);
	                Cell cell1 = row.getCell(1);
	                if (cell0 == null || cell1 == null) {
	                    LOG.debug("Skipping row due to null cell values: Row " + row.getRowNum());
	                    continue;
	                }

	                String documentTypeName = cell0.getStringCellValue();
	                String documentName = cell1.getStringCellValue();
	                if (documentTypeName == null || documentTypeName.isEmpty() || documentName == null
	                        || documentName.isEmpty()) {
	                    LOG.debug("Skipping row due to null or empty values: Row " + row.getRowNum());
	                    continue;
	                }

	                LOG.debug("Row " + row.getRowNum() + " - Document Type: " + documentTypeName + ", Document: "
	                        + documentName);

	                // Check if we are processing a new DocumentType
	                if (currentDocumentType == null || !currentDocumentType.getTypeName().equals(documentTypeName)) {
	                    // If there's a previous DocumentType, add it to the product
	                    if (currentDocumentType != null) {
	                        LOG.debug("Adding DocumentType: " + currentDocumentType.getTypeName());
	                        productMaster.getDocumentTypes().add(currentDocumentType);
	                    }

	                    // Create a new DocumentType
	                    currentDocumentType = new DocumentType();
	                    currentDocumentType.setId(documentTypeIdCounter++); // Manually set the ID
	                    currentDocumentType.setTypeName(documentTypeName);
	                    currentDocumentType.setProduct(productMaster); // Set the ProductMaster reference
	                    currentDocumentType.setOrderNo(documentTypeOrderNo++); // Set DocumentType orderNo

	                    LOG.debug("Created new DocumentType: " + documentTypeName);
	                }

	                // Create Document and add to current DocumentType
	                Document document = new Document();
	                document.setId(documentIdCounter++); // Manually set the ID
	                document.setDocumentName(documentName);
	                document.setDocumentType(currentDocumentType); // Set the DocumentType reference
	                document.setOrderNo(documentOrderNo++); // Set Document orderNo

	                // Add document to DocumentType
	                currentDocumentType.getDocuments().add(document);

	                LOG.debug("Added document: " + documentName);
	            }

	            // Add the last DocumentType to the product
	            if (currentDocumentType != null) {
	                LOG.debug("Adding last DocumentType: " + currentDocumentType.getTypeName());
	                productMaster.getDocumentTypes().add(currentDocumentType);
	            }

	            // Save ProductMaster and related entities to the database
	            productMasterRepository.save(productMaster);

	            // Reset documentOrderNo for the next product
	            documentOrderNo = 1;

	            LOG.debug("Finished processing sheet: " + productName);
	        }

	    } catch (IOException e) {
	        LOG.error("Error while processing Excel file", e);
	    }

	    LOG.debug("Data import completed.");
	    JSONObject response = new JSONObject();
	    response.put("status", "success");
	    return response.toString();
	}


	@Override
	public JSONObject getAllProductMasters(String channel, String X_Session_ID, String X_User_ID, boolean isEncrypt) {
		JSONObject response = new JSONObject();
		JSONObject responseData = new JSONObject();
		JSONObject requestJson = null;

		// 1 VALIDATE & DECRYPT REQUEST
		if (isEncrypt) {
			encryptDecryptHelper.validateHeadersAndSessionId(channel, X_Session_ID);
		}

		// 4. PROCESS
		List<ProductMasterDTO> allProducts = productMasterRepository.findAllProducts();
		responseData.put("allProducts", allProducts);
		response.put("Data", responseData);
		return response;
	}

	@Override
	public JSONObject getProductMasterById(String request, String channel, String X_Session_ID, String X_User_ID,
			boolean isEncrypt) {
		JSONObject response = new JSONObject();
		JSONObject responseData = new JSONObject();
		JSONObject requestJson = new JSONObject();

		// 1 VALIDATE & DECRYPT REQUEST
		if (isEncrypt) {
			requestJson = encryptDecryptHelper.validateHeadersAndSessionId(request, channel, X_Session_ID);
		} else {
			requestJson = new JSONObject(request);
		}

		// 4. PROCESS
		LOG.debug("DecryptRequestString response :: {}", requestJson.toString());

		// 4. PROCESS
		// 4.1 Get & Validate Data
		JSONObject data = requestJson.optJSONObject("Data");
		if (data == null) {
			LOG.debug("Data is null:: {}", data);
			throw new LoanException("Data is null :" + data);
		}

		// Validate productId
		Long productId = data.optLong("productId");
		if (productId == 0) {
			LOG.debug("productId null or empty :: {}", productId);
			throw new LoanException("productId user userRole is :" + productId);
		}

		ProductMaster product = findById(productId);
		JSONObject productJSON = ProductMasterMaapper.convertToJSON(product);
		responseData.put("product", productJSON);
		response.put("Data", responseData);
		return response;
	}

	@Override
	public ProductMaster findById(Long productId) {
		ProductMaster product = productMasterRepository.findById(productId)
				.orElseThrow(() -> new LoanException("Product not found with id: " + productId));
		return product;
	}

	@Override
	public String getEmployeeNameById(Integer empId) {
		return productMasterRepository.getEmployeeNameById(empId);
	}

	public String checkSessingId(String sessionId) {
		return productMasterRepository.checkSessingId(sessionId);
	}

}