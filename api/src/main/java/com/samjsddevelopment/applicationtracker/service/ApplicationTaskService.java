package com.samjsddevelopment.applicationtracker.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samjsddevelopment.applicationtracker.dto.UserTaskDto;
import com.samjsddevelopment.applicationtracker.enums.TaskStateEnum;
import com.samjsddevelopment.applicationtracker.exception.FlowableStateException;
import com.samjsddevelopment.applicationtracker.exception.NotFoundException;
import com.samjsddevelopment.applicationtracker.model.Application;
import com.samjsddevelopment.applicationtracker.repository.ApplicationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationTaskService {

    private final ApplicationRepository applicationRepository;

    @Transactional(readOnly = true)
    public List<UserTaskDto> getAvailableTasks(List<String> userGroups) {
        // // SDK does not support filtering by multiple user groups so make multiple
        // // requests in a loop
        // List<UserTask> items = new ArrayList<>();
        // userGroups.forEach(group -> {
        //     var results = camundaClient.newUserTaskQuery()
        //             .filter(f -> f.candidateGroup(group))
        //             .send()
        //             .join();
        //     items.addAll(results.items());
        // });

        // // Filter out tasks with assignees and map to DTOs
        // var test = items.stream()
        //         // .filter(task -> task.getAssignee() == null || task.getAssignee().isEmpty())
        //         .map(task -> {
        //             // Look up application by process instance key
        //             Application application = applicationRepository
        //                     .findByprocessInstanceId(task.getprocessInstanceId()).orElseThrow(NotFoundException::new);

        //             return UserTaskDto.builder().assigneeUsername(task.getAssignee()).key(task.getKey()).state(TaskStateEnum.fromString(task.getState()))
        //                     .elementId(task.getElementId()).applicationId(application.getId()).build();
        //         })
        //         .collect(Collectors.toList());
        // return test;
        return Collections.emptyList();
    }

    public void makeApplicationDecision(String userId, long userTaskId, boolean approved) {
        // var userTaskList = camundaClient.newUserTaskQuery()
        //         .filter(f -> f.assignee(userId).key(userTaskId).state("CREATED"))
        //         .send()
        //         .join().items();

        // if (userTaskList.isEmpty() || userTaskList == null) {
        //     throw new NotFoundException("A user task assigned to the current user was not found");
        // }
        // if (userTaskList.size() > 1) {
        //     throw new CamundaStateException("More than one user task was found");
        // }

        // Map<String, Object> variables = new HashMap<>();
        // variables.put("approved", approved);
        // camundaClient.newUserTaskCompleteCommand(userTaskId).variables(variables).send().join();
    }

    public void claimTask(String userId, long userTaskId) {
        // var userTaskList = camundaClient.newUserTaskQuery()
        //         .filter(f -> f.key(userTaskId))
        //         .send()
        //         .join().items();

        // if (userTaskList.isEmpty() || userTaskList == null) {
        //     throw new NotFoundException("A user task assigned to the current user was not found");
        // }
        // if (userTaskList.size() > 1) {
        //     throw new CamundaStateException("More than one user task was found");
        // }

        // camundaClient.newUserTaskAssignCommand(userTaskId).assignee(userId).send().join();
    }

    @Transactional(readOnly = true)
    public UserTaskDto getTask(long key) {
        // var userTaskList = camundaClient.newUserTaskQuery()
        //         .filter(f -> f.key(key))
        //         .send()
        //         .join().items();

        // if (userTaskList.isEmpty() || userTaskList == null) {
        //     throw new NotFoundException("A user task assigned to the current user was not found");
        // }
        // if (userTaskList.size() > 1) {
        //     throw new CamundaStateException("More than one user task was found");
        // }
        // var userTask = userTaskList.getFirst();
        // Application application = applicationRepository
        //         .findByprocessInstanceId(userTask.getprocessInstanceId()).orElseThrow();

        // return UserTaskDto.builder().elementId(userTask.getElementId()).applicationId(application.getId())
        //         .state(TaskStateEnum.fromString(userTask.getState()))
        //         .assigneeUsername(userTask.getAssignee()).build();
        return new UserTaskDto(key, null, null, null, null);
    }

}
