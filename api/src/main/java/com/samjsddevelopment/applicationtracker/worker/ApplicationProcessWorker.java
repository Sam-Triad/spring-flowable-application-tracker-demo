package com.samjsddevelopment.applicationtracker.worker;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.samjsddevelopment.applicationtracker.service.ApplicationService;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationProcessWorker {

    private final ApplicationService applicationService;
    
    @JobWorker(type = "request_approvals")
    public void requestApprovals(final ActivatedJob job) {
        log.info("Requesting Approvals for " + job.getBpmnProcessId());
    }

    @JobWorker(type = "Reject_Application")
    public void applicationRejected(@Variable(name = "applicationId") String applicationId) {
        applicationService.rejectApplication(UUID.fromString(applicationId));
    }

    @JobWorker(type = "Approve_Application")
    public void applicationApproved(@Variable(name = "applicationId") String applicationId) {
        applicationService.approveApplication(UUID.fromString(applicationId));
    }
}
