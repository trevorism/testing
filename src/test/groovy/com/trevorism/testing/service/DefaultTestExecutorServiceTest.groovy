package com.trevorism.testing.service

import com.trevorism.testing.model.TestSuite
import org.junit.Test

class DefaultTestExecutorServiceTest {

    @Test
    void testExecuteTestSuite() {
        TestExecutorService testExecutorService = new DefaultTestExecutorService()
        assert testExecutorService
        //println testExecutorService.executeTestSuite(new TestSuite(id: "4792240945758208", name: "acceptance_action", source: "action", kind: "cucumber"))
    }

    void testUpdateTestSuiteFromStatus() {
    }
}
