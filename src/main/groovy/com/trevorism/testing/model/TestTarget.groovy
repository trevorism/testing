package com.trevorism.testing.model

import com.fasterxml.jackson.annotation.JsonProperty

class TestTarget {

    enum TestTargetType{
        @JsonProperty("CODEBASE")
        CODEBASE,
        @JsonProperty("DEPLOYED_APP")
        DEPLOYED_APP,
        @JsonProperty("INTEGRATION")
        INTEGRATION
    }

    String id
    String testTargetType
    String version
    String location //Most tests target a single thing
    List<String> locations //Integration tests may target multiple things
}