package com.suryoday.loantrackingphase2.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.loantrackingphase2.model.Document;
import com.suryoday.loantrackingphase2.repository.DocumentRepository;
import com.suryoday.loantrackingphase2.service.DocumentService;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Override
    public Document save(Document document) {
        return documentRepository.save(document);
    }

    @Override
    public List<Document> findAll() {
        return documentRepository.findAll();
    }

    @Override
    public Document findById(Long id) {
        return documentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Document not found"));
    }

    @Override
    public void deleteById(Long id) {
        documentRepository.deleteById(id);
    }
}
