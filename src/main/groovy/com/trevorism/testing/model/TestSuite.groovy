package com.trevorism.testing.model

import com.fasterxml.jackson.annotation.JsonFormat

class TestSuite {

    String id
    //Easily identify the test suite
    String name
    //Type of test suite
    String kind
    //Where the test suite lives
    String source

    boolean lastRunSuccess
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    Date lastRunDate
    long lastRuntimeSeconds
}
