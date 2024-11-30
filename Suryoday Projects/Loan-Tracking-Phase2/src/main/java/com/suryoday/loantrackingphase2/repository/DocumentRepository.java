package com.suryoday.loantrackingphase2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.suryoday.loantrackingphase2.model.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
}
