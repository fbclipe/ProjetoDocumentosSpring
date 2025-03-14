package com.extraidados.challenge.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.extraidados.challenge.entity.Documents;
import com.extraidados.challenge.exception.MessageException;
import com.extraidados.challenge.model.CreateDocumentModel;
import com.extraidados.challenge.repository.DocumentRepository;
import com.extraidados.challenge.response.DocumentResponse;

@Service
public class DocumentsService {

    @Autowired
    private DocumentRepository documentRepository;

    public List<Documents> listAll() {
        return documentRepository.findAll();
    }

    public Documents findById(Long id) {
        Optional<Documents> document = documentRepository.findById(id);
        if(!document.isPresent()){
            throw new MessageException("document not found");
        }
        return document.get();
        
    }

    public DocumentResponse createDocument(CreateDocumentModel documentmodel) {
        Documents documents = new Documents();
         String path = "C:\\Users\\carva\\OneDrive\\Área de Trabalho\\DESAFIO EXTRAIDADOS\\challenge";
         documents.setPath(path);
         LocalDate date = LocalDate.now();
         documents.setDate(date);
         String classification = documentmodel.getClassification();
         documents.setClassification(classification);
         String content = "Conteudo extraido do documento";
         documents.setContent(content);
         String extraction = "Extração realizada";
         documents.setExtraction(extraction);
         Documents savedDocument = documentRepository.save(documents);
         return new DocumentResponse(savedDocument);
    }

    public DocumentResponse updateDocument(Long id, Documents document) {
        Optional<Documents> optionalDocument = documentRepository.findById(id);

        if (optionalDocument.isEmpty()) {
           // return ResponseEntity.notFound().build();
           throw new MessageException("Document not found");
        }

        Documents existingDocument = optionalDocument.get();
        

        Documents updatedDocument = documentRepository.save(existingDocument);
        //return ResponseEntity.ok(updatedDocument);
                return new DocumentResponse(updatedDocument);
    }

    public String deleteDocument(Long id) {
        Optional<Documents> optionalDocument = documentRepository.findById(id);

        if (optionalDocument.isEmpty()) {
            //return ResponseEntity.notFound().build();
            throw new MessageException("Document not found.");
        }

        documentRepository.delete(optionalDocument.get());
        //return ResponseEntity.noContent().build();
            return "Document excluded with sucess";
    }
}
