package com.trevorism.testing.model

class TestSuite {

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
    List<Test> tests
}
