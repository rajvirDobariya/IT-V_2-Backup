package com.digitisation.branchreports.service;

import org.springframework.web.multipart.MultipartFile;

import com.digitisation.branchreports.model.Filetable;

public interface DocumentFileService {

	public Filetable createDocument(MultipartFile file);

}
