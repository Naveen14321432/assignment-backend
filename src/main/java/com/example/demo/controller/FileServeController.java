package com.example.demo.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class FileServeController {

   // private static final String UPLOAD_DIR = "D:/project files/uploads/";

    @RequestMapping("/uploads/{fileName:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String fileName) {
        try {
        	//System.out.println("Requested file: " + fileName);
            Path file = Paths.get("D:/project files/uploads").resolve(fileName).normalize();
            Resource resource = new UrlResource(file.toUri());
           // System.out.println("Requested file: " + fileName);
            if (resource.exists() && resource.isReadable()) {
                MediaType mediaType = fileName.endsWith(".pdf") ? MediaType.APPLICATION_PDF : MediaType.APPLICATION_OCTET_STREAM;
                return ResponseEntity.ok()
                        .contentType(mediaType)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                throw new RuntimeException("File not found or not readable: " + file);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not read the file: " + fileName, e);
        }
    }

}
