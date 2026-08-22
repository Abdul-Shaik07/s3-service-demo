package com.java.s3_service_demo.repo;

import com.java.s3_service_demo.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    Optional<Service> findByImageKey(String imageKey);
}
