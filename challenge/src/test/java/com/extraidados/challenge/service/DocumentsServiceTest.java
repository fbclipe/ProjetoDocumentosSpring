package com.extraidados.challenge.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import com.extraidados.challenge.entity.Documents;
import com.extraidados.challenge.exception.MessageException;
import com.extraidados.challenge.model.Base64Dto;
import com.extraidados.challenge.repository.DocumentRepository;

@ExtendWith(MockitoExtension.class)
class DocumentsServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private AuthTokenService authTokenService;

    @Mock
    private FileTreatmentService fileTreatmentService;

    @Mock
    private MultipartFile file;

    @InjectMocks
    private DocumentsService documentsService;

    private Documents document;

    @BeforeEach
    @DisplayName("Dados para realizar os testes")
    void setUp() {
        document = new Documents();
        document.setId(1L);
        document.setPath("/path/to/document");
        document.setDate(LocalDate.now());
        document.setClassification("Confidential");
        document.setContent("content");
        document.setExtraction("Extracted");
        document.setFileName("document.pdf");
        document.setExtension("pdf");
    }

    @Test
    @DisplayName("Deve Achar o ID pela busca com sucesso")
    void MustFindByIdSuccessfully() {
        //AMBIENTE
        when(authTokenService.isTokenValid("valid-token")).thenReturn(true);
        when(documentRepository.findById(1L)).thenReturn(Optional.of(document));
        //EXECUÇÃO
        Documents foundDocument = documentsService.findById(1L, "valid-token");
        //VERIFICAÇÃO
        assertNotNull(foundDocument);
        assertEquals(1L, foundDocument.getId());
        verify(documentRepository, times(1)).findById(1L);
    }
    @Test
    @DisplayName("Deve lançar exceção quando documento não for encontrado")
    void MustThrowExceptionWhenDocumentNotFound() {
        //Ambiente
        when(authTokenService.isTokenValid("valid-token")).thenReturn(true);
        when(documentRepository.findById(1L)).thenReturn(Optional.empty());
        
        try {
        //Execução
            documentsService.findById(1L, "valid-token");
        } catch (MessageException e) {
        //Verificação
            System.out.println("ERRO CAPTURADO: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Deve conseguir criar um documento com sucesso")
    void MustCreateDocumentSuccessfully() {
        //Ambiente
        when(authTokenService.isTokenValid("valid-token")).thenReturn(true);
        when(file.getOriginalFilename()).thenReturn("document.pdf");
        when(documentRepository.save(any(Documents.class))).thenReturn(document);
        //Execução
        Documents response = documentsService.createDocument("Confidential", file, "valid-token");
        //Verificação  
        assertNotNull(response);
        assertEquals("document.pdf", response.getFileName());
        verify(documentRepository, times(1)).save(any(Documents.class));
        verify(fileTreatmentService, times(1)).saveDocuments(any(Documents.class), eq(file));
    }

    @Test
    @DisplayName("Deve conseguir atualizar um documento com sucesso")
    void MustUpdateDocumentContentSuccessfully() {
        //Ambiente
        when(authTokenService.isTokenValid("valid-token")).thenReturn(true);
        when(documentRepository.findById(1L)).thenReturn(Optional.of(document));
        when(documentRepository.save(any(Documents.class))).thenReturn(document);
        //Execução
        Documents updatedDocument = documentsService.updateContent(1L, "Updated content", "valid-token");
        //Verificação
        assertNotNull(updatedDocument);
        assertEquals("Updated content", updatedDocument.getContent());
        verify(documentRepository, times(1)).save(document);
    }

    @Test
    @DisplayName("Deve deletar um documento com sucesso")
    void MustDeleteDocumentSuccessfully() {
        //Ambiente
        when(authTokenService.isTokenValid("valid-token")).thenReturn(true);
        when(documentRepository.findById(1L)).thenReturn(Optional.of(document));
        doNothing().when(documentRepository).delete(document);
        //Execução
        String response = documentsService.deleteDocument(1L, "valid-token");
        //Verificação
        assertEquals("Document excluded with sucess", response);
        verify(documentRepository, times(1)).delete(document);
    }
    @Test //pode dividir o teste em 2
    @DisplayName("Deve lançar exceção quando deletar documento que não existe")
    void MustThrowExceptionWhenDeletingNonExistentDocument() {
        //Ambiente
        when(authTokenService.isTokenValid("valid-token")).thenReturn(true);
        when(documentRepository.findById(1L)).thenReturn(Optional.empty());
       //Execução
        MessageException lalala = assertThrows(MessageException.class,()->  documentsService.deleteDocument(1L, "valid-token") );
       //Verificação
       assertEquals(lalala.getMessage(), "Document not found.");
       verify(documentRepository, times(0)).delete(document); 
    }
    

    @Test
    @DisplayName("Deve transformar o arquivo em base64 com sucesso")
    void MustTransformToBase64Successfully() {
        //Ambiente
        when(authTokenService.isTokenValid("valid-token")).thenReturn(true);
        when(documentRepository.findById(1L)).thenReturn(Optional.of(document));
        //Execução
        Base64Dto mockBase64Dto = new Base64Dto();
        mockBase64Dto.setBase64("base64data");
        //Ambiente
        when(fileTreatmentService.encodetoBase64(anyString(), anyString())).thenReturn(mockBase64Dto);
        //Execução
        Base64Dto base64Dto = documentsService.transformToBase64(1L, "valid-token");
        //Verificação
        assertNotNull(base64Dto);
        assertEquals("base64data", base64Dto.getBase64());
    }
    
}
