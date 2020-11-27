package com.trevorism.testing.service

import com.trevorism.testing.model.TestSuite
import com.trevorism.testing.service.run.ExecuteTestStrategy
import com.trevorism.testing.service.run.ExecuteTestStrategyFactory

class DefaultTestExecutorService implements TestExecutorService {

    @Override
    boolean executeTestSuite(TestSuite testSuite) {
        ExecuteTestStrategy strategy = ExecuteTestStrategyFactory.create(testSuite.kind)
        strategy?.runTest(testSuite)
    }

}
