package com.suryoday.loantrackingphase2.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.loantrackingphase2.model.DocumentType;
import com.suryoday.loantrackingphase2.repository.DocumentTypeRepository;
import com.suryoday.loantrackingphase2.service.DocumentTypeService;

@Service
public class DocumentTypeServiceImpl implements DocumentTypeService {

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Override
    public DocumentType save(DocumentType documentType) {
        return documentTypeRepository.save(documentType);
    }

    @Override
    public List<DocumentType> findAll() {
        return documentTypeRepository.findAll();
    }

    @Override
    public DocumentType findById(Long id) {
        return documentTypeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("DocumentType not found"));
    }

    @Override
    public void deleteById(Long id) {
        documentTypeRepository.deleteById(id);
    }
}
