package com.test.myproject.infrastructure.interfaces;

import java.io.File;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface IFileStorage {
    void init();

    void save(MultipartFile multipartFile);

    File convertToFile(MultipartFile multipartFile, String fileName) throws IOException;

    String getExtension(String fileName);

    String uploadFile(File file, String fileName) throws IOException;
}
