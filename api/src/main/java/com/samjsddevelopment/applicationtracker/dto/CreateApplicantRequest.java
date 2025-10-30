package com.samjsddevelopment.applicationtracker.dto;

import java.time.LocalDate;

public record CreateApplicantRequest(
        String firstName,
        String lastName,
        LocalDate dateOfBirth) {
}