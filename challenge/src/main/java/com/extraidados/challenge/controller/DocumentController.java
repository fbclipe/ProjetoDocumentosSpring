package com.extraidados.challenge.controller;

import com.extraidados.challenge.entity.Documents;
import com.extraidados.challenge.model.Base64Dto;
import com.extraidados.challenge.repository.DocumentRepository;
import com.extraidados.challenge.response.DocumentResponse;
import com.extraidados.challenge.service.DocumentsService;
import com.extraidados.challenge.service.FileTreatmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private final DocumentsService documentsService;

    @Autowired
    private final FileTreatmentService fileTreatmentService;

    @Autowired
    public DocumentController(DocumentsService documentsService, FileTreatmentService fileTreatmentService) {
        this.documentsService = documentsService;
        this.fileTreatmentService = fileTreatmentService;
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
    public DocumentResponse createDocument(@RequestParam ("classification") String classification,@RequestParam("file")
     MultipartFile file, @RequestHeader("Authorization") String token){
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

    @PostMapping("/base64/{document_id}")
    public Base64Dto transformToBase64(@PathVariable("document_id") Long documentId, @RequestHeader("Authorization") String token) {

        return documentsService.transformToBase64(documentId, token);
    }

    @PostMapping("/base64-to-file")
    public String convertBase64ToFile(@RequestParam String base64Data, @RequestParam String outputPath) {
        fileTreatmentService.decodeBase64ToFile(base64Data, outputPath);
        return outputPath;
    }
}
