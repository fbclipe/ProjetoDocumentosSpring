package com.extraidados.challenge.model;

import org.springframework.web.multipart.MultipartFile;

public class CreateDocumentModel {
    private String classification;
    private MultipartFile file;
    
    public String getClassification() {
        return classification;
    }
    public void setClassification(String classification) {
        this.classification = classification;
    }
    public MultipartFile getFile() {
        return file;
    }
    public void setFile(MultipartFile file) {
        this.file = file;
    }
    
}
