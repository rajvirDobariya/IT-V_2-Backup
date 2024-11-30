package com.suryoday.loantrackingphase2.mapper;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.suryoday.loantrackingphase2.model.Document;
import com.suryoday.loantrackingphase2.model.DocumentType;
import com.suryoday.loantrackingphase2.model.ProductMaster;

public class ProductMasterMaapper {

	public static JSONObject convertToJSON(ProductMaster product) {

		// Initialize the top-level JSON object for ProductMaster
		JSONObject productJson = new JSONObject();
		productJson.put("id", product.getId());
		productJson.put("productName", product.getProductName());

		// Create a JSON array for DocumentTypes
		JSONArray documentTypesArray = new JSONArray();

		// Iterate over the DocumentTypes
		Set<DocumentType> uniqueDocumentTypes = new HashSet<>(product.getDocumentTypes());
		for (DocumentType documentType : uniqueDocumentTypes) {
			System.out.println("DOCUMENT TYPE NAME ::" + documentType.getTypeName());
			JSONObject documentTypeJson = new JSONObject();

			// Create a JSON array for Documents under each DocumentType
			JSONArray documentsArray = new JSONArray();
			
			for (Document document : documentType.getDocuments()) {
				JSONObject documentJson = new JSONObject();
				documentJson.put("id", document.getId());
				documentJson.put("documentName", document.getDocumentName());
				documentsArray.put(documentJson); // Add each document to the array
			}

			// Add documents to documentType JSON object
			documentTypeJson.put("documents", documentsArray);
			documentTypeJson.put("id", documentType.getId());
			documentTypeJson.put("name", documentType.getTypeName());

			// Add documentType to the documentTypes array
			documentTypesArray.put(documentTypeJson);
		}

		// Add the documentTypes array to the product JSON
		productJson.put("documentTypes", documentTypesArray);

		// Return the constructed JSON object
		return productJson;
	}
}
