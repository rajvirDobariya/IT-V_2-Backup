package com.suryoday.loantrackingphase2.service;

import java.util.List;

import com.suryoday.loantrackingphase2.model.Document;

public interface DocumentService {
    Document save(Document document);
    List<Document> findAll();
    Document findById(Long id);
    void deleteById(Long id);
}
