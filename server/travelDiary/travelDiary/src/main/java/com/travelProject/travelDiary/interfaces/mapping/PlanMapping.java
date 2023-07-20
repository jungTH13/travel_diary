package com.travelProject.travelDiary.interfaces.mapping;

import java.time.LocalDateTime;

public interface PlanMapping {
    Long getId();
    LocalDateTime getDateTime();
    String getTitle();
    String getMemo();
}
