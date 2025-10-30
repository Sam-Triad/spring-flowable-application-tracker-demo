package com.samjsddevelopment.applicationtracker.service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.samjsddevelopment.applicationtracker.dto.ApplicationDto;
import com.samjsddevelopment.applicationtracker.dto.CreateApplicationRequest;
import com.samjsddevelopment.applicationtracker.dto.UpdateApplicationRequest;
import com.samjsddevelopment.applicationtracker.enums.ApplicationStatusEnum;
import com.samjsddevelopment.applicationtracker.exception.CamundaStateException;
import com.samjsddevelopment.applicationtracker.exception.NotFoundException;
import com.samjsddevelopment.applicationtracker.mapper.ApplicationMapper;
import com.samjsddevelopment.applicationtracker.model.Application;
import com.samjsddevelopment.applicationtracker.repository.ApplicationRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationService {

        private final ApplicationRepository applicationRepository;
        private final ApplicationMapper applicationMapper;

        @Value("${processes.application-process-id}")
        private String processId;

        @Transactional
        public ApplicationDto createApplication(CreateApplicationRequest request) {
                // var application = Application.builder()
                //                 .applicantUsername(request.applicantUsername())
                //                 .build();
                // var savedApplication = applicationRepository.save(application);

                // var processInstanceResult = camundaClient.newCreateInstanceCommand()
                //                 .bpmnProcessId(processId)
                //                 .latestVersion()
                //                 .variables(Map.of("applicationId", savedApplication.getId().toString()))
                //                 .send()
                //                 .join();
                // application.setProcessInstanceKey(processInstanceResult.getProcessInstanceKey());
                // applicationRepository.save(application);

                // return applicationMapper.toDto(savedApplication);
                return new ApplicationDto();
        }

        public ApplicationDto updateApplication(UUID id, UpdateApplicationRequest request) {
                // var application = applicationRepository.findById(id)
                //                 .orElseThrow(() -> new EntityNotFoundException("Application not found with id: " + id));

                // if (!application.getApplicationStatus().equals(ApplicationStatusEnum.WAITING_FOR_SUBMISSION)) {
                //         throw new CamundaStateException("Application must be waiting for submission to update.");
                // }

                // application.setInformation(request.information());
                // var updatedApplication = applicationRepository.save(application);
                // return applicationMapper.toDto(updatedApplication);
                return new ApplicationDto();
        }

        public void submitApplication(UUID id, String userId, String userEmail) {
                // var application = applicationRepository.findById(id)
                //                 .orElseThrow(() -> new NotFoundException("Application not found with id: " + id));

                // // Find user task and get key
                // var results = camundaClient.newUserTaskQuery()
                //                 .filter(f -> f.processInstanceKey(application.getProcessInstanceKey()))
                //                 .send()
                //                 .join();
                // var items = results.items();
                // if (items == null || items.isEmpty()) {
                //         throw new CamundaStateException(
                //                         "No user task found for process instance: "
                //                                         + application.getProcessInstanceKey());
                // }
                // var userTaskKey = items.getFirst().getKey();

                // // Assign and complete user task
                // camundaClient.newUserTaskAssignCommand(userTaskKey)
                //                 .assignee(userId)
                //                 .send()
                //                 .join();

                // Map<String, Object> variables = new HashMap<>();
                // variables.put("completedBy", userId);
                // variables.put("completedByEmail", userEmail);
                // variables.put("completedAt", Instant.now().toString());

                // camundaClient.newUserTaskCompleteCommand(userTaskKey)
                //                 .variables(variables)
                //                 .send()
                //                 .join();
                // application.setApplicationStatus(ApplicationStatusEnum.IN_REVIEW);
                // applicationRepository.save(application);
        }

        public Page<ApplicationDto> listApplications(Pageable pageable) {
                var page = applicationRepository.findAll(pageable);
                var dtos = page.map(applicationMapper::toDto);
                return dtos;
        }

        public void approveApplication(UUID applicationId) {
                var application = applicationRepository.findById(applicationId)
                                .orElseThrow(() -> new CamundaStateException(
                                                "Application not found with id: " + applicationId));

                log.info("Application {} approved", applicationId);
                application.setApplicationStatus(ApplicationStatusEnum.APPROVED);
                applicationRepository.save(application);
        }

        public void rejectApplication(UUID applicationId) {
                var application = applicationRepository.findById(applicationId)
                                .orElseThrow(() -> new CamundaStateException(
                                                "Application not found with id: " + applicationId));

                log.info("Application {} rejected", applicationId);
                application.setApplicationStatus(ApplicationStatusEnum.REJECTED);
                applicationRepository.save(application);
        }

        public ApplicationDto getApplication(UUID applicationId) {
                var application = applicationRepository.findById(applicationId)
                                .orElseThrow(() -> new CamundaStateException(
                                                "Application not found with id: " + applicationId));
                return applicationMapper.toDto(application);
        }

        public void deleteApplication(UUID id) {
                // var application = applicationRepository.findById(id)
                //                 .orElseThrow(() -> new NotFoundException("Application not found with id: " + id));
                // camundaClient.newCancelInstanceCommand(application.getProcessInstanceKey()).send().join();

                // applicationRepository.delete(application);
        }

}
