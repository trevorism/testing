package com.trevorism.testing.model

class TestMetadata {

    String id
    boolean disabled
    boolean shouldFail

    //Scheduling
    String repeatFrequency
    Date startDate

    //notifications
    boolean alertOnFailure
}
