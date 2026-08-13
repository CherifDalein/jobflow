package com.jobflow.jobflow.repositories;

import com.jobflow.jobflow.models.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {
    List<Interview> findByApplicationUserIdAndScheduledDateAfterOrderByScheduledDateAsc(Long applicationUserId, LocalDateTime scheduledDate);
}
