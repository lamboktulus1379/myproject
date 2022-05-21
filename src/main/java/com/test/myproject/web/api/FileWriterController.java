package com.test.myproject.web.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.test.myproject.infrastructure.interfaces.IFileStorage;
import com.test.myproject.infrastructure.services.FileStorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class FileWriterController {

    @Autowired
    IFileStorage fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity<Object> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        String message = "";
        List<String> fileNames = new ArrayList<>();
        Arrays.asList(files).stream().forEach(multipartFile -> {
            String fileName = multipartFile.getOriginalFilename(); // to get original file name
            fileNames.add(fileName);
            fileName = UUID.randomUUID().toString().concat(fileStorageService.getExtension(fileName));

            File file;
            try {
                file = fileStorageService.convertToFile(multipartFile, fileName);

                try {
                    String url = fileStorageService.uploadFile(file, fileName);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                file.delete();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        });
        message = "Uploaded the files successfully: " + fileNames;
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

}
