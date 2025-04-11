package com.extraidados.challenge.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import javax.print.attribute.standard.Media;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.extraidados.challenge.entity.Documents;
import com.extraidados.challenge.entity.User;
import com.extraidados.challenge.repository.DocumentRepository;
import com.extraidados.challenge.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) //troca o banco de dados real pelo banco de memoria H2
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) //Destroi e reconstroi o contexto da aplicação a cada teste
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class DocumentControllerExceptionsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @BeforeEach
    void setUp() {
        documentRepository.deleteAll();
        
        User user = new User();
        user.setUsername("testedoc");
        user.setPassword("testedoc");
        user.setRole("admin");
        userRepository.save(user);
    }

    @AfterEach
    void setUpAfter() {
        userRepository.deleteAll();
        documentRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve listar nenhum documento")
    void mustNotListAllDocumentsFromDatabase () throws Exception {
        String userJson = "{\"username\": \"testedoc\",\"password\": \"testedoc\"}";

        MvcResult result = mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(userJson))
        .andExpect(status().isOk())
        .andReturn();

        String response = result.getResponse().getContentAsString();
        
        ObjectMapper objectMapper = new ObjectMapper();
        String token = objectMapper.readTree(response).get("token").asText();
        documentRepository.deleteAll();

        MvcResult finalresponse = mockMvc.perform(get("/documents/findall")
        .header("Authorization", token)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andReturn();

        String responseBody = finalresponse.getResponse().getContentAsString();
        System.out.println("Resposta recebida: " + responseBody);
    }

    @Test
    @DisplayName("Não deve encontrar o documento pelo id passado")
    void mustNotFindDocumentByIdFromDatabase () throws Exception {
        String userJson = "{\"username\": \"testedoc\",\"password\": \"testedoc\"}";

        MvcResult result = mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(userJson))
        .andExpect(status().isOk())
        .andReturn();

        String response = result.getResponse().getContentAsString();
        
        ObjectMapper objectMapper = new ObjectMapper();
        String token = objectMapper.readTree(response).get("token").asText();

        documentRepository.deleteById((long) 1);

        MvcResult finalresponse = mockMvc.perform(get("/documents/id/1")
        .header("Authorization", token)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andReturn(); 

        String responseBody = finalresponse.getResponse().getContentAsString();
        System.out.println("Resposta recebida: " + responseBody);
    }

    @Test //ta dando badrequest mas nao retorna mensagem de erro
    @DisplayName("Deve criar um documento com sucesso")
    void mustNotCreateDocumentInDatabase () throws Exception {
    String userJson = "{\"username\": \"testedoc\",\"password\": \"testedoc\"}";
    MvcResult result = mockMvc.perform(post("/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(userJson))
        .andExpect(status().isOk())
        .andReturn();

    String response = result.getResponse().getContentAsString();
    ObjectMapper objectMapper = new ObjectMapper();
    String token = objectMapper.readTree(response).get("token").asText();

    MockMultipartFile mockFile = new MockMultipartFile(
        "file",
        "document.txt",
        MediaType.TEXT_PLAIN_VALUE,
        "conteúdo do arquivo".getBytes());

    MvcResult finalResponse = mockMvc.perform(multipart("/documents/create")
            .file(mockFile)
            .header("Authorization", token)
            .param("classification", "")
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isBadRequest())
        .andReturn();

    String responseBody = finalResponse.getResponse().getContentAsString();
    System.out.println("Resposta recebida: " + responseBody);

          
    }

    @Test
    @DisplayName("Deve deletar um documento pelo id informado")
    void mustNotDeleteDocumentById () throws Exception {
        String userJson = "{\"username\": \"testedoc\",\"password\": \"testedoc\"}";

        MvcResult result = mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(userJson))
        .andExpect(status().isOk())
        .andReturn();

        String response = result.getResponse().getContentAsString();
        
        ObjectMapper objectMapper = new ObjectMapper();
        String token = objectMapper.readTree(response).get("token").asText();

        MvcResult finalresponse = mockMvc.perform(delete("/documents/id/1")
        .header("Authorization", token))
        .andExpect(status().isBadRequest())
        .andReturn();

        String responseBody = finalresponse.getResponse().getContentAsString();
        System.out.println("Resposta recebida: " + responseBody);
    }

    @Test
    @DisplayName("Deve transformar o arquivo em String Base64")
    void mustNotTransformFromFileToBase64 () throws Exception {
        String userJson = "{\"username\": \"testedoc\",\"password\": \"testedoc\"}";

        MvcResult result = mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(userJson))
        .andExpect(status().isOk())
        .andReturn();

        String response = result.getResponse().getContentAsString();
        
        ObjectMapper objectMapper = new ObjectMapper();
        String token = objectMapper.readTree(response).get("token").asText();

        MvcResult finalresponse = mockMvc.perform(post("/documents/base64/1")
        .header("Authorization", token)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andReturn();

        String responseBody = finalresponse.getResponse().getContentAsString();
        System.out.println("Resposta recebida: " + responseBody);
    }

    @Test
    @DisplayName("Deve encontrar os documentos cadastrados na data especificada e retorna-lo")
    void mustNotFindDocumentByDate () throws Exception {
        String userJson = "{\"username\": \"testedoc\",\"password\": \"testedoc\"}";

        MvcResult result = mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(userJson))
        .andExpect(status().isOk())
        .andReturn();

        String response = result.getResponse().getContentAsString();
        
        ObjectMapper objectMapper = new ObjectMapper();
        String token = objectMapper.readTree(response).get("token").asText();

        String today = LocalDate.now().plusDays(1).toString();
       MvcResult finalresponse =  mockMvc.perform(get("/documents/findbydate")
        .param("date" , today)
        .header("Authorization", token)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andReturn();

        String responseBody = finalresponse.getResponse().getContentAsString();
        System.out.println("Resposta recebida: " + responseBody);
    }

    @Test
    @DisplayName("Deve receber duas datas e procurar documentos cadastrados durante essas datas")
    void mustNotFindDocumentBetweenTwoDates () throws Exception {
        String userJson = "{\"username\": \"testedoc\",\"password\": \"testedoc\"}";

        MvcResult result = mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(userJson))
        .andExpect(status().isOk())
        .andReturn();

        String response = result.getResponse().getContentAsString();
        
        ObjectMapper objectMapper = new ObjectMapper();
        String token = objectMapper.readTree(response).get("token").asText();

        String today = LocalDate.now().plusDays(1).toString();
        String yesterday = LocalDate.now().minusDays(1).toString();

        Documents doc = new Documents();
        doc.setClassification("foto");
        doc.setFileName("fototeste");
        doc.setPath("/path/to/document");
        doc.setDate(LocalDate.now().minusDays(1));
        doc.setContent("conteudo do arquivo");
        doc.setExtension("extension");
        doc.setExtraction(("Extraido com sucesso"));
        documentRepository.save(doc);

        MvcResult finalresponse = mockMvc.perform(get("/documents/findbydatebetween")
        .param("begin", yesterday)
        .param("end", today)
        .header("Authorization", token)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andReturn();

        String responseBody = finalresponse.getResponse().getContentAsString();
        System.out.println("Resposta recebida: " + responseBody);
    }
}