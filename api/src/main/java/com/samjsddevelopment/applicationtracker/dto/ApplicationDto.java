package com.samjsddevelopment.applicationtracker.dto;

import java.util.UUID;

import com.samjsddevelopment.applicationtracker.enums.ApplicationStatusEnum;

public record ApplicationDto(
        UUID id,
        String applicantUsername,
        String information,
        ApplicationStatusEnum applicationStatus) {
}