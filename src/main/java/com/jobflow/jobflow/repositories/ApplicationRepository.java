package com.jobflow.jobflow.repositories;

import com.jobflow.jobflow.models.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

}
