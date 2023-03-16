package com.cyber.kitchen.repository;

import com.cyber.kitchen.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    Document findDocumentById(Long id);
}
