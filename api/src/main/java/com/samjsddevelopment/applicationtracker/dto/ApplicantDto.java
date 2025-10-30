package com.samjsddevelopment.applicationtracker.dto;

import java.time.LocalDate;
import java.util.UUID;

public record ApplicantDto(
        UUID id,
        String firstName,
        String lastName,
        LocalDate dateOfBirth) {
}