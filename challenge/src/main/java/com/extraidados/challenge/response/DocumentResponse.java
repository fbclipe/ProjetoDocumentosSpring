package com.extraidados.challenge.response;

import com.extraidados.challenge.entity.Documents;

public class DocumentResponse {
    private Documents savedDocument;

    public DocumentResponse(Documents savedDocument) {
        this.savedDocument = savedDocument;
    }

    public Documents getSavedDocument() {
        return savedDocument;
    }

    public void setSavedDocument(Documents savedDocument) {
        this.savedDocument = savedDocument;
    }

    public Object getClassification() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getClassification'");
    }

    public Long getId() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getId'");
    }

}
