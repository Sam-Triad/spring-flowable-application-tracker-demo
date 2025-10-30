package com.samjsddevelopment.applicationtracker.dto;

import java.util.UUID;

public record ReviewTaskDto(
        long taskKey,
        String taskName,
        UUID applicationId,
        String applicantFirstName,
        String applicantLastName) {
}
