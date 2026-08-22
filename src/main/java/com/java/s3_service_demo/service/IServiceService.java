package com.java.s3_service_demo.service;

import com.java.s3_service_demo.dto.ServiceRequest;
import com.java.s3_service_demo.response.ServiceResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IServiceService {

    ServiceResponse createService(ServiceRequest serviceRequest, MultipartFile multipartFile)
            throws IOException;

    ServiceResponse findByFileName(String fileName);
    ServiceResponse findById(Long id);
}
