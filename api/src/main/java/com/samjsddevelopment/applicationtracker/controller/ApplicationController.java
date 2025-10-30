package com.samjsddevelopment.applicationtracker.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.samjsddevelopment.applicationtracker.dto.ApplicationDto;
import com.samjsddevelopment.applicationtracker.dto.CreateApplicationRequest;
import com.samjsddevelopment.applicationtracker.dto.UpdateApplicationRequest;
import com.samjsddevelopment.applicationtracker.service.ApplicationService;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    @Operation(summary = "Create a new application")
    public ResponseEntity<ApplicationDto> createApplication(@RequestBody CreateApplicationRequest request) {
        return new ResponseEntity<>(applicationService.createApplication(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an application")
    public ResponseEntity<ApplicationDto> updateApplication(@PathVariable UUID id,
            @RequestBody UpdateApplicationRequest request) {
        return ResponseEntity.ok(applicationService.updateApplication(id, request));
    }

    @PostMapping("/{id}/submit")
    @Operation(summary = "Submit an application for review")
    public ResponseEntity<Void> submitApplication(@PathVariable UUID id, @AuthenticationPrincipal Jwt jwt) {
        var userId = jwt.getClaimAsString("preferred_username");
        var userEmail = jwt.getClaimAsString("email");

        applicationService.submitApplication(id, userId, userEmail);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an application")
    public ResponseEntity<ApplicationDto> getApplication(@PathVariable UUID id) {
        return ResponseEntity.ok(applicationService.getApplication(id));
    }

    @GetMapping
    @Operation(summary = "List applications (paginated), sorted by creation date ascending")
    public ResponseEntity<Page<ApplicationDto>> listApplications(
            @ParameterObject Pageable pageable) {
        var sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Direction.ASC, "createdDate"));
        var page = applicationService.listApplications(sortedPageable);
        return ResponseEntity.ok(page);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an application, including the underlying Camunda process")
    public ResponseEntity<Void> deleteApplication(@PathVariable UUID id) {
        applicationService.deleteApplication(id);
        return ResponseEntity.ok().build();
    }
}
