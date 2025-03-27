package com.extraidados.challenge.controller;

import com.extraidados.challenge.entity.Documents;
import com.extraidados.challenge.response.DocumentResponse;
import com.extraidados.challenge.service.AuthTokenService;
import com.extraidados.challenge.service.DocumentsService;
import com.extraidados.challenge.service.FileSaveService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;


@RestController
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
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

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    //@RequestParam("classification") String classification,
    public DocumentResponse createDocument(@RequestParam ("classification") String classification,@RequestParam("file")
     MultipartFile file, @RequestHeader("Authorization") String token){
    //public DocumentResponse createDocument(@RequestBody CreateDocumentModel documentModel,@RequestHeader("Authorization") String token) {
        authTokenService.isTokenValid(token);
        //refatorar createmodel passando novos parametros
        return documentsService.createDocument(classification,file,token);
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
