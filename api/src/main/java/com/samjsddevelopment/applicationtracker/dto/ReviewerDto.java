package com.samjsddevelopment.applicationtracker.dto;

import java.util.UUID;

public record ReviewerDto(
                UUID id,
                String firstName,
                String lastName) {
}