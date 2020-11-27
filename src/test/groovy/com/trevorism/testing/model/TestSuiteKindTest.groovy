package com.trevorism.testing.model

import org.junit.Test

class TestSuiteKindTest {

    @Test
    void testGetValidKind(){
        TestSuiteKind kind = TestSuiteKind.valueOf(null)

        println kind
    }
}
