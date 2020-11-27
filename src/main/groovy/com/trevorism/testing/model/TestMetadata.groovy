package com.trevorism.testing.model

class TestMetadata {

    String id
    String testSuiteId
    boolean disabled
    boolean shouldFail
    InvocationInfo invocationInfo = new InvocationInfo()
    ScheduleConfig scheduleConfig = new ScheduleConfig()
    NotificationConfig notificationConfig = new NotificationConfig()
}
