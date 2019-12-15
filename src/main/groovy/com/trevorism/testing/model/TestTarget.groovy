package com.trevorism.testing.model

class TestTarget {

    enum TestTargetType{
        CODEBASE,
        DEPLOYED_APP,
        INTEGRATION
    }

    String id
    TestTargetType type
    String location //Most tests target a single thing
    List<String> locations //Integration tests may target multiple things
}