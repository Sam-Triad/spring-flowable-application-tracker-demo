package com.samjsddevelopment.applicationtracker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import com.samjsddevelopment.applicationtracker.dto.ApplicationDto;
import com.samjsddevelopment.applicationtracker.model.Application;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ApplicationMapper {
    ApplicationDto toDto(Application application);
}