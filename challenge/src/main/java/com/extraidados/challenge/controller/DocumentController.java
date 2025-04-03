package com.extraidados.challenge.controller;

import com.extraidados.challenge.response.ApiListDocumentos;
import com.extraidados.challenge.entity.Documents;
import com.extraidados.challenge.exception.ApiException;
import com.extraidados.challenge.model.Base64Dto;
import com.extraidados.challenge.repository.DocumentRepository;
import com.extraidados.challenge.response.DocumentResponse;
import com.extraidados.challenge.service.DocumentsService;
import com.extraidados.challenge.service.FileTreatmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
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
    public ResponseEntity<?> listAllDocuments(@RequestHeader("Authorization") String token) {
        try {
        List<Documents> documents = documentsService.listAll(token);
        ApiListDocumentos response = new ApiListDocumentos(documents);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiException apiException = new ApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
        }
    }

    
    @PutMapping("/{id}/content")
    public ResponseEntity<?> updateDocumentContent(@PathVariable Long id, @RequestBody String content, @RequestHeader("Authorization") String token) {
        try {
            Documents document = documentsService.updateContent(id, content, token);
            return ResponseEntity.ok(document);
        } catch (Exception e) {
            ApiException apiException = new ApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/id/{document_id}")
    public ResponseEntity<?> getDocumentById(@PathVariable("document_id") Long document_id, @RequestHeader("Authorization") String token) {
        try {
            Documents document = documentsService.findById(document_id, token);
            return ResponseEntity.ok(document);
        } catch (Exception e) {
            ApiException apiException = new ApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createDocument(@RequestParam("classification") String classification, @RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String token) {
        try {
            DocumentResponse response = documentsService.createDocument(classification, file, token);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ApiException apiException = new ApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/id/{document_id}")
    public ResponseEntity<?> deleteDocument(@PathVariable("document_id") Long document_id, @RequestHeader("Authorization") String token) {
        try {
            String result = documentsService.deleteDocument(document_id, token);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            ApiException apiException = new ApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/base64/{document_id}")
    public ResponseEntity<?> transformToBase64(@PathVariable("document_id") Long documentId, @RequestHeader("Authorization") String token) {
        try {
            Base64Dto base64Dto = documentsService.transformToBase64(documentId, token);
            return ResponseEntity.ok(base64Dto);
        } catch (Exception e) {
            ApiException apiException = new ApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/base64-to-file")
    public ResponseEntity<?> convertBase64ToFile(@RequestParam String base64Data, @RequestParam String outputPath) {
        try {
            fileTreatmentService.decodeBase64ToFile(base64Data, outputPath);
            return ResponseEntity.ok(outputPath);
        } catch (Exception e) {
            ApiException apiException = new ApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findbydate")
    public ResponseEntity<?> findByDate(@RequestParam LocalDate date, @RequestHeader ("Authorization") String token) {
        try {
            List<Documents> documents = documentsService.findByDate(date,token);
            ApiListDocumentos response = new ApiListDocumentos(documents);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
           ApiException apiException = new ApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
        }
        //return documentsService.findByDate(date, token);
    }

    @GetMapping("/findbydatebetween")
    public ResponseEntity<?> findByDateBetween(@RequestParam LocalDate begin, @RequestParam LocalDate end, @RequestHeader ("Authorization") String token) {
        try {
            List<Documents> documents = documentsService.findByDateBetween(begin, end, token);
            ApiListDocumentos response = new ApiListDocumentos(documents);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiException apiException = new ApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
        }
        //return documentsService.findByDateBetween(begin, end, token);
    }
}
//todos tem q ter try catch com response entity