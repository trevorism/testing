package com.trevorism.testing.service


import com.trevorism.testing.model.TestSuite
import com.trevorism.testing.model.WorkflowStatus

interface TestExecutorService {

    boolean executeTestSuite(TestSuite test)

    TestSuite updateTestSuiteFromStatus(TestSuite testSuite, WorkflowStatus workflowStatus)
}