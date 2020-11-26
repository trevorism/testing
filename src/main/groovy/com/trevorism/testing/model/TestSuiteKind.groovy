package com.trevorism.testing.model

import com.fasterxml.jackson.annotation.JsonProperty

enum TestSuiteKind {
    @JsonProperty("junit")
    JUNIT,
    @JsonProperty("karma")
    KARMA,
    @JsonProperty("cucumber")
    CUCUMBER,
    @JsonProperty("web")
    WEB,
    @JsonProperty("powershell")
    POWERSHELL,
    @JsonProperty("selenium")
    SELENIUM
}