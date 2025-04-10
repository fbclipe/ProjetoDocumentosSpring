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
public class DocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @BeforeEach
    void setUp() {
        documentRepository.deleteAll();

        Documents document1 = new Documents();
        document1.setPath("/path/to/document");
        document1.setDate(LocalDate.now());
        document1.setClassification("documento");
        document1.setContent("content");
        document1.setExtraction("Extracted");
        document1.setFileName("document.txt");
        document1.setExtension("txt");
        documentRepository.save(document1);
        
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
    @DisplayName("Deve listar todos os documentos")
    void mustListAllDocumentsFromDatabase () throws Exception {
        String userJson = "{\"username\": \"testedoc\",\"password\": \"testedoc\"}";

        MvcResult result = mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(userJson))
        .andExpect(status().isOk())
        .andReturn();

        String response = result.getResponse().getContentAsString();
        
        ObjectMapper objectMapper = new ObjectMapper();
        String token = objectMapper.readTree(response).get("token").asText();

        mockMvc.perform(get("/documents/findall")
        .header("Authorization", token)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.documents.length()").value(1));
    }

    @Test
    @DisplayName("Deve encontrar o documento pelo id passado")
    void mustFindDocumentByIdFromDatabase () throws Exception {
        String userJson = "{\"username\": \"testedoc\",\"password\": \"testedoc\"}";

        MvcResult result = mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(userJson))
        .andExpect(status().isOk())
        .andReturn();

        String response = result.getResponse().getContentAsString();
        
        ObjectMapper objectMapper = new ObjectMapper();
        String token = objectMapper.readTree(response).get("token").asText();


        mockMvc.perform(get("/documents/id/1")
        .header("Authorization", token)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.fileName").value("document.txt"));  
    }

    @Test
    @DisplayName("Deve criar um documento com sucesso")
    void mustCreateDocumentInDatabase () throws Exception {
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

    String classification = "documento";

    mockMvc.perform(multipart("/documents/create")
            .file(mockFile)
            .param("classification", classification)
            .header("Authorization", token)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.fileName").value("document.txt"));
    }

    @Test
    @DisplayName("Deve deletar um documento pelo id informado")
    void mustDeleteDocumentById () throws Exception {
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
        .andExpect(status().isOk())
        .andReturn();

        String responseBody = finalresponse.getResponse().getContentAsString();
        System.out.println("Resposta recebida: " + responseBody);
    }

    @Test
    @DisplayName("Deve transformar o arquivo em String Base64")
    void mustTransformFromFileToBase64 () throws Exception {
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
        .andExpect(status().isOk())
        .andReturn();

        String responseBody = finalresponse.getResponse().getContentAsString();
        System.out.println("Resposta recebida: " + responseBody);
    }

    @Test
    @DisplayName("Deve encontrar os documentos cadastrados na data especificada e retorna-lo")
    void mustFindDocumentByDate () throws Exception {
        String userJson = "{\"username\": \"testedoc\",\"password\": \"testedoc\"}";

        MvcResult result = mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(userJson))
        .andExpect(status().isOk())
        .andReturn();

        String response = result.getResponse().getContentAsString();
        
        ObjectMapper objectMapper = new ObjectMapper();
        String token = objectMapper.readTree(response).get("token").asText();

        String today = LocalDate.now().toString();
       MvcResult finalresponse =  mockMvc.perform(get("/documents/findbydate")
        .param("date" , today)
        .header("Authorization", token)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

        String responseBody = finalresponse.getResponse().getContentAsString();
        System.out.println("Resposta recebida: " + responseBody);
    }

    @Test
    @DisplayName("Deve receber duas datas e procurar documentos cadastrados durante essas datas")
    void mustFindDocumentBetweenTwoDates () throws Exception {
        String userJson = "{\"username\": \"testedoc\",\"password\": \"testedoc\"}";

        MvcResult result = mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(userJson))
        .andExpect(status().isOk())
        .andReturn();

        String response = result.getResponse().getContentAsString();
        
        ObjectMapper objectMapper = new ObjectMapper();
        String token = objectMapper.readTree(response).get("token").asText();

        String today = LocalDate.now().toString();
        String yesterday = LocalDate.of(2025, 04, 9).toString();

        Documents doc = new Documents();
        doc.setClassification("foto");
        doc.setFileName("fototeste");
        doc.setPath("/path/to/document");
        doc.setDate(LocalDate.of(2025, 04, 9));
        doc.setContent("conteudo do arquivo");
        doc.setExtension("extension");
        doc.setExtraction(("Extraido com sucesso"));
        documentRepository.save(doc);

        MvcResult finalresponse = mockMvc.perform(get("/documents/findbydatebetween")
        .param("begin", yesterday)
        .param("end", today)
        .header("Authorization", token)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

        String responseBody = finalresponse.getResponse().getContentAsString();
        System.out.println("Resposta recebida: " + responseBody);
    }
}