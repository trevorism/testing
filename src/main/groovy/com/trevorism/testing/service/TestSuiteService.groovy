package com.trevorism.testing.service

import com.trevorism.testing.model.TestSuite

interface TestSuiteService {

    TestSuite create(TestSuite collection)
    List<TestSuite> list()
    TestSuite get(String collectionId)
    TestSuite delete(String collectionId)
    TestSuite update(String collectionId, TestSuite testSuite)
}