package com.java.s3_service_demo.response;

import lombok.Data;

@Data
public class ServiceResponse {

    private Long id;
    private String name;
    private String imageKey;
    private String imageUrl;

}
