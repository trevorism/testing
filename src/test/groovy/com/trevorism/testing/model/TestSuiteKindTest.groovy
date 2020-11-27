package com.trevorism.testing.model

import org.junit.Test

class TestSuiteKindTest {

    @Test
    void testGetValidKind(){
        assert TestSuiteKind.valueOf("unit".toUpperCase())
        assert TestSuiteKind.valueOf("karma".toUpperCase())
        assert TestSuiteKind.valueOf("cucumber".toUpperCase())
        assert TestSuiteKind.valueOf("web".toUpperCase())
        assert TestSuiteKind.valueOf("powershell".toUpperCase())
        assert TestSuiteKind.valueOf("selenium".toUpperCase())
    }

    @Test
    void testGetToString(){
        assert TestSuiteKind.values().toString()
        assert TestSuiteKind.values().toString().contains("[UNIT,")
    }
}