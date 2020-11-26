package com.trevorism.testing.model

class TestMetadata {

    String id
    String testId
    boolean disabled
    boolean shouldFail
    ScheduleConfig scheduleConfig = new ScheduleConfig()
    NotificationConfig notificationConfig = new NotificationConfig()
}
