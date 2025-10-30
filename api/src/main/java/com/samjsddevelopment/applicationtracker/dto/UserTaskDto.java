package com.samjsddevelopment.applicationtracker.dto;

import java.util.UUID;

import com.samjsddevelopment.applicationtracker.enums.TaskStateEnum;

import lombok.Builder;

@Builder
public record UserTaskDto(long key, String elementId, UUID applicationId, String assigneeUsername, TaskStateEnum state) {
    
}
