package com.java.s3_service_demo.service;

import com.java.s3_service_demo.repo.ServiceRepository;
import com.java.s3_service_demo.dto.ServiceRequest;
import com.java.s3_service_demo.response.ServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ServiceService implements IServiceService{

    private final ServiceRepository serviceRepository;
    private final S3Service s3Service;

    @Override
    public ServiceResponse createService(ServiceRequest serviceRequest, MultipartFile multipartFile) throws IOException {
        com.java.s3_service_demo.entity.Service service = new com.java.s3_service_demo.entity.Service();
        service.setName(serviceRequest.getName());
        com.java.s3_service_demo.entity.Service saved = serviceRepository.save(service);
        String imageKey = s3Service.upload(multipartFile);
        saved.setImageKey(imageKey);
        com.java.s3_service_demo.entity.Service saved1 = serviceRepository.save(saved);
        ServiceResponse serviceResponse = new ServiceResponse();
        serviceResponse.setId(saved1.getId());
        serviceResponse.setName(saved1.getName());
        serviceResponse.setImageKey(saved1.getImageKey());
        return serviceResponse;
    }

    @Override
    public ServiceResponse findByFileName(String fileName) {
        com.java.s3_service_demo.entity.Service service =
                serviceRepository.findByImageKey(fileName)
                        .orElseThrow(() -> new RuntimeException("Key doesn't exist"));
        ServiceResponse serviceResponse = new ServiceResponse();
        serviceResponse.setId(service.getId());
        serviceResponse.setName(service.getName());
        serviceResponse.setImageKey(service.getImageKey());
        String preSignedUrl = s3Service.generatePreSignedUrl(service.getImageKey());
        serviceResponse.setImageUrl(preSignedUrl);
        return serviceResponse;
    }

    @Override
    public ServiceResponse findById(Long id) {
        com.java.s3_service_demo.entity.Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Id doesn't exist"));
        ServiceResponse serviceResponse = new ServiceResponse();
        serviceResponse.setId(service.getId());
        serviceResponse.setName(service.getName());
        serviceResponse.setImageKey(service.getImageKey());
        String url = s3Service.generatePreSignedUrl(service.getImageKey());
        serviceResponse.setImageUrl(url);
        return serviceResponse;
    }
}
