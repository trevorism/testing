package com.trevorism.testing.service

import com.trevorism.testing.model.TestEvent
import com.trevorism.testing.model.TestSuite

interface TestExecutorService {

    boolean executeTestSuite(TestSuite test)

    TestSuite updateTestSuiteFromEvent(TestEvent testEvent)
}