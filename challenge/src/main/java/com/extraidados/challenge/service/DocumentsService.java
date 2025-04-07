package com.extraidados.challenge.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.extraidados.challenge.entity.Documents;
import com.extraidados.challenge.exception.MessageException;
import com.extraidados.challenge.model.Base64Dto;
import com.extraidados.challenge.repository.DocumentRepository;
import com.extraidados.challenge.response.DocumentResponse;

@Service
public class DocumentsService {
    
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private AuthTokenService authTokenService;
    @Autowired
    private FileTreatmentService fileTreatmentService;

    private void validateToken(String token) {
        if (!authTokenService.isTokenValid(token)) {
            throw new MessageException("token invalid or expired");
        }
    }

    public List<Documents> listAll(String token) {
        validateToken(token);
        return documentRepository.findAll();
    }

    public Documents findById(Long id, String token) {
        validateToken(token);
        Optional<Documents> document = documentRepository.findById(id);
        if(!document.isPresent()){
            throw new MessageException("document not found");
        }
        return document.get();
    }

    public Documents createDocument(String classification, MultipartFile file, String token) {
        validateToken(token);
    
        Documents documents = new Documents();
        //extrairnome do file
        String path = "C:\\Users\\carva\\OneDrive\\Área de Trabalho\\DESAFIO EXTRAIDADOS\\challenge\\" + file.getOriginalFilename();
        documents.setPath(path);
        
        LocalDate date = LocalDate.now();
        documents.setDate(date);
        
        documents.setClassification(classification);
        
        String content = "Conteudo extraido do documento";
        documents.setContent(content);
        
        String extraction = "Extração realizada";
        documents.setExtraction(extraction);
        
        String fileName = file.getOriginalFilename();
        documents.setFileName(fileName);
        
        String extension = "";
        if (fileName != null && fileName.contains(".")) {
            extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        }
        documents.setExtension(extension);
    
        Documents savedDocument = documentRepository.save(documents);
        System.out.println(documents.toString());
        fileTreatmentService.saveDocuments(savedDocument,file);
        return savedDocument;
    }

    public Documents save(Documents documents) {
        return documentRepository.save(documents);
    }
    

    public Documents updateContent(Long id, String newContent, String token) {
        validateToken(token);
        try {
            Optional<Documents> optionalDocument = documentRepository.findById(id);
            if (!optionalDocument.isPresent()) {
                throw new RuntimeException("Erro: Documento não encontrado");
            }
            
            Documents document = optionalDocument.get();
            document.setContent(newContent);
            return documentRepository.save(document);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar o documento", e);
        }
    }


    public String deleteDocument(Long id, String token) {
        validateToken(token);
        Optional<Documents> optionalDocument = documentRepository.findById(id);

        if (optionalDocument.isEmpty()) {
            //return ResponseEntity.notFound().build();
            throw new MessageException("Document not found.");
        }

        documentRepository.delete(optionalDocument.get());
        //return ResponseEntity.noContent().build();
            return "Document excluded with sucess";
    }

    public Base64Dto transformToBase64(Long documentId, String token) {
        Documents variavel = findById(documentId , token);

        return fileTreatmentService.encodetoBase64(variavel.getFileName(), variavel.getClassification());
    }

    public List<Documents> findByDate (LocalDate date, String token) {
        validateToken(token);
        LocalDate error = LocalDate.now();
        if(date.isAfter(error)){
            throw new MessageException("invalid date");
        }
        return documentRepository.findByDate(date);
    }

    public List<Documents> findByDateBetween (LocalDate begin, LocalDate end, String token){
        validateToken(token);
        LocalDate error = LocalDate.now();
        if(end.isAfter(error)){
            throw new MessageException("invalid date");
        }
        return documentRepository.findByDateBetween(begin, end);
    }
    
    
    
}
