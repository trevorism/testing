package com.trevorism.testing.model

import com.fasterxml.jackson.annotation.JsonProperty

enum TestSuiteKind {
    @JsonProperty("unit")
    UNIT,
    @JsonProperty("javascript")
    JAVASCRIPT,
    @JsonProperty("cucumber")
    CUCUMBER,
    @JsonProperty("web")
    WEB,
    @JsonProperty("powershell")
    POWERSHELL,
    @JsonProperty("cypress")
    CYPRESS
}