package com.samjsddevelopment.applicationtracker.mapper;

import java.util.List;

import org.flowable.task.api.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import com.samjsddevelopment.applicationtracker.dto.UserTaskDto;
import com.samjsddevelopment.applicationtracker.enums.TaskStateEnum;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserTaskMapper {

    @Mapping(source = "state", target = "state", qualifiedByName = "stateFromString")
    @Mapping(source = "assignee", target = "assigneeUsername")
    UserTaskDto toDto(Task task);

    List<UserTaskDto> toDtoList(List<Task> tasks);

    @Named("stateFromString")
    default TaskStateEnum stateFromString(String state) {
        return TaskStateEnum.fromString(state);
    }
}
