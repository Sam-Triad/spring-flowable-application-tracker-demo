package com.samjsddevelopment.applicationtracker.enums;

public enum TaskStateEnum {
    CREATED,
    COMPLETED;

    public static TaskStateEnum fromString(String state) throws IllegalArgumentException{
        return TaskStateEnum.valueOf(state.trim().toUpperCase());
    }
}
