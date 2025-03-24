package com.extraidados.challenge.controller;

import com.extraidados.challenge.entity.Documents;
import com.extraidados.challenge.model.CreateDocumentModel;
import com.extraidados.challenge.response.DocumentResponse;
import com.extraidados.challenge.service.AuthTokenService;
import com.extraidados.challenge.service.DocumentsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentsService documentsService;
    private final AuthTokenService authTokenService;
    @Autowired
    public DocumentController(DocumentsService documentsService, AuthTokenService authTokenService) {
        this.documentsService = documentsService;
        this.authTokenService = authTokenService;
    }

    @GetMapping
    public List<Documents> listAllDocuments(@RequestHeader("Authorization") String token) {
        return documentsService.listAll(token);
    }
    
    @PutMapping("/{id}/content")
    public Documents updateDocumentContent(@PathVariable Long id, @RequestBody String content,@RequestHeader("Authorization") String token) {
        return documentsService.updateContent(id, content,token);
    }


    @GetMapping("/id/{document_id}")
    public Documents getDocumentById(@PathVariable ("document_id") Long document_id,@RequestHeader("Authorization") String token) {
            return documentsService.findById(document_id,token); 
    }

    @PostMapping("/create")
    public DocumentResponse createDocument(@RequestBody CreateDocumentModel documentModel,@RequestHeader("Authorization") String token) {
        authTokenService.isTokenValid(token);
        return documentsService.createDocument(documentModel,token);
    }

    @DeleteMapping("/id/{document_id}")
    public String deleteDocument(@PathVariable ("document_id") Long document_id,@RequestHeader("Authorization") String token) {
        try {
            return documentsService.deleteDocument(document_id,token);
        } catch (Exception e) {
           // return new ApiException(e.getMessage());
           return e.getMessage();
        }
        //return documentsService.deleteDocument(document_id); 
    }
}
