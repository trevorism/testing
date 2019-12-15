package com.trevorism.testing.service


import com.trevorism.testing.model.TestSuite

class DefaultTestExecutorService implements TestExecutorService {
    @Override
    boolean executeTestSuite(TestSuite tests) {
        return false
    }


}
