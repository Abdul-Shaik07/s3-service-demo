package com.java.s3_service_demo.controller;

import com.java.s3_service_demo.dto.ServiceRequest;
import com.java.s3_service_demo.response.ServiceResponse;
import com.java.s3_service_demo.service.IServiceService;
import com.java.s3_service_demo.service.S3Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/S3")
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;
    private final IServiceService iServiceService;

    @PostMapping("/create-service")
    public ResponseEntity<ServiceResponse> createService(@ModelAttribute @Valid ServiceRequest serviceRequest, @RequestParam("fileName") MultipartFile multipartFile) throws IOException {
        ServiceResponse serviceResponse = iServiceService.createService(serviceRequest, multipartFile);
        return ResponseEntity.status(201).body(serviceResponse);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<ServiceResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(iServiceService.findById(id));
    }

    @GetMapping("/filePath/{fileName}")
    public ResponseEntity<ServiceResponse> findByFileName(@PathVariable String fileName) {
        return ResponseEntity.status(200).body(iServiceService.findByFileName(fileName));
    }

    @PostMapping("/upload-file")
    public ResponseEntity<String> uploadFile(@RequestParam("fileName")MultipartFile multipartFile) throws IOException {
        return ResponseEntity.ok(s3Service.upload(multipartFile));
    }

    @GetMapping("/download-file/{fileName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName) {
        return ResponseEntity.ok(s3Service.download(fileName));
    }

    @DeleteMapping("/delete-file/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
        return ResponseEntity.ok(s3Service.deleteFile(fileName));
    }

    @GetMapping("/message")
    public String message() {
        return "Hello, S3 bucket...";
    }

}
