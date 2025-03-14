package com.extraidados.challenge.controller;

import com.extraidados.challenge.entity.Documents;
import com.extraidados.challenge.model.CreateDocumentModel;
import com.extraidados.challenge.response.DocumentResponse;
import com.extraidados.challenge.service.DocumentsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentsService documentsService;

    public DocumentController(DocumentsService documentsService) {
        this.documentsService = documentsService;
    }

   

    @GetMapping
    public List<Documents> listAllDocuments() {
        return documentsService.listAll();
    }

    @GetMapping("/id/{document_id}")
    public Documents getDocumentById(@PathVariable ("document_id") Long document_id) {
            return documentsService.findById(document_id); 
    }

    @PostMapping("/create")
    public DocumentResponse createDocument(@RequestBody CreateDocumentModel documentModel) {
        return documentsService.createDocument(documentModel);
    }

    @DeleteMapping("/id/{document_id}")
    public String deleteDocument(@PathVariable ("document_id") Long document_id) {
        try {
            return documentsService.deleteDocument(document_id);
        } catch (Exception e) {
           // return new ApiException(e.getMessage());
           return e.getMessage();
        }
        //return documentsService.deleteDocument(document_id); 
    }
}
