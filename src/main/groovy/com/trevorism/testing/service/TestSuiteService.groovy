package com.trevorism.testing.service

import com.trevorism.testing.model.TestSuite

interface TestSuiteService {

    TestSuite create(TestSuite testSuite)
    List<TestSuite> list()
    TestSuite get(String id)
    TestSuite delete(String id)
    TestSuite update(String id, TestSuite testSuite)

}