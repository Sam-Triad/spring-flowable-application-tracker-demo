package com.samjsddevelopment.applicationtracker.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.samjsddevelopment.applicationtracker.enums.ApplicationStatusEnum;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "applications")
public class Application extends Auditable{

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private UUID id;

    private long processInstanceKey;

    private String applicantUsername;

    @ElementCollection
    @Builder.Default
    private Set<UUID> reviewerIds = new HashSet<>();

    @Lob
    private String information;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ApplicationStatusEnum applicationStatus = ApplicationStatusEnum.WAITING_FOR_SUBMISSION;

}
