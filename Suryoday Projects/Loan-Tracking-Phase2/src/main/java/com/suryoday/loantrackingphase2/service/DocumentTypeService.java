package com.suryoday.loantrackingphase2.service;

import java.util.List;

import com.suryoday.loantrackingphase2.model.DocumentType;

public interface DocumentTypeService {
    DocumentType save(DocumentType documentType);
    List<DocumentType> findAll();
    DocumentType findById(Long id);
    void deleteById(Long id);
}
