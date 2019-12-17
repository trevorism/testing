package com.trevorism.testing.model

import com.fasterxml.jackson.annotation.JsonProperty

class TestSuite {

    enum TestSuiteType {
        @JsonProperty("JUNIT")
        JUNIT,
        @JsonProperty("KARMA")
        KARMA,
        @JsonProperty("CUCUMBER")
        CUCUMBER,
        @JsonProperty("WEB")
        WEB
    }

    String id
    String name
    TestSuiteType type
    String location
    String version
}
