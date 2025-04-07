package com.extraidados.challenge.repository;

import com.extraidados.challenge.entity.Documents;
import com.extraidados.challenge.repository.DocumentRepository;
import com.extraidados.challenge.service.AuthTokenService;
import com.extraidados.challenge.service.DocumentsService;
import com.extraidados.challenge.service.FileTreatmentService;
import com.extraidados.challenge.exception.MessageException;
import com.extraidados.challenge.model.Base64Dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class DocumentsServiceIntegrationTest {

    @Autowired
    private DocumentsService documentsService;

    @Autowired
    private DocumentRepository documentRepository;

    @MockBean
    private AuthTokenService authTokenService;

    @MockBean
    private FileTreatmentService fileTreatmentService;

    private String token = "valid-token";

    @BeforeEach
    void setup() {
        when(authTokenService.isTokenValid(token)).thenReturn(true);
    }

    @Test
    @DisplayName("Deve criar um documento e achar ele pela ID")
    void mustCreateAndFindById() throws Exception {
        //Ambiente
        MockMultipartFile file = new MockMultipartFile("file", "test.pdf", "application/pdf", "mock content".getBytes());
        //Execução
        Documents document = documentsService.createDocument("Financeiro", file, token);
        //Verificação
        assertNotNull(document);
        assertEquals("Financeiro", document.getClassification());
        //Execução
        Documents doc = documentsService.findById(document.getId(), token);
        //Verificação
        assertNotNull(doc);
        assertEquals("test.pdf", doc.getFileName());
    }

    @Test
    @DisplayName("Deve listar todos os documentos")
    void mustListAll() throws Exception {
        //Ambiente
        MockMultipartFile file = new MockMultipartFile("file", "another.pdf", "application/pdf", "content".getBytes());
        //Execução
        documentsService.createDocument("RH", file, token);
        List<Documents> allDocs = documentsService.listAll(token);
        //Verificação
        assertFalse(allDocs.isEmpty());
    }

    @Test
    @DisplayName("Deve atualizar os documentos")
    void mustUpdateContent() throws Exception {
        //Ambiente
        MockMultipartFile file = new MockMultipartFile("file", "update.pdf", "application/pdf", "content".getBytes());
        //Execução
        Documents document = documentsService.createDocument("TI", file, token);
        Documents updated = documentsService.updateContent(document.getId(), "Novo conteúdo atualizado", token);
        //Verificação
        assertEquals("Novo conteúdo atualizado", updated.getContent());
    }

    @Test
    @DisplayName("Deve deletar um documento")
    void mustDeleteDocument() throws Exception {
        //Ambiente
        MockMultipartFile file = new MockMultipartFile("file", "delete.pdf", "application/pdf", "content".getBytes());
        //Execução
        Documents document = documentsService.createDocument("Jurídico", file, token);
        String result = documentsService.deleteDocument(document.getId(), token);
        //Verificação
        assertEquals("Document excluded with sucess", result);
        assertThrows(MessageException.class, () -> {
            documentsService.findById(document.getId(), token);
        });
    }

    @Test
    @DisplayName("Deve achar documento pela data")
    void mustFindByDate() throws Exception {
        //Ambiente
        LocalDate today = LocalDate.now();
        MockMultipartFile file = new MockMultipartFile("file", "bydate.pdf", "application/pdf", "content".getBytes());
        //Execução
        documentsService.createDocument("Contábil", file, token);
        List<Documents> docs = documentsService.findByDate(today, token);
        //Verificação
        assertFalse(docs.isEmpty());
    }

    @Test
    @DisplayName("Deve achar documentos pelo intervalo de data selecionado")
    void mustFindByDateBetween() throws Exception {
        //Ambiente
        LocalDate today = LocalDate.now();
        MockMultipartFile file = new MockMultipartFile("file", "range.pdf", "application/pdf", "content".getBytes());
        //Execução
        documentsService.createDocument("Financeiro", file, token);
        List<Documents> docs = documentsService.findByDateBetween(today, today, token);
        //Verificação
        assertFalse(docs.isEmpty());
    }

    @Test
    @DisplayName("Deve transformar para base64")
    void mustTransformToBase64() throws Exception {
        //Ambiente
        MockMultipartFile file = new MockMultipartFile("file", "base64.pdf", "application/pdf", "content".getBytes());
        //Execução
        Documents document = documentsService.createDocument("Legal", file, token);
        Base64Dto base64Dto = new Base64Dto();
        when(fileTreatmentService.encodetoBase64("base64.pdf", "Legal")).thenReturn(base64Dto);
        Base64Dto result = documentsService.transformToBase64(document.getId(), token);
        //Verificação
        assertNotNull(result);
    }
}
