package com.extraidados.challenge.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.extraidados.challenge.entity.Documents;
import com.extraidados.challenge.repository.DocumentRepository;

public class DocumentsServiceTest {
    @Autowired
    private DocumentsService documentsService;
    Long documentsId = 1L;

    @Autowired
    private DocumentRepository documentRepository;

    private Documents documents;
    //listall
    //findbyid
    //createdocument
    //updatedocument
    //deletedocument
    @BeforeEach
    void setUp(){
        documentsService = new DocumentsService();
        Documents documents = new Documents();
        documents.setClassification("restrito");
        documents.setContent("conteudo de teste");
        documents.setId(documentsId);
        documents.setExtraction("extração realizada");
        documents.setPath("C:\\Users\\carva\\OneDrive\\Área de Trabalho\\DESAFIO EXTRAIDADOS\\challenge");
        documents.setDate(LocalDate.now());
        //documents = documentRepository.save(documents);

    }
    @Test
    void MustReturnDocumentId() {
        Documents doc = new Documents();
        doc.setId(1L);
        assertEquals(doc.getId(), documentsId);
        assertEquals("2L", documentsId);
    }

    @Test
    void MustReturnCreatedContent() {
        Documents doc = new Documents();
        doc.setId(1L);
        doc.setPath("test_path");
        doc.setDate(LocalDate.now());
        doc.setClassification("Confidencial");
        doc.setContent("Teste");
        doc.setExtraction("Realizada");

        assertNotNull(doc);
        assertNull(doc);
    }

    @Test 
    void MustReturnUpdatedContent() {
        Documents doc = new Documents();
        doc.setId(1L);
        doc.setPath("test_path");
        doc.setDate(LocalDate.now());
        doc.setClassification("Confidencial");
        doc.setContent("Teste");
        doc.setExtraction("Realizada");
        assertEquals(documents, doc);
    }

    @Test
    void MustReturnDeletedDocument() {
        Optional<Documents> optionalDocument = documentRepository.findById(documentsId);
        assertFalse(optionalDocument.isEmpty());
        
    }
}
