package com.jobflow.jobflow.repositories;

import com.jobflow.jobflow.models.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByUserIdOrderByApplicationDateDesc(Long userId);

    Page<Application> findByUserId(Long userId, Pageable pageable);
    Page<Application> findByUserIdAndStatus(Long userId, String status, Pageable pageable);
}
