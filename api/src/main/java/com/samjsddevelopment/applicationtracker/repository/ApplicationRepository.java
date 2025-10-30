package com.samjsddevelopment.applicationtracker.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.samjsddevelopment.applicationtracker.model.Application;

public interface ApplicationRepository extends JpaRepository<Application, UUID> {

    Optional<Application> findByProcessInstanceKey(long processInstanceKey);
}