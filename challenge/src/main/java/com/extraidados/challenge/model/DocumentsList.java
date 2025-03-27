package com.extraidados.challenge.model;

import java.util.ArrayList;
import java.util.List;
import com.extraidados.challenge.entity.Documents;

public class DocumentsList {
    private List<Documents> documents;

    public DocumentsList(List<Documents> documents) {
        this.documents = new ArrayList();

        for(int i = 0; i < documents.size(); i++) {
            Documents newdDocuments = new Documents();
            newdDocuments.getId();
            newdDocuments.getFileName();
            newdDocuments.getExtraction();
            newdDocuments.getExtension();
            newdDocuments.getContent();
            newdDocuments.getClassification();
            
            this.documents.add(newdDocuments);
        }
    }
}
