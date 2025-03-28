package com.extraidados.challenge.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.extraidados.challenge.entity.Documents;
import com.extraidados.challenge.model.Base64Dto;
import com.extraidados.challenge.repository.DocumentRepository;

@Service
public class FileTreatmentService {
  

    private final String fileBasePath = "C:\\Users\\carva\\OneDrive\\Área de Trabalho\\DESAFIO EXTRAIDADOS\\challenge\\challenge\\arquivos\\";

    public void saveDocuments(Documents documents, MultipartFile file) {
        try {
            File folder = new File(fileBasePath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            Path destination = Paths.get(fileBasePath, file.getOriginalFilename()).normalize();
            Files.write(destination, file.getBytes(), StandardOpenOption.CREATE);
            documents.setPath(destination.toAbsolutePath().toString());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar o arquivo: " + e.getMessage(), e);
        }
    }

    public Base64Dto encodetoBase64(String fileName, String classification) {
        String filePath = fileBasePath + fileName;
        File file = new File(filePath);
        try {
            byte[] encodedBytes = Base64.getEncoder().encode(Files.readAllBytes(file.toPath()));
            Base64Dto base64Dto = new Base64Dto();
            base64Dto.setOriginalName(fileName);
            base64Dto.setType(classification);
            base64Dto.setBase64(new String(encodedBytes));
            return base64Dto;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao codificar o arquivo para Base64: " + e.getMessage(), e);
        }
    }
    

    public void decodeBase64ToFile(String base64Data, String outputPath) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(base64Data);
            Files.write(Paths.get(outputPath), decodedBytes);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao decodificar o Base64 e salvar o arquivo: " + e.getMessage(), e);
        }
    }
}
