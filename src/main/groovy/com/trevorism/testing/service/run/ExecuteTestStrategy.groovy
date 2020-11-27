package com.trevorism.testing.service.run

import com.trevorism.testing.model.TestSuite

interface ExecuteTestStrategy {

    boolean runTest(TestSuite testSuite)
}