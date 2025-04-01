package com.extraidados.challenge.response;

import com.extraidados.challenge.entity.Documents;
import java.util.List;

public class ApiListDocumentos {
    private List<Documents> documents;

    public ApiListDocumentos(List<Documents> documents) {
        this.documents = documents;
    }

    public List<Documents> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Documents> documents) {
        this.documents = documents;
    }
}
