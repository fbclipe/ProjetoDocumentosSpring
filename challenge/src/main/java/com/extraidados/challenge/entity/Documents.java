package com.extraidados.challenge.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;



@Entity
@Table(name = "documents")

public class Documents {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String classification;
    @Column(nullable = false)
    private String path;
    @Column(nullable = false)
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @Column(nullable = false)
    private String extraction;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }



    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getExtraction() {
        return extraction;
    }

    public void setExtraction(String extraction) {
        this.extraction = extraction;
    }

    @Override
    public String toString() {
        return "Documents [id=" + id + ", classification=" + classification + ", path="
                + path + ", content=" + content + ", date=" + date + "]";
    }
}
