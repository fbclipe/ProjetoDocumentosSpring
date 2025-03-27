package com.extraidados.challenge.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Base64;

import org.springframework.web.multipart.MultipartFile;

import com.extraidados.challenge.entity.Documents;
import com.extraidados.challenge.model.Base64Dto;

public class FileTreatmentService {
  
    final String filePath = "C:\\Users\\carva\\OneDrive\\Área de Trabalho\\DESAFIO EXTRAIDADOS\\challenge\\challenge\\arquivos\\";

    public void saveDocuments(Documents documents, MultipartFile file) {
        try {
            File folders = new File(filePath);
            if (!folders.exists()) {
                folders.mkdirs();
            }
            Path destino = Path.of(filePath, file.getOriginalFilename());

            //objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath + file.getOriginalFilename()), documents);
            Files.write(destino, file.getBytes(), StandardOpenOption.CREATE);
        } catch(IOException e){
            e.printStackTrace();
        }    
    }

    public Base64Dto encodetoBase64(MultipartFile file, String classification) throws IOException {
        Base64Dto base64Dto = new Base64Dto();
        base64Dto.setOriginalName(file.getOriginalFilename());
        base64Dto.setType(classification);
        File inImage = new File(filePath);
        byte[] encodedImageBytes = Base64.getEncoder().encode(Files.readAllBytes(inImage.toPath()));
        String encodedImageDataAsString = new String(encodedImageBytes); 
        base64Dto.setBase64(encodedImageDataAsString);
        return base64Dto;
    }
}
