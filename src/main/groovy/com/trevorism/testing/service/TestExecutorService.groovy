package com.trevorism.testing.service


import com.trevorism.testing.model.TestSuite

interface TestExecutorService {

    boolean executeTestSuite(TestSuite test)
    String getLastTestResultUrl(TestSuite test)

}