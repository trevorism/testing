package com.trevorism.testing.model

class TestSuite implements HasTests{

    enum TestSuiteType {
        JUNIT,
        KARMA,
        CUCUMBER,
        WEB
    }

    String id
    String name
    TestSuiteType type
    String location
    List<HasTests> tests
}
