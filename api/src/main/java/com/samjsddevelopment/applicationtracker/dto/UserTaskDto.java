package com.samjsddevelopment.applicationtracker.dto;

import java.util.UUID;

import com.samjsddevelopment.applicationtracker.enums.TaskStateEnum;

public record UserTaskDto(UUID id, String name, UUID processInstanceId, String assigneeUsername, TaskStateEnum state) {
    
}
