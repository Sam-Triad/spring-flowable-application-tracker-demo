package com.samjsddevelopment.applicationtracker.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.samjsddevelopment.applicationtracker.dto.ApprovalDecisionRequest;
import com.samjsddevelopment.applicationtracker.dto.UserTaskDto;
import com.samjsddevelopment.applicationtracker.service.ApplicationTaskService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {

    private final ApplicationTaskService taskService;

    @GetMapping("/available")
    @Operation(summary = "List unassigned tasks")
    public ResponseEntity<List<UserTaskDto>> getAvailableTasks(
            @AuthenticationPrincipal Jwt jwt) {
        var userGroups = jwt.getClaimAsStringList("groups");
        if (userGroups == null) {
            userGroups = Collections.emptyList();
        }

        return ResponseEntity.ok(taskService.getAvailableTasks(userGroups));
    }

    @GetMapping("/{key}")
    public ResponseEntity<UserTaskDto> getTask(@PathVariable long key) {
        return ResponseEntity.ok(taskService.getTask(key));
    }

    @PostMapping("/decide-approval")
    public ResponseEntity<Void> decideApproval(@AuthenticationPrincipal Jwt jwt, @RequestBody ApprovalDecisionRequest approvalDecisionRequest) {
        var userId = jwt.getClaimAsString("preferred_username");
        taskService.makeApplicationDecision(userId, approvalDecisionRequest.userTaskKey(), approvalDecisionRequest.approved());

        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/claim-task/{userTaskId}")
    public ResponseEntity<Void> claimTask(@AuthenticationPrincipal Jwt jwt, @PathVariable long userTaskId) {
        var userId = jwt.getClaimAsString("preferred_username");
        taskService.claimTask(userId, userTaskId);

        return ResponseEntity.ok().build();
    }
    
}
