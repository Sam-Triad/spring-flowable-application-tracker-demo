package com.samjsddevelopment.applicationtracker.enums;

public enum TaskStateEnum {
    CREATED,
    CLAIMED,
    IN_PROGRESS,
    SUSPENDED,
    COMPLETED,
    TERMINATED;

    public static TaskStateEnum fromString(String state) throws IllegalArgumentException{
        return TaskStateEnum.valueOf(state.trim().toUpperCase());
    }
}
