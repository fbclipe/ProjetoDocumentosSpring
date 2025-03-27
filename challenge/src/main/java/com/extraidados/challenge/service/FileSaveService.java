package com.extraidados.challenge.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.extraidados.challenge.entity.Documents;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FileSaveService {
    //final ObjectMapper objectMapper = new ObjectMapper();
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
}
